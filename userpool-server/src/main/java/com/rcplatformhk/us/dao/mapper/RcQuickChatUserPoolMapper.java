package com.rcplatformhk.us.dao.mapper;

import com.rcplatformhk.pojo.QuickChatUserInfo;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

@Repository
public interface RcQuickChatUserPoolMapper {

    @Insert({ "insert into rc_quick_chat_user (user_id, pool_id, channel, vip_level, " +
            "gender, country_id, eroticism_behavior,head_img,user_name,age,app_id,stone_version,create_time," +
            "active_time,status,extend,platform_type) values (#{userId}, #{poolId}, #{channel}, #{vipLevel}, #{gender},#{countryId}," +
            "#{eroticismBehavior},#{headImg},#{userName},#{age},#{appId},#{stoneVersion},#{createTime, jdbcType=TIMESTAMP}," +
            "#{activeTime, jdbcType=TIMESTAMP},#{status},#{extend},#{platformType})" })
    int insertQuickChatUserInfo(QuickChatUserInfo userInfo);
}
