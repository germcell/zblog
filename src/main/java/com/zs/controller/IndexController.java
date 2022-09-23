package com.zs.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zs.config.Const2;
import com.zs.service.BlogOutlineService;
import com.zs.service.BlogService;
import com.zs.service.IndexService;
import com.zs.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * 首页控制器
 * @Created by zs on 2022/4/20.
 */
@Api(tags = "社区首页管理接口")
@RestController
@CrossOrigin
@RequestMapping("/v2/index")
public class IndexController {

    private final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);

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
    @ApiOperation("获取首页数据")
    @GetMapping({"/",""})
    public ResultVO loadIndexData() {
        return indexService.getIndexData();
    }

    /**
     * 首页最新文章分页查询接口
     * @param currentPage
     * @return
     */
    @ApiOperation("首页文章分页查询")
    @ApiImplicitParam(name = "currentPage", value = "页码", required = true, paramType = "path", dataTypeClass = Integer.class)
    @GetMapping("/article/page/{currentPage}")
    public ResultVO indexArticlePage(@PathVariable("currentPage") Integer currentPage) {
        return blogOutlineService.page(currentPage);
    }

    /**
     * 查询并显示文章内容，需将markdown转为html
     * @param bid
     * @return
     */
    @ApiOperation("查询文章")
    @ApiImplicitParam(name = "bid", value = "文章id", paramType = "path", required = true, dataTypeClass = Long.class)
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
    @ApiOperation("搜索")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "keyword", value = "搜索关键词", paramType = "path", required = true, dataTypeClass = String.class),
        @ApiImplicitParam(name = "p", value = "页码", paramType = "query", required = true, dataTypeClass = Integer.class),
        @ApiImplicitParam(name = "t", value = "搜索类别", paramType = "query", required = false, dataTypeClass = String.class)
    })
    @GetMapping("/search/{keyword}")
    public ResultVO search(@PathVariable("keyword") String keyword,
                           @RequestParam("p") int p,
                           @RequestParam(value = "t", required = false) String searchType) {
        if (Objects.equals(null, keyword) || !StringUtils.hasText(keyword)) {
            return new ResultVO(Const2.PARAMETERS_IS_NULL, "no input keyword", null);
        }
        return blogService.search(keyword, p, searchType);
    }

    /**
     * 获取文章排行榜
     * @return
     */
    @ApiOperation("获取文章排行榜")
    @GetMapping("/article/rank")
    public ResultVO getArticleRank() {
        try {
            return indexService.getArticleRank();
        } catch (JsonProcessingException e) {
            LOGGER.info("获取排行榜异常{}",e);
        }
       return new ResultVO(Const2.SERVICE_FAIL, "exception", null);
    }
}
