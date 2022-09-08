package com.zs.vo;

import com.zs.pojo.TbComment;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author zengshuai
 * @create 2022-09-08 22:12
 */
@Data
@ToString
public class MessageVO {
    private Long sendId;
    private String avatar;
    private String name;
    private String sex;
    private List<TbComment> comments;
    // TODO 与用户相关的消息sql
}
