package com.zs.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Controller异常处理器
 *
 * @description 使用 @Controller 注解的类中抛出异常时会被此类处理,由 exceptionHandler 方法将错误信息保存并转发到 500.html.
 * @Created by zs on 2022/2/21.
 */
@ControllerAdvice
public class ControllerExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(Exception.class)
    public ModelAndView exceptionHandler(HttpServletRequest request, Exception e) throws Exception {
        logger.info("请求url : {}, 异常信息 : {}", request.getRequestURL(), e);
//        logger.isWarnEnabled()

        /* 当抛出的异常 e 带了HTTP状态码的时候，由异常自己处理，而不会由异常处理器处理.
           主要是为了区分此异常是 404 还是 500；500由异常处理器处理，404 则抛出.
         */
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
            throw e;
        }

        ModelAndView mv = new ModelAndView();
        mv.setViewName("500");
        mv.addObject("errorURL", request.getRequestURL());
        mv.addObject("exception", e);
        return mv;
    }

}
