package com.zs.controller;

import com.zs.config.WeChatConfig;
import com.zs.handler.UniversalException;
import com.zs.vo.ResultVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 接入微信登录
 * TODO 无微信开发者账号
 *
 * @author zengshuai
 * @create 2022-09-30 16:49
 */
@Slf4j
@Controller
@CrossOrigin
@RequestMapping("/v2/wx/login")
public class WechatController {

    /**
     * 通过微信提供的appid等信息获得登录二维码
     * @return
     */
    @ApiOperation("获取微信登录参数")
    @GetMapping("/getLoginParam")
    @ResponseBody
    public ResultVO genQrContent() {
        try {
            Map<String,Object> map = new HashMap<>();
            map.put("appid", WeChatConfig.WX_OPEN_APP_ID);
            map.put("scope", "snsapi_login");
            String redirectUrl = WeChatConfig.WX_OPEN_APP_REDIRECT_URl;
            redirectUrl = URLEncoder.encode(redirectUrl, "utf-8");
            map.put("redirect_uri", redirectUrl);
            map.put("state", System.currentTimeMillis() + "");
            return ResultVO.success(map);
        } catch (UnsupportedEncodingException e) {
            log.warn("微信登录异常==>{}", e);
            throw new UniversalException("微信登录异常");
        }
    }

    /**
     *
     * @param code
     * @param state
     * @return
     */
    @GetMapping("/callback")
    public ResultVO loginCallback(@RequestParam("code") String code, @RequestParam("state") String state) {
        return null;
    }

}
