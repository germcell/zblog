package com.zs.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * elasticSearch存储的文档数据对应的实体
 * @Created by zs on 2022/5/22.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogES {
    private Long bid;
    private String title;
    private String outline;
    private String firstPicture;
    private Date writeTime;
    private Long likeNum;
    private Long views;
}
