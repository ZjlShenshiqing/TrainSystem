package com.zjl.train.common.exception;

/**
 * 自定义异常枚举类，将异常报错的详细信息封装到此处
 * Created By Zhangjilin 2024/10/29
 */
public enum BusinessExceptionEnum {


    MEMBER_MOBILE_EXIST("手机号已注册"),
    MEMBER_MOBILE_NOT_EXIST("请先获取短信验证码!"),
    MEMBER_MOBILE_CODE_ERROR("验证码错误，请重新输入！"),
    SMS_CODE_EXPIRED("验证码已过期，请重新获取"),
    INVALID_SMS_CODE("验证码输入错误！"),
    BUSINESS_STATION_NAME_UNIQUE_ERROR("车站已存在！"),
    BUSINESS_TRAIN_CODE_UNIQUE_ERROR("车次已存在！"),
    BUSINESS_TRAIN_STATION_INDEX_UNIQUE_ERROR("车次经停站序号已存在！"),
    BUSINESS_TRAIN_STATION_NAME_UNIQUE_ERROR("车次经停站站名已存在！"),
    BUSINESS_TRAIN_CARRIAGE_INDEX_UNIQUE_ERROR("同车次车厢已存在！");

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
