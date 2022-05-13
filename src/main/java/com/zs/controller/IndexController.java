package com.zs.controller;

import com.zs.service.BlogOutlineService;
import com.zs.service.BlogService;
import com.zs.service.IndexService;
import com.zs.vo.ResultVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

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

}
