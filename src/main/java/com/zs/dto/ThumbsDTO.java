package com.zs.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 点赞接收参数
 * @Created by zs on 2022/5/12.
 */
@Data
@NoArgsConstructor
@ToString
public class ThumbsDTO {
    // 点赞者id
    private Long mid;
    // 作者id
    private Long uid;
    // 文章id
    private Long bid;
}
