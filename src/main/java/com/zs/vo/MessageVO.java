package com.zs.vo;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author zengshuai
 * @create 2022-09-10 20:58
 */
@Data
@ToString
public class MessageVO {
    private Long sendId;
    private String avatar;
    private String name;
    private String sex;
    private List<Long> commentIds;
}
