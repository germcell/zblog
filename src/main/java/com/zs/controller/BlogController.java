package com.zs.controller;

import com.zs.config.Const;
import com.zs.config.Const2;
import com.zs.handler.RandomUtils;
import com.zs.handler.UploadUtils;
import com.zs.pojo.Blog;
import com.zs.pojo.EditorJson;
import com.zs.service.BlogService;
import com.zs.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Created by zs on 2022/4/21.
 */
@Api(tags = "文章处理接口")
@RestController
@CrossOrigin
@RequestMapping("/v2/article")
public class BlogController {

    @Resource
    private BlogService blogService;

    @Resource
    private HashMap<String, List<String>> imageMap;

    @ApiOperation(value = "发布文章", notes = "不推荐测试,参数不满足")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "blog", value = "文章对象", required = true, paramType = "form", dataTypeClass = Blog.class),
        @ApiImplicitParam(name = "token", value = "用户身份token", required = true, paramType = "header", dataTypeClass = String.class),
        @ApiImplicitParam(name = "mail", value = "用户邮箱", required = true, paramType = "form", dataTypeClass = String.class),
        @ApiImplicitParam(name = "file", value = "文章封面文件", required = false, paramType = "form", dataTypeClass = MultipartFile.class)
    })
    @PostMapping("/add")
    public ResultVO addArticle(Blog blog,
                               @RequestHeader String token,
                               String mail,
                               MultipartFile file) {
        return blogService.addArticle(blog, file, mail);
    }

    @ApiOperation("更改文章状态")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "u", value = "用户id", required = true, paramType = "path", dataTypeClass = String.class),
        @ApiImplicitParam(name = "b", value = "文章id", required = true, paramType = "path", dataTypeClass = String.class),
        @ApiImplicitParam(name = "s", value = "状态值,1:发布 0:草稿", required = true, paramType = "path", dataTypeClass = String.class),
        @ApiImplicitParam(name = "token", value = "用户身份token", required = true, paramType = "header", dataTypeClass = String.class)
    })
    @PutMapping("/status/{u}/{b}/{s}")
    public ResultVO updateStatus(@PathVariable("u") String u,
                                 @PathVariable("s") String s,
                                 @PathVariable("b") String b,
                                 @RequestHeader String token) {
        return blogService.updateArticleStatus(b, s, u);
    }

    @ApiOperation("删除文章")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "u", value = "用户id", required = true, paramType = "path", dataTypeClass = String.class),
        @ApiImplicitParam(name = "b", value = "文章id", required = true, paramType = "path", dataTypeClass = String.class)
    })
    @DeleteMapping("/{u}/{b}")
    public ResultVO delete(@PathVariable("u") String u,
                           @PathVariable("b") String b) {
        return blogService.deleteBlogById2(u, b);
    }

    @ApiOperation("查询文章的markdown格式")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "uid", value = "用户id", required = true, paramType = "path", dataTypeClass = String.class),
        @ApiImplicitParam(name = "bid", value = "文章id", required = true, paramType = "path", dataTypeClass = String.class),
        @ApiImplicitParam(name = "token", value = "用户身份token", required = true, paramType = "header", dataTypeClass = String.class)
    })
    @GetMapping("/{uid}/{bid}")
    public ResultVO get(@PathVariable("uid") String uid,
                        @PathVariable("bid") String bid,
                        @RequestHeader String token) {
        return blogService.getEditViewBlogById(uid, bid);
    }

    @ApiOperation(value = "编辑文章", notes = "不推荐测试,参数不满足")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "blog", value = "文章对象", required = true, paramType = "body", dataTypeClass = Blog.class),
        @ApiImplicitParam(name = "bid", value = "文章id", required = true, paramType = "path", dataTypeClass = String.class),
        @ApiImplicitParam(name = "token", value = "用户身份token", required = true, paramType = "header", dataTypeClass = String.class),
        @ApiImplicitParam(name = "file", value = "文章封面文件", required = false, paramType = "form", dataTypeClass = MultipartFile.class)
    })
    @PutMapping("/{bid}")
    public ResultVO update(Blog blog,
                           @PathVariable("bid") String bid,
                           @RequestHeader String token,
                           MultipartFile file) {
        return blogService.updateBlog2(blog, file);
    }

    /**
     * 处理editor-md编辑器上传图片
     * 在ZBLOG单体架构时可用，前后端分离后不可用
     * @param file
     * @param request
     * @return
     */
    @Deprecated()
    @ApiIgnore()
    @PostMapping("/editormd/images")
    public EditorJson imagesHandler(@RequestParam("editormd-image-file") MultipartFile file,
                                    HttpServletRequest request) {

        EditorJson result = new EditorJson();
        try {
            ResultVO uploadResultVO = UploadUtils.uploadFileHandler(file, RandomUtils.generateRandomNum(),
                                                                    Const.BLOG_CONTENT_PICTURE_SIZE,
                                                                    Const.PICTURE_SUPPORT_FORMAT, 0);

            if (uploadResultVO.getCode() == 538) {
                String remoteAddr = request.getRemoteAddr();
                List<String> listDir = imageMap.get(remoteAddr);
                if (listDir == null) {
                    listDir = new ArrayList<>();
                    listDir.add((String) uploadResultVO.getData());
                    imageMap.put(remoteAddr, listDir);
                } else {
                    listDir.add((String) uploadResultVO.getData());
                }
            }  else {
                return new EditorJson(0, "file size or file type not support", null);
            }
            // 返回处理结果
            result.setSuccess(1);
            result.setMessage("upload success");
            String os = System.getProperty("os.name");
            if (os.contains("windows") || os.contains("Windows")) {
                result.setUrl(Const2.ARTICLE_PICTURE_SAVE_DIR_WINDOWS + uploadResultVO.getData());
            } else if (os.contains("linux") || os.contains("Linux")) {
                result.setUrl(Const2.ARTICLE_PICTURE_SAVE_DIR_LINUX + uploadResultVO.getData());
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new EditorJson(0, "fail", null);
        }
    }
}


