package com.zs.controller;

import com.zs.config.Const;
import com.zs.pojo.RequestResult;
import com.zs.pojo.User;
import com.zs.service.UserService;
import org.aspectj.weaver.ast.Var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;

/**
 * @Created by zs on 2022/2/22.
 */
@Controller
@RequestMapping("/admin")
public class LoginController {

    @Autowired
    private UserService userService;

    /**
     * 转发页面
     * @return admin/login.html
     */
    @GetMapping("/loginPage")
    public String loginPage() {
        return "admin/login";
    }

    /**
     * 转发页面到后台管理首页
     * @return admin/index.html
     */
    @GetMapping("/index")
    public String indexPage() {
        return "admin/index";
    }

    /**
     * 登录请求
     * @param nickname
     * @param pwd
     * @param session
     * @param attributes
     * @return
     */
    @PostMapping("/login.do")
    public String login(@RequestParam String nickname,
                        @RequestParam String pwd,
                        HttpSession session,
                        RedirectAttributes attributes) {

        if (StringUtils.hasText(nickname) && StringUtils.hasText(pwd)) {
            User loginUser = userService.checkUser(nickname, pwd);
            if (loginUser != null) {
                session.setAttribute("loginUser", loginUser);
                return "redirect:index";
            } else {
                attributes.addFlashAttribute("prompt", "用户名或密码错误");
                return "redirect:admin/loginPage";
            }
        }
        attributes.addFlashAttribute("prompt", "请输入用户名或密码");
        return "redirect:admin/loginPage";
    }

    /**
     * 安全退出请求
     * @param session
     * @return
     */
    @GetMapping("/loginOut")
    public String loginOut(HttpSession session) {
        session.invalidate();
        return "redirect:admin/loginPage";
    }

    /**
     * 异步生成验证码并发送到邮箱
     * @param session 存放验证码
     * @param mail 申请邮箱
     * @return
     */
    @GetMapping("/validate")
    @ResponseBody
    public RequestResult generateValidateCode(String mail,
                                       HttpSession session) {

        RequestResult result = userService.registerValidateCode(mail);
        if (result.getCode() == Const.GENERATE_CODE_SUCCESS) {
            session.setAttribute(Const.REGISTER_VALIDATE_CODE_SESSION, result.getData());
        }
        return result;
    }

    /**
     * 注册请求
     * @param nickname 用户名
     * @param pwd 密码
     * @param mail 邮箱
     * @param validateCode 验证码
     * @param session
     * @return
     */
    @PostMapping("/register.do")
    @ResponseBody
    public RequestResult register(@RequestParam String nickname,
                           @RequestParam String pwd,
                           @RequestParam String mail,
                           @RequestParam String validateCode,
                           HttpSession session) {

        // 验证码判断
        String codeSession = (String) session.getAttribute(Const.REGISTER_VALIDATE_CODE_SESSION);
        if (!(StringUtils.hasText(validateCode) && validateCode.equals(codeSession))) {
            RequestResult checkCodeResult = new RequestResult();
            checkCodeResult.setCode(Const.VALIDATE_CODE_CHECK_FAILED);
            checkCodeResult.setMessage("验证码有误");
            return checkCodeResult;
        }

        // 注册
        RequestResult registerResult = userService.register(nickname, pwd, mail);
        if (registerResult.getCode() == Const.REGISTER_SUCCESS) {
            session.removeAttribute(Const.REGISTER_VALIDATE_CODE_SESSION);
        }
        return registerResult;
    }
}
