package com.zs.vo;

import com.zs.pojo.Blog;
import com.zs.pojo.Category;
import com.zs.pojo.Copyright;
import com.zs.pojo.Writer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Created by zs on 2022/4/21.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BlogVO {
    private Blog blog;
    private Writer writer;
    private Category category;
    private Copyright copyright;
}
