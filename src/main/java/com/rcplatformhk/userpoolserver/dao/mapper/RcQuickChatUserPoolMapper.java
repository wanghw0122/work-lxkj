package com.rcplatformhk.userpoolserver.dao.mapper;

import com.rcplatformhk.userpoolserver.dao.entity.RcQuickChatConfigEntity;
import com.rcplatformhk.userpoolserver.pojo.QuickChatUserInfo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Repository
public interface RcQuickChatUserPoolMapper {

    @Insert({ "insert into rc_video_chat.rc_quick_chat_user_pool (user_id, pool_id, cannel, vip_level, " +
            "gender, country_id, eroticism_behavior,head_img,user_name,age,app_id,stone_version,create_time," +
            "active_time,status,extend) values (#{userId}, #{poolId}, #{channel}, #{vipLevel}, #{gender},#{countryId}," +
            "#{eroticismBehavior},#{headImg},#{userName},#{age},#{appId},#{stoneVersion},#{createTime, jdbcType=TIMESTAMP}," +
            "#{activeTime, jdbcType=TIMESTAMP},#{status},#{extend})" })
    int insertQuickChatUserInfo(QuickChatUserInfo userInfo);
}
