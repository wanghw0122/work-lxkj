package com.rcplatformhk.userpoolserver.service.impl;

import com.google.common.collect.Lists;
import com.mysql.jdbc.StringUtils;
import com.rcplatformhk.userpoolserver.pojo.UserInfo;
import com.rcplatformhk.userpoolserver.service.Queue;
import com.rcplatformhk.userpoolserver.service.UserPool;
import com.rcplatformhk.userpoolserver.utils.SerializeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
public class RedisCacheQueue implements Queue {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private BoundListOperations boundListOperations;

    private static String QUEUE = "CACHE_QUEUE";

    @PostConstruct
    public void init(){
        boundListOperations = redisTemplate.boundListOps(QUEUE);
    }

    @Override
    public List<Optional<UserInfo>> pop(int n) {
        List<Optional<UserInfo>> list = Lists.newArrayList();
        int i = 0;
        while (i < n){
            try {
                Object o =  boundListOperations.leftPop(10,TimeUnit.SECONDS);
                if (Objects.isNull(o)) {
                    Thread.yield();
                    continue;
                }
                Optional<UserInfo> optionalUserInfo = SerializeUtils.deserialize((String) o,UserInfo.class);
                list.add(optionalUserInfo);
                ++i;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return list;
    }

    public UserInfo pop(){
        return pop(1).get(0).orElse(null);
    }
}
