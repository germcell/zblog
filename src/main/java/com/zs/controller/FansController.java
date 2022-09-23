package com.zs.controller;

import com.zs.config.Const2;
import com.zs.dto.ThumbsDTO;
import com.zs.pojo.Fans;
import com.zs.service.FansService;
import com.zs.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @Created by zs on 2022/4/30.
 */
@Api(tags = "关注关系处理接口")
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
     * @return code == 200 成功
     *         code == 505 请求失败，参数为null
     *         code == 701 已经关注过该用户
     */
    @ApiOperation("关注作者")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "fans", value = "关注详情对象", paramType = "body", required = true, dataTypeClass = Fans.class),
        @ApiImplicitParam(name = "token", value = "用户身份token", paramType = "header", required = true, dataTypeClass = String.class)
    })
    @PostMapping("/add")
    public ResultVO add(@RequestBody Fans fans, @RequestHeader String token) {
        if (paramsCheck(fans)) {
            return fansService.becomeFans(fans);
        }
        return new ResultVO(Const2.PARAMETERS_IS_NULL, "request parameter is null", null);
    }

    /**
     * 查询用户已经关注的作者
     * @param uid
     * @param token
     * @return
     */
    @ApiOperation("查询用户已经关注的作者")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "uid", value = "用户id", paramType = "path", required = true, dataTypeClass = String.class),
        @ApiImplicitParam(name = "token", value = "用户身份token", paramType = "header", required = true, dataTypeClass = String.class)
    })
    @GetMapping("/list/{uid}")
    public ResultVO list(@PathVariable("uid") String uid, @RequestHeader String token) {
        return fansService.listFansByUid(uid);
    }

    /**
     * 取消关注
     * @param fans
     * @param token
     * @return code == 200 取关成功
     *         code == 702 取关失败，还未关注过
     *         code == 505 请求失败，参数为null
     */
    @ApiOperation("取消关注")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "fans", value = "关注详情对象", paramType = "body", required = true, dataTypeClass = Fans.class),
        @ApiImplicitParam(name = "token", value = "用户身份token", paramType = "header", required = true, dataTypeClass = String.class)
    })
    @DeleteMapping("/delete")
    public ResultVO delete(@RequestBody Fans fans, @RequestHeader String token) {
        if (paramsCheck(fans)) {
            return fansService.unFollow(fans);
        }
        return new ResultVO(Const2.PARAMETERS_IS_NULL, "request parameter is null", null);
    }

    /**
     * 获取对某个用户的关注状态
     * @param token
     * @param fans
     * @return
     */
    @ApiOperation("获取对某个用户的关注状态")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "fans", value = "关注详情对象", paramType = "body", required = true, dataTypeClass = Fans.class),
        @ApiImplicitParam(name = "token", value = "用户身份token", paramType = "header", required = true, dataTypeClass = String.class)
    })
    @PostMapping("/status")
    public ResultVO status(@RequestHeader String token, @RequestBody Fans fans) {
        if (paramsCheck(fans)) {
            return fansService.getFollowStatus(fans);
        }
        return new ResultVO(Const2.PARAMETERS_IS_NULL, "request parameter is null", false);
    }

    /**
     * 关注操作参数校验
     * @param fans
     * @return true 参数不为null
     *         false 参数为null
     */
    public Boolean paramsCheck(Fans fans) {
        if (Objects.isNull(fans)) {
            return false;
        }
        if (Objects.isNull(fans.getUid()) || Objects.isNull(fans.getUid2())) {
            return false;
        }
        return true;
    }

}
