package com.zs.handler;

import com.zs.dto.ThumbsDTO;
import com.zs.service.ThumbsService;
import com.zs.vo.ResultVO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Created by zs on 2022/3/8.
 */
@SpringBootTest
public class RedisTest {

    @Resource
    private FansRedisHelper fansRedisHelper;
    @Resource
    private ThumbsService thumbsService;

    @Test
    void testFansRedisHelperTest() {
        Boolean fans = fansRedisHelper.isFans(String.valueOf(7), String.valueOf(1));
        System.out.println("用户7是否关注了用户1:" + fans); // false

        Long l1 = fansRedisHelper.becomeFans(String.valueOf(7), String.valueOf(1));
        Long l2 = fansRedisHelper.becomeFans(String.valueOf(7), String.valueOf(2));
        System.out.println("关注结果:" + l1 + ' ' + l2);

        Long l3 = fansRedisHelper.unfollow(String.valueOf(7), String.valueOf(2));
        System.out.println("取消关注结果:" + l3);

        Set<String> stringCursor = fansRedisHelper.allFans(String.valueOf(7));
        System.out.print("所有关注信息:");
        stringCursor.stream().forEach(c -> System.out.println(c));
    }

    @Test
    void testNull() {
//        Set<String> strings = fansRedisHelper.allFansOfKey("2343fs");
//        System.out.println(strings.size());
        Set<String> diff = fansRedisHelper.diff("F-7", "UF-7");
        diff.stream().forEach(d -> System.out.println(d));
    }

}
