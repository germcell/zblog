package com.zs.service;

import com.github.pagehelper.PageInfo;
import com.zs.pojo.*;
import com.zs.vo.ResultVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Created by zs on 2022/3/3.
 */
public interface BlogService {

    /**
     * 分页查询博客
     * @param currentPage
     * @param rows
     * @return
     */
    PageInfo<Blog> listPageBlogs(Integer currentPage, Integer rows);

    /**
     * 条件分页查询博客
     * @param currentPage 页码
     * @param rows 每页行数
     * @param title 博客标题
     * @param categoryId  博客所属分类id
     * @param isPublish  是否发布
     * @return
     */
    PageInfo<Blog> pageConditionBlog(Integer currentPage, Integer rows, String title, Integer categoryId, Boolean isPublish);

    /**
     * 按博客id删除博客
     * @param bid
     * @return
     */
    RequestResult deleteBlogById(Integer bid);

    /**
     * 保存一条博客
     * @param blog 博客内容
     * @param loginUser 发布作者
     * @return
     */
    RequestResult insertBlog(Blog blog, User loginUser);

    /**
     * 查询博客
     * @param bid
     * @return
     */
    ResultVO getBlogById(Long bid);

    /**
     * 按照博客id编辑博客
     * @param blog
     * @param bid
     * @return
     */
    RequestResult updateBlog(Blog blog, Long bid) throws Exception;

    /**
     * 查询浏览量前 10 的文章概要
     * @return
     */
    List<BlogOutline> listRecommendBlog();

    /**
     * 查询博客，并将内容由 Markdown 转为 HTML
     * @param bid
     * @return
     */
    ResultVO getBlogByIdAndConvert(Long bid);

    /**
     * 分页查询指定分类的博客
     * @param cid
     * @return
     */
    PageInfo<Blog> listPageBlogsByCid(Integer currentPage, Integer rows, Integer cid);

    /**
     * 查询博客基本信息(留言管理显示)
     * @param bid
     * @return
     */
    Blog getBlogBaseMsg(Long bid);

    /**
     * 发布文章（2.0）
     * @param blog 文章内容
     * @param firstPicture 文章封面
     * @param mail 用户邮箱
     * @return
     */
    ResultVO addArticle(Blog blog, MultipartFile firstPicture, String mail);

    /**
     * 更新文章状态
     * @param bid 文章id
     * @param status 状态 1：发布 0：草稿
     * @param uid 用户id
     * @return
     */
    ResultVO updateArticleStatus(String bid, String status, String uid);

    /**
     * 删除文章
     * @param uid 用户id
     * @param bid 文章id
     * @return
     */
    ResultVO deleteBlogById2(String uid, String bid);

    /**
     * 查询指定文章markdown形式内容
     * @param uid 用户id
     * @param bid 文章id
     * @return
     */
    ResultVO getEditViewBlogById(String uid, String bid);

    /**
     * 编辑文章（2.0）
     * @param blog 编辑后内容
     * @param file 上传封面
     * @return
     */
    ResultVO updateBlog2(Blog blog, MultipartFile file);

    /**
     * 内容搜索
     * @param keyword 关键词
     * @param p 页码
     * @param searchType 搜索类别， 为userInfo表示搜索用户，反之搜索文章
     * @return
     */
    ResultVO search(String keyword, int p, String searchType);
}
