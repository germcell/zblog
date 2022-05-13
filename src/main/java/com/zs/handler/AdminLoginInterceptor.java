package com.zs.handler;

import com.zs.pojo.User;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 管理员登录拦截器，基于session验证
 * @Created by zs on 2022/2/22.
 */
public class AdminLoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        User loginUser = (User) request.getSession().getAttribute("loginUser");
        if (loginUser != null) {
            return true;
        }
        request.setAttribute("prompt","请登录");
        request.getRequestDispatcher("/admin/loginPage").forward(request, response);
        return false;
    }


}
