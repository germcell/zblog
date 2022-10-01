package com.zs.controller;

import com.zs.config.Const2;
import com.zs.service.BlogOutlineService;
import com.zs.service.CategoryService;
import com.zs.service.CopyrightService;
import com.zs.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Created by zs on 2022/4/25.
 */
@Api(tags = "分类-版权处理接口")
@RestController
@CrossOrigin
@RequestMapping("/v2/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;
    @Resource
    private CopyrightService copyrightService;
    @Resource
    private BlogOutlineService blogOutlineService;


    @ApiOperation(value = "获取所有文章分类-已登录状态", tags = "一般在用户发表文章时")
    @ApiImplicitParam(name = "token", value = "用户身份token", paramType = "header", required = true, dataTypeClass = String.class)
    @GetMapping("/listcg")
    public ResultVO listCategory(@RequestHeader String token) {
        return categoryService.listCategories(null);
    }

    @ApiOperation("获取文章分类-未登录状态")
    @GetMapping("/list")
    public ResultVO pageCategory() {
        return categoryService.listCategories(null);
    }

    @ApiOperation("获取所有版权形式")
    @ApiImplicitParam(name = "token", value = "用户身份token", paramType = "header", required = true, dataTypeClass = String.class)
    @GetMapping("/listcr")
    public ResultVO listCopyright(@RequestHeader String token) {
        return copyrightService.listCopyright(null);
    }

    @ApiOperation("按分类分页查询文章概要")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "cid", value = "分类id", paramType = "query", required = true, dataTypeClass = Integer.class),
        @ApiImplicitParam(name = "p", value = "页码", paramType = "query", required = true, dataTypeClass = Integer.class)
    })
    @GetMapping("/outline/page")
    public ResultVO pageBlogOutlineByCid(@RequestParam("cid") int cid, @RequestParam("p") int p) {
        return blogOutlineService.pageByCid(cid, p);
    }
}
