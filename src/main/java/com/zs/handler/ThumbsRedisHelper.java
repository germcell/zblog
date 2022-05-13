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

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
     * 将点赞记录缓存到redis中
     * @param key
     * @param value
     * @return
     */
    public Boolean like(String key, ThumbsDTO value) throws JsonProcessingException {
        String s = om.writeValueAsString(value);
        Long add = stringRedisTemplate.boundSetOps(key).add(s);
        if (add > 0) {
            return true;
        }
        return false;
    }

    /**
     * 缓存用户所有点赞记录
     */
    public Boolean cacheUserLikes(String key, List<Like> likes) throws JsonProcessingException {
        String value = om.writeValueAsString(likes);
        stringRedisTemplate.boundValueOps(key).set(value);
        return true;
    }

    public List<Like> getUserLikes(String key) throws JsonProcessingException {
        String s = stringRedisTemplate.boundValueOps(key).get();
        if (s == null) {
            return null;
        }
        JavaType javaType = om.getTypeFactory().constructParametricType(ArrayList.class, Like.class);
        return om.readValue(s, javaType);
    }
}
