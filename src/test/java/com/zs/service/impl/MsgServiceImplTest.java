package com.zs.service.impl;

import com.zs.service.MsgService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author zengshuai
 * @create 2022-09-10 21:50
 */
@SpringBootTest
class MsgServiceImplTest {

    @Resource
    private MsgService msgService;

    @Test
    void getPrivateMsgUserInfoByUid() {
        msgService.getPrivateMsgUserInfoByUid(7);
    }

    @Test
    void t2() {
       msgService.getPageArticleComments(41, 2);
    }
}