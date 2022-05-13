package com.zs.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * 类别实体类
 * @Created by zs on 2022/2/22.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Category {

    private Integer cid;
    private String name;
    private Integer uid;
    private Date createTime;
    private String description;
    private String picture;
    private User user;
    private Long blogCounts;  // 所属分类博客数量
    /* 和博客类构成一对多关系：一个分类 --> 多篇博客 */
    private List<Blog> listBlogs;

}
