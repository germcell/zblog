package com.zs.service.impl;

import com.zs.config.Const2;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author zengshuai
 * @create 2022-09-07 15:04
 */
@Service
public class MsgService {

    @Resource
    private AmqpTemplate amqpTemplate;

    public void productMsg(String msgDTO) {
        System.out.println("生产消息: " + msgDTO);
        amqpTemplate.convertAndSend(Const2.MSG_QUEUE_1, msgDTO);
    }

}
