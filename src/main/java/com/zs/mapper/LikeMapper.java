package com.zs.mapper;

import com.zs.pojo.Like;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Created by zs on 2022/5/12.
 */
@Mapper
public interface LikeMapper {
    /**
     * 查询指定用户的所有点赞记录
     * @param mid 用户id
     * @return
     */
    List<Like> listLikesByMid(@Param("mid") Long mid);
}
