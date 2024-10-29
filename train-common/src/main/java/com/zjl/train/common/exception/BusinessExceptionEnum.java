package com.zjl.train.common.exception;

/**
 * 自定义异常枚举类，将异常报错的详细信息封装到此处
 * Created By Zhangjilin 2024/10/29
 */
public enum BusinessExceptionEnum {

    MEMBER_MOBILE_EXIST("手机号已注册");

    private String description;

    BusinessExceptionEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "BusinessExceptionEnum{" +
                "description='" + description + '\'' +
                '}';
    }
}
