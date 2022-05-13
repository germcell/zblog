package com.zs.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * 一篇博客在首页展示时的部分内容，因为博客的内容是markdown格式，
 * 会有一些特殊符号，而内容太长的话又会将布局撑开
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BlogOutline {
  private Long did;
  private String outline;
  private Long views;
  private String title;
  private Long likeNum;
  private String firstPicture;
  private Boolean isPublish;
  private Integer uid;
  private Date writeTime;
}