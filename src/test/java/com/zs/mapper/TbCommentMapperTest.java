package com.zs.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.Collections;
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

    @Test
    void getByIds() {
        ArrayList<Long> ids = new ArrayList<>();
        Collections.addAll(ids, 1L, 2L);
        assertEquals(2, tbCommentMapper.getByIds(ids).size());
    }
}