package com.zs.service.impl;

import com.zs.dto.AliPayDTO;
import com.zs.handler.DateUtils;
import com.zs.mapper.OrderMapper;
import com.zs.pojo.Order;
import com.zs.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.Date;

/**
 * 订单服务模块
 * @author Air
 * @create 2022-06-05 10:19
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderMapper orderMapper;

    /**
     * 创建赞赏订单
     * @param aliPayDTO
     * @return
     */
    @Override
    @Transactional
    public int createAppreciateOrder(AliPayDTO aliPayDTO) {
        Order order = new Order();
        order.setOrderNo(aliPayDTO.getPayOrderNo());
        order.setMoney(Double.parseDouble(aliPayDTO.getPayTotalAmount()));
        order.setSubject(aliPayDTO.getPaySubject());
        order.setUid(aliPayDTO.getUid());
        order.setUid2(aliPayDTO.getUid2());
        order.setStatus(0);
        order.setComment(aliPayDTO.getPayComment());
        order.setCreateTime(new Date());

        return orderMapper.insert(order);
    }

    /**
     * 修改订单状态
     * @param status 状态值
     * @param payOrderNo 订单号
     * @param alipayTradeNo 支付宝流水号
     * @param gmtPayment 支付完成时间
     * @return
     */
    @Override
    public int updateOrderStatus(int status, String payOrderNo, String alipayTradeNo, String gmtPayment) {
        Order updateOrder = new Order();
        updateOrder.setOrderNo(payOrderNo);
        updateOrder.setStatus(status);
        updateOrder.setAlipayTradeNo(alipayTradeNo);
        try {
            updateOrder.setPayTime(DateUtils.stringToDate(gmtPayment, "yyyy-MM-dd HH:mm:ss"));
            System.out.println(this.getClass());
        } catch (ParseException e) {
            log.warn("{} 处格式化日期异常 {}", this.getClass(), e);
        }
        return orderMapper.update(updateOrder);
    }
}
