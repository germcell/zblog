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
import java.util.Set;

/**
 * 关注相关操作redis帮助类
 * 以【F-id】为key的是新增的并已缓存到redis的关注信息，表示每个用户新增的
 * 以【DB-id】为key的是从DB中查询并已缓存到redis的关注信息，表示每个用户已关注的
 *
 * @Created by zs on 2022/5/1.
 */
@Component("fansRedisHelper")
public class FansRedisHelper {

    @Resource
    private ObjectMapper om;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 判断是否已关注用户 (set)
     * @param uid 关注人id （key）
     * @param uid2 被关注人id （value）
     * @return
     */
    public Boolean isFans(String uid, String uid2) {
        return stringRedisTemplate.boundSetOps(uid).isMember(uid2);
    }

    /**
     * 关注指定用户 (set)
     * @param uid 关注人id （key）
     * @param uid2 被关注人id （value）
     * @return 1 成功 0 失败
     */
    public Long becomeFans(String uid, String uid2) {
        return stringRedisTemplate.boundSetOps(uid).add(uid2);
    }

    /**
     * 取消关注 (set)
     * @param uid 取消发起人id （key）
     * @param uid2 被取消关注人id （value）
     * return 1 成功 0 失败
     */
    public Long unfollow(String uid, String uid2) {
         return stringRedisTemplate.boundSetOps(uid).remove(uid2);
    }

    /**
     * 返回指定用户存储在redis中的关注列表 (set) / 返回满足指定匹配模式指定的key
     * @param uid key
     * @return
     */
    public Set<String> allFans(String uid) {
        return stringRedisTemplate.boundSetOps(uid).members();
    }

    /**
     * 删除指定key中的value
     * @param key
     * @param value
     * @return
     */
    public Long removeValue(String key, String value) {
        return stringRedisTemplate.boundSetOps(key).remove(value);
    }

    /**
     * 删除指定key
     * @param key
     */
    public void removeKey(String key) {
        stringRedisTemplate.delete(key);
    }

    /**
     * 返回满足指定匹配模式指定的key
     * @param keyPattern key
     * @return
     */
    public Set<String> allFansOfKey(String keyPattern) {
        return stringRedisTemplate.keys(keyPattern);
    }

    /**
     * 获取从DB中获取并缓存的关注信息 (string)
     * @param key
     * @return 将string 转回 list
     */
    public List<Fans> allFansFromDBToList(String key) throws JsonProcessingException {
        String s = stringRedisTemplate.boundValueOps(key).get();
        if (s == null) {
            return null;
        }
        JavaType javaType = om.getTypeFactory().constructParametricType(ArrayList.class, Fans.class);
        return om.readValue(s, javaType);
    }

    /**
     * 缓存关注信息到redis,以string方式（json）
     * @param key
     * @param value
     */
    public void cacheRedis(String key, Object value) throws JsonProcessingException {
        String s = om.writeValueAsString(value);
        stringRedisTemplate.boundValueOps(key).set(s);
    }

    /**
     * 返回关注集合，取关集合的差集
     * @param k1
     * @param k2
     * @return
     */
    public Set<String> diff(String k1, String k2) {
        return stringRedisTemplate.opsForSet().difference(k1, k2);
    }

}
