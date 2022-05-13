package com.zs.service;

import com.zs.pojo.Comment;
import com.zs.pojo.DelComment;
import com.zs.pojo.MDate;
import com.zs.pojo.RequestResult;

import java.util.List;

/**
 * @Created by zs on 2022/3/11.
 */
public interface CommentService {

    /**
     * 提交评论
     * @param comment
     * @return
     */
    int postComments(Comment comment);

    /**
     * 查询评论(前台显示，需有层级)
     * 流程 : 先查询父评论、再根据父评论的id作为子评论的com_parent_id查询子评论
     * @param bid
     * @return
     * FIXME 实现多级评论
     * TODO 评论时间处理，(今天、昨天、刚刚...)
     */
    List<Comment> listCommentsByBid(Long bid);

    /**
     * 查询评论(后台管理，不需要有层级)
     * @param comment 动态条件对象
     * @return
     */
    List<Comment> listCommentsByCondition(Comment comment, MDate date);

    /**
     * 删除留言(单个删除、批量删除)
     * @param comIds 留言id数组
     * @return
     */
    RequestResult deleteComments(Long[] comIds);

    /**
     * 删除单个留言(方法抽取)
     * @param comIds 留言id数组，长度为1
     * @return
     */
    RequestResult deleteComment(Long[] comIds);

    /**
     * 判断留言数组中的每个留言是否有子留言，将其归纳后存入 DelComment 对象中
     * @param comIds
     * @return DelComment 存放留言类别的对象
     */
    DelComment judgeComment(List<Long> comIds);

    /**
     * 批量审核留言
     * @param comIds
     * @return
     */
    RequestResult passComments(Long[] comIds, Comment comment);
}
