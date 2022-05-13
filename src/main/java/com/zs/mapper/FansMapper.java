package com.zs.mapper;

import com.zs.pojo.Fans;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Created by zs on 2022/4/30.
 */
@Mapper
public interface FansMapper {

    int insert(@Param("fans") Fans fans);

    List<Fans> listByCondition(@Param("fans") Fans fans);

    int deleteByCondition(@Param("fans") Fans fans) throws Exception;
}
