package com.rcplatformhk.us.dao.mapper;

import com.rcplatformhk.us.dao.entity.UserLevelEntity;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Repository
public interface RcUserLevelMapper {

    @Select("SELECT * FROM rc_user_level")
    @Results({
            @Result(property = "id",  column = "id"),
            @Result(property = "appId", column = "app_id"),
            @Result(property = "name", column = "name"),
            @Result(property = "maxMoney", column = "max_money", javaType = BigDecimal.class),
            @Result(property = "minMoney", column = "min_money", javaType = BigDecimal.class),
            @Result(property = "createTime", column = "create_time", javaType = Date.class),
            @Result(property = "platformType", column = "platform_type"),
            @Result(property = "fakeLevelId",column = "fake_level_id")
    })
    List<UserLevelEntity> selectAll();
}
