package com.rcplatformhk.us.lock;

import io.lettuce.core.SetArgs;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.cluster.api.async.RedisAdvancedClusterAsyncCommands;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
@SuppressWarnings("unchecked")
public class RedisLock {
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 存储当前线程  设置锁对应的value   用来释放锁时进行校验
     */
    private final ThreadLocal<String> lockValue = new ThreadLocal<>();

    private final String keyLock = "lock";


    /**
     * 获取锁 （带超时时间）
     *
     * @param timeout
     * @param expireTime
     * @return
     * @throws InterruptedException
     */
    public boolean tryLockWithTimeout(Integer timeout, Integer expireTime) {
        String value = UUID.randomUUID().toString();
        //获取未来过期时间点
        long invalidTime = System.currentTimeMillis() + timeout * 1000;
        boolean flag = false;
        while (System.currentTimeMillis() < invalidTime) {
            flag = tryLock(keyLock, value, expireTime);
            if (flag) {
                break;
            } else {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    log.error(e.toString());
                }
            }

        }
        if (!flag) {
            log.error("竞争锁失败");
        }
        return flag;
    }

    /**
     * 获取锁 （一直抢到为止） 阻塞锁
     *
     * @param expireTime 锁失效时间
     * @return
     */
    public boolean blockLock(Long expireTime) throws InterruptedException {
        String value = UUID.randomUUID().toString();
        boolean flag;
        while (true) {
            flag = tryLock(keyLock, value, expireTime);
            if (flag) {
                break;
            } else {
                Thread.sleep(10);
            }

        }
        return flag;
    }

    /**
     * 释放锁
     *
     * @return
     */
    public boolean unlock() {
        //重试次数
        Integer retryTimes = 3;
        //先拿到当前锁对应的值（理解为版本号）
        String value = redisTemplate.opsForValue().get(keyLock);
        Boolean flag = false;
        //如果相等 锁应该未被释放  不相等则锁已过期自动释放此时如果有锁应为其他进程的锁
        if (lockValue.get().equals(value)) {
            while (retryTimes >0) {
                try {
                    flag = redisTemplate.delete(keyLock);
                    break;
                } catch (Exception e) {
                    log.error("释放锁异常");
                    retryTimes--;
                }
            }
        }
        return flag;
    }

    /**
     * 设置锁 如果不存在key设置成功，
     *
     * @param key        redis key
     * @param value      redis value
     * @param expireTime key过期时间 好吗
     * @return
     */
    private boolean tryLock(String key, String value, long expireTime) {
        try {
            String result = (String) redisTemplate.execute((RedisCallback) connection -> {
                try {
                    String redisResult = null;
                    //获取连接
                    Object nativeConnection = connection.getNativeConnection();
                    //获取序列化工具 （不使用这个工具序列化 redistemplate会取不到值)
                    RedisSerializer keySerializer = redisTemplate.getKeySerializer();
                    //序列化
                    byte[] keyByte = keySerializer.serialize(key);
                    byte[] valueByte = keySerializer.serialize(value);
                    //单机模式
                    if (nativeConnection instanceof RedisAsyncCommands) {
                        RedisAsyncCommands commands = (RedisAsyncCommands) nativeConnection;
                        redisResult = commands.getStatefulConnection()
                                .sync()
                                .set(keyByte, valueByte, SetArgs.Builder.nx().ex(expireTime));
                    } else if (nativeConnection instanceof RedisAdvancedClusterAsyncCommands) {
                        //集群模式
                        RedisAdvancedClusterAsyncCommands clusterAsyncCommands = (RedisAdvancedClusterAsyncCommands) nativeConnection;
                        redisResult = clusterAsyncCommands.getStatefulConnection()
                                .sync()
                                .set(keyByte, valueByte, SetArgs.Builder.nx().ex(expireTime));
                    } else {
                        log.error("REDISLIBMISTCH");
                    }
                    return redisResult;
                } catch (Exception e) {
                    log.error("Failed to lock, closing connection");
                    connection.close();
                    return "";
                }

            });
            //如果成功设置锁 则将value逸出
            boolean eq = "OK".equals(result);
            if (eq) {
                lockValue.set(value);
            }
            return eq;
        } catch (Exception e) {
            log.error("设置锁异常");
            return false;
        }
    }
}

