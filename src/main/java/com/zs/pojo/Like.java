package com.zs.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * 点赞表实体
 * @Created by zs on 2022/5/12.
 */
@Data
@NoArgsConstructor
@ToString
public class Like {
    // 自增主键
    private Long lid;
    // 点赞文章id
    private Long bid;
    // 文章作者id
    private Long uid;
    // 点赞人id
    private Long mid;
    // 点赞时间
    private Date likeTime;
}
