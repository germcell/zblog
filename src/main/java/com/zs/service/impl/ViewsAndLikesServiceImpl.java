package com.zs.service.impl;

import com.zs.handler.RedisUtils;
import com.zs.mapper.BlogMapper;
import com.zs.mapper.BlogOutlineMapper;
import com.zs.pojo.Blog;
import com.zs.pojo.BlogOutline;
import com.zs.pojo.Comment;
import com.zs.service.ViewsAndLikesService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.Set;

/**
 * @Created by zs on 2022/3/19.
 */
@Service
public class ViewsAndLikesServiceImpl implements ViewsAndLikesService {

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private BlogMapper blogMapper;

    @Resource
    private BlogOutlineMapper blogOutlineMapper;

    /**
     * +浏览量(暂存redis中)
     * @param bid
     */
    @Override
    public void addView(Long bid) {
        String value = redisUtils.getString(Long.toString(bid));
        if (value != null) {
            value = Long.toString(Long.parseLong(value) + 1);
            redisUtils.setString(String.valueOf(bid), value);
        } else {
            redisUtils.setString(String.valueOf(bid), 1 + "");
        }
    }

    /**
     * 持久化浏览量(同步到数据库)
     *      1、获取所有的键(暂时redis索引为1的库中只存储浏览量相关的数据)
     *      2、遍历键，获取对应的值，再查询数据库指定bid的views值
     *      3、键值 + views ，更新 tb_blog 和 tb_blogoutline 表
     *      4、更新后删除键
     *
     * FIXME 考虑同步数据时用户的访问
     */
    @Override
    public int saveView() {
        Set<String> keys = redisUtils.getKeys();
        if (keys.size() == 0) {
            return keys.size();
        }
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            // redis中的键 --> tb_blog的bid字段
            String nextKey = iterator.next();
            // redis中的值 --> tb_blog的views字段
            String nextValue = redisUtils.getString(nextKey);
            BlogOutline blogOutlineById = blogOutlineMapper.getBlogOutlineById(Long.parseLong(nextKey));
            Long views = blogOutlineById.getViews();
            views = views + Long.parseLong(nextValue);
            // 数据库同步
            BlogOutline conditionBO = new BlogOutline();
            conditionBO.setDid(blogOutlineById.getDid());
            conditionBO.setViews(views);
            blogOutlineMapper.updateByCondition(conditionBO);
            Blog conditionB = new Blog();
            conditionB.setBid(blogOutlineById.getDid());
            conditionB.setViews(views);
            blogMapper.updateBlogById(conditionB, conditionB.getBid());
        }
        // 删除redis数据
        redisUtils.delKeys(keys);
        return keys.size();
    }
}

