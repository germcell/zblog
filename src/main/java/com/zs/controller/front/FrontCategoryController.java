package com.zs.controller.front;

import com.zs.service.CategoryService;
import com.zs.service.CopyrightService;
import com.zs.vo.ResultVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Created by zs on 2022/4/25.
 */
@RestController
@CrossOrigin
@RequestMapping("/v2/category")
public class FrontCategoryController {

    @Resource
    private CategoryService categoryService;
    @Resource
    private CopyrightService copyrightService;

    /**
     * 获取所有文章分类
     * @param token
     * @return
     */
    @GetMapping("/listcg")
    public ResultVO listCategory(@RequestHeader String token) {
        return categoryService.listCategories(null);
    }

    /**
     * 获取所有文章发布形式
     * @param token
     * @return
     */
    @GetMapping("/listcr")
    public ResultVO listCopyright(@RequestHeader String token) {
        return copyrightService.listCopyright(null);
    }
}
