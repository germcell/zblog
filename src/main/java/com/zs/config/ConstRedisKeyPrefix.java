package com.zs.config;

/**
 * redis key/key前缀
 * @Created by zs on 2022/5/12.
 */
public class ConstRedisKeyPrefix {
    /**
     * 每个用户的点赞记录key前缀
     */
    public final static String THUMBS_PREFIX = "thumbs_";

    /**
     * 缓存在redis中用户的所有点赞记录key前缀
     */
    public final static String USER_ALL_THUMBS = "all_thumbs_";
}
