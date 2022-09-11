package com.zs.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author zengshuai
 * @create 2022-09-10 21:12
 */
@SpringBootTest
class TbCommentMapperTest {

    @Resource
    private TbCommentMapper tbCommentMapper;

    @Test
    void getByReceiveId() {
//        System.out.println(tbCommentMapper.getByReceiveId(7));

        List list = new ArrayList<Integer>();

        list.add(1);
        list.add("2dfsdfsd34");

        System.out.println(list);

    }
}