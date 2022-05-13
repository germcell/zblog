package com.zs.controller;

import com.zs.config.Const2;
import com.zs.pojo.Writer;
import com.zs.service.WriterService;
import com.zs.vo.ResultVO;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @Created by zs on 2022/4/22.
 */
@RestController
@CrossOrigin
@RequestMapping("/v2/lr")
public class WriterController {

    @Resource
    private WriterService writerService;

    /**
     * 查询用户名\邮箱是否已被使用
     * @param name
     * @return 状态码为601表示已被使用 反之为602
     */
    @GetMapping("/check")
    public ResultVO findName(@RequestParam(value = "name", required = false) String name,
                             @RequestParam(value = "mail", required = false) String mail) {
        return writerService.repeatName(name, mail);
    }

    /**
     * 发送验证码接口
     * @param mail
     * @return
     */
    @GetMapping("/code")
    public ResultVO sendValidateCode(@RequestParam("mail") String mail) {
        return writerService.sendValidateCode(mail);
    }

    /**
     * 用户注册接口
     * @param writer 用户账户信息
     * @param code 验证码
     * @return
     */
    @PostMapping("/register.do")
    public ResultVO doRegister(@RequestBody Writer writer, @RequestParam("code") String code) {
        return writerService.register(writer, code);
    }

    /**
     * 用户登录接口
     * @param writer
     * @return
     */
    @PostMapping("/login.do")
    public ResultVO doLogin(@RequestBody Writer writer) {
        return writerService.login(writer);
    }

    /**
     * 验证用户是否登录、token是否还生效
     * @param token
     * @return
     */
    @GetMapping("/isLogin")
    public ResultVO isLogin(@RequestHeader String token) {
        return new ResultVO(Const2.LOGIN_SUCCESS, "already logged", null);
    }

    /**
     * 查询自己的信息
     * @param uid 用户id
     * @param token
     * @return
     */
    @GetMapping("/{uid}")
    public ResultVO getWriter(@PathVariable("uid") String uid, @RequestHeader String token) {
        return writerService.getWriterByUid(uid, new Integer(0));
    }

    /**
     * 查询用户的信息
     * @param uid 用户id
     * @return
     */
    @GetMapping("/access/{uid}")
    public ResultVO getWriterByAccess(@PathVariable("uid") String uid) {
        return writerService.getWriterByUid(uid, new Integer(1));
    }


    /**
     * 更新用户信息
     * @param uuid 用户id
     * @param token
     * @param writer 更新信息
     * @return
     */
    @PutMapping("/{uuid}")
    public ResultVO updateWriter(@PathVariable String uuid,
                                 @RequestHeader String token,
                                 @RequestBody Writer writer) {
        return writerService.updateWriterByUid(uuid, writer);
    }

    /**
     * 更新用户头像
     * @param uuid
     * @param token
     * @param avatarFile
     * @return
     */
    @PostMapping("/{uuid}/avatar")
    public ResultVO updateAvatar(@PathVariable String uuid,
                                 @RequestHeader String token,
                                 String mail,
                                 MultipartFile avatarFile) {
        return writerService.updateAvatarByUid(uuid, mail, avatarFile);
    }
}
