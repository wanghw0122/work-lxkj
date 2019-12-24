package com.rcplatformhk.us.dao.service;

import com.rcplatformhk.pojo.QuickChatUserInfo;
import com.rcplatformhk.us.annotation.DbType;
import com.rcplatformhk.us.dao.mapper.RcQuickChatUserPoolMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@DbType(type = 1)
public class RcQuickChatUserPoolService {

    @Autowired
    private RcQuickChatUserPoolMapper rcQuickChatUserPoolMapper;

    public int insertQuickChatUserInfo(QuickChatUserInfo userInfo){
        return rcQuickChatUserPoolMapper.insertQuickChatUserInfo(userInfo);
    }
}
