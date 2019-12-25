package com.rcplatformhk.us.common;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.rcplatformhk.common.ConfigInitException;
import com.rcplatformhk.us.config.*;
import com.rcplatformhk.us.dao.entity.RcQuickChatConfigEntity;
import com.rcplatformhk.us.dao.mapper.RcQuickChatConfigMapper;
import com.rcplatformhk.us.dao.service.RcQuickChatConfigService;
import com.rcplatformhk.utils.SerializeUtils;
import com.rcplatformhk.utils.SpringContextUtil;
import com.rcplatformhk.utils.ValidationUtils;
import lombok.Getter;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

@Getter
public enum ConfigType {

    NEWUSER_IN_CONFIG("newUserInConfig", "新用户入池规则", NewUserInConfigDto.class),
//    NEWUSER_OUT_CONFIG("newUserOutConfig", "新用户出池规则", NewUserOutConfigDto.class),
    OLDFREEUSER_IN_CONFIG("oldFreeUserInConfig", "免费老用户入池规则", OldFreeUserInConfigDto.class),
//    OLDFREEUSER_OUT_CONFIG("oldFreeUserOutConfig", "免费老用户出池规则", OldFreeUserOutConfigDto.class),
    OLDPAYUSER_IN_CONFIG("oldPayUserInConfig", "付费老用户入池规则", OldPayUserInConfigDto.class);
//    OLDPAYUSER_OUT_CONFIG("oldPayUserOutConfig", "付费老用户出池规则", OldPayUserOutConfigDto.class);

    private String type;
    private String msg;
    private Class clazz;
    private ConfigDto configDto;
    private static RcQuickChatConfigService rcQuickChatConfigService;

    ConfigType(String type, String msg, Class clazz) {
        this.type = type;
        this.msg = msg;
        this.clazz = clazz;
    }

    public static void init() throws Exception {
        rcQuickChatConfigService = SpringContextUtil.getBean(RcQuickChatConfigService.class);
        for (ConfigType configType : ConfigType.values()) {
            configType.initialize(rcQuickChatConfigService);
        }
    }

    public void initialize(RcQuickChatConfigService rcQuickChatConfigService) throws Exception {
        List<RcQuickChatConfigEntity> rcQuickChatConfigEntities = rcQuickChatConfigService.getConfigByType(this.type);
        if (!CollectionUtils.isEmpty(rcQuickChatConfigEntities)) {
            RcQuickChatConfigEntity rcQuickChatConfigEntity = rcQuickChatConfigEntities.get(0);
            String configText = rcQuickChatConfigEntity.getConfigText();
            Optional<ConfigDto> optional = SerializeUtils.deserialize(configText, this.clazz, PropertyNamingStrategy.LOWER_CAMEL_CASE);
            if (!optional.isPresent())
                throw new ConfigInitException("Init Config Error! Type : " + this.msg);
            this.configDto = optional.get();
            ValidationUtils.ValidationResult validationResult = ValidationUtils.validateEntity(configDto);
            if (validationResult.isHasErrors()){
                throw new ConfigInitException("config param invalid! :" + validationResult.getErrorMsg());
            }
        } else throw new ConfigInitException("No Config Error! Type : " + this.msg);
    }
}
