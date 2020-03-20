package com.czkj.mail.config;

import com.czkj.mail.common.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 统一异常处理
 * @Author steven.sheng
 * @Date 2019/9/19/01917:40
 */
@ControllerAdvice
@Slf4j
@Deprecated
public class MailExceptionHandler {

    @ExceptionHandler(Exception.class)
    public CommonResponse handle(Exception e){
        log.error("系统异常:{}", ExceptionUtils.getStackTrace(e));
        return CommonResponse.fail(e.getMessage());
    }
}
