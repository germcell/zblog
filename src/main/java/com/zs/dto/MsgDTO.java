package com.zs.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author zengshuai
 * @create 2022-09-07 14:54
 */
@Data
public class MsgDTO {
    @NotBlank(message = "请提供消息发送者ID")
    private Long sendId;
    @NotBlank(message = "请提供消息接收者ID")
    private Long receiveId;
    /** 发件内容 */
    @NotBlank(message = "消息不能为空")
    private String content;
    /** 信件类型 0:私信 1:文章评论 */
    private Integer msgTag;
    /** 文章id */
    @NotBlank(message = "请提供文章id")
    private Long bid;
    @NotBlank(message = "请提供父评论id")
    /** 父评论id */
    private Long pid;
}
