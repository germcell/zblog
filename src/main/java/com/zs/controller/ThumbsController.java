package com.zs.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zs.config.Const2;
import com.zs.dto.ThumbsDTO;
import com.zs.service.ThumbsService;
import com.zs.vo.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

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
     * 点赞
     * @param token 用户令牌
     * @param thumbsDTO 点赞信息
     * @return code = 200 成功
     *         code = 500 失败，服务端异常
     *         code = 505 失败，请求参数为null
     *         code = 754 失败，已点赞
     */
    @PostMapping("")
    public ResultVO like(@RequestHeader String token, @RequestBody ThumbsDTO thumbsDTO) throws JsonProcessingException {
        Boolean paramsCheckResult = paramsCheck(thumbsDTO);
        if (paramsCheckResult) {
            return thumbsService.like(thumbsDTO);
        }
        return new ResultVO(Const2.PARAMETERS_IS_NULL, "parameters should not be null", null);

        // 注释：参数方法测试
//        if (thumbsDTO != null) {
//            if (thumbsDTO.getBid() != null && thumbsDTO.getMid() != null && thumbsDTO.getUid() != null) {
//                try {
//                    return thumbsService.like(thumbsDTO);
//                } catch(JsonProcessingException e) {
//                    e.printStackTrace();
//                    logger.info("点赞接口json序列化异常");
//                    return new ResultVO(Const2.SERVICE_FAIL, "fail", null);
//                }
//            }
//        }
//        return new ResultVO(Const2.PARAMETERS_IS_NULL, "parameters should not be null", null);
    }

    /**
     * 获取文章点赞数
     * @param bid 文章id
     * @return code = 200 成功
     *         code = 500 失败，服务端异常
     *         code = 505 失败，参数为null
     */
    @GetMapping("/get/{bid}")
    public ResultVO getLikeNum(@PathVariable("bid") Long bid) {
        if (bid != null) {
            try {
                return thumbsService.getLikesByBid(bid);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                logger.info("获取文章点赞数接口json序列化异常");
                return new ResultVO(Const2.SERVICE_FAIL, "fail", null);
            }
        }
        return new ResultVO(Const2.PARAMETERS_IS_NULL, "parameters should not be null", null);
    }

    /**
     * 查询当前用户对文章的点赞状态
     * @param token
     * @param thumbsDTO
     * @return  code == 754 已点赞
     *          code == 753 未点赞
     *          code == 505 请求失败，参数为null
     *          code == 525 请求失败，服务端异常
     */
    @PostMapping("/isLike")
    public ResultVO isLike(@RequestHeader String token, @RequestBody ThumbsDTO thumbsDTO) throws JsonProcessingException {
        Boolean paramsCheckResult = paramsCheck(thumbsDTO);
        if (paramsCheckResult) {
            return thumbsService.isLike(thumbsDTO);
        }
        return new ResultVO(Const2.PARAMETERS_IS_NULL, "parameters should not be null", null);

        // 参数方法测试
//        if (thumbsDTO != null) {
//            if (thumbsDTO.getBid() != null && thumbsDTO.getMid() != null && thumbsDTO.getUid() != null) {
//                try {
//                    return thumbsService.isLike(thumbsDTO);
//                } catch (JsonProcessingException e) {
//                    e.printStackTrace();
//                    logger.info("获取用户点赞状态接口json序列化异常");
//                    return new ResultVO(Const2.SERVICE_FAIL, "fail", null);
//                }
//            }
//        }
//        return new ResultVO(Const2.PARAMETERS_IS_NULL, "parameters should not be null", null);
    }

    /**
     * 取消点赞
     * @param token
     * @param thumbsDTO
     * @return  code == 756 取消成功
     *          code == 753 取消失败，该用户还未点赞
     *          code == 505 参数为null
     */
    @PutMapping("/cancel")
    public ResultVO cancelLike(@RequestHeader String token, @RequestBody ThumbsDTO thumbsDTO) {
        Boolean paramsCheckResult = paramsCheck(thumbsDTO);
        if (paramsCheckResult) {
            return thumbsService.cancelLike(thumbsDTO);
        }
        return new ResultVO(Const2.PARAMETERS_IS_NULL, "parameters should not be null", null);
    }

    /**
     * 请求参数校验
     * @param thumbsDTO
     * @return
     */
    public Boolean paramsCheck(ThumbsDTO thumbsDTO) {
        if (thumbsDTO == null) {
            return false;
        }
        if (thumbsDTO.getBid() == null || thumbsDTO.getUid() == null || thumbsDTO.getMid() == null) {
            return false;
        }
        return true;
    }

}
