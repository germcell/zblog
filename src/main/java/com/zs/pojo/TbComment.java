package com.zs.pojo;


import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class TbComment {
  private Long id;
  private Long sendId;
  private String sendAvatar;
  private Long receiveId;
  private String content;
  private String receiveAvatar;
  private Long pId;
  private Long bId;
  private Date replyTime;
  private Integer msgTag;
  private Integer isRead;
  private Integer isConsume;
  private Date createTime;
  private Date updateTime;
}
