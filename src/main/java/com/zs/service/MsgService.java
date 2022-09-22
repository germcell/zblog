package com.zs.service;

import com.zs.vo.ResultVO;

import java.util.List;

/**
 * @author zengshuai
 * @create 2022-09-10 21:26
 */
public interface MsgService {

    /**
     * 获取指定用户的私信信息
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
}
