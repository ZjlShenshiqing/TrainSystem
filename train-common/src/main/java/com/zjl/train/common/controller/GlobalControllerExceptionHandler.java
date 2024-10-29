package com.zjl.train.common.controller;

import com.zjl.train.common.exception.BusinessException;
import com.zjl.train.common.resp.CommonResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常处理、数据预处理等
 * Created By Zhangjilin 2024/10/29
 */
@ControllerAdvice
public class GlobalControllerExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

    /**
     * 所有异常统一处理
     * Created By Zhangjilin 2024/10/29
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public CommonResp exceptionHandler(Exception e){
        CommonResp commonResp = new CommonResp();
        LOG.error("系统异常：", e);
        commonResp.setSuccess(false);
        commonResp.setMessage("系统出现异常，请联系管理员");
        return commonResp;
    }

    /**
     * 业务异常统一处理
     * Created By Zhangjilin 2024/10/29
     */
    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public CommonResp exceptionHandler(BusinessException e){
        CommonResp commonResp = new CommonResp();
        LOG.error("业务异常：{}", e.getBusinessExceptionEnum().getDescription());
        commonResp.setSuccess(false);
        commonResp.setMessage(e.getBusinessExceptionEnum().getDescription());
        return commonResp;
    }
}
