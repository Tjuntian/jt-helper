package com.asphyxia.exception;

import com.asphyxia.exception.BusinessException;
import com.asphyxia.exception.SystemException;
import com.asphyxia.pojo.Code;
import com.asphyxia.pojo.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class ProjectExceptionAdvice {
    //处理器类对应的异常类型
    @ExceptionHandler(SystemException.class)
    public Result doSystemException(SystemException ex){
        return new Result(ex.getCode(),null,ex.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public Result doBusinessException(BusinessException ex){
        return new Result(ex.getCode(),null,ex.getMessage());
    }

    //非预期的异常
    @ExceptionHandler(Exception.class)
    public Result doOtherException(Exception ex){
        return new Result(Code.SYSTEM_UNKNOW_ERR,null,"系统繁忙，请稍后再试！");
    }
}
