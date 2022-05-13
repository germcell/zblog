package com.zs.controller;

import com.zs.config.Const;
import com.zs.pojo.Blog;
import com.zs.pojo.MDate;
import com.zs.pojo.RequestResult;
import com.zs.service.BlogService;
import com.zs.service.CommentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import com.zs.pojo.Comment;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Created by zs on 2022/3/14.
 */
@Controller
@RequestMapping("/admin")
public class CommentController {

    @Resource
    private CommentService commentService;

    @Resource
    private BlogService blogService;

    /**
     * 跳转至博客的留言管理页面
     * @param bid 博客id
     * @param model
     * @return
     */
    @GetMapping("/comments/{bid}")
    public String comments(@PathVariable("bid") Long bid, Model model) {
        if (bid != null) {
            // 查询博客基本信息
            Blog blogBaseMsg = blogService.getBlogBaseMsg(bid);
            // 查询博客对应的留言,mDate为查询留言的日期条件
            Comment comment = new Comment();
            comment.setBid(bid);
            MDate mDate = new MDate();
            if (blogBaseMsg == null) {
                return "error/404";
            }
            // 渲染博客信息、留言信息
            model.addAttribute("blog", blogBaseMsg);
            List<Comment> comments = commentService.listCommentsByCondition(comment, mDate);
            model.addAttribute("comments",comments);
            return "admin/comment";
        } else {
            return "error/404";
        }
    }

    /**
     * 删除留言
     * @param comIdsStr
     * @param model
     * @return
     */
    @DeleteMapping("/comment")
    @ResponseBody
    public RequestResult delete(@RequestParam("comIds") String comIdsStr, Model model) {
        if (StringUtils.hasText(comIdsStr)) {
            String[] split = comIdsStr.split(",");
            Long[] comIds = Arrays.stream(split).map(s -> Long.parseLong(s.trim())).toArray(Long[]::new);
            RequestResult requestResult = commentService.deleteComments(comIds);
            return requestResult;
        } else {
            RequestResult requestResult = new RequestResult();
            requestResult.setCode(Const.COMMENT_DELETE_FAIL_DATABASE);
            requestResult.setMessage("删除失败");
            return requestResult;
        }
    }

    /**
     * 留言审核
     * @param comIdsStr
     * @param comment
     * @return
     */
    @PostMapping("/comment")
    @ResponseBody
    public RequestResult post(@RequestParam("comIds") String comIdsStr, Comment comment) {
        if (StringUtils.hasText(comIdsStr) && comment != null) {
            String[] split = comIdsStr.split(",");
            Long[] comIds = Arrays.stream(split).map(s -> Long.parseLong(s.trim())).toArray(Long[]::new);
            RequestResult requestResult = commentService.passComments(comIds, comment);
            return requestResult;
        } else {
            RequestResult requestResult = new RequestResult();
            requestResult.setCode(Const.COMMENT_PASS_FAIL);
            requestResult.setMessage("审核失败");
            return requestResult;
        }
    }

    /**
     * 留言查询
     * @param comment
     * @param mDate
     * @param model
     * @return
     */
    @PostMapping("/comment/search")
    public String search(Comment comment, MDate mDate, Model model) {
        if (comment == null && mDate == null) {
            return "error/404";
        } else {
            List<Comment> comments = commentService.listCommentsByCondition(comment, mDate);
            model.addAttribute("comments", comments);
            return "admin/comment :: commentList";
        }
    }

}

