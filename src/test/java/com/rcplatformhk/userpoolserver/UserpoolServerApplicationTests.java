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
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
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
        long l = redisTemplate.opsForList().rightPush("h","1234");
        String p = (String) redisTemplate.opsForList().leftPop("h");
        System.out.println(p);
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

    @Test
    void testLog(){
        log.info("test log");
    }
}
