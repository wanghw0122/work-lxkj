package com.rcplatformhk.userpoolserver.dao.mapper;

import com.rcplatformhk.userpoolserver.dao.entity.TeacherEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface TeacherMapper {
    @Select("SELECT * FROM teacher")
    @Results({
            @Result(property = "id",  column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "address",column = "address"),
            @Result(property = "year",column = "year")
    })
    List<TeacherEntity> getAll();


    @Select({"SELECT * FROM teacher where id = #{id}"})
    @Results({
            @Result(property = "id",  column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "address",column = "address"),
            @Result(property = "year",column = "year")
    })
    List<Map<String, Object>> getByMap(Integer id);
}
