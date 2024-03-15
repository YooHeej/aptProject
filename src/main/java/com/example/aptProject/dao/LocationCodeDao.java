package com.example.aptProject.dao;

import com.example.aptProject.entity.LocationCode;
import org.apache.ibatis.annotations.Select;

public interface LocationCodeDao {

    @Select("SELECT * from locationcode WHERE lName=#{lName}")
    LocationCode getLocationCodeBylName(String lName);

    @Select("SELECT * from locationcode WHERE lCode=#{lCode}")
    LocationCode getLocationCodeBylCode(int lCode);
}
