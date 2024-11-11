package com.zjl.train.member.mapper;


import com.zjl.train.member.entity.Passenger;
import org.apache.ibatis.annotations.Param;

public interface PassengerCustomizableMapper {

    Passenger selectByMemberIdAndIdCard(@Param("memberId") Long memberId, @Param("idCard") String idCard);
}