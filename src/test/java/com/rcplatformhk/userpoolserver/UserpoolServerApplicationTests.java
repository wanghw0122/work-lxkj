package com.rcplatformhk.userpoolserver;

import com.rcplatformhk.userpoolserver.dao.entity.TeacherEntity;
import com.rcplatformhk.userpoolserver.dao.mapper.RcUserMapper;
import com.rcplatformhk.userpoolserver.dao.mapper.RcVideoFriendMapper;
import com.rcplatformhk.userpoolserver.dao.mapper.RcVideoRecordOddMapper;
import com.rcplatformhk.userpoolserver.dao.mapper.TeacherMapper;
import com.rcplatformhk.userpoolserver.pojo.UserInfo;
import com.rcplatformhk.userpoolserver.service.impl.RedisDelayQueue;
import com.rcplatformhk.userpoolserver.service.impl.RedisUserPool;
import com.rcplatformhk.userpoolserver.utils.SpringContextUtil;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserpoolServerApplicationTests {

    @Autowired
    RedisUserPool redisUserPool;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    TeacherMapper teacherMapper;
    @Autowired
    RedisDelayQueue redisDelayQueue;
    @Autowired
    RcVideoRecordOddMapper rcVideoRecordOddMapper;
    @Autowired
    RcVideoFriendMapper rcVideoFriendMapper;
    @Autowired
    RcUserMapper rcUserMapper;

    @Test
    void contextLoads() {
       List<TeacherEntity> teachers = teacherMapper.getAll();
    }

    @Test
    void testRedis(){
        redisDelayQueue.put(UserInfo.builder().updateTime("2019-12-17 12:00:00").build());
    }

    @Test
    void testBean(){
        RedisUserPool redisUserPool =  SpringContextUtil.getBean(RedisUserPool.class);
    }

    @Test
    void testMapper(){
        rcVideoRecordOddMapper.getLiveChatStatisticsByUserId(648);
        rcVideoRecordOddMapper.getLiveChatStatisticsByUserIdAndRequestType(null,648);
    }

}
