package com.zs.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zs.config.Const2;
import com.zs.vo.ResultVO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 用户登录拦截器，基于请求头token验证
 * 1.自定义请求头，做预见操作
 * 2.获取token
 * 3.解析token
 * 4.返回结果
 * @Created by zs on 2022/4/20.
 */
@Slf4j
@Component
public class UserLoginInterceptor implements HandlerInterceptor {

    private JwtParser parser = Jwts.parser().setSigningKey(Const2.TOKEN_PWD);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 放行swagger文档
        if (request.getRequestURL().toString().contains("api-docs")) {
            return true;
        }

        // 预检操作
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        // 获取token
        String token = request.getHeader("token");
        if (token == null) {
            log.info("操作拦截,未登录");
            doResponse(response, new ResultVO(Const2.NO_LOGIN, "please login", null));
            return false;
        }
        // 解析
        try {
            Jws<Claims> claimsJws = parser.parseClaimsJws(token);
            log.info("授权放行,token==>{}", token);
            return true;
        } catch (Exception e) {
            log.info("操作拦截,token错误");
            doResponse(response, new ResultVO(Const2.TOKEN_EXPIRE, "token expire", null));
            return false;
        }
    }

    /**
     * 被拦截后响应请求数据
     * @param response
     * @param resultVO
     * @throws IOException
     */
    private void doResponse(HttpServletResponse response, ResultVO resultVO) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        PrintWriter writer = response.getWriter();
        ObjectMapper om = new ObjectMapper();
        String s = om.writeValueAsString(resultVO);
        writer.print(s);
        writer.flush();
        writer.close();
    }
}
