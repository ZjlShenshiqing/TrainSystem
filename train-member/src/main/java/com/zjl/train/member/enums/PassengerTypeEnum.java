package com.zjl.train.member.enums;

public enum PassengerTypeEnum {
    ADULT("1","成人"),
    CHILDREN("2","儿童"),
    STUDENT("3","学生");

    // 存入数据库的
    private String code;

    // 给前端用户看的
    private String description;

    PassengerTypeEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }


}
