package com.zs.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Created by zs on 2022/4/20.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ResultVO {

    private Integer code;
    private String message;
    private Object data;

}
