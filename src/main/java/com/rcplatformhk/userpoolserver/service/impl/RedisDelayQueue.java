package com.rcplatformhk.userpoolserver.service.impl;

import com.google.common.collect.Maps;
import com.rcplatformhk.userpoolserver.pojo.UserInfo;
import com.rcplatformhk.userpoolserver.service.Queue;
import com.rcplatformhk.userpoolserver.utils.DateUtil;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class RedisDelayQueue implements Queue, Serializable {

    private static final long serialVersionUID = 1L;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    private BoundZSetOperations boundZSetOperations;

    private static String QUEUE = "DELAY_QUEUE";

    @PostConstruct
    private void init() {
        if (Objects.isNull(boundZSetOperations))
            boundZSetOperations = redisTemplate.boundZSetOps(QUEUE);
    }

    @Override
    public LinkedHashMap<UserInfo,Long> pop(int n) {
        LinkedHashMap res = Maps.newLinkedHashMap();
        if (n <= 0 ) return res;
        int l = n-1;
        List list;
        try {
            list = (List) new SessionCallback() {
                @Override
                public Object execute(RedisOperations redisOperations) throws DataAccessException {
                    redisOperations.multi();
                    boundZSetOperations.size();
                    boundZSetOperations.rangeWithScores(0, l);
                    boundZSetOperations.removeRange(0, l);
                    return redisOperations.exec();
                }
            }.execute(redisTemplate);
            long p = (long) list.get(0);
            long q = (long) list.get(2);
            Set set = (Set) list.get(1);
            if (p == 0L) return null;
            else assert set != null && q >= 1L;
            set.stream().filter(Objects::nonNull).forEach(o -> {
                ZSetOperations.TypedTuple typedTuple1 =  (ZSetOperations.TypedTuple)o;
                UserInfo userInfo = (UserInfo) typedTuple1.getValue();
                long score =  typedTuple1.getScore().longValue();
                res.put(userInfo,score);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public Optional<UserInfo> pop() throws InterruptedException {

        LinkedHashMap<UserInfo,Long> map = this.pop(1);
        while (map == null || map.size() == 0)
        {
            TimeUnit.SECONDS.sleep(10);
            map = pop(1);
        }
        return map.keySet().stream().findAny();
    }

    public boolean put(UserInfo... userInfos) {
        Set<ZSetOperations.TypedTuple<Object>> typedTupleSet = new HashSet<>();
        int l = userInfos.length;
        try {
            for (UserInfo userInfo : userInfos) {
                //TODO log
                if (StringUtil.isNullOrEmpty(userInfo.getUpdateTime()))
                    return false;
                ZSetOperations.TypedTuple<Object> typedTuple = new DefaultTypedTuple<>(userInfo, (double) DateUtil.parseToTimeStemp(userInfo.getUpdateTime()));
                typedTupleSet.add(typedTuple);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        Long res = boundZSetOperations.add(typedTupleSet);
        return l == res;
    }
}
