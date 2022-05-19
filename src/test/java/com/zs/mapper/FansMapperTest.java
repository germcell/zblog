package com.zs.mapper;

import com.zs.pojo.Fans;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Created by zs on 2022/5/18.
 */
@SpringBootTest
public class FansMapperTest {

    @Resource
    private FansMapper fansMapper;

    @Test
    void listConditionTest() {
        Fans condition = new Fans();
        condition.setUid2(52L);
        List<Fans> fans = fansMapper.listByCondition(condition);
        System.out.println(fans);
    }

}
