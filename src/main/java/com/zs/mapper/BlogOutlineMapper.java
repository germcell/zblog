package com.zs.mapper;

import com.zs.pojo.BlogOutline;
import com.zs.vo.BlogOutlineVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Created by zs on 2022/3/7.
 */
@Mapper
public interface BlogOutlineMapper {
    /**
     * 插入博客对应的概要信息
     * @param blogOutline
     * @return
     */
    int insert(@Param("blogOutline") BlogOutline blogOutline);

    /**
     * 浏览量降序查询
     * @return
     */
    List<BlogOutline> listSortByViewsBlogOutline();

    /**
     * 按id查询
     * @param bid
     * @return
     */
    BlogOutline getBlogOutlineById(@Param("bid") Long bid);

    /**
     * 删除
     * @param bid
     * @return
     */
    int deleteByBid(@Param("bid") Integer bid);

    /**
     * 条件更新
     * @param bo
     * @return
     */
    int updateByCondition(@Param("bo") BlogOutline bo);

    /**
     * 查询所有文章概要信息，按发布时间降序排序
     * @return
     */
    List<BlogOutlineVO> listBlogOutlines() throws Exception;

    /**
     * 查询指定作者的文章概要信息，（指定查询已发布、草稿状态）
     * @param uid 用户id
     * @param isPublish 是否发布
     * @return
     */
    List<BlogOutline> listBlogOutlinesByUid(@Param("uid") Integer uid,
                                            @Param(("isPublish")) Integer isPublish);

    /**
     * 根据用户id，文章id更新数据
     * @param bo
     * @return
     */
    int updateByBidAndUid(@Param("bo") BlogOutline bo);

    /**
     * 根据bid获取文章概要信息
     * @param bid 文章id
     * @return
     */
    BlogOutline getBlogOutlineByBid(@Param("bid") Long bid);
}
