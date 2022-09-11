package com.zs.vo;

import com.zs.pojo.TbComment;
import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * @author zengshuai
 * @create 2022-09-08 22:12
 */
@Data
@ToString
public class CommentVO {
    private Long id;
    private Long sendId;
    private Long receiveId;
    private Integer msgTag;
    private Date createTime;
    private String name;
    private String sex;
    private String avatar;
    private Long unread;
    private List<Long> commentIds;
}
