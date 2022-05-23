package com.zs.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * @Created by zs on 2022/5/23.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MyPageInfo<T> {
    /**
     * 总记录数
     */
    private Integer total;
    /**
     * 开始索引
     */
    private Integer start;
    /**
     * 总页数
     */
    private Integer totalPage;
    /**
     * 分页记录
     */
    private List<T> list;
}
