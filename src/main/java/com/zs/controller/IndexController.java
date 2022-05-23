package com.zs.controller;

import com.zs.config.Const2;
import com.zs.service.BlogOutlineService;
import com.zs.service.BlogService;
import com.zs.service.IndexService;
import com.zs.vo.ResultVO;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * 首页控制器
 * @Created by zs on 2022/4/20.
 */
@RestController
@CrossOrigin
@RequestMapping("/v2/index")
public class IndexController {

    @Resource
    private IndexService indexService;
    @Resource
    private BlogOutlineService blogOutlineService;
    @Resource
    private BlogService blogService;

    /**
     * 首页数据加载接口
     * @return
     */
    @GetMapping({"/",""})
    public ResultVO loadIndexData() {
        return indexService.getIndexData();
    }

    /**
     * 首页最新文章分页查询接口
     * @param currentPage
     * @return
     */
    @GetMapping("/article/page/{currentPage}")
    public ResultVO indexArticlePage(@PathVariable("currentPage") Integer currentPage) {
        return blogOutlineService.page(currentPage);
    }

    /**
     * 查询并显示文章内容，需将markdown转为html
     * @param bid
     * @return
     */
    @GetMapping("/article/view/{bid}")
    public ResultVO viewArticle(@PathVariable("bid") Long bid) {
        return blogService.getBlogByIdAndConvert(bid);
    }

    /**
     * 搜索接口
     * @param keyword 搜索关键字
     * @param p 页码
     * @param searchType 搜索类别 ：null 表示文章， userInfo 表示搜索用户
     * @return code == 200 搜索成功，返回搜索分页对象
     *         code == 505 无搜索关键词
     */
    @GetMapping("/search/{keyword}")
    public ResultVO search(@PathVariable("keyword") String keyword,
                           @RequestParam("p") int p,
                           @RequestParam(value = "t", required = false) String searchType) {
        if (Objects.equals(null, keyword) || !StringUtils.hasText(keyword)) {
            return new ResultVO(Const2.PARAMETERS_IS_NULL, "no input keyword", null);
        }
        return blogService.search(keyword, p, searchType);

//        if (Objects.isNull(searchType)) {
//            return blogService.search(keyword, p);
//        }
//        if (Objects.equals("userInfo", searchType)) {
//            // 查询用户
//            return blogService.searchUser(keyword, p);
//            return null;
//        } else {
//            return blogService.search(keyword, p);
//        }
    }
}
