package com.zs.service.impl;

import com.zs.config.Const;
import com.zs.handler.QQMailUtils;
import com.zs.handler.RandomUtils;
import com.zs.mapper.UserMapper;
import com.zs.pojo.RequestResult;
import com.zs.pojo.User;
import com.zs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import sun.security.provider.MD5;

import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * @Created by zs on 2022/2/22.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 按照指定条件检查有无记录
     * @param nickname 昵称
     * @param pwd 密码
     * @return 用户
     */
    @Override
    public User checkUser(String nickname, String pwd) {
        User userByNicknamePwd = userMapper.getUserByNicknamePwd(nickname,
                                 DigestUtils.md5DigestAsHex(pwd.getBytes(StandardCharsets.UTF_8)));
        if (userByNicknamePwd != null) {
            userMapper.updateLoginTime(userByNicknamePwd.getUid());
        }
        return userByNicknamePwd;
    }

    /**
     * 生成注册验证码
     * @param mail 申请邮箱
     * @return 请求结果
     */
    @Override
    public RequestResult registerValidateCode(String mail) {
        User user = new User();
        user.setMail(mail);
        RequestResult result = new RequestResult();
        // 1. 查询注册邮箱是否已被注册
        User userByMail = userMapper.getUser(user);
        if (userByMail == null) {
            // 2. 返回验证码
            String code = RandomUtils.generateRandomNum();
            if (code != null) {
                boolean sendResult = QQMailUtils.sendMail(mail, code, "【Blog】验证码");
                if (sendResult) {
                    result.setCode(Const.GENERATE_CODE_SUCCESS);
                    result.setMessage("验证码已发送,请留意邮箱");
                    result.setData(code);
                } else {
                    result.setCode(Const.SEND_VALIDATE_CODE_FAILED);
                    result.setMessage("验证码发送失败，请检查邮箱状态");
                }
            } else {
                result.setCode(Const.GENERATE_CODE_FAILED);
                result.setMessage("验证码生成失败,请刷新页面后重试");
            }
        } else {
            result.setCode(Const.REGISTER_FAILED_USED);
            result.setMessage("注册失败,邮箱已被注册");
        }
        return result;
    }

    /**
     * 注册
     * @param nickname
     * @param pwd
     * @param mail
     * @return
     */
    @Override
    public RequestResult register(String nickname, String pwd, String mail) {
        RequestResult result = new RequestResult();
        User user = new User();
        user.setNickname(nickname);
        user.setPwd(DigestUtils.md5DigestAsHex(pwd.getBytes(StandardCharsets.UTF_8)));
        user.setMail(mail);
        user.setAvatar("../../static/images/del_avatar3.jpeg");
        user.setType(0);
        user.setRegisterTime(new Date());
        user.setUpdateTime(new Date());
        int rows = userMapper.insertUser(user);
        if (rows == 1) {
            result.setCode(Const.REGISTER_SUCCESS);
            result.setMessage("注册成功");
        }
        return result;
    }
}
