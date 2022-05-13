package com.zs.mapper;

import com.zs.pojo.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Created by zs on 2022/2/24.
 */
@Mapper
public interface CategoryMapper {

    /**
     * 查询所有分类信息
     * @return
     */
    List<Category> listCategories();

    /**
     * 插入一条分类信息
     * @param category
     * @return
     */
    @Transactional
    int insertCategory(@Param("category") Category category) throws Exception;

    /**
     * 条件查询
     * @param condition
     * @return
     */
    List<Category> listConditionCategories(@Param("condition") String condition);

    /**
     * 按cid删除记录
     * @param cid
     * @return
     */
    @Transactional
    int deleteCategoryById(@Param("cid") Integer cid);

    /**
     * 动态条件查询sql
     * @param category
     * @return
     */
    Category getCategory(@Param("category") Category category);

    /**
     * 按cid更新记录
     * @param category
     * @return
     */
    @Transactional
    int updateCategoryById(@Param("category") Category category);

    /**
     * 按分类所拥有的博客数量排序(降序)
     * @return
     */
    List<Category> listSortCategoriesByBlogs();
}
