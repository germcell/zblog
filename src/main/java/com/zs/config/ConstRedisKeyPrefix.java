package com.zs.config;

/**
 * redis key/key前缀
 * @Created by zs on 2022/5/12.
 */
public class ConstRedisKeyPrefix {
    /**
     * 每个用户的点赞记录key前缀
     */
    public final static String THUMBS_PREFIX = "new_thumbs_";

    /**
     * 缓存在redis中用户的所有点赞记录key前缀
     */
    public final static String USER_ALL_THUMBS = "all_thumbs_";

    /**
     * 缓存文章信息key
     */
    public final static String ALL_ARTICLE_CACHE = "all_article_cache";

    /**
     * 每篇文章最新的点赞记录key前缀
     */
    public final static String ARTICLE_NEW_LIKES_PREFIX = "articleNewLikes_";

    /**
     * 每篇文章的点赞数
     */
    public final static String ARTICLE_ALL_LIKES_PREFIX = "articleAllLikes_";
}
