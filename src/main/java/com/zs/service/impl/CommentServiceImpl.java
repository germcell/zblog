package com.zs.service.impl;

import com.zs.config.Const;
import com.zs.handler.RandomUtils;
import com.zs.mapper.CommentMapper;
import com.zs.pojo.Comment;
import com.zs.pojo.DelComment;
import com.zs.pojo.MDate;
import com.zs.pojo.RequestResult;
import com.zs.service.CommentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Created by zs on 2022/3/11.
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Resource
    private CommentMapper commentMapper;

    @Value("${postAvatar}")
    private String postAvatar;

    @Value("${replyAvatar}")
    private String replyAvatar;

    /**
     * 发布、回复评论
     * @param comment
     * @return
     */
    @Transactional
    @Override
    public int postComments(Comment comment) {
        comment.setAvatar(RandomUtils.generateAvatar());
        comment.setReplyTime(new Date());
        int rows = commentMapper.insert(comment);
        return rows;
    }

    /**
     * 查询评论(前台显示，需有层级)
     * 流程 : 先查询父评论、再根据父评论的id作为子评论的com_parent_id查询子评论
     * @param bid
     * @return
     * FIXME 实现多级评论
     * TODO 评论时间处理，(今天、昨天、刚刚...)
     */
    @Override
    public List<Comment> listCommentsByBid(Long bid) {
        // 查询所有父评论(查询 com_parent_id == -1 和 指定 bid 的记录)
        List<Comment> comments = commentMapper.listCommentsByBid(bid);
        // 再查询父评论的所有子评论
        Iterator<Comment> commentsIterator = comments.iterator();
        while (commentsIterator.hasNext()) {
            Comment next = commentsIterator.next();
            List<Comment> listChildComments = commentMapper.listCommentsByComId(next.getComId());
            if (listChildComments.size() != 0) {
                next.setListChildComments(listChildComments);
            }
        }
        return comments;
    }

    /**
     * 查询评论(后台管理，不需要有层级)
     * @param condition 动态条件对象
     * @return
     */
    @Override
    public List<Comment> listCommentsByCondition(Comment condition, MDate date) {
        return commentMapper.listCommentsByCondition(condition, date);
    }

    /**
     * 删除留言(单个删除、批量删除)
     * @param comIds 留言id数组
     * @return 删除结果
     */
    @Transactional
    @Override
    public RequestResult deleteComments(Long[] comIds) {
        RequestResult requestResult = new RequestResult();
        /*
            1.单例删除,当数组长度等于1时
                1.1 当留言为子留言时，直接删除
                1.2 当留言为父留言时，并且没有子留言，直接删除
                1.2 当留言为父留言时，并且有子留言，那么做伪删除，将父留言文本替换为"此留言已被删除"
         */
        if (comIds.length == 1) {
            RequestResult deleteResult = deleteComment(comIds);
            if (deleteResult.getCode() == Const.COMMENT_DELETE_SUCCESS ||
                deleteResult.getCode() == Const.COMMENT_DELETE_SUCCESS_FAKE) {
                requestResult = deleteResult;
            }
        } else {
            /*
                2.批量删除,当长度大于1时
                    2.1 归纳留言类别(DelComment)
                            子留言特征:com_parent_id != -1 ；归纳到DelComment对象的noChildrenComment属性
                            父留言特征:com_parent_id == -1 ；归纳到DelComment对象的haveChildrenComment属性
                    2.1 先删除子留言
                            以DelComment对象noChildrenComment属性里的值为删除条件
                    2.2 再调用judgeComment()对haveChildrenComment属性进行留言类别归纳(判断)
                            更新条件:有子留言,伪删除，把留言内容替换为"此留言已被删除"
                            删除条件:无子留言,删除
             */
            DelComment delComment = new DelComment(new ArrayList<>(), new ArrayList<>());
            List<Comment> comments = commentMapper.listCommentsByComIds(Arrays.asList(comIds));
            for (Comment comment : comments) {
                if (comment.getComParentId() != -1) {
                    delComment.getNoChildrenComment().add(comment.getComId());
                } else {
                    delComment.getHaveChildrenComment().add(comment.getComId());
                }
            }
            if (delComment.getNoChildrenComment().size() != 0) {
                commentMapper.deleteCommentByComIds(delComment.getNoChildrenComment());
            }
            DelComment delComment2 = judgeComment(delComment.getHaveChildrenComment());
            if (delComment2.getHaveChildrenComment().size() != 0) {
                Comment tmp = new Comment();
                tmp.setContent("此留言已被删除");
                commentMapper.updateCommentByComIds(tmp, delComment2.getHaveChildrenComment());
            }
            if (delComment2.getNoChildrenComment().size() != 0) {
                commentMapper.deleteCommentByComIds(delComment2.getNoChildrenComment());
            }
            requestResult.setCode(Const.COMMENT_DELETE_SUCCESS);
            requestResult.setMessage("删除成功");
        }
        return requestResult;
    }

    /**
     * 删除单个留言(方法抽取)
     * @param comIds
     * @return 删除结果
     */
    @Transactional
    @Override
    public RequestResult deleteComment(Long[] comIds) {
        RequestResult deleteResult = new RequestResult();
        Comment condition = new Comment();
        condition.setComId(comIds[0]);
        Comment getCommentByComId = commentMapper.getCommentByCondition(condition);
        // 1.1 当留言为子留言时，直接删除
        if (getCommentByComId.getComParentId() != -1) {
            int rows = commentMapper.deleteCommentByComIds(Arrays.asList(comIds));
            if (rows == 1) {
                deleteResult.setCode(Const.COMMENT_DELETE_SUCCESS);
                deleteResult.setMessage("删除成功");
            }
        } else {
            // 1.2 当留言为父留言时，并且没有子留言，直接删除
            List<Comment> comments = commentMapper.listCommentsByComId(comIds[0]);
            if (comments.size() == 0) {
                int rows = commentMapper.deleteCommentByComIds(Arrays.asList(comIds));
                if (rows == 1) {
                    deleteResult.setCode(Const.COMMENT_DELETE_SUCCESS);
                    deleteResult.setMessage("删除成功");
                }
            } else {
                // 1.2 当留言为父留言时，并且有子留言，那么做伪删除，将父留言文本替换为"此留言已被删除"
                Comment tmp = new Comment();
                tmp.setContent("此留言已被删除");
                tmp.setIsPass(getCommentByComId.getIsPass());
                int rows = commentMapper.updateCommentByComIds(tmp, Arrays.asList(comIds));
                if (rows == 1) {
                    deleteResult.setCode(Const.COMMENT_DELETE_SUCCESS_FAKE);
                    deleteResult.setMessage("删除成功");
                }
            }
        }
        return deleteResult;
    }

    /**
     * 判断留言是否有子留言
     * @param comIds
     * @return DelComment
     */
    @Override
    public DelComment judgeComment(List<Long> comIds) {
        DelComment delComment = new DelComment(new ArrayList<>(), new ArrayList<>());
        for (Long comId : comIds) {
            List<Comment> comments = commentMapper.listCommentsByComId(comId);
            if (comments.size() == 0) {
                delComment.getNoChildrenComment().add(comId);
            } else {
                delComment.getHaveChildrenComment().add(comId);
            }
        }
        return delComment;
    }

    /**
     * 批量审核留言
     * @param comIds
     * @return
     */
    @Transactional
    @Override
    public RequestResult passComments(Long[] comIds, Comment comment) {
        RequestResult requestResult = new RequestResult();
        if (comIds.length != 0) {
            int rows = commentMapper.updateCommentByComIds(comment, Arrays.asList(comIds));
            if (rows > 0) {
                requestResult.setCode(Const.COMMENT_PASS_SUCCESS);
                requestResult.setMessage("审核成功");
            } else {
                requestResult.setCode(Const.COMMENT_PASS_FAIL);
                requestResult.setMessage("审核失败");
            }
        } else {
            requestResult.setCode(Const.COMMENT_PASS_FAIL);
            requestResult.setMessage("审核失败");
        }
        return requestResult;
    }
}
