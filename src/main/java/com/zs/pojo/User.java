package com.zs.pojo;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 用户实体类
 * @Created by zs on 2022/2/22.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class User {

  private long uid;
  private String nickname;
  private String pwd;
  private String mail;
  private String avatar;
  private long type;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date updateTime;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date registerTime;
  /* 和博客类构成一对多关系：一个用户 --> 多篇博客 */
  private List<Blog> listBlogs;



}
