package com.zs.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Created by zs on 2022/3/20.
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EditorJson {
    /**
     * editormd上传图片返回json数据
     */
    private int success;
    private String message;
    private String url;
}
