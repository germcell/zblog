package com.zs.mapper;

import com.zs.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Created by zs on 2022/2/22.
 */
@Mapper
public interface UserMapper {
    /**
     * 按条件查找一条记录
     * @param nickname
     * @param pwd
     * @return
     */
    User getUserByNicknamePwd(String nickname, String pwd);

    /**
     * 插入一条记录
     * @param user
     * @return
     */
    int insertUser(@Param("user") User user);

    /**
     * 查找记录
     * @param user
     * @return
     */
    User getUser(@Param("user") User user);

    /**
     * 修改用户登录时间
     * @param id
     * @return
     */
    int updateLoginTime(@Param("uid") Long uid);
}
