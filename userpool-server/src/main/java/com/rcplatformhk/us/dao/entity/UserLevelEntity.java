package com.rcplatformhk.us.dao.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLevelEntity {
    private Integer id;
    private Integer appId;
    private String name;
    private BigDecimal maxMoney;
    private BigDecimal minMoney;
    private Date createTime;
    private Integer platformType;
    private Integer fakeLevelId;
}
