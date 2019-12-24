package com.rcplatformhk.us.dao.service;

import com.rcplatformhk.us.annotation.DbType;
import com.rcplatformhk.us.dao.mapper.RcVideoChatMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@DbType(type = 1)
public class RcVideoChatService {

    @Autowired
    private RcVideoChatMapper rcVideoChatMapper;

    public List<Map<String,Object>> checkPayStatusByHoursAndId(Integer id, String time){
        return rcVideoChatMapper.checkPayStatusByHoursAndId(id,time);
    }
}
