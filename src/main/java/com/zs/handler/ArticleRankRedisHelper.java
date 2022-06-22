package com.zs.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zs.vo.BlogES;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * 文章排行榜redis帮助类 (ZSet结构)
 * @Created by zs on 2022/5/24.
 */
@Component("articleRankRedisHelper")
public class ArticleRankRedisHelper {

    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 添加一个文章信息到排行榜中
     * @param key
     * @param value
     * @return
     */
    public Boolean addToRank(String key, Object value, double score) throws JsonProcessingException {
        String s = objectMapper.writeValueAsString(value);
        if (Objects.isNull(s)) {
            return false;
        }
        return stringRedisTemplate.boundZSetOps(key).add(s, score);
    }

    /**
     * 获取某个区间的排行 redis指令 zrevrange key start end withscores
     * @param key
     * @param start
     * @param end
     * @return
     */
    public List<BlogES> getRankTopN(String key, int start, int end) throws JsonProcessingException {
        List<BlogES> list = new LinkedList<>();
        Set<ZSetOperations.TypedTuple<String>> typedTupleSet = stringRedisTemplate.boundZSetOps(key).reverseRangeWithScores(start, end);
        Iterator<ZSetOperations.TypedTuple<String>> iterator = typedTupleSet.iterator();
        while (iterator.hasNext()) {
            ZSetOperations.TypedTuple<String> next = iterator.next();
            String value = next.getValue();
            list.add(objectMapper.readValue(value, BlogES.class));
        }
        return list;
    }

}
