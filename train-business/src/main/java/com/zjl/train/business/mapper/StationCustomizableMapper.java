package com.zjl.train.business.mapper;

import com.zjl.train.business.entity.Station;
import org.apache.ibatis.annotations.Param;

public interface StationCustomizableMapper {

    Station selectByUnique(@Param("name") String name);
}
