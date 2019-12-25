package com.rcplatformhk.us.dao.service;

import com.rcplatformhk.us.annotation.DbType;
import com.rcplatformhk.us.dao.entity.RcQuickChatConfigEntity;
import com.rcplatformhk.us.dao.mapper.RcQuickChatConfigMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@DbType
public class RcQuickChatConfigService {
    @Autowired
    private RcQuickChatConfigMapper rcQuickChatConfigMapper;

    public List<RcQuickChatConfigEntity> getConfigByType(String type){
        return rcQuickChatConfigMapper.getConfigByType(type);
    }
}
