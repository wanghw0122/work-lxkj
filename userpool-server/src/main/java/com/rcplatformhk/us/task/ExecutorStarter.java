package com.rcplatformhk.us.task;

import com.rcplatformhk.common.ConfigInitException;
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

import javax.annotation.PostConstruct;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.*;

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

    @PostConstruct
    private void init() {
        ExecutorService executorService = new ThreadPoolExecutor(size, size * 2,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());
        new Thread(() -> {
            while (true) {
                Optional<UserInfo> userInfo = redisDelayQueue.pop();
                userInfo.ifPresent(info -> executorService.submit(new Task(info, chainRuleServer, mysqlSink)));
            }
        }, "Delay_Queue_Thread")
                .start();

    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            log.info("rules init start...");
            chainRuleServer.init();
            log.info("rules init success...");
            for (int i = 0; i < size; i++) {
                String cacheName = "cache_queue_pop_thread_" + i;
                start_cache_queue_pop_thread(cacheName);
            }
            log.info("ExecutorStarter Thread start success!");
        } catch (ConfigInitException e) {
            log.error("ExecutorStarter ERROR: config init error! {}", e.getMessage(), e);
            log.error("ExecutorStarter ERROR: rules init failed!");
        } catch (Exception e) {
            log.error("ExecutorStarter ERROR: {}", e.getMessage(), e);
            log.error("ExecutorStarter THREAD START FAILED!");
        }
    }

    private void start_cache_queue_pop_thread(String name) throws Exception {
        new Thread(() -> {
            while (true) {
                UserInfo userInfo = redisCacheQueue.pop();
                if (Objects.isNull(userInfo))
                    continue;
                String userId = String.valueOf(userInfo.getId());
                if (!redisUserPool.checkAndPut(userId)) {
                    redisDelayQueue.put(userInfo);
                }
            }
        }, name).start();
    }

}
