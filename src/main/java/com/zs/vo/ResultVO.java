package com.zs.vo;

import com.zs.config.Const2;
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

    public static ResultVO success(Object data) {
        return new ResultVO(Const2.SERVICE_SUCCESS, "成功", data);
    }

    public static ResultVO paramError(Object data) {
        return new ResultVO(Const2.PARAMETER_FAIL, "参数有误", data);
    }

    public static ResultVO globalException() {
        return new ResultVO(Const2.GLOBAL_EXCEPTION, "系统异常", null);
    }
}
