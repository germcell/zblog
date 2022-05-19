package com.zs.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zs.config.Const2;
import com.zs.pojo.Fans;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * 关注相关操作redis帮助类 (set结构)
 *     以【allWriterFans_】为key前缀的表示缓存的每个用户的关注列表
 * @Created by zs on 2022/5/1.
 */
@Component("fansRedisHelper")
public class FansRedisHelper {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 获取用户关注列表
     * @param key allWriterFans_ + 用户id
     * @return
     */
    public Set<String> getAllFans(String key) {
        Set<String> members = stringRedisTemplate.boundSetOps(key).members();
        if (Objects.equals(null, members) || members.size() == 0) {
            return null;
        }
        return members;
    }

    /**
     * 缓存用户关注列表
     *   遍历集合，将被关注用户id为value缓存到redis中
     * @param key allWriterFans_ + 用户id
     * @param value List<Fans>集合，需转为Set<String>
     */
    public void cacheAllFansData(String key, List<Fans> value) {
       value.stream().forEach((v) -> {
           follow(key, String.valueOf(v.getUid()));
       });
    }

    /**
     * 关注用户，添加一条记录
     * @param key allWriterFans_ + 用户id
     * @param value 被关注用户id
     * @return
     */
    public Long follow(String key, String value) {
        return stringRedisTemplate.boundSetOps(key).add(value);
    }

    /**
     * 检测用户是否已关注
     * @param key allWriterFans_ + 用户id
     * @param value 被关注人id
     * @return
     */
    public Boolean isFollow(String key, String value) {
        return stringRedisTemplate.boundSetOps(key).isMember(value);
    }

    /**
     * 取消关注
     * @param key allWriterFans_ + 取关发起人id （key）
     * @param value 被取关人id
     * return 1 成功 0 失败
     */
    public Long unfollow(String key, String value) {
        return stringRedisTemplate.boundSetOps(key).remove(value);
    }

    /**
     * 根据key匹配模式获取所有符合规则的key
     * @param pattern key匹配模式
     * @return
     */
    public Set<String> getPatternKey(String pattern) {
        Set<String> keys = stringRedisTemplate.keys(pattern);
        if (Objects.isNull(keys)) {
            return null;
        }
        if (keys.size() == 0) {
            return null;
        }
        return keys;
    }
}
