package com.zs.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zs.pojo.Fans;
import com.zs.vo.ResultVO;

/**
 * @Created by zs on 2022/4/30.
 */
public interface FansService {

    /**
     * 关注用户（redis）
     *      先判断请求参数是否为空，然后判断该请求用户是否已经关注过此用户(一般前端会根据是否关注发送对应的请求，
     *      但有可能前端样式渲染有误造成这种情况),如果确认未关注后，则执行关注操作
     * @param fans 【uid被关注人】 【uid2主关注人】
     * @return
     */
    ResultVO becomeFans(Fans fans);

    /**
     * 查询用户的关注列表
     *    先查询redis，没有数据则查询DB
     * @param uid 用户id
     * @return
     */
    ResultVO listFansByUid(String uid);

    /**
     * 取关
     * @param fans
     * @return
     */
    ResultVO unFollow(Fans fans);

    /**
     * 同步redis中的关注信息到DB
     * @return
     */
//    ResultVO syncFans() throws JsonProcessingException;

}
