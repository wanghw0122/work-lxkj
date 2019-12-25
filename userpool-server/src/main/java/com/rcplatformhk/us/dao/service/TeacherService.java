package com.rcplatformhk.us.dao.service;

import com.rcplatformhk.us.annotation.DbType;
import com.rcplatformhk.us.dao.entity.TeacherEntity;
import com.rcplatformhk.us.dao.mapper.TeacherMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

//@Service
//@DbType(type = 1)
public class TeacherService {

    @Autowired
    private TeacherMapper teacherMapper;

    public List<TeacherEntity> getAll(){
        return teacherMapper.getAll();
    }

    public List<Map<String, Object>> getByMap(Integer id){
        return teacherMapper.getByMap(id);
    }
}
