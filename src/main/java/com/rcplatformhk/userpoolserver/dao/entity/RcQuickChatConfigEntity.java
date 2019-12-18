package com.rcplatformhk.userpoolserver.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RcQuickChatConfigEntity {
    private Long id;
    private String type;
    private String configText;
    private Date createTime;
    private Date updateTime;
    private int status;
}
