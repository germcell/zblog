package com.zs.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zs.config.Const;
import com.zs.config.Const2;
import com.zs.dto.ThumbsDTO;
import com.zs.service.ThumbsService;
import com.zs.vo.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * 点赞控制器
 * @Created by zs on 2022/5/12.
 */
@RestController
@CrossOrigin
@RequestMapping("/v2/thumbs-up")
public class ThumbsController {

    private static Logger logger = LoggerFactory.getLogger(ThumbsController.class);

    @Resource
    private ThumbsService thumbsService;

    /**
     * 点赞接口(http)
     * @param token 用户令牌
     * @param thumbsDTO 点赞信息
     * @return code = 200 成功
     *         code = 500 失败，服务端异常
     *         code = 505 失败，请求参数为null
     */
    @PostMapping("")
    public ResultVO like(@RequestHeader String token, @RequestBody ThumbsDTO thumbsDTO) {
        if (thumbsDTO != null) {
            if (thumbsDTO.getBid() != null && thumbsDTO.getMid() != null && thumbsDTO.getUid() != null) {
                try {
                    return thumbsService.like(thumbsDTO);
                } catch(JsonProcessingException e) {
                    e.printStackTrace();
                    logger.info("点赞接口json序列化异常");
                    return new ResultVO(Const2.SERVICE_FAIL, "fail", null);
                }
            }
        }
        return new ResultVO(Const2.PARAMETERS_IS_NULL, "parameters should not be null", null);
    }

    /**
     * 获取指定用户所有点赞记录
     * @param mid 用户id
     * @return code = 200 成功
     *         code = 500 失败，服务端异常
     *         code = 505 失败，参数为null
     */
    @GetMapping("/{mid}")
    public ResultVO getLikes(@PathVariable("mid") Long mid) {
        if (mid != null) {
            try {
                return thumbsService.getLikesByMid(mid);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                logger.info("获取用户点赞记录接口json序列化异常");
                return new ResultVO(Const2.SERVICE_FAIL, "fail", null);
            }
        }
        return new ResultVO(Const2.PARAMETERS_IS_NULL, "parameters should not be null", null);
    }

    @GetMapping()
}
