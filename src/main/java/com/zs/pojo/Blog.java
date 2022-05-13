package com.zs.pojo;

import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 博客实体类
 * @Created by zs on 2022/2/22.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
@Getter
public class Blog {

    private Long bid; // id
    private String title; // 博客标题
    private String content; // 博客内容
    private String firstPicture; // 博客首图
    private Integer cid; // 分类id
    private Long views; // 浏览次数
    private Boolean isAppreciate; // 是否开启赞赏  true开启 false关闭
    private Boolean isComment; // 是否开启评论
    private Boolean isPublish; // 是否发布/草稿
    private Boolean isReprint; // 是否发布/草稿
    private Date writeTime; // 创建时间
    private Date updateTime; // 最近更新时间
    private Integer uid;
    private Integer crTipId; // 版权标识
    private Long likeNum; // 点赞数

    /* 版权信息 */
    private Copyright copyright;
    /* 一个用户对应多篇博客 */
    private User user;
    /* 一个分类对应多篇博客 */
    private Category category;
    /* 一篇博客对应多个评论 */
    private List<Comment> listComments;
    /* 博客概要，也就是博客的前135个字符 */
    private BlogOutline blogOutline;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Blog blog = (Blog) o;
        return Objects.equals(bid, blog.bid) && Objects.equals(title, blog.title) && Objects.equals(content, blog.content) && Objects.equals(cid, blog.cid) && Objects.equals(isAppreciate, blog.isAppreciate) && Objects.equals(isComment, blog.isComment) && Objects.equals(isPublish, blog.isPublish) && Objects.equals(uid, blog.uid) && Objects.equals(crTipId, blog.crTipId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bid, title, content, cid, isAppreciate, isComment, isPublish, uid, crTipId);
    }

}
