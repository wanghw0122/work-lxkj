package com.rcplatformhk.us.service;

import com.rcplatformhk.constant.AppIdConstant;
import com.rcplatformhk.pojo.UserInfo;
import com.rcplatformhk.us.dao.entity.UserLevelEntity;
import com.rcplatformhk.us.dao.service.RcUserLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ViplevelService {

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private RcUserLevelService rcUserLevelService;

    private static final String USER_PAY_RECORD = "user_pay_record";


    public Integer getUserLevelId(UserInfo userInfo) {
        String key = packageKey(USER_PAY_RECORD, userInfo.getId());
        String value = redisTemplate.opsForValue().get(key);
        BigDecimal money = new BigDecimal(value);
        return this.getUserLevelIdByMoney(money, userInfo.getAppId(), userInfo.getGender(), userInfo.getPlatformType());
    }

    private Integer getUserLevelIdByMoney(BigDecimal money, Integer appId, Integer gender, Integer platformType) {
        UserLevelEntity userLevel = getUserLevel(money, appId, gender, platformType);
        return Optional.ofNullable(userLevel).map(UserLevelEntity::getId).orElse(0);
    }

    private String packageKey(Object... objects) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(objects[0]);
        if (objects.length > 1) {
            for (int i = 1; i < objects.length; i++) {
                if (objects[i] != null && !objects[i].toString().equals("")) {
                    stringBuilder.append(":").append(objects[i]);
                }
            }
        }
        return stringBuilder.toString();
    }

    private UserLevelEntity getUserLevel(BigDecimal money, Integer appId, Integer gender, Integer platformType) {
        if (gender == 1) {
            List<UserLevelEntity> userLevels = rcUserLevelService.selectAll();
            List<UserLevelEntity> levels = userLevels.stream()
                    .filter(userLevel -> userLevel.getAppId().equals(AppIdConstant.getRealAppId(appId)) && userLevel
                            .getPlatformType().equals(platformType)).collect(Collectors.toList());
            for (UserLevelEntity userLevel : levels) {
                if (userLevel.getMaxMoney().compareTo(new BigDecimal(-1)) == 0
                        && money.compareTo(userLevel.getMinMoney()) >= 0) {
                    return userLevel;
                } else if (money.compareTo(userLevel.getMaxMoney()) <= 0
                        && money.compareTo(userLevel.getMinMoney()) >= 0) {
                    return userLevel;
                }
            }
        }
        return null;
    }

}
