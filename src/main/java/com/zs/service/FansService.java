package com.zs.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zs.pojo.Fans;
import com.zs.vo.ResultVO;

/**
 * @Created by zs on 2022/4/30.
 */
public interface FansService {

    /**
     * 关注用户
     * @param fans 【uid关注人】 【uid2被关注人】
     * @return
     */
    ResultVO becomeFans(Fans fans);

    /**
     * 查询当前用户已关注的作者
     * @param uid
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
    ResultVO syncFans() throws JsonProcessingException;

}
