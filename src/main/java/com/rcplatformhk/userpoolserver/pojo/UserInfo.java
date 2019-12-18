package com.rcplatformhk.userpoolserver.pojo;


import com.rcplatformhk.userpoolserver.annotation.FieldType;
import lombok.*;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class UserInfo {

    @FieldType(field = "id")
    private Integer id;
    @FieldType(field = "user_account")
    private String userAccount;
    @FieldType(field = "three_party_id")
    private String threePartyId;
    @FieldType(field = "three_party_email")
    private String threePartyEmail;
    @FieldType(field = "password")
    private String password;
    @FieldType(field = "user_name")
    private String userName;
    @FieldType(field = "app_id")
    private Integer appId;
    @FieldType(field = "background")
    private String background;
    @FieldType(field = "head_img")
    private String headImg;
    @FieldType(field = "gender")
    private Integer gender;
    @FieldType(field = "country_id")
    private Integer countryId;
    @FieldType(field = "country_name")
    private String countryName;
    @FieldType(field = "gold_num")
    private Float goldNum;
    @FieldType(field = "language_id")
    private String languageId;
    @FieldType(field = "language_name")
    private String languageName;
    @FieldType(field = "age")
    private Integer age;
    @FieldType(field = "birthday")
    private String birthday;
    @FieldType(field = "platform_type")
    private Integer platformType;
    @FieldType(field = "type")
    private Integer type;
    @FieldType(field = "pay_status")
    private Integer payStatus;
    @FieldType(field = "status")
    private Integer status;
    @FieldType(field = "create_time")
    private String createTime;
    @FieldType(field = "update_time")
    private String updateTime;
    @FieldType(field = "stone")
    private Float stone;
    @FieldType(field = "stone_version")
    private Integer stoneVersion;
}
