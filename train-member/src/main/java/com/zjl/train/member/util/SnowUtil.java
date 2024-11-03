package com.zjl.train.member.util;

import cn.hutool.core.util.IdUtil;

/**
 * 封装hutool雪花算法
 * Created By Zhangjilin 2024/10/29
 */
public class SnowUtil {

    private static long dataCenterId = 1;  //数据中心
    private static long workerId = 1;     //机器标识

    /**
     * 封装，隐藏数据中心和机器标识
     * Created By Zhangjilin 2024/10/29
     * @return
     */
    public static long getSnowflakeNextId() {
        return IdUtil.getSnowflake(workerId, dataCenterId).nextId();
    }

    public static String getSnowflakeNextIdStr() {
        return IdUtil.getSnowflake(workerId, dataCenterId).nextIdStr();
    }
}
