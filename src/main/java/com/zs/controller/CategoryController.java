package com.zs.controller;

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

    @ApiOperation("获取所有文章分类")
    @ApiImplicitParam(name = "token", value = "用户身份token", paramType = "header", required = true, dataTypeClass = String.class)
    @GetMapping("/listcg")
    public ResultVO listCategory(@RequestHeader String token) {
        return categoryService.listCategories(null);
    }

    @ApiOperation("获取所有版权形式")
    @ApiImplicitParam(name = "token", value = "用户身份token", paramType = "header", required = true, dataTypeClass = String.class)
    @GetMapping("/listcr")
    public ResultVO listCopyright(@RequestHeader String token) {
        return copyrightService.listCopyright(null);
    }
}
