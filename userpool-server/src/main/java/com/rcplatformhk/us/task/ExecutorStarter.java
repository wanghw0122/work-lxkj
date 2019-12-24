package com.rcplatformhk.us.task;

import com.rcplatformhk.pojo.UserInfo;
import com.rcplatformhk.us.component.ChainRuleServer;
import com.rcplatformhk.us.service.impl.RedisCacheQueue;
import com.rcplatformhk.us.service.impl.RedisDelayQueue;
import com.rcplatformhk.us.service.impl.RedisUserPool;
import com.rcplatformhk.us.sink.Sink;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
@Slf4j
public class ExecutorStarter implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private RedisDelayQueue redisDelayQueue;
    @Autowired
    private RedisUserPool redisUserPool;
    @Autowired
    private RedisCacheQueue redisCacheQueue;
    @Autowired
    private Sink mysqlSink;
    @Autowired
    private ChainRuleServer chainRuleServer;

    @Value("${thread.size}")
    private int size;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            log.info("rules init start...");
            chainRuleServer.init();
            log.info("rules init complete...");
            for (int i = 0; i < size; i++) {
                String cacheName = "cache_queue_pop_thread_" + i;
                String queueName = "delay_queue_pop_thread" + i;
                start_cache_queue_pop_thread(cacheName);
                start_delay_queue_pop_thread(queueName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void start_cache_queue_pop_thread(String name) throws Exception {
        new Thread(() -> {
            do {
                UserInfo userInfo = redisCacheQueue.pop();
                if (Objects.isNull(userInfo))
                    continue;
                String userId = String.valueOf(userInfo.getId());
                if (!redisUserPool.checkAndPut(userId)) {
                    redisDelayQueue.put(userInfo);
                }
            } while (true);
        }, name).start();
    }

    private void start_delay_queue_pop_thread(String name) throws Exception {
        new Thread(() -> {
            while (true) {
                try {
                    Optional<UserInfo> userInfo = redisDelayQueue.pop();
                    userInfo.ifPresent(info -> new Task(info, chainRuleServer, mysqlSink).flow());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, name).start();
    }
}
