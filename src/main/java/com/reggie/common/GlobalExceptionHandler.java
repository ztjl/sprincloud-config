package com.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 异常处理方法
     * @param exception
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public Result<String> exceptionHandler(SQLIntegrityConstraintViolationException exception){
        log.info(exception.getMessage());
        if (exception.getMessage().contains("Duplicate entry")){
            String[] msg = exception.getMessage().split(" ");
            return Result.error(msg[2]+"已存在");
        }
        return Result.error("未知错误");
    }

    /**
     * 异常处理方法
     * @param exception
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public Result<String> customException(CustomException exception){
        log.info(exception.getMessage());
        return Result.error(exception.getMessage());
    }



}
