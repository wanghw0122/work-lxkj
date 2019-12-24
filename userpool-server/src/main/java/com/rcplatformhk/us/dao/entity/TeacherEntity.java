package com.rcplatformhk.us.dao.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherEntity {
    private Integer id;
    private String name;
    private String address;
    private Date year;
}
