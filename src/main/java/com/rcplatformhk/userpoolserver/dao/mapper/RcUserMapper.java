package com.rcplatformhk.userpoolserver.dao.mapper;

import com.rcplatformhk.userpoolserver.dao.entity.RcQuickChatConfigEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface RcUserMapper {
    @Select("SELECT id,pay_status FROM rc_user where id = #{id}")
    @Results({
            @Result(property = "id",  column = "id"),
            @Result(property = "payStatus", column = "pay_status"),
    })
    List<Map<String,Object>> getPayStatusById(@Param("id")Integer id);
}
