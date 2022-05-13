package com.zs.aspect;

import com.sun.corba.se.impl.corba.RequestImpl;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * @description 日志切面类，将所有经过Controller的请求都记录到日志文件
 * @Created by zs on 2022/2/21.
 */
@Component
@Aspect
public class LogAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 切点
     */
    @Pointcut("execution(* com.zs.controller.*.*(..))")
    public void log() {}

    /**
     * 前置通知，在请求处理之前将请求信息写入日志
     * @param joinPoint 用于获取请求方法和请求参数
     */
    @Before("log()")
    public void doBeforeLog(JoinPoint joinPoint) {
        // 用于获取当前请求的request对象
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String requestURL = request.getRequestURL().toString();
        String remoteIP = request.getRemoteAddr();
        String requestMethod = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        Object[] requestArgs = joinPoint.getArgs();
        // 设置请求日志类对象
        RequestLog requestLog = new RequestLog(requestURL, remoteIP, requestMethod, requestArgs);

        logger.info("RequestLog:{}", requestLog);
    }

    /**
     * 请求日志类，包括:请求url，请求ip，请求方法，请求参数
     */
    private class RequestLog {
        private String requestURL;
        private String requestIP;
        private String requestMethod;
        private Object[] requestArgs;

        public RequestLog(String url, String requestIP, String requestMethod, Object[] args) {
            this.requestURL = url;
            this.requestIP = requestIP;
            this.requestMethod = requestMethod;
            this.requestArgs = args;
        }

        @Override
        public String toString() {
            return "{" +
                    "url='" + requestURL + '\'' +
                    ", requestIP='" + requestIP + '\'' +
                    ", requestMethod='" + requestMethod + '\'' +
                    ", requestArgs=" + Arrays.toString(requestArgs) +
                    '}';
        }
    }

}
