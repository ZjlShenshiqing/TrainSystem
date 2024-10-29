package com.zjl.train.common.exception;

public class BusinessException extends RuntimeException{

    private BusinessExceptionEnum businessExceptionEnum;

    public BusinessException(BusinessExceptionEnum businessExceptionEnum) {
        this.businessExceptionEnum = businessExceptionEnum;
    }

    public BusinessExceptionEnum getBusinessExceptionEnum() {
        return businessExceptionEnum;
    }

    public void setBusinessExceptionEnum(BusinessExceptionEnum businessExceptionEnum) {
        this.businessExceptionEnum = businessExceptionEnum;
    }

    /**
     * 不写入堆栈信息，提高性能
     * Created By Zhangjilin 2024/10/29
     */
    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
