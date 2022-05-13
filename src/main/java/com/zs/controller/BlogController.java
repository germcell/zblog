package com.zs.controller;

import com.github.pagehelper.PageInfo;
import com.zs.config.Const;
import com.zs.handler.UniversalException;
import com.zs.handler.UploadUtils;
import com.zs.pojo.Blog;
import com.zs.pojo.EditorJson;
import com.zs.pojo.RequestResult;
import com.zs.pojo.User;
import com.zs.service.BlogService;
import com.zs.service.CategoryService;
import com.zs.service.CopyrightService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * @Created by zs on 2022/2/22.
 */
@Slf4j
@Controller
@RequestMapping("/admin")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CopyrightService copyrightService;

    @Resource
    private HashMap<String, List<String>> imageMap;

    /**
     * 分页查询
     * @param currentPage
     * @param model
     * @return
     */
    @GetMapping("/blogs/page/{currentPage}")
    public String page(@PathVariable Integer currentPage, Model model) {
        if (currentPage == null) {
            currentPage = 1;
        }
        model.addAttribute("title", null);
        model.addAttribute("pageInfo", blogService.listPageBlogs(currentPage, Const.BLOG_PAGE_ROWS));
        model.addAttribute("categories", categoryService.listCategories());
        return "admin/blog";
    }

    /**
     * 条件分页查询
     * @param currentPage 当前页码
     * @param title 博客标题
     * @param categoryId 博客所属分类id
     * @param isPublish 是否已发布
     * @param model
     * @return
     */
    @GetMapping("/blogs/page/{currentPage}/search")
    public String conditionPage(@PathVariable("currentPage") Integer currentPage,
                                @RequestParam(value = "title", required = false) String title,
                                @RequestParam("categoryId") Integer categoryId,
                                @RequestParam(value = "isPublish", required = false) String[] isPublish,
                                Model model) {
        /* 查询条件为空时 */
        if (!StringUtils.hasText(title) && categoryId == 0 && isPublish == null) {
            return "redirect:/admin/blogs/page/1";
        }
        /* 有查询条件时 */
        Boolean isPublished = false;
        if (isPublish != null) {
            isPublished = true;
        }
        PageInfo<Blog> pageInfo = blogService.pageConditionBlog(currentPage, Const.BLOG_PAGE_ROWS, title, categoryId, isPublished);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("categories", categoryService.listCategories());
        model.addAttribute("title", title);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("isPublish", isPublish);
        return "admin/blog";
    }

    /**
     * 删除博客
     * @param bid
     * @return
     */
    @DeleteMapping("/blogs/del/{bid}")
    @ResponseBody
    public RequestResult blogDelte(@PathVariable("bid") Integer bid) {
        if (bid != null) {
            return blogService.deleteBlogById(bid);
        }
        RequestResult requestResult = new RequestResult();
        requestResult.setCode(Const.DELETE_BLOG_FAILED);
        requestResult.setMessage("删除失败 : 不存在此分类");
        return requestResult;
    }

    /**
     * 转发到编辑博客页面
     * @return
     */
    @GetMapping("/blogs/edit/{bid}")
    public String blogAddPage(@PathVariable("bid") Long bid, Model model) {
        model.addAttribute("categories", categoryService.listCategories());
        model.addAttribute("copyright", copyrightService.listCopyright());
        model.addAttribute("requestResult", null);
        // 前端写死了，当url的bid为0时，表示是发布博客，反之为编辑博客
        if (bid == 0) {
            /*
                由于编辑博客和发布博客公用一个页面，而编辑博客需要回显数据，但发布博客不需要回显数据，
                所以在发布博客时也需要在model中传递一个blog对象，并且设置分类、版权分类的默认值，
                否则当blog为null时，thymeleaf会报错
            */
            Blog blog = new Blog();
            blog.setCid(1);
            blog.setCrTipId(1);
            model.addAttribute("blog", blog);
            return "admin/blog_add";
        } else {
            model.addAttribute("blog", blogService.getBlogById(bid));
            return "admin/blog_add";
        }
    }

    /**
     * 编辑/发布博客
     * @param blog
     * @param loginUser
     * @param attributes
     * @return
     */
    @PostMapping({"/blogs/do_edit/{bid}","/blogs/do_edit/"})
    public String doEdit(Blog blog,
                         RedirectAttributes attributes,
                         MultipartFile firstPictureFile,
                         @PathVariable(value = "bid", required = false) Long bid,
                         @RequestParam(value = "isPublish", required = false) String isPublish,
                         @RequestParam(value = "isAppreciate", required = false) String[] isAppreciate,
                         @RequestParam(value = "isComment", required = false) String[] isComment,
                         @SessionAttribute("loginUser") User loginUser) {

        RequestResult requestResult = new RequestResult();

        /* 上传图片处理 */
        try {
            String serverSaveFileName = UploadUtils.uploadPictureHandler(firstPictureFile, loginUser.getNickname(),
                                                                         Const.BLOG_FIRST_PICTURE_SIZE);
            if (serverSaveFileName == null) {
                requestResult.setCode(Const.EDIT_BLOG_FAILED);
                requestResult.setMessage("博客发布失败 : 首图大小应在3MB内，支持的格式为"+Const.PICTURE_SUPPORT_FORMAT);
                attributes.addFlashAttribute("requestResult",requestResult);
                return "redirect:/admin/blogs/page/1";
            } else if("".equals(serverSaveFileName)) {
                blog.setFirstPicture(serverSaveFileName);
            } else {
                blog.setFirstPicture(Const.BLOG_FIRST_PICTURE_ACCESS_DIR + serverSaveFileName);
            }
        } catch (Exception e) {
            e.printStackTrace();
            requestResult.setCode(Const.EDIT_BLOG_FAILED_UPLOAD_EXCEPTION);
            requestResult.setMessage("博客发布失败 : 图片上传失败");
            attributes.addFlashAttribute("requestResult",requestResult);
            return "redirect:/admin/blogs/page/1";
            // TODO 发布、编辑失败后，已编辑数据的回显
        }

        /* 表单数据处理 */
        if (StringUtils.hasText(blog.getTitle()) && StringUtils.hasText(blog.getContent())) {
            // FIXME 防止前端将表单value值人为改变，可将equals替换为contains方法模糊判断
            // 将前端boolean值以字符串的形式传递，后端再将字符串改为对应的boolean类型
            if ("true".equals(isPublish)) {
                blog.setIsPublish(true);
            } else {
                blog.setIsPublish(false);
            }
            if (isAppreciate != null && "true".equals(isAppreciate[0])) {
                blog.setIsAppreciate(true);
            } else {
                blog.setIsAppreciate(false);
            }
            if (isComment != null && "true".equals(isComment[0])) {
                blog.setIsComment(true);
            } else {
                blog.setIsComment(false);
            }
            // 参数bid为null时表示在做发布博客操作
            if (bid == null) {
                requestResult = blogService.insertBlog(blog, loginUser);
                attributes.addFlashAttribute("requestResult", requestResult);
            } else {
                // 更新博客内容
                try {
                    requestResult = blogService.updateBlog(blog, bid);
                    attributes.addFlashAttribute("requestResult", requestResult);
                } catch (Exception e) {
                    e.printStackTrace();
                    requestResult.setCode(Const.EDIT_BLOG_FAILED);
                    requestResult.setMessage("博客发布异常");
                    attributes.addFlashAttribute("requestResult", requestResult);
                }
            }
        } else {
            requestResult.setCode(Const.EDIT_BLOG_FAILED_CONTENT_IS_NULL);
            requestResult.setMessage("博客发布失败 : 无发布内容");
            attributes.addFlashAttribute("requestResult", requestResult);
        }
        return "redirect:/admin/blogs/page/1";
    }

    /**
     * 处理editormd的上传图片请求
     * @param file
     * @param user
     * @return
     */
    @PostMapping("/editormd/images")
    @ResponseBody
    public EditorJson imagesHandler(@RequestParam("editormd-image-file") MultipartFile file,
                                    @SessionAttribute("loginUser") User user,
                                    HttpServletRequest request) {

        EditorJson result = new EditorJson();
        try {
            String accessDir = UploadUtils.uploadPictureHandler(file, user.getNickname(), Const.BLOG_CONTENT_PICTURE_SIZE);
            if (accessDir == null || Objects.equals("", accessDir)) {
                result.setSuccess(0);
                result.setMessage("错误 : 只支持5MB的jpg,jpeg,png格式的图片");
            } else {
                // 保存处理信息(请求ip，图片路径)
                String remoteAddr = request.getRemoteAddr();
                List<String> listDir = imageMap.get(remoteAddr);
                if (listDir == null) {
                    listDir = new ArrayList<>();
                    listDir.add(accessDir);
                    imageMap.put(remoteAddr, listDir);
                } else {
                    listDir.add(accessDir);
                }
                // 返回处理结果
                result.setSuccess(1);
                result.setMessage("upload success");
                result.setUrl(Const.BLOG_FIRST_PICTURE_ACCESS_DIR + accessDir);
            }
        } catch (Exception e) {
            throw new UniversalException("图片上传失败,格式或大小错误");
        }
        return result;
    }

}
