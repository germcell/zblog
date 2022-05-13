package com.zs.service;

import com.zs.pojo.Writer;
import com.zs.vo.ResultVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Created by zs on 2022/4/22.
 */
public interface WriterService {

    /**
     * 判断用户名是否已存在
     * @param name
     * @return
     */
    ResultVO repeatName(String name, String mail);

    /**
     * 给指定邮箱发送验证码
     * @param mail
     * @return
     */
    ResultVO sendValidateCode(String mail);

    /**
     * 用户注册
     * @param writer
     * @param code 验证码
     * @return
     */
    ResultVO register(Writer writer, String code);

    /**
     * 用户登录
     * @param writer
     * @return
     */
    ResultVO login(Writer writer);

    /**
     * 根据用户id查询用户信息
     * @param uid 用户id
     * @param whose 谁的信息
     *        whose == 0 查询自己的信息
     *        whose == 1 查询别人的信息
     */
    ResultVO getWriterByUid(String uid, Integer whose);

    /**
     * 更新指定用户信息
     * @param uid
     * @param writer
     * @return
     */
    ResultVO updateWriterByUid(String uid, Writer writer);

    /**
     * 更新指定用户头像
     * @param uid
     * @param avatarFile
     * @return
     */
    ResultVO updateAvatarByUid(String uid, String mail, MultipartFile avatarFile);
}
