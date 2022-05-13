package com.zs.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @description 项目里的资源（如查找的博客）找不到的时候抛出的异常，
 *              这个注解表示 @ResponseStatus(HttpStatus.NOT_FOUND) 如果抛出这个异常的时候会带一个 HTTP 的状态码，为404
 * @Created by zs on 2022/2/21.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class CustomNotFoundException extends RuntimeException{
    public CustomNotFoundException() {
        super();
    }

    public CustomNotFoundException(String message) {
        super(message);
    }

    public CustomNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
