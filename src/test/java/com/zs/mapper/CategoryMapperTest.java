package com.zs.mapper;

import com.zs.pojo.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Created by zs on 2022/2/28.
 */
@SpringBootTest
class CategoryMapperTest {

    @Autowired
    CategoryMapper categoryMapper;

    @Test
    void listConditionCategories() {
        List<Category> list = categoryMapper.listConditionCategories("m");
        assertEquals(list.size(), 2);
    }

    @Test
    void listSortCategoriesByBlogsTest() {
        List<Category> list = categoryMapper.listSortCategoriesByBlogs();
        System.out.println(list);
    }

    @Test
    void listSortCategoriesAndBlogs() {

    }
}