package com.rcplatformhk.us.dao.service;

import com.rcplatformhk.us.annotation.DbType;
import com.rcplatformhk.us.dao.mapper.RcVideoFriendMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@DbType(type = 1)
public class RcVideoFriendService {

    @Autowired
    private RcVideoFriendMapper rcVideoFriendMapper;

    public List<Map<String,Object>> getFriendsCountByUserId(Integer id){
        return rcVideoFriendMapper.getFriendsCountByUserId(id);
    }
}
