package com.zs.handler;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @Created by zs on 2022/3/8.
 */
@Component("redisUtils")
public class RedisUtils {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public void setString(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    public String getString(String key) {
        String value = stringRedisTemplate.opsForValue().get(key);
        return value;
    }

    public Set<String> getKeys() {
        return stringRedisTemplate.keys("*");
    }

    public void delKeys(Set<String> keys) {
        stringRedisTemplate.delete(keys);
    }

}
