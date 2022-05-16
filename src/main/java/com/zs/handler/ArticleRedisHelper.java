package com.zs.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zs.config.ConstRedisKeyPrefix;
import com.zs.vo.BlogVO;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 文章信息缓存帮助类 (hash)
 * @Created by zs on 2022/5/13.
 */
@Component("articleRedisHelper")
public class ArticleRedisHelper {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private ObjectMapper om;

    /**
     * 获取已缓存到redis中的文章信息
     * @param key
     * @param filed
     * @return
     */
    public BlogVO getArticleCacheByBid(String key, String filed) throws JsonProcessingException {
        String v = (String)stringRedisTemplate.boundHashOps(key).get(filed);
        if (v != null) {
            return om.readValue(v, BlogVO.class);
        }
        return null;
    }

    /**
     * 缓存文章信息
     * @param key
     * @param filed
     * @param value
     */
    public void cacheArticle(String key, String filed, Object value) throws JsonProcessingException {
        String valueStr = om.writeValueAsString(value);
        stringRedisTemplate.boundHashOps(key).put(filed, valueStr);
    }

}
