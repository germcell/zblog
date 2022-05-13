package com.zs.handler;

/**
 * @Created by zs on 2022/3/3.
 */
public class UniversalException extends RuntimeException {

    public UniversalException() {
    }

    public UniversalException(String message) {
        super(message);
    }

    public UniversalException(String message, Throwable cause) {
        super(message, cause);
    }
}
