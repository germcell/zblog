package com.zs.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * @Created by zs on 2022/4/30.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Fans {
    private Long fid;
    private Long uid; // 关注人id
    private Long uid2; // 被关注人id
    private Date joinTime;
}
