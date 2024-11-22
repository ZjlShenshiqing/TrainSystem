package com.zjl.train.business.mapper;

import com.zjl.train.business.entity.Station;
import org.apache.ibatis.annotations.Param;

/**
 * 站点自定义持久层
 * Created By Zhangjilin 2024/11/22
 */
public interface StationCustomizableMapper {

    Station selectByUnique(@Param("name") String name);
}
