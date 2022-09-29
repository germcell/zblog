package com.zs.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zs.config.Const2;
import com.zs.dto.MsgDTO;
import com.zs.mapper.TbCommentMapper;
import com.zs.pojo.TbComment;
import com.zs.service.MsgService;
import com.zs.vo.ArticleCommentVO;
import com.zs.vo.CommentVO;
import com.zs.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @author zengshuai
 * @create 2022-09-07 15:04
 */
@Service
@Slf4j
public class MsgServiceImpl implements MsgService {

    private static final String COMMENT_DELETE_TAG = "<span style='color: #999;'>此评论已被删除</span>";

    @Resource
    private AmqpTemplate amqpTemplate;
    @Resource
    private TbCommentMapper tbCommentMapper;

    public ResultVO getPrivateMsgByCIds(List<Long> cIds) {
        // 查询私信(他人)
        List<TbComment> receive = tbCommentMapper.getByIds(cIds, 0);

        // 将私信置为已读
        tbCommentMapper.updateStatusByIds(cIds, 1);

        // 查询回复信息(我)
        long sendId = receive.get(0).getReceiveId();
        long receiveId = receive.get(0).getSendId();
        List<TbComment> send = tbCommentMapper.getCommunication(sendId, receiveId);

        Map<String, List> result = new HashMap<>();
        result.put("receive", receive);
        result.put("send", send);
        return ResultVO.success(result);
    }

    @Override
    public ResultVO getPrivateMsgUserInfoByUid(long uid) {
        // 获取用户的私信信息
        List<CommentVO> comments = tbCommentMapper.getByReceiveId(uid);

        if (comments.size() == 0) {
            return ResultVO.success(comments);
        }

        // 提取私信用户id(key)和最新的私信id(value)
        Map<Long,Long> map = new HashMap<>();

        comments.stream().forEach(commentVO -> {
            boolean contains = map.entrySet().stream().anyMatch(m -> {
                if (Objects.equals(commentVO.getSendId(), m.getValue())) return true;
                return false;
            });
            if (!contains) map.put(commentVO.getId(), commentVO.getSendId());
        });

        // 提取私信id,并添加到【最新私信】对象的commentIds集合中，那么不是最新私信对象的commentIds.size() == 0
        AtomicReference<List<CommentVO>> temp = new AtomicReference<>();

        map.entrySet().stream().forEach(m -> {
            temp.set(comments.stream().filter(c -> Objects.equals(m.getValue(), c.getSendId())).collect(Collectors.toList()));

            temp.get().stream().forEach(t -> {
                if (Objects.isNull(t.getCommentIds())) {
                    t.setCommentIds(new ArrayList<>());
                }
                if (Objects.equals(m.getKey(), t.getId()) && Objects.equals(m.getValue(), t.getSendId())) {
                    t.getCommentIds().add(t.getId());
                } else {
                    temp.get().stream().anyMatch(t2 -> {
                        if (Objects.equals(t2.getId(), m.getKey()) && Objects.equals(m.getValue(), t2.getSendId())) {
                            t2.getCommentIds().add(t.getId());
                            return true;
                        }
                        return false;
                    });
                }
            });
        });

        // 去除重复的私信信息,只保留最新的私信和其所有私信id
        comments.removeIf(c -> c.getCommentIds().size() == 0);

        return ResultVO.success(comments);
    }

    @Override
    public void productMsg(String msgDTO) {
        log.info("生产消息==>{}", msgDTO);
        amqpTemplate.convertAndSend(Const2.MSG_QUEUE_1, msgDTO);
    }

    @Override
    public ResultVO getUnreadMsgByUid(long uid) {
        return ResultVO.success(tbCommentMapper.getUnreadByReceiveId(uid));
    }

    @Override
    public ResultVO getNewUnreadMsgIds(long receiveId, long sendId) {
        return ResultVO.success(tbCommentMapper.getNewUnreadIds(receiveId, sendId));
    }

    @Transactional
    @Override
    public ResultVO deleteAllMsgByUser(long sendId, long receiveId) {
        tbCommentMapper.updateBySendIdAndReceiveId(sendId, receiveId);
        return ResultVO.success(null);
    }

    @Override
    public ResultVO getPageArticleComments(long bid, int p) {
        // 分页查询根评论
        PageHelper.startPage(p, Const2.ARTICLE_COMMENT_PAGE_SIZE);
        List<ArticleCommentVO> rootComments = tbCommentMapper.pageRootComments(bid);
        List<ArticleCommentVO> rootCommentTrees = new ArrayList<>();

        // 生成每个根节点的评论树
        rootComments.stream().forEach(root -> {
            rootCommentTrees.add(commentTree(root));
        });

        PageInfo<ArticleCommentVO> pageInfo = new PageInfo<>(rootComments);

        HashMap<String, Object> map = new HashMap<>();
        map.put("commentPageInfo", pageInfo);
        map.put("commentTrees", rootCommentTrees);

        return ResultVO.success(map);
    }

    /**
     * 递归生成评论节点的评论树
     * @param rootComment 评论节点
     * @return
     */
    private ArticleCommentVO commentTree(ArticleCommentVO rootComment) {
        List<ArticleCommentVO> childComments = tbCommentMapper.listCommentsByPid(rootComment.getBid(), rootComment.getId());

        if (childComments.size() == 0) {
            return rootComment;
        }

        childComments.stream().forEach(child -> {
            if (Objects.isNull(rootComment.getChildComments())) {
                rootComment.setChildComments(new ArrayList<>());
            }
            rootComment.getChildComments().add(commentTree(child));
        });

        return rootComment;
    }

    @Override
    public ResultVO addComment(MsgDTO msgDTO) {
        // 插入评论
        TbComment newComment = new TbComment();
        newComment.setSendId(msgDTO.getSendId());
        newComment.setReceiveId(msgDTO.getReceiveId());
        newComment.setContent(msgDTO.getContent());
        newComment.setPId(msgDTO.getPid());
        newComment.setBId(msgDTO.getBid());
        newComment.setMsgTag(1);
        newComment.setIsConsume(1);
        tbCommentMapper.insert(newComment);
        return ResultVO.success(null);
    }

    @Transactional
    @Override
    public ResultVO deleteCommentById(long commentId) {
        List<Long> ids = new ArrayList<>();
        ids.add(commentId);

        // 查询根评论，及其子评论
        List<TbComment> byIds = tbCommentMapper.getByIds(ids, 1);
        TbComment root = byIds.get(0);
        List<TbComment> children = tbCommentMapper.getCommentByPid(root.getId());

        // 被删除评论是根评论，且没有没有子评论，则直接删除
        if ((root.getPId() == -1 || root.getPId() != -1) && children.size() == 0) {
            tbCommentMapper.deleteById(root.getId());
            log.info("删除评论ID==>{}", commentId);
            return ResultVO.success(null);
        }

        // 被删除评论是根评论，且有子评论，则将根评论置为特定值
        if ((root.getPId() == -1 || root.getPId() != -1) && children.size() > 0) {
            TbComment condition = new TbComment();
            condition.setId(root.getId());
            condition.setContent(COMMENT_DELETE_TAG);
            tbCommentMapper.updateByCondition(condition);
            log.info("删除评论ID==>{}", commentId);
            return ResultVO.success(null);
        }

        // 被删除的是子评论，但没有子评论，则直接删除
//        if (root.getPId() != -1 && children.size() == 0) {
//            tbCommentMapper.deleteById(root.getId());
//            log.info("删除评论ID==>", commentId);
//            return ResultVO.success(null);
//        }
//
//        // 被删除的是子评论，但其还有子评论，则置为特定值
//        if (root.getPId() != -1 && children.size() > 0) {
//            TbComment condition = new TbComment();
//            condition.setId(root.getId());
//            condition.setContent(COMMENT_DELETE_TAG);
//            tbCommentMapper.updateByCondition(condition);
//            log.info("删除评论ID==>", commentId);
//            return ResultVO.success(null);
//        }

        // TODO 被删除评论是子评论，且它的所有父评论都已被"删除"，则将其全部删除

        return ResultVO.globalException();
    }

}
