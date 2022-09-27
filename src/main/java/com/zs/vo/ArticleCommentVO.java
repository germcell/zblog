package com.zs.vo;

import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * 评论列表展示
 * @author zengshuai
 * @create 2022-09-26 13:50
 */
@Data
@ToString
public class ArticleCommentVO {
    private Long id;
    private Long sendId;
    private Long receiveId;
    private String sendName;
    private String receiveName;
    private String sendSex;
    private String receiveSex;
    private String sendAvatar;
    private String receiveAvatar;
    private String content;
    private Long pid;
    private Long bid;
    private Integer msgTag;
    private Date createTime;
    private List<ArticleCommentVO> childComments;
}
