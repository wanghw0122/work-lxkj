package com.rcplatformhk.userpoolserver.pojo;

import com.rcplatformhk.userpoolserver.annotation.Default;
import com.rcplatformhk.userpoolserver.annotation.FieldType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuickChatUserInfo {
    @FieldType(field = "id")
    @NotNull
    private Long userId;
    @FieldType(field = "poolId")
    @NotNull
    private Integer poolId;
    @NotNull
    @FieldType(field = "channel")
    @Default("0")
    private Integer channel;//不确定字段
    @FieldType(field = "vipLevel")
    private Integer vipLevel;// a,b,c
    @FieldType(field = "gender")
    @NotNull
    private Integer gender;
    @FieldType(field = "countryId")
    @NotNull
    private Integer countryId;
    @FieldType(field = "eroticismBehavior")
    @NotNull
    private Integer eroticismBehavior;//不确定字段
    @FieldType(field = "headImg")
    @NotNull
    @Default
    private String headImg;
    @FieldType(field = "userName")
    @NotNull
    @Default
    private String userName;
    @FieldType(field = "age")
    @NotNull
    private Integer age;
    @FieldType(field = "appId")
    @NotNull
    private Integer appId;
    @FieldType(field = "stoneVersion")
    @NotNull
    private Integer stoneVersion;
    @FieldType(field = "createTime")
    private Date createTime;
    @FieldType(field = "activeTime")
    private Date activeTime;
    @FieldType(field = "status")
    @NotNull
    private Integer status;
    @FieldType(field = "extend")
    private String extend;
}
