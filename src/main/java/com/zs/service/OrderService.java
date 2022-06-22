package com.zs.service;

import com.zs.dto.AliPayDTO;

/**
 * @author Air
 * @create 2022-06-05 10:16
 */
public interface OrderService {

    /**
     * 创建赞赏订单
     * @param aliPayDTO
     * @return
     */
    int createAppreciateOrder(AliPayDTO aliPayDTO);

    /**
     * 修改订单状态
     * @param status 状态值
     * @param payOrderNo 订单号
     * @param alipayTradeNo 支付宝流水号
     * @param gmtPayment 支付完成时间
     * @return
     */
    int updateOrderStatus(int status, String payOrderNo, String alipayTradeNo, String gmtPayment);
}
