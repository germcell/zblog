package com.zs.service;

import com.zs.dto.MsgDTO;
import com.zs.vo.ResultVO;

import java.util.List;

/**
 * @author zengshuai
 * @create 2022-09-10 21:26
 */
public interface MsgService {

    /**
     * 获取指定用户的私信信息，根据消息id查询
     * @param cIds
     * @return
     */
    ResultVO getPrivateMsgByCIds(List<Long> cIds);

    /**
     * 获取指定用户的所有私信用户信息
     * @param uid
     * @return
     */
    ResultVO getPrivateMsgUserInfoByUid(long uid);

    /**
     * 发送私信
     * @param msg
     */
    void productMsg(String msg);

    /**
     * 获取用户未读消息数量
     * @param uid
     * @return
     */
    ResultVO getUnreadMsgByUid(long uid);

    /**
     * 获取用户新增私信
     * @param receiveId
     * @param sendId
     * @return
     */
    ResultVO getNewUnreadMsgIds(long receiveId, long sendId);

    /**
     * 删除和某个用户的对话，只是单向的，即当前用户删除后自己不可见，对方仍然可见
     * @param sendId
     * @param receiveId
     * @return
     */
    ResultVO deleteAllMsgByUser(long sendId, long receiveId);

    /**
     * 分页查询文章评论信息
     * @param bid 文章id
     * @param p 页码
     * @return
     */
    ResultVO getPageArticleComments(long bid, int p);

    /**
     * 添加文章评论
     * @param msgDTO
     * @return
     */
    ResultVO addComment(MsgDTO msgDTO);

    /**
     * 根据id删除评论
     * @param commentId
     * @return
     */
    ResultVO deleteCommentById(long commentId);

}
