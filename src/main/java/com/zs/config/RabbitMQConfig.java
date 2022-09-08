package com.zs.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zengshuai
 * @create 2022-09-07 14:57
 */
//@Configuration
public class RabbitMQConfig {

    /**
     * 处理《私信》、《文章评论》队列
     * @return MsgQueue
     */
    @Bean
    public Queue msgQueue() {
        return new Queue(Const2.MSG_QUEUE_1);
    }

}
