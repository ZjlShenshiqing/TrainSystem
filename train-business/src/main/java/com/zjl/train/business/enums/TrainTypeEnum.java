package com.zjl.train.business.enums;

import java.math.BigDecimal;

/**
 * 车次分类枚举类
 * Created By Zhangjilin 2024/11/22
 */
public enum TrainTypeEnum {
    G("G", "高铁", new BigDecimal("1.2")),
    D("D", "动车", new BigDecimal("1")),
    T("T", "特快", new BigDecimal("0.8")),
    K("K", "快速",new BigDecimal("0.6"));

    private String code;

    private String description;

    /**
     *  票价比例
     *
     *  举例：高铁票价是普通列车的1.5倍
     */
    private BigDecimal priceRate;

    TrainTypeEnum(String code, String description, BigDecimal priceRate) {
        this.code = code;
        this.description = description;
        this.priceRate = priceRate;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPriceRate() {
        return priceRate;
    }
}
