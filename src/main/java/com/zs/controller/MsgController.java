package com.zs.controller;

import com.alibaba.fastjson.JSON;
import com.zs.dto.MsgDTO;
import com.zs.service.MsgService;
import com.zs.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @author zengshuai
 * @create 2022-09-07 15:15
 */
@Api(tags = "私信-评论管理接口")
@RestController
@Slf4j
@CrossOrigin
@RequestMapping("/v2/msg")
public class MsgController {

    @Resource
    private MsgService msgService;

    @ApiOperation("发表文章评论")
    @ApiImplicitParam(name = "msgDTO", value = "消息对象", paramType = "body", required = true, dataTypeClass = MsgDTO.class)
    @PostMapping("/privateMsg")
    public ResultVO privateMsg(@RequestBody MsgDTO msgDTO) {
        // TODO 发表文章评论
        return null;
    }

    /**
     * 获取指定用户的所有私信用户信息(聊天框左侧私信列表)
     * @return
     */
    @ApiOperation("获取用户的私信列表")
    @ApiImplicitParam(name = "uid", value = "用户id", paramType = "path", required = true, dataTypeClass = String.class)
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
    @ApiOperation("获取用户未读私信数量")
    @ApiImplicitParam(name = "uid", value = "用户id", paramType = "path", required = true, dataTypeClass = String.class)
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
    @ApiOperation("获取私信对话信息")
    @ApiImplicitParam(name = "cIdsJson", value = "私信idJson", paramType = "body", required = true, dataTypeClass = String.class)
    @PostMapping("/private")
    public ResultVO getAllPrivateMsg(@RequestBody String cIdsJson) {
        if (Objects.isNull(cIdsJson)) return ResultVO.paramError(null);
        if (cIdsJson.trim().length() == 0) return ResultVO.paramError(cIdsJson);

        try {
            List<Long> cIds = JSON.parseArray(cIdsJson, Long.class);
            if (cIds.size() == 0) return ResultVO.paramError(cIdsJson);
            return msgService.getPrivateMsgByCIds(cIds);
        } catch (Exception e) {
            log.warn("获取用户私信异常==>{}", e);
            return ResultVO.paramError(cIdsJson);
        }
    }

    @ApiOperation("获取用户未读私信数量2")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "receiveId", value = "接收者id", paramType = "path", required = true, dataTypeClass = String.class),
        @ApiImplicitParam(name = "sendId", value = "发送者id", paramType = "path", required = true, dataTypeClass = String.class)
    })
    @GetMapping("/newUnread/{receiveId}/{sendId}")
    public ResultVO newUnread(@PathVariable("receiveId") String receiveId,
                              @PathVariable("sendId") String sendId) {

        if (Objects.isNull(receiveId))
            return ResultVO.paramError(null);
        if (Objects.isNull(sendId))
            return ResultVO.paramError(null);
        if (receiveId.trim().length() == 0)
            return ResultVO.paramError(receiveId + "," + sendId);
        if (sendId.trim().length() == 0)
            return ResultVO.paramError(receiveId + "," + sendId);

        try {
            long receiveIdNum = Long.parseLong(receiveId);
            long sendIdNum = Long.parseLong(sendId);
            return msgService.getNewUnreadMsgIds(receiveIdNum, sendIdNum);
        } catch (NumberFormatException e) {
            log.warn("获取用户新增私信异常==>{}", e);
            return ResultVO.paramError(receiveId + "," + sendId);
        }
    }

    @ApiOperation("删除私信对话")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "sendId", value = "消息发送者id", paramType = "path", required = true, dataTypeClass = Long.class),
        @ApiImplicitParam(name = "receiveId", value = "消息接收者id【删除发起者】", paramType = "path", required = true, dataTypeClass = Long.class),
    })
    @DeleteMapping("/del/{sendId}/{receiveId}")
    public ResultVO del(@PathVariable("sendId") long sendId, @PathVariable("receiveId") long receiveId) {
        return msgService.deleteAllMsgByUser(sendId, receiveId);
    }

    @ApiOperation("分页查询文章评论")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "bid", value = "文章id", paramType = "path", required = true, dataTypeClass = Long.class),
        @ApiImplicitParam(name = "p", value = "页码", paramType = "query", required = true, dataTypeClass = Integer.class)
    })
    @GetMapping("/comments/{bid}")
    public ResultVO getComments(@PathVariable("bid") long bid, @RequestParam("p") int p) {
        return msgService.getPageArticleComments(bid, p);
    }

    @ApiOperation("添加/回复文章评论")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "msgDTO", value = "评论详情对象", paramType = "body",required = true, dataTypeClass = MsgDTO.class),
        @ApiImplicitParam(name = "token", value = "用户身份token", required = true, paramType = "header", dataTypeClass = String.class)
    })
    @PostMapping("/comments")
    public ResultVO add(@RequestBody @Validated MsgDTO msgDTO, @RequestHeader String token) {
        return msgService.addComment(msgDTO);
    }

    @ApiOperation("删除文章评论")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "commentId", value = "评论id", paramType = "path", required = true, dataTypeClass = Long.class),
        @ApiImplicitParam(name = "token", value = "用户身份token", paramType = "header", required = true, dataTypeClass = String.class)
    })
    @DeleteMapping("/commentDel/{commentId}")
    public ResultVO commentDel(@PathVariable("commentId") long commentId, @RequestHeader String token) {
        return msgService.deleteCommentById(commentId);
    }

}
