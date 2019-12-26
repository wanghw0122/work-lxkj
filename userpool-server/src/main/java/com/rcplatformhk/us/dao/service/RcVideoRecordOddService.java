package com.rcplatformhk.us.dao.service;

import com.rcplatformhk.us.annotation.DbType;
import com.rcplatformhk.us.dao.mapper.RcVideoRecordEvenMapper;
import com.rcplatformhk.us.dao.mapper.RcVideoRecordOddMapper;
import com.rcplatformhk.utils.DateUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@DbType(type = 2)
public class RcVideoRecordOddService {

    @Autowired
    private RcVideoRecordOddMapper rcVideoRecordOddMapper;

    @Autowired
    private RcVideoRecordEvenMapper rcVideoRecordEvenMapper;

    public List<Map<String,Object>> getLiveChatStatisticsByUserIdAndRequestType(Integer requestType, Integer id){
        if (!DateUtil.ifOddDayToday()){
            return rcVideoRecordEvenMapper.getLiveChatStatisticsByUserIdAndRequestType(requestType,id);
        }
        return rcVideoRecordOddMapper.getLiveChatStatisticsByUserIdAndRequestType(requestType,id);
    }


    public List<Map<String,Object>> getLiveChatStatisticsByUserId(Integer id){
        return rcVideoRecordOddMapper.getLiveChatStatisticsByUserId(id);
    }
}
