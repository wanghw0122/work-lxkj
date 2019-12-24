package com.rcplatformhk.us.dao.service;

import com.rcplatformhk.us.annotation.DbType;
import com.rcplatformhk.us.dao.mapper.RcUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@DbType(type = 1)
public class RcUserService {

    @Autowired
    private RcUserMapper rcUserMapper;

    public List<Map<String,Object>> getPayStatusById(Integer id){
        return rcUserMapper.getPayStatusById(id);
    }
}
