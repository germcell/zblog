package com.zs.handler;

import com.zs.dto.ThumbsDTO;
import com.zs.pojo.Fans;
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

    }

    @Test
    void testNull() {
//        Set<String> strings = fansRedisHelper.allFansOfKey("2343fs");
//        System.out.println(strings.size());
//        Set<String> diff = fansRedisHelper.diff("F-7", "UF-7");
//        diff.stream().forEach(d -> System.out.println(d));
    }

    @Test
    void nullTest() {
        Fans f1 = null;
        Fans f2 = new Fans();
//        System.out.println(Objects.isNull(f1));
//        System.out.println(Objects.isNull(f2));

        if (Objects.isNull(f2) || Objects.isNull(null)) {
            System.out.println("1============");
        }

        if (Objects.isNull(null) && Objects.isNull(f2)) {
            System.out.println("2============");
        }
    }

}
