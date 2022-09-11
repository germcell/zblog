package com.zs.controller.front;

import com.alibaba.fastjson.JSON;
import com.zs.dto.MsgDTO;
import com.zs.service.MsgService;
import com.zs.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @author zengshuai
 * @create 2022-09-07 15:15
 */
@RestController
@Slf4j
@CrossOrigin
@RequestMapping("/v2/msg")
public class MsgController {

    @Resource
    private MsgService msgService;

    /**
     * 发送私信
     * @param msgDTO
     * @return
     */
    @PostMapping("/privateMsg")
    public ResultVO privateMsg(@RequestBody MsgDTO msgDTO) {
        msgService.productMsg(JSON.toJSONString(msgDTO));
        return new ResultVO(100000,"成功", null);
    }

    /**
     * 获取指定用户的所有私信用户信息
     * @return
     */
    @GetMapping("/user/{uid}")
    public ResultVO getAllPrivateMsgUserInfo(@PathVariable("uid") String uid) {
        if (Objects.isNull(uid)) return ResultVO.paramError(null);
        if (uid.trim().length() == 0) return ResultVO.paramError(uid);

        try {
            long uidNum = Long.parseLong(uid);
            return msgService.getPrivateMsgUserInfoByUid(uidNum);
        } catch (NumberFormatException e) {
            log.warn("获取私信用户信息异常==>{}", e);
            return ResultVO.paramError(uid);
        }
    }

    /**
     * 获取用户未读私信数量
     * @param uid
     * @return
     */
    @GetMapping("/unread/{uid}")
    public ResultVO getUnreadNum(@PathVariable("uid") String uid) {
        if (Objects.isNull(uid)) return ResultVO.paramError(null);
        if (uid.trim().length() == 0) return ResultVO.paramError(uid);

        try {
            long uidNum = Long.parseLong(uid);
            return msgService.getUnreadMsgByUid(uidNum);
        } catch (NumberFormatException e) {
            log.warn("获取用户未读私信数量异常==>{}", e);
            return ResultVO.paramError(uid);
        }
    }

    /**
     * 获取指定用户的所有私信
     * @param cIdsJson
     * @return
     */
    @PostMapping("/private")
    public ResultVO getAllPrivateMsg(@RequestBody String cIdsJson) {
        if (Objects.isNull(cIdsJson)) return ResultVO.paramError(null);
        if (cIdsJson.trim().length() == 0) return ResultVO.paramError(cIdsJson);

        try {
            List<Long> cIds = JSON.parseArray(cIdsJson, Long.class);
            return msgService.getPrivateMsgByCIds(cIds);
        } catch (Exception e) {
            log.warn("获取用户私信异常==>{}", e);
            return ResultVO.paramError(cIdsJson);
        }

    }

}
