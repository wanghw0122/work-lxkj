package com.rcplatformhk.userpoolserver.dao.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface RcVideoChatMapper {
    @Select("select user_id,count(1) count from rc_video_chat.rc_consume_record where user_id in (#{id}) and create_time > #{time} and money > 0")
    @Results({
            @Result(property = "id",  column = "user_id"),
            @Result(property = "payCount", column = "count"),
    })
    List<Map<String,Object>> checkPayStatusByHoursAndId(@Param("id")Integer id, @Param("time") String time);
}
