package com.rcplatformhk.us.service.impl;

import com.google.common.collect.Lists;
import com.rcplatformhk.pojo.UserInfo;
import com.rcplatformhk.us.service.Queue;
import com.rcplatformhk.utils.SerializeUtils;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class RedisCacheQueue implements Queue {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private BoundListOperations boundListOperations;

    private static String QUEUE = "CACHE_QUEUE";

    @PostConstruct
    public void init() {
        boundListOperations = redisTemplate.boundListOps(QUEUE);
    }

    @Override
    public List<Optional<UserInfo>> pop(int n) {
        List<Optional<UserInfo>> list = Lists.newArrayList();
        int i = 0;
        while (i < n) {
            try {
                String o = (String) boundListOperations.leftPop();
                if (StringUtil.isNullOrEmpty(o)) {
                    Thread.yield();
                    continue;
                }
                log.info(MessageFormat.format("thread : {0}, pop Object : {1}", Thread.currentThread().getName(), o));
                Optional<UserInfo> optionalUserInfo = SerializeUtils.deserialize(o, UserInfo.class);
                list.add(optionalUserInfo);
                ++i;
            } catch (Exception e) {
                log.error(MessageFormat.format("thread : {0}, pop Object Error:{2}",
                        Thread.currentThread().getName(), e.getMessage()), e);
            }
        }
        return list;
    }

    public UserInfo pop() {
        return pop(1).get(0).orElse(null);
    }
}
