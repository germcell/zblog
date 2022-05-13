package com.zs.pojo;

import lombok.*;

import java.util.Date;
import java.util.List;

/**
 * 评论实体类
 * @Created by zs on 2022/2/22.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class Comment {

  private Long comId; // 评论id
  private String nickname;
  private String mail;
  private String content;
  private String avatar;
  private Date replyTime; // 发布时间
  private Long bid;
  private Long comParentId; // 父评论id
  private Boolean isPass; // 审核状态 1通过 0未通过
  private String passContent; // 审核状态为0时的显示文本
  /* 和博客类构成多对一关系：多个评论 --> 一篇博客 */
  private Blog blog;
  /* 和本身构成一对多关系：一个评论 --> 多个子评论，暂时仅支持两级评论 */
  private List<Comment> listChildComments;
  /* 多对一，多个评论对应一个父评论 */
  private Comment parentComment;
}
