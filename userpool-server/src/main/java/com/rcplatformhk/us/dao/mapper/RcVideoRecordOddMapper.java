package com.rcplatformhk.us.dao.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface RcVideoRecordOddMapper {

    @Select("SELECT user_id,count(1) count FROM rc_live_chat_statistics.rc_video_record_odd where request_type=#{requestType} and user_id in (#{id}) group by user_id")
    @Results({
            @Result(property = "id",  column = "user_id"),
            @Result(property = "videoCount", column = "count"),
    })
    List<Map<String,Object>> getLiveChatStatisticsByUserIdAndRequestType(@Param("requestType") Integer requestType, @Param("id") Integer id);

    @Select("SELECT user_id,count(1) count FROM rc_live_chat_statistics.rc_video_record_odd where user_id in (#{id}) group by user_id")
    @Results({
            @Result(property = "id",  column = "user_id"),
            @Result(property = "videoCount", column = "count"),
    })
    List<Map<String,Object>> getLiveChatStatisticsByUserId(@Param("id") Integer id);

}
