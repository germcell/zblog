package com.zs.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * elasticSearch存储的用户信息对应的实体
 * @Created by zs on 2022/5/25.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class WriterES {
    private Integer uid;
    private String writerName;
    private String writerAvatar;
    private Long fans;
    private Integer articleNum;
    private Long allViews;
    private Long allLikeNums;
}
