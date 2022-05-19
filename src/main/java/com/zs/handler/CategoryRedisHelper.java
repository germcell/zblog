package com.zs.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zs.config.ConstRedisKeyPrefix;
import com.zs.pojo.Category;
import com.zs.pojo.Copyright;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * 分类 、 版权信息缓存帮助类
 * @Created by zs on 2022/5/19.
 */
@Component("categoryRedisHelper")
public class CategoryRedisHelper {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private ObjectMapper om;

    /**
     * 缓存文章分类 / 文章版权信息
     * 一旦上述内容在数据库执行了任何增删改查操作，都应立即更新缓存
     * @param key
     * @param value
     */
    public void cache(String key, Object value) throws JsonProcessingException {
        String s = om.writeValueAsString(value);
        stringRedisTemplate.boundValueOps(key).set(s);
    }

    /**
     * 获取文章分类 / 文章版权信息缓存信息
     * @param key
     * @return
     */
    public List getAll(String key) throws JsonProcessingException {
        String s = stringRedisTemplate.boundValueOps(key).get();
        if (Objects.isNull(s)) {
            return null;
        }
        if (Objects.equals(ConstRedisKeyPrefix.ALL_CATEGORIES, key)) {
            JavaType javaType = om.getTypeFactory().constructParametricType(List.class, Category.class);
            List<Category> categories = om.readValue(s, javaType);
            return categories;
        }
        if (Objects.equals(ConstRedisKeyPrefix.ALL_COPYRIGHTS, key)) {
            JavaType javaType = om.getTypeFactory().constructParametricType(List.class, Copyright.class);
            List<Copyright> copyrights = om.readValue(s, javaType);
            return copyrights;
        }
        return null;
    }
}
