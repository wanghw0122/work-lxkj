package com.rcplatformhk.userpoolserver.sink;

import com.rcplatformhk.userpoolserver.dao.mapper.RcQuickChatUserPoolMapper;
import com.rcplatformhk.userpoolserver.task.Task;
import com.rcplatformhk.userpoolserver.pojo.QuickChatUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MysqlSink implements Sink<Task> {

    @Autowired
    private RcQuickChatUserPoolMapper rcQuickChatUserPoolMapper;

    @Override
    public void sink(Task task) {
        QuickChatUserInfo userInfo = getUserInfoFromTask(task);
        int l = rcQuickChatUserPoolMapper.insertQuickChatUserInfo(userInfo);
    }

    private QuickChatUserInfo getUserInfoFromTask(Task task){
        return null;
    }
}
