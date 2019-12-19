package com.rcplatformhk.userpoolserver.sink;

import com.rcplatformhk.userpoolserver.common.MapToObjectException;
import com.rcplatformhk.userpoolserver.dao.mapper.RcQuickChatUserPoolMapper;
import com.rcplatformhk.userpoolserver.task.Task;
import com.rcplatformhk.userpoolserver.pojo.QuickChatUserInfo;
import com.rcplatformhk.userpoolserver.utils.Map2ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.util.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class MysqlSink implements Sink<Task> {

    @Autowired
    private RcQuickChatUserPoolMapper rcQuickChatUserPoolMapper;

    @Override
    public void sink(Task task) {
        QuickChatUserInfo userInfo = null;
        try {
            userInfo = getUserInfoFromTask(task);
            int l = rcQuickChatUserPoolMapper.insertQuickChatUserInfo(userInfo);
            if (l <= 0)
                log.error("Save to Mysql Error!");
        } catch (MapToObjectException e) {
            log.error(e.getMessage(), e);
        }catch (Exception e){
            log.error("Save to Mysql Error! object:{} errorMsg:{}", userInfo, e.getMessage(), e);
        }
    }

    private QuickChatUserInfo getUserInfoFromTask(Task task) throws Exception {
        Map<String, Object> stringObjectMap = Map2ObjectUtil.objectToMap(task.getUserInfo());
        Map<String, Object> contextMap = task.getContext();
        stringObjectMap.entrySet().stream()
                .filter(stringObjectEntry -> !contextMap.containsKey(stringObjectEntry.getKey()))
                .forEach(stringObjectEntry -> {
                    contextMap.put(stringObjectEntry.getKey(), stringObjectEntry.getValue());
                });
        return Map2ObjectUtil.mapToObject(contextMap, QuickChatUserInfo.class);
    }
}
