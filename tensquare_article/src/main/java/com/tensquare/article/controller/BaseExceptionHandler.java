package com.tensquare.article.controller;
import entity.Result;
import entity.StatusCode;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: HeWei·Yuan
 * @CreateTime: 2021-03-21 20:31
 * @Description:
 */
@ControllerAdvice
public class BaseExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result handler(Exception e) {
        System.out.println("异常处理:" + e.getMessage());
        return new Result(false, StatusCode.ERROR, e.getMessage());
    }
}
