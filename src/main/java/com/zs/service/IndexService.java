package com.zs.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zs.vo.ResultVO;

/**
 * @Created by zs on 2022/4/20.
 */
public interface IndexService {
    /**
     * 获取网站首页数据（未登录状态）
     * @return
     */
    ResultVO getIndexData();

    /**
     * 获取文章排行榜
     * @return
     */
    ResultVO getArticleRank() throws JsonProcessingException;
}
