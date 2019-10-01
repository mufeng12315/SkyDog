package com.mufeng.skydog.web;

import com.mufeng.skydog.bean.BaseResult;
import com.mufeng.skydog.constants.SdErrorCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 类ControllerAspect.java的实现描述：Controler切面增强
 * 
 */
@ControllerAdvice
@Slf4j
public class ControllerAspect {

    @ExceptionHandler()
    @ResponseBody
    public BaseResult<Void> handleException(Exception e){
        log.error("errror",e);
        BaseResult<Void> errorResult = new BaseResult<Void>();
        errorResult.setCode(SdErrorCodeEnum.SYS_ERROR.getCode());
        errorResult.setMessage(SdErrorCodeEnum.SYS_ERROR.getName());
        return errorResult;
    }

}
