package com.zs.server;

import com.alibaba.fastjson.JSON;
import com.zs.dto.MsgDTO;
import com.zs.service.MsgService;
import com.zs.service.impl.MsgServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Resource;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zengshuai
 * @create 2022-09-13 21:52
 */
@Slf4j
@Component
@ServerEndpoint("/v2/msg/ws/{sendId}")
public class WebSocketServer {

    private static Map<Long, Session> sessionsMap = new ConcurrentHashMap<>();

    // 由于WebSocket是多例，此处注入的bean为单例，不能直接用Resource等方式注入
    private static MsgService msgService;
    @Autowired
    public void setMsgService(MsgService msgService) {
        WebSocketServer.msgService = msgService;
    }

    @OnOpen
    public void onOpen(@PathParam("sendId") Long sendId, Session session) {
        sessionsMap.put(sendId, session);
        log.info("当前websocket连接用户==>{},{}", sendId, sessionsMap.get(sendId));
    }

    @OnMessage
    public void onMessage(String msg) {
//        log.info("websocket接收==>{}", msg);
        MsgDTO msgDTO = JSON.parseObject(msg, MsgDTO.class);

        // 消息队列消费接收到消息，消息会经过消费者端入库，不在这直接将消息入库的原因是为了异步操作
        msgService.productMsg(msg);

        // 回显消息到前端
        pushMsg(msgDTO.getSendId(), msg);

        // 判断服务器是否与接收者建立了ws连接，是则推送，否则不推送
        if (sessionsMap.get(msgDTO.getReceiveId()) != null) {
            pushMsg(msgDTO.getReceiveId(), msg);
        }
    }

    @OnClose
    public void onClose(@PathParam("sendId") Long sendId) {
        sessionsMap.remove(sendId);
        log.info("用户 {} 断开WS连接", sendId);
    }

    @OnError
    public void onError(Throwable err) {
        log.warn("websocket连接异常==>{}", err);
    }

    public static void pushMsg(Long sendId, String text) {
        try {
            Session session = sessionsMap.get(sendId);
            session.getBasicRemote().sendText(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
