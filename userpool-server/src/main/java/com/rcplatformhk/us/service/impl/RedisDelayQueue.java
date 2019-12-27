package com.rcplatformhk.us.service.impl;

import com.google.common.collect.Maps;
import com.rcplatformhk.pojo.UserInfo;
import com.rcplatformhk.us.lock.RedisLock;
import com.rcplatformhk.us.service.Queue;
import com.rcplatformhk.utils.DateUtil;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RedisDelayQueue implements Queue, Serializable {

    private static final long serialVersionUID = 1L;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    private BoundZSetOperations boundZSetOperations;
    @Autowired
    private RedisLock redisLock;

    private static String QUEUE = "DELAY_QUEUE";

    @PostConstruct
    private void init() {
        if (Objects.isNull(boundZSetOperations))
            boundZSetOperations = redisTemplate.boundZSetOps(QUEUE);
    }

    @Override
    public LinkedHashMap<UserInfo, Long> pop(int n) {
        LinkedHashMap res = Maps.newLinkedHashMap();
        if (n <= 0) return res;
        int l = n - 1;
        List list;
        String threadName = Thread.currentThread().getName();
        try {
            redisLock.blockLock(2L);
            log.info("DELAY_QUEUE THREAD {} LOCK SUCCESS!", threadName);
            list = redisTemplate.executePipelined(new SessionCallback() {
                @Override
                public List<Object> execute(RedisOperations redisOperations) throws DataAccessException {
                    BoundZSetOperations zSetOperations = redisOperations.boundZSetOps(QUEUE);
                    zSetOperations.size();
                    zSetOperations.rangeWithScores(0, l);
                    zSetOperations.removeRange(0, l);
                    return null;
                }
            });
            log.info("DELAY_QUEUE THREAD {} POP OBJECT {} {} {} ", threadName, list.get(0), list.get(1), list.get(2));
            if (redisLock.unlock())
                log.info("DELAY_QUEUE THREAD {} UNLOCK SUCCESS!", threadName);
            long p = (long) list.get(0);
            long q = (long) list.get(2);
            HashSet set = (HashSet) list.get(1);
            if (p == 0L || q == 0L) return null;
            else assert set != null && q >= 1L;
            log.info(MessageFormat.format("DELAY_QUEUE THREAD {0} POP OBJECT SUCCESS!", threadName));
            set.stream().filter(Objects::nonNull).forEach(o -> {
                ZSetOperations.TypedTuple typedTuple1 = (ZSetOperations.TypedTuple) o;
                UserInfo userInfo = (UserInfo) typedTuple1.getValue();
                long score = typedTuple1.getScore().longValue();
                res.put(userInfo, score);
            });
        } catch (Exception e) {
            log.error(MessageFormat.format("DELAY_QUEUE THREAD {0} POP OBJECT ERROR!! MSG:{1}", threadName, e.getMessage(), e));
        } finally {
            if (redisLock.unlock()) {
                log.info("DELAY_QUEUE THREAD {} UNLOCK SUCCESS!", threadName);
            }
        }
        return res;
    }

    public Optional<UserInfo> pop() {

        LinkedHashMap<UserInfo, Long> map = this.pop(1);
        while (map == null || map.size() == 0) {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException ignored) {
            }
            map = pop(1);
        }
        return map.keySet().stream().findAny();
    }

    public boolean put(UserInfo... userInfos) {
        Set<ZSetOperations.TypedTuple<Object>> typedTupleSet = new HashSet<>();
        int l = userInfos.length;
        try {
            for (UserInfo userInfo : userInfos) {
                if (StringUtil.isNullOrEmpty(userInfo.getUpdateTime()))
                    return false;
                ZSetOperations.TypedTuple<Object> typedTuple = new DefaultTypedTuple<>(userInfo, (double) DateUtil.parseToTimeStemp(userInfo.getUpdateTime()));
                typedTupleSet.add(typedTuple);
                log.info(MessageFormat.format("thread : {0} typedTupleSet.add({1})", Thread.currentThread().getName(), userInfo));
            }
        } catch (Exception e) {
            log.error("RedisDelayQueue put Exception:{}", e.getMessage(), e);
            return false;
        }
        Long res = boundZSetOperations.add(typedTupleSet);
        log.info(MessageFormat.format("DELAY_QUEUE THREAD {0} ADD OBJECT {1} SUCCESS!", Thread.currentThread().getName(), userInfos));
        return l == res;
    }
}
