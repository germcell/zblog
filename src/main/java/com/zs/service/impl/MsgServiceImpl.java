package com.zs.service.impl;

import com.zs.config.Const2;
import com.zs.handler.UniversalException;
import com.zs.mapper.TbCommentMapper;
import com.zs.pojo.TbComment;
import com.zs.service.MsgService;
import com.zs.vo.CommentVO;
import com.zs.vo.ResultVO;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @author zengshuai
 * @create 2022-09-07 15:04
 */
@Service
public class MsgServiceImpl implements MsgService {

    @Resource
    private AmqpTemplate amqpTemplate;
    @Resource
    private TbCommentMapper tbCommentMapper;

    public ResultVO getPrivateMsgByCIds(List<Long> cIds) {
        // 查询私信(他人)
        List<TbComment> receive = tbCommentMapper.getByIds(cIds);

        // 将私信置为已读
        tbCommentMapper.updateStatusByIds(cIds, 1);

        // 查询回复信息(我)
        long sendId = receive.get(0).getReceiveId();
        long receiveId = receive.get(0).getSendId();
        List<TbComment> send = tbCommentMapper.getCommunication(sendId, receiveId);

        if (send.size() == 0) return ResultVO.success(receive);

        Map<String, List> result = new HashMap<>();
        result.put("receive", receive);
        result.put("send", send);
        return ResultVO.success(result);
    }

    @Override
    public ResultVO getPrivateMsgUserInfoByUid(long uid) {
        // 获取私信信息
        List<CommentVO> comments = tbCommentMapper.getByReceiveId(uid);

        if (comments.size() == 0) {
            return null;
        }

        // 提取私信用户id(key)和最新的私信id(value)
        Map<Long,Long> map = new HashMap<>();

        comments.stream().forEach(commentVO -> {
            boolean contains = map.entrySet().stream().anyMatch(m -> {
                if (Objects.equals(commentVO.getSendId(), m.getValue())) return true;
                return false;
            });
            if (!contains) map.put(commentVO.getId(), commentVO.getSendId());
        });

        // 提取私信id,并添加到【最新私信】对象的commentIds集合中，那么不是最新私信对象的commentIds.size() == 0
        AtomicReference<List<CommentVO>> temp = new AtomicReference<>();

        map.entrySet().stream().forEach(m -> {
            temp.set(comments.stream().filter(c -> Objects.equals(m.getValue(), c.getSendId())).collect(Collectors.toList()));

            temp.get().stream().forEach(t -> {
                if (Objects.isNull(t.getCommentIds())) {
                    t.setCommentIds(new ArrayList<>());
                }
                if (Objects.equals(m.getKey(), t.getId()) && Objects.equals(m.getValue(), t.getSendId())) {
                    t.getCommentIds().add(t.getId());
                } else {
                    temp.get().stream().anyMatch(t2 -> {
                        if (Objects.equals(t2.getId(), m.getKey()) && Objects.equals(m.getValue(), t2.getSendId())) {
                            t2.getCommentIds().add(t.getId());
                            return true;
                        }
                        return false;
                    });
                }
            });
        });

        // 去除重复的私信信息,只保留最新的私信和其所有私信id
        comments.removeIf(c -> c.getCommentIds().size() == 0);

        return ResultVO.success(comments);
    }

    @Override
    public void productMsg(String msgDTO) {
        System.out.println("生产消息: " + msgDTO);
        amqpTemplate.convertAndSend(Const2.MSG_QUEUE_1, msgDTO);
    }

    @Override
    public ResultVO getUnreadMsgByUid(long uid) {
        return ResultVO.success(tbCommentMapper.getUnreadByReceiveId(uid));
    }
}
