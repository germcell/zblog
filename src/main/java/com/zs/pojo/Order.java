package com.zs.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Order {
  private Long oid;
  private String orderNo;
  private Double money;
  private String subject;
  private Integer uid;
  private Integer uid2;
  private Integer status;
  private String comment;
  private Date createTime;
  private String alipayTradeNo;
  private Date payTime;
}
