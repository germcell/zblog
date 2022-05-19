package com.zs.vo;

import com.zs.pojo.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * 文章详情内容
 * @Created by zs on 2022/4/21.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BlogVO {
    /**
     * 文章内容
     */
    private Blog blog;
    /**
     * 文章作者信息
     */
    private Writer writer;
    /**
     * 文章分类信息
     */
    private Category category;
    /**
     * 文章版权信息
     */
    private Copyright copyright;
    /**
     * 当前用户的热门文章概要信息
     */
    private List<BlogOutline> hotArticlesList;
}
