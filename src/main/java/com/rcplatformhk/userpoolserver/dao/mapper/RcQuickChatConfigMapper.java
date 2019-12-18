package com.rcplatformhk.userpoolserver.dao.mapper;

import com.rcplatformhk.userpoolserver.dao.entity.RcQuickChatConfigEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface RcQuickChatConfigMapper {
    @Select("SELECT * FROM rc_quick_chat_config where type = ${type}")
    @Results({
            @Result(property = "id",  column = "id"),
            @Result(property = "type", column = "type"),
            @Result(property = "configText",column = "config_text"),
            @Result(property = "createTime",column = "create_time",javaType = Date.class),
            @Result(property = "updateTime",column = "update_time",javaType = Date.class),
            @Result(property = "status",column = "has_del")
    })
    List<RcQuickChatConfigEntity> getConfigByType(@Param("type")String type);


    @Select("SELECT * FROM rc_quick_chat_config")
    @Results({
            @Result(property = "id",  column = "id"),
            @Result(property = "type", column = "type"),
            @Result(property = "configText",column = "config_text"),
            @Result(property = "createTime",column = "create_time"),
            @Result(property = "updateTime",column = "update_time"),
            @Result(property = "status",column = "has_del")
    })
    List<RcQuickChatConfigEntity> getAll();
}
