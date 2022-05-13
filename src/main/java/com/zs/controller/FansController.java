package com.zs.controller;

import com.zs.pojo.Fans;
import com.zs.service.FansService;
import com.zs.vo.ResultVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Created by zs on 2022/4/30.
 */
@RestController
@CrossOrigin
@RequestMapping("/v2/fans")
public class FansController {

    @Resource
    private FansService fansService;

    /**
     * 关注作者
     * @param fans
     * @param token
     * @return
     */
    @PostMapping("/add")
    public ResultVO add(@RequestBody Fans fans, @RequestHeader String token) {
        return fansService.becomeFans(fans);
    }

    /**
     * 查询用户已经关注的作者
     * @param uid
     * @param token
     * @return
     */
    @GetMapping("/list/{uid}")
    public ResultVO list(@PathVariable("uid") String uid, @RequestHeader String token) {
        return fansService.listFansByUid(uid);
    }

    @DeleteMapping("/delete")
    public ResultVO delete(@RequestBody Fans fans, @RequestHeader String token) {
        return fansService.unFollow(fans);
    }

}
