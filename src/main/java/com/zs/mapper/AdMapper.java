package com.zs.mapper;

import com.zs.pojo.Ad;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Created by zs on 2022/4/20.
 */
@Mapper
public interface AdMapper {

    /**
     * 根据状态获取活动记录（激活状态）
     * @param status
     * @return
     */
    Ad getAdByStatus(@Param("status") Integer status) throws Exception;

}
