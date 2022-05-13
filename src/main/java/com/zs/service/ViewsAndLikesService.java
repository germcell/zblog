package com.zs.service;

/**
 * 处理浏览量和点赞业务
 * @Created by zs on 2022/3/19.
 */
public interface ViewsAndLikesService {

    /**
     * +浏览量(暂存redis中)
     * @param bid
     */
    void addView(Long bid);

    /**
     * 持久化浏览量(同步到数据库)
     */
    int saveView();

}
