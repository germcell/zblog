package com.zs.mapper;

import com.zs.pojo.Copyright;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Created by zs on 2022/3/4.
 */
@Mapper
public interface CopyrightMapper {

    /**
     * 查询所有版权信息
     * @return
     */
    List<Copyright> listCopyright();

    /**
     * 按版权id查询
     * @param crTipId 版权id
     * @return
     */
    Copyright getCopyRightByCrTipId(@Param("crTipId") Integer crTipId);

}
