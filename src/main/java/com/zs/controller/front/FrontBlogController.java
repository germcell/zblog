package com.zs.controller.front;

import com.zs.config.Const;
import com.zs.config.Const2;
import com.zs.handler.RandomUtils;
import com.zs.handler.UniversalException;
import com.zs.handler.UploadUtils;
import com.zs.pojo.Blog;
import com.zs.pojo.EditorJson;
import com.zs.pojo.User;
import com.zs.service.BlogService;
import com.zs.vo.ResultVO;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * @Created by zs on 2022/4/21.
 */
@RestController
@CrossOrigin
@RequestMapping("/v2/article")
public class FrontBlogController {

    @Resource
    private BlogService blogService;

    @Resource
    private HashMap<String, List<String>> imageMap;

    /**
     * 发布文章（2.0）
     * @param blog 文章内容
     * @param token 用户认证令牌
     * @param mail 用户邮箱
     * @param file 文章封面
     * @return
     */
    @PostMapping("/add")
    public ResultVO addArticle(Blog blog,
                               @RequestHeader String token,
                               String mail,
                               MultipartFile file) {
        return blogService.addArticle(blog, file, mail);
    }


    /**
     * 更新文章状态
     * @param s 状态 1：发布 0：草稿
     * @param b 文章id
     * @param u 用户id
     * @param token
     * @return
     */
    @PutMapping("/status/{u}/{b}/{s}")
    public ResultVO updateStatus(@PathVariable("u") String u,
                                 @PathVariable("s") String s,
                                 @PathVariable("b") String b,
                                 @RequestHeader String token) {
        return blogService.updateArticleStatus(b, s, u);
    }

    /**
     * 删除文章
     * @param u 用户id
     * @param b 文章id
     * @return
     */
    @DeleteMapping("/{u}/{b}")
    public ResultVO delete(@PathVariable("u") String u,
                           @PathVariable("b") String b) {
        return blogService.deleteBlogById2(u, b);
    }

    /**
     * 查询并编辑文章内容，不用将markdown转为html
     * @param uid 用户id
     * @param bid 文章id
     * @return
     */
    @GetMapping("/{uid}/{bid}")
    public ResultVO get(@PathVariable("uid") String uid,
                        @PathVariable("bid") String bid,
                        @RequestHeader String token) {
        return blogService.getEditViewBlogById(uid, bid);
    }

    @PutMapping("/{bid}")
    public ResultVO update(Blog blog,
                           @PathVariable("bid") String bid,
                           @RequestHeader String token,
                           MultipartFile file) {
        return blogService.updateBlog2(blog, file);
    }

    /**
     * 处理editormd的上传图片请求
     * @param file
     * @return
     */
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


