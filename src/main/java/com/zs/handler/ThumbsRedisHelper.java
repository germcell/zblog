package com.zs.handler;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * redis点赞帮助类
 *  用于缓存文章点赞记录，规则：
 *  1.进入文章详情页时，先获取文章的点赞数量，并以键前缀为 articleAllLikes_ 缓存（string）。
 *  2.用户触发新的点赞，则创建键前缀为 articleNewLikes_ 的记录（set），并将总数+1.
 *  3.用户触发取消点赞，则在对应的记录上删除，并将总数-1。
 *  4.以一天为一个周期，同步点赞总数到数据库中对应的文章。
 *  5.可选择定期（3天）清除 articleNewLikes_ 数据来减少内存占用，但不能保证用户只能为文章点赞一次。
 * @Created by zs on 2022/5/12.
 */
@Component("thumbsRedisHelper")
public class ThumbsRedisHelper {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 添加点赞记录
     * @param key   文章id
     * @param value 用户id
     * @return
     */
    public Boolean addLike(String key, String value) {
        Long add = stringRedisTemplate.boundSetOps(key).add(value);
        if (add > 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否已赞
     * @param key   文章id
     * @param value 用户id
     * @return
     */
    public Boolean isLike(String key, String value) {
        return stringRedisTemplate.boundSetOps(key).isMember(value);
    }

    /**
     * 累加点赞量
     * @param key 文章id
     */
    public void incrLikeNum(String key) {
        // TODO 并发时点赞数累加的问题，不过点赞数据不重要，就算并发导致累加结果不满足预期也可以接受
//        AtomicInteger likes = new AtomicInteger(Long.parseLong());
        stringRedisTemplate.boundValueOps(key).increment();
    }

    /**
     * 缓存文章点赞数量
     * @param key   文章id
     * @param value 文章点赞数
     */
    public void cacheArticleLikeNum(String key, String value) {
        stringRedisTemplate.boundValueOps(key).set(value);
    }

    /**
     * 获取文章点赞数
     * @param key
     * @return null key不存在
     *         Long 文章点赞数量
     */
    public Long getArticleLikeNum(String key) {
        String s = stringRedisTemplate.boundValueOps(key).get();
        if (s == null) {
            return null;
        }
        if (StringUtils.hasText(s)) {
            return Long.parseLong(s);
        }
        return null;
    }

    /**
     * 取消点赞，自减对应key的值
     * @param key
     * @return 自减后的文章点赞数量
     */
    public Long decrLikeNum(String key) {
        Long articleLikeNum = getArticleLikeNum(key);
        if (articleLikeNum == 0) {
            return null;
        }
        return stringRedisTemplate.boundValueOps(key).decrement();
    }

    /**
     * 取消点赞
     * @param key
     * @param value
     * @return
     */
    public Boolean removeLike(String key, String value) {
        Long remove = stringRedisTemplate.boundSetOps(key).remove(value);
        if (remove > 0) {
            return true;
        }
        return false;
    }
}
