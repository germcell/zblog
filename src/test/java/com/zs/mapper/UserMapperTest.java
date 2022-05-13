package com.zs.mapper;

import com.zs.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Created by zs on 2022/2/22.
 */
@SpringBootTest
class UserMapperTest {

    @Autowired
    UserMapper userMapper;

    @Test
    void getUser() {
//        User jack = userMapper.getUser("jack", "2301");
//        assertNotNull(jack);
    }
}