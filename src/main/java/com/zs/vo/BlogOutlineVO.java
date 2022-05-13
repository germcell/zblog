package com.zs.vo;

import com.zs.pojo.BlogOutline;
import com.zs.pojo.Writer;
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
public class BlogOutlineVO {
    private BlogOutline blogOutline;
    private Writer writer;
}
