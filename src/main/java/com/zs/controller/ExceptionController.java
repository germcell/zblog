package com.zs.controller;

import com.zs.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author zengshuai
 * @create 2022-09-25 22:05
 */
@Slf4j
@RestControllerAdvice
public class ExceptionController {
    @ExceptionHandler(Exception.class)
    public ResultVO globalExceptionHandler(Exception e) {
      log.warn("系统异常==>{}", e);
      return ResultVO.globalException();
    }
}
