package com.zs.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zs.config.Const;
import com.zs.mapper.CategoryMapper;
import com.zs.pojo.Category;
import com.zs.pojo.RequestResult;
import com.zs.service.CategoryService;
import org.apache.ibatis.annotations.Param;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.file.Path;
import java.util.List;

/**
 * @Created by zs on 2022/2/24.
 */
@Controller
@RequestMapping("/admin")
public class CategoryController {

    private static Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    CategoryService categoryService;

    /**
     * 分页
     * @param currentPage 当前页
     * @param model 数据存域
     * @return
     */
    @GetMapping("/category/{currentPage}")
    public String page(@PathVariable("currentPage") Integer currentPage, Model model) {
        if (currentPage == null) {
            currentPage = 1;
        }
        PageInfo<Category> pageInfo = categoryService.pageCategory(currentPage, Const.CATEGORY_PAGE_ROWS);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("condition", null);
        return "admin/category";
    }

    /**
     * 新建分类,分类图片接收采取先将其存入到本地项目路径下,再通过重启服务器把图片添加到 target 文件中的方式
     * @param uid 创建人id
     * @param category 分类信息
     * @param pictureFile 上传图片
     * @param request
     * @return
     */
    @PostMapping("/category/add")
    @ResponseBody
    public RequestResult add(Integer uid,
                             Category category,
                             MultipartFile pictureFile,
                             HttpServletRequest request) {

        RequestResult requestResult = new RequestResult();
        // 文件大小判断
        if (pictureFile.getSize() > (1024 * 1024)) {
            requestResult.setCode(Const.CATEGORY_ADD_FAILED);
            requestResult.setMessage("新建失败:上传文件大小不能超过1MB");
            return requestResult;
        } else if (category != null && pictureFile != null) {
            String originalFilename = pictureFile.getOriginalFilename();
            String ext = originalFilename.substring(originalFilename.lastIndexOf('.'));
            // 文件类型判断
            if (Const.PICTURE_SUPPORT_FORMAT.contains(ext)) {
                // 生成文件在服务器端保存的文件名
                String serverFileName = System.currentTimeMillis() + ext;
                // 获取项目工作目录 D:\IDEA_workspace01\blog
                String parentDir = System.getProperty("user.dir");
                // 文件存储目录,存储在本地，需重启服务器后才能访问
                String childrenDir = "/src/main/resources/static/images/category/" + serverFileName;
                // 本地的绝对路径，也就是文件存储的目录
                File destPath = new File(parentDir + childrenDir);
                try {
                    // 存储文件
                    pictureFile.transferTo(destPath);
                } catch (Exception e) {
                    e.printStackTrace();
                    requestResult.setCode(Const.CATEGORY_ADD_FAILED);
                    requestResult.setMessage("新建失败:图片保存失败");
                    return requestResult;
                }
                // 生成数据库中存储的访问路径
                String dataFileName = "/res/images/category/" + serverFileName;
                category.setPicture(dataFileName);
                category.setUid(uid);
                // 调用业务层插入记录
                int result = 0;
                try {
                    result = categoryService.addCategory(category);
                    if (result == 1) {
                        requestResult.setCode(Const.CATEGORY_ADD_SUCCESS);
                        requestResult.setMessage("新建成功");
                    } else {
                        requestResult.setCode(Const.CATEGORY_ADD_FAILED);
                        requestResult.setMessage("新建失败:数据库存储失败");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    requestResult.setCode(Const.CATEGORY_ADD_FAILED);
                    requestResult.setMessage("新建失败:已有此分类");
                    return requestResult;
                }
            } else {
                requestResult.setCode(Const.CATEGORY_ADD_FAILED);
                requestResult.setMessage("新建失败:不支持的文件格式");
            }
        } else {
            requestResult.setCode(Const.CATEGORY_ADD_FAILED);
            requestResult.setMessage("新建失败:表单提交参数为空");
        }
        return requestResult;
    }

    /**
     * 分页条件查询
     * @param condition
     * @param currentPage
     * @param model
     * @return
     */
    @GetMapping("/category/{condition}/{currentPage}")
    public String conditionPage(@PathVariable("condition") String condition,
                                @PathVariable("currentPage") Integer currentPage,
                                Model model) {
        if (StringUtils.hasText(condition)) {
            if (!(currentPage != null)) {
                currentPage = 1;
            }
            PageInfo<Category> pageInfo = categoryService.conditionPageCategory(condition,
                                                                                currentPage,
                                                                                Const.CATEGORY_PAGE_ROWS);
            model.addAttribute("pageInfo", pageInfo);
            model.addAttribute("condition", condition);
            return "admin/category";
        } else {
            return "redirect:/admin/category/1";
        }
    }

    /**
     * 删除分类请求
     * @param cid
     * @return
     */
    @DeleteMapping("/category/del/{cid}")
    @ResponseBody
    public RequestResult delete(@PathVariable("cid") Integer cid) {
        RequestResult requestResult = categoryService.deleteCategoryById(cid);
        return requestResult;
    }

    /**
     * 编辑分类请求
     * @param cid 分类id
     * @param category 编辑后的分类数据
     * @param pictureFile
     * @return
     */
    @PutMapping("/category/update/{cid}")
    @ResponseBody
    public RequestResult update(@PathVariable("cid") Integer cid, Category category, MultipartFile pictureFile) {
        RequestResult requestResult = new RequestResult();
        // 上传文件处理
        if (pictureFile != null && pictureFile.getSize() > 0) {
            String ext = pictureFile
                            .getOriginalFilename()
                            .substring(pictureFile.getOriginalFilename().lastIndexOf('.'));
            if (Const.PICTURE_SUPPORT_FORMAT.contains(ext)) {
                if (pictureFile.getSize() < (1024 * 1024)) {
                    String serverSaveFileName = System.currentTimeMillis() + ext;
                    String parentDir = System.getProperty("user.dir");
                    String destDir = parentDir + Const.CATEGORY_PICTURE_SAVE_DIR + serverSaveFileName;
                    try {
                        pictureFile.transferTo(new File(destDir));
                        category.setPicture(Const.CATEGORY_PICTURE_ACCESS_DIR + serverSaveFileName);
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.info("上传文件处理异常{}", e);
                        requestResult.setCode(Const.CATEGORY_ADD_FAILED);
                        requestResult.setMessage("编辑失败:500");
                        return requestResult;
                    }
                } else {
                    requestResult.setCode(Const.CATEGORY_ADD_FAILED);
                    requestResult.setMessage("编辑失败:上传文件大于1MB");
                    return requestResult;
                }
            } else {
                requestResult.setCode(Const.CATEGORY_ADD_FAILED);
                requestResult.setMessage("编辑失败:不支持的文件格式");
                return requestResult;
            }
        }
        // 表单处理
        requestResult = categoryService.updateCategory(category);
        return requestResult;
    }

}
