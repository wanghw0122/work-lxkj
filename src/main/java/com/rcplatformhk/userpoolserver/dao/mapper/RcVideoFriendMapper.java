package com.rcplatformhk.userpoolserver.dao.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface RcVideoFriendMapper {
    @Select("SELECT user_id,count(1) count FROM rc_video_chat.rc_user_friend where user_id in (#{id}) group by user_id")
    @Results({
            @Result(property = "id",  column = "user_id"),
            @Result(property = "friendCount", column = "count"),
    })
    List<Map<String,Object>> getFriendsCountByUserId(@Param("id")Integer id);

}
