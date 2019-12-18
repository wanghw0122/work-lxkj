package com.rcplatformhk.userpoolserver.task;


import com.rcplatformhk.userpoolserver.component.ChainRuleServer;
import com.rcplatformhk.userpoolserver.pojo.UserInfo;
import com.rcplatformhk.userpoolserver.sink.Sink;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.flink.shaded.curator.org.apache.curator.shaded.com.google.common.collect.Maps;

import java.util.Map;
import java.util.Objects;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    UserInfo userInfo;
    Long timeStamp;
    String createTime;
    String updateTime;
    Map<String,Object> context;
    ChainRuleServer chainRuleServer;
    Sink sinker;
    Integer sinkerId;
    public void sink(){
        if (Objects.isNull(sinker)){
            //Todo log not sinker
            return;
        }
        sinker.sink(this);
    }
    public void flow(){
        try {
            chainRuleServer.start(this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Task(UserInfo userInfo, ChainRuleServer chainRuleServer, Sink sink){
        this.userInfo = userInfo;
        this.createTime = userInfo.getCreateTime();
        this.updateTime = userInfo.getUpdateTime();
        this.context = Maps.newHashMap();
        this.chainRuleServer = chainRuleServer;
        this.sinker = sink;
        this.sinkerId = null;
    }
}
