package com.zs.config;

/**
 * redis key/key前缀
 * @Created by zs on 2022/5/12.
 */
public class ConstRedisKeyPrefix {

    /**
     * 缓存文章信息key
     */
    public final static String ALL_ARTICLE_CACHE = "all_article_cache";

    /**
     * 每篇文章最新的点赞记录key前缀，结构为set
     */
    public final static String ARTICLE_NEW_LIKES_PREFIX = "articleNewLikes_";

    /**
     * 每篇文章的点赞数量，结构为string
     */
    public final static String ARTICLE_ALL_LIKES_PREFIX = "articleAllLikes_";

    /**
     * 首页数据
     */
    public final static String INDEX_PAGE_DATA = "indexPageData";

    /**
     * 用户数据库中的关注列表
     */
//    public final static String WRITER_FANS_TO_DB = "allWriterFansToDB_";

    /**
     * 用户的关注列表
     */
    public final static String WRITER_FANS_TO_REDIS = "allWriterFans_";

    /**
     * 文章分类信息缓存信息key
     */
    public static final String ALL_CATEGORIES = "allCategories";

    /**
     * 文章表达形式缓存信息key
     */
    public static final String ALL_COPYRIGHTS = "allCopyrights";

    /**
     * 文章排行榜key
     */
    public static final String ARTICLE_RANK = "articleRank";

}
