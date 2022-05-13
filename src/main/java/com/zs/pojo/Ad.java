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
public class Ad {
    private Integer aid;
    private String adCode;
    private Integer type;
    private Long bid;
    private String adTitle;
    private String adDesc;
    private String adImgUrl;
    private String adUrl;
    private Date createTime;
    private Integer adStatus;
}
