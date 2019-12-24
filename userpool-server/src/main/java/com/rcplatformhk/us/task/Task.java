package com.rcplatformhk.us.task;

import com.google.common.collect.Maps;
import com.rcplatformhk.pojo.UserInfo;
import com.rcplatformhk.us.component.ChainRuleServer;
import com.rcplatformhk.us.sink.Sink;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
