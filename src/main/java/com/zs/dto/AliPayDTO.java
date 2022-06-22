package com.zs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * alipay 接口参数
 * @author Air
 * @create 2022-06-04 16:13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AliPayDTO {
    /**
     * 支付主题（商品名）
     */
    private String paySubject;
    /**
     * 支付订单号，自己系统中生成的唯一订单号
     */
    private String payOrderNo;
    /**
     * 支付金额
     */
    private String payTotalAmount;
    /**
     * 支付留言
     */
    private String payComment;
    /**
     * 支付人用户id
     */
    private Integer uid;
    /**
     * 收款人用户id
     */
    private Integer uid2;
}
