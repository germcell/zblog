package com.zs.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zs.config.Const;
import com.zs.config.Const2;
import com.zs.handler.RedisUtils;
import com.zs.mapper.BlogOutlineMapper;
import com.zs.pojo.BlogOutline;
import com.zs.service.BlogOutlineService;
import com.zs.vo.BlogOutlineVO;
import com.zs.vo.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Created by zs on 2022/3/10.
 */
@Service
public class BlogOutlineServiceImpl implements BlogOutlineService {

    private Logger logger = LoggerFactory.getLogger(BlogOutlineServiceImpl.class);

    @Resource
    private BlogOutlineMapper blogOutlineMapper;

    @Override
    public List<BlogOutline> listBlogOutlines() {
        return  blogOutlineMapper.listSortByViewsBlogOutline();
    }

    @Override
    public BlogOutline getBlogOutlineById(Long bid) {
        return blogOutlineMapper.getBlogOutlineById(bid);
    }

    /**
     * 分页查询文章概要
     * @param currentPage 查询页数
     * @return
     */
    @Override
    public ResultVO page(Integer currentPage) {
        if (currentPage == null) {
            logger.info("首页文章分类查询失败，页码为null");
            return new ResultVO(Const2.SERVICE_FAIL, "fail: page is null", null);
        }
        try {
            PageHelper.startPage(currentPage, Const.BLOG_PAGE_ROWS);
            List<BlogOutlineVO> blogOutlineVOS = blogOutlineMapper.listBlogOutlines();
            PageInfo<BlogOutlineVO> pageInfo = new PageInfo<>(blogOutlineVOS);
            return new ResultVO(Const2.SERVICE_SUCCESS, "success", pageInfo);
        } catch (Exception e) {
            logger.info("首页文章分类查询失败");
            return new ResultVO(Const2.SERVICE_FAIL, "fail", null);
        }
    }


}
