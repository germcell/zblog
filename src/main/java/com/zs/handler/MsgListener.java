package com.zs.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author zengshuai
 * @create 2022-09-07 17:43
 */
@Component
@Slf4j
public class MsgListener implements RabbitTemplate.ReturnsCallback, RabbitTemplate.ConfirmCallback {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void registerConfirmListener() {
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean b, String s) {
        if (b) {
//            System.out.println("入队列成功......");
        } else {
            log.warn("消息入队列失败 ==> {}", s);
        }
    }

    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        String msg = new String(returnedMessage.getMessage().getBody());
        String exchange = returnedMessage.getExchange();
        System.out.println("消息如队列失败: 消息==> " + msg + " 交换机==> " + exchange);
    }
}
