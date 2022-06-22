package com.zs.mapper;

import com.zs.pojo.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author Air
 * @create 2022-06-05 10:54
 */
@Mapper
public interface OrderMapper {
    /**
     * 插入一条记录
     * @param order
     * @return
     */
    int insert(@Param("order") Order order);

    /**
     * 按订单id更新记录
     * @param updateOrder
     * @return
     */
    int update(@Param("order") Order updateOrder);
}
