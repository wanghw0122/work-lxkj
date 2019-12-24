package com.rcplatformhk.us.service.impl;

import com.rcplatformhk.us.service.UserPool;
import com.rcplatformhk.utils.DateUtil;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUserPool implements UserPool, Serializable {

    private static final long serialVersionUID = 1L;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    private String cacheKey;
    private BoundSetOperations<String, Object> boundSetOperations;

    @PostConstruct
    private void init() {
        getKey();
        if (Objects.isNull(boundSetOperations)) {
            boundSetOperations = redisTemplate.boundSetOps(cacheKey);
        }
    }

    private synchronized String getKey() {
        String key = DateUtil.getTodayDate();
        if (StringUtil.isNullOrEmpty(cacheKey) || !cacheKey.equals(key)) {
            cacheKey = key;
            boundSetOperations = redisTemplate.boundSetOps(cacheKey);
            Long expire = redisTemplate.getExpire(cacheKey);
            if (expire <= 0L) {
                redisTemplate.expire(cacheKey, 1, TimeUnit.DAYS);
            }
        }
        return key;
    }

    @Override
    public boolean exist(Object object) {
        return boundSetOperations.isMember(object);
    }

    @Override
    public boolean delete(Object object) {
        Long remove = boundSetOperations.remove(object);
        return 1L == remove;
    }

    public boolean checkAndPut(Object object) {
        try {
            getKey();
            List list = redisTemplate.execute(new SessionCallback<List<Object>>() {
                @Override
                public List<Object> execute(RedisOperations operations) throws DataAccessException {

                    operations.multi();
                    boundSetOperations.isMember(object);
                    boundSetOperations.add(object);
                    return operations.exec();
                }
            });
            assert list != null;
            return (boolean) list.get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void save(Object o) {
        boundSetOperations.add(o);
    }
}
