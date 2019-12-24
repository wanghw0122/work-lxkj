package com.rcplatformhk.us.dao.service;

import com.rcplatformhk.us.annotation.DbType;
import com.rcplatformhk.us.dao.mapper.RcUserRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@DbType(type = 1)
public class RcUserRecordService {

    @Autowired
    private RcUserRecordMapper rcUserRecordMapper;

    public List<Map<String,Object>> getActiveDaysByIdAndTime(Integer id, String createTime){
        return rcUserRecordMapper.getActiveDaysByIdAndTime(id,createTime);
    }
}
