package com.zs.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mysql.cj.log.Log;
import com.sun.glass.ui.Size;
import com.zs.config.Const;
import com.zs.config.Const2;
import com.zs.mapper.BlogMapper;
import com.zs.mapper.CategoryMapper;
import com.zs.pojo.Blog;
import com.zs.pojo.Category;
import com.zs.pojo.RequestResult;
import com.zs.service.CategoryService;
import com.zs.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Created by zs on 2022/2/24.
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private BlogMapper blogMapper;

    /**
     * 分页查询分类
     * @param currentPage 需查询的页数
     * @param rows 行数
     * @return
     */
    @Override
    public PageInfo<Category> pageCategory(int currentPage, int rows) {
        // 计算查询索引
//        int start = currentPage * rows - rows;
        PageHelper.startPage(currentPage, rows);
        PageInfo<Category> info = new PageInfo<>(categoryMapper.listCategories());
        return info;
    }

    /**
     * 新建分类
     * @param category
     * @return
     */
    @Override
    public int addCategory(Category category) throws Exception{
        return categoryMapper.insertCategory(category);
    }

    /**
     * 条件分页
     * @param condition
     * @param currentPage
     * @param rows
     * @return
     */
    @Override
    public PageInfo<Category> conditionPageCategory(String condition, Integer currentPage, int rows) {
        PageHelper.startPage(currentPage, rows);
        List<Category> list =  categoryMapper.listConditionCategories(condition);
        PageInfo<Category> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    /**
     * 删除分类
     * @param cid 被删除id
     * @return
     */
    @Transactional
    @Override
    public RequestResult deleteCategoryById(Integer cid) {
        RequestResult requestResult = new RequestResult();
        // 查询被删除分类是否有关联的博客
        List<Blog> listBlogsByCid = blogMapper.listConditionBlogs(null, cid, null);
        if (listBlogsByCid.size() == 0) {
            int rows = categoryMapper.deleteCategoryById(cid);
            requestResult.setCode(Const.DELETE_CATEGORY_SUCCESS);
            requestResult.setMessage("删除成功");
        } else {
            requestResult.setCode(Const.DELETE_CATEGORY_FAILED);
            requestResult.setMessage("删除失败 : 该分类下有关联博客");
        }
        return requestResult;
    }

    /**
     * 修改分类信息
     * @param category
     * @return
     */
    @Transactional
    @Override
    public RequestResult updateCategory(Category category) {
        RequestResult requestResult = new RequestResult();
        Category tempCondition = new Category();
        // 是否上传了文件
        if(category.getPicture() == null) {
            tempCondition.setCid(category.getCid());
            Category getCategoryById = categoryMapper.getCategory(tempCondition);
            // 是否更改了分类信息 (name、description)
            if (getCategoryById.getName().equals(category.getName())
                    && getCategoryById.getDescription().equals(category.getDescription())) {
                requestResult.setCode(Const.CATEGORY_ADD_FAILED);
                requestResult.setMessage("编辑失败:无更改项");
            } else {
                categoryMapper.updateCategoryById(category);
                requestResult.setCode(Const.CATEGORY_ADD_SUCCESS);
                requestResult.setMessage("编辑成功");
            }
        } else {
            tempCondition.setName(category.getName());
            Category getCategoryByName = categoryMapper.getCategory(tempCondition);
            // 是否已存在更改后的同名分类，并且 getCategoryByName != category， 通过cid判断
            if (getCategoryByName != null && getCategoryByName.getCid() != category.getCid()) {
                requestResult.setCode(Const.CATEGORY_ADD_FAILED);
                requestResult.setMessage("编辑失败:该分类已存在");
            } else {
                categoryMapper.updateCategoryById(category);
                requestResult.setCode(Const.CATEGORY_ADD_SUCCESS);
                requestResult.setMessage("编辑成功");
            }
        }
        return requestResult;
    }

    /**
     * 查询所有分类
     * @return
     */
    @Override
    public List<Category> listCategories() {
        return categoryMapper.listCategories();
    }

    /**
     * 查询所有分类(2.0)
     * @return
     */
    @Override
    public ResultVO listCategories(Integer status) {
        return new ResultVO(Const2.SERVICE_SUCCESS, "success", categoryMapper.listCategories());
    }

    /**
     * 查询前 6 个分类，按分类下所存在博客数量排序
     * @return
     */
    @Override
    public List<Category> listSortCategories() {
        List<Category> list = categoryMapper.listSortCategoriesByBlogs();
        if (list.size() > 6) {
            return list.subList(0, 6);
        }
        return list;
    }
}
