package com.zs.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * @Created by zs on 2022/4/20.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Writer {
    private Integer uid;
    private String writerName;
    private String writerSex;
    private Integer writerAge;
    private Date writerBirthday;
    private String mail;
    private String pwd;
    private Integer writerStatus;
    private String writerAvatar;
    private String writerPhone;
    private String writerIntroduce;
    private Date registerTime;
    private Date updateTime;
    private Long fans;
    private Integer articleNum;
    private Integer isMember;
    private Long allViews;
    private Long allLikeNums;
}
