package com.zs.service;

import com.zs.pojo.RequestResult;
import com.zs.pojo.User;

/**
 * @Created by zs on 2022/2/22.
 */
public interface UserService {
    /**
     * 检查用户是否存在
     * @param nickname
     * @param pwd
     * @return 用户
     */
    User checkUser(String nickname, String pwd);

    /**
     * 生成注册验证码
     * @param mail 申请邮箱
     * @return 验证码
     */
    RequestResult registerValidateCode(String mail);

    /**
     * 注册
     * @param nickname
     * @param pwd
     * @param mail
     * @return
     */
    RequestResult register(String nickname, String pwd, String mail);
}
