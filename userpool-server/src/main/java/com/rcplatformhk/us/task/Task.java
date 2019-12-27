package com.rcplatformhk.us.task;

import com.google.common.collect.Maps;
import com.rcplatformhk.pojo.UserInfo;
import com.rcplatformhk.us.component.ChainRuleServer;
import com.rcplatformhk.us.sink.Sink;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Objects;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
@ToString
public class Task implements Runnable {
    UserInfo userInfo;
    Long timeStamp;
    String createTime;
    String updateTime;
    Map<String, Object> context;
    ChainRuleServer chainRuleServer;
    Sink sinker;
    Integer poolId;

    public void sink() {
        if (Objects.isNull(sinker)) {
            return;
        }
        sinker.sink(this);
    }

    private void flow() throws Exception {
        chainRuleServer.start(this);
    }

    public Task(UserInfo userInfo, ChainRuleServer chainRuleServer, Sink sink) {
        this.userInfo = userInfo;
        this.createTime = userInfo.getCreateTime();
        this.updateTime = userInfo.getUpdateTime();
        this.context = Maps.newHashMap();
        this.chainRuleServer = chainRuleServer;
        this.sinker = sink;
        this.poolId = null;
    }

    @Override
    public void run() {
        try {
            this.flow();
        } catch (Exception e) {
            log.error("Flow Task {} Exception {}", this, e.getMessage(), e);
        }
    }
}
