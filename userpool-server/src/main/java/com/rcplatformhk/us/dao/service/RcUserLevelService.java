package com.rcplatformhk.us.dao.service;

import com.rcplatformhk.us.annotation.DbType;
import com.rcplatformhk.us.dao.entity.UserLevelEntity;
import com.rcplatformhk.us.dao.mapper.RcUserLevelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@DbType(type = 1)
public class RcUserLevelService {
    @Autowired
    private RcUserLevelMapper rcUserLevelMapper;

    public List<UserLevelEntity> selectAll(){
        return rcUserLevelMapper.selectAll();
    }
}
