package com.zs.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zs.dto.ThumbsDTO;
import com.zs.pojo.Fans;
import com.zs.pojo.Like;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * redis点赞帮助类
 * @Created by zs on 2022/5/12.
 */
@Component("thumbsRedisHelper")
public class ThumbsRedisHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThumbsRedisHelper.class);

    @Resource
    private ObjectMapper om;

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
     * @return
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
