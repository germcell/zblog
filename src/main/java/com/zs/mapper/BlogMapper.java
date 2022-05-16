package com.zs.mapper;

import com.zs.pojo.Blog;
import com.zs.pojo.User;
import com.zs.vo.BlogVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Created by zs on 2022/3/3.
 */
@Mapper
public interface BlogMapper {

    /**
     * 查询所有记录
     * @return
     */
    List<Blog> listBlogs();

    /**
     * 查询指定cid的所有记录
     * @return
     */
    List<Blog> listBlogs(@Param("cid") Integer cid);


    /**
     * 条件查询
     * @param title 博客标题(模糊查询)
     * @param cid 博客分类id
     * @param isPublish 是否发布
     * @return
     */
    List<Blog> listConditionBlogs(@Param("title") String title,
                                  @Param("cid") Integer cid,
                                  @Param("isPublish") Boolean isPublish);

    /**
     * 根据博客id删除博客
     * @param bid
     * @return
     */
    int deleteBlogById(@Param("bid") Integer bid);

    /**
     * 插入一条记录
     * @param blog 博客内容
     * @param user 发布作者
     * @return
     */
    @Transactional
    int insertBlog(@Param("blog") Blog blog,
                   @Param("user") User user);

    /**
     * 动态条件查询，但至少具备一个条件，否则使用 listBlogs，返回博客的编辑视图
     * @param blog
     * @return
     */
    Blog getBlog(@Param("blog") Blog blog);

    /**
     * 按照文章id查询
     * @param bid
     * @return
     */
    BlogVO getBlogById(@Param("bid") Long bid) throws Exception;

    /**
     * 根据博客id修改记录
     * @param blog 条件，里面只包含了需要修改项的数据，其余为null
     * @param bid
     * @return
     * TODO 修改 tb_blog 表记录时，对应修改 tb_blogoutline 表记录
     */
    @Transactional
    int updateBlogById(@Param("blog") Blog blog,
                       @Param("bid") Long bid);

    /**
     * 根据bid查询博客的浏览视图
     * @param bid
     * @return
     */
    Blog getBlogView(@Param("bid") Long bid);

    /**
     * 查询blog表部分信息
     * @param bid
     * @return
     */
    Blog getBlogBaseMsg(@Param("bid") Long bid);

    /**
     * 添加一条记录
     * @param blog
     * @return 返回自增长主键
     */
    Long insert(@Param("blog") Blog blog);

    /**
     * 根据用户id和文章id查询记录
     * @param uid
     * @param bid
     * @return
     */
    Blog getBlogByBIdAndUid(@Param("uid") Long uid, @Param("bid") Long bid);

    /**
     * 根据uid和bid更新内容
     * @param blog
     * @return
     */
    int updateBlogByBIdAndUid(@Param("blog") Blog blog);

    /**
     * 按bid查询
     * @param bid 文章id
     * @return
     */
    Blog getBlogByBid(@Param("bid") Long bid);
}

