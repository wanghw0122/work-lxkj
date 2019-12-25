package com.rcplatformhk.us.dao.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface RcUserRecordMapper {
    @Select("select user_id,count(distinct(date_format(create_time,'%y%m%d'))) count from rc_user_record where user_id in (#{id}) and create_time > #{createTime}")
    @Results({
            @Result(property = "id",  column = "user_id"),
            @Result(property = "activeCount", column = "count"),
    })
    List<Map<String,Object>> getActiveDaysByIdAndTime(@Param("id") Integer id, @Param("createTime") String createTime);
}
