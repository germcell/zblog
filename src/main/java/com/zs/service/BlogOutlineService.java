package com.zs.service;

import com.zs.pojo.BlogOutline;
import com.zs.vo.ResultVO;

import java.util.List;

/**
 * @Created by zs on 2022/3/10.
 */
public interface BlogOutlineService {

    List<BlogOutline> listBlogOutlines();

    BlogOutline getBlogOutlineById(Long bid);


    /**
     * 分页查询文章概要
     * @param currentPage 查询页数
     * @return
     */
    ResultVO page(Integer currentPage);
}
