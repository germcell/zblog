package com.zs.service.impl;

import com.zs.service.FansService;
import com.zs.vo.ResultVO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @Created by zs on 2022/5/18.
 */
@SpringBootTest
public class FansServiceImplTest {

    @Resource
    private FansService fansService;

    @Test
    void syncFansTest() {
        ResultVO resultVO = fansService.syncFans();
        System.out.println(resultVO);
    }

}
