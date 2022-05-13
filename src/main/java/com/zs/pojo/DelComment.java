package com.zs.pojo;

import lombok.*;

import java.util.List;

/**
 * 存放留言类别
 * @Created by zs on 2022/3/15.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class DelComment {

    private List<Long> haveChildrenComment;  // 有子留言
    private List<Long> noChildrenComment;    // 没有子留言

}
