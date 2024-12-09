package com.zjl.train.business.enums;

/**
 * 确认订单信息的枚举类
 * Created By Zhangjilin 2024/12/9
 */
public enum ConfirmOrderStatusEnum {

    INIT("I","初始"),
    PENDING("P", "处理中"),
    SUCCESS("F","失败"),
    FAILURE("E","无票"),
    CANCEL("C","取消");

    private String code;

    private String desc;

    ConfirmOrderStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "ConfirmOrderStatusEnum{" +
                "code='" + code + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
