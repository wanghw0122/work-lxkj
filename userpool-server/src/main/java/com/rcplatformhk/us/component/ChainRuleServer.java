package com.rcplatformhk.us.component;

import com.rcplatformhk.pojo.UserInfo;
import com.rcplatformhk.us.dao.mapper.*;
import com.rcplatformhk.us.dao.service.*;
import com.rcplatformhk.us.rule.Rule;
import com.rcplatformhk.us.task.Task;
import com.rcplatformhk.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class ChainRuleServer {


    @Resource
    private RcUserService rcUserService;
    @Resource
    private RcVideoRecordOddService rcVideoRecordOddService;
    @Resource
    private RcVideoFriendService rcVideoFriendService;
    @Resource
    private RcUserRecordService rcUserRecordService;
    @Resource
    private RcVideoChatService rcVideoChatService;

    private static Rule root = Rule.root();

    private static int[] poolId = {1, 2, 3};

    public void init() throws Exception {
//        //新老用户规则
//        ConfigType.init();
//        //新老用户规则
//        ConfigDto newUserConfigDto = ConfigType.NEWUSER_IN_CONFIG.getConfigDto();
//        ConfigDto olderUserUnpaidConfigDto = ConfigType.OLDFREEUSER_IN_CONFIG.getConfigDto();
//        ConfigDto olderUserPaidConfigDto = ConfigType.OLDPAYUSER_IN_CONFIG.getConfigDto();

        //todo 更新配置信息
        Rule new_user_saver = Rule.builder().name("new_user_saver").save().behavior(task -> {
            task.setSinkerId(poolId[0]);
            return task;
        }).build();

        Rule old_user_unpaid_saver = Rule.builder().name("old_user_unpaid_saver").delay(3 * 60).save().behavior(task -> {
            task.setSinkerId(poolId[1]);
            return task;
        }).build();

        Rule old_user_paid_saver = Rule.builder().name("old_user_paid_saver").delay(3 * 60).save().behavior(task -> {
            task.setSinkerId(poolId[2]);
            return task;
        }).build();

        Rule new_or_old_user_test = Rule.builder().name("new_or_old_user_test").checker(o -> {
            long seconds = DateUtil.minus(o.getUpdateTime(), o.getCreateTime());
            return seconds < 24 * 60 * 60;
        }).build();

        Rule new_user_test_pay = Rule.builder().name("new_user_test_pay").delay(3 * 60).behavior(task -> {
            UserInfo userInfo = task.getUserInfo();
            List<Map<String, Object>> mapList = rcUserService.getPayStatusById(userInfo.getId());
            int payStatus = (Integer) mapList.stream().findAny().map(x -> x.get("payStatus")).orElse(-1);
            if (payStatus < 0) payStatus = userInfo.getPayStatus();
            userInfo.setPayStatus(payStatus);
            task.getContext().put("payStatus", payStatus);
            return task;
        }).checker(o -> {
            return 0 == o.getUserInfo().getPayStatus();//未付费
        }).build();

        Rule new_user_test_match_count = Rule.builder().name("new_user_test_match_count").behavior(task -> {
            UserInfo userInfo = task.getUserInfo();
            Map context = task.getContext();
            List<Map<String, Object>> mapList = rcVideoRecordOddService.getLiveChatStatisticsByUserIdAndRequestType(0, userInfo.getId());
            mapList.stream().filter(
                    stringObjectMap -> stringObjectMap.getOrDefault("id", -1)
                            .equals(userInfo.getId())
            ).findAny().ifPresent(context::putAll);
            return task;
        }).checker(o -> (Integer) o.getContext().getOrDefault("friendCount", -1) < 1).build();

        Rule new_user_test_friends = Rule.builder().name("new_user_test_friends").behavior(task -> {
            UserInfo userInfo = task.getUserInfo();
            Map context = task.getContext();
            List<Map<String, Object>> mapList = rcVideoFriendService.getFriendsCountByUserId(userInfo.getId());
            mapList.stream().filter(
                    stringObjectMap -> stringObjectMap.getOrDefault("id", -1)
                            .equals(userInfo.getId())
            ).findAny().ifPresent(context::putAll);
            return task;
        }).checker(o -> (Integer) o.getContext().getOrDefault("videoCount", -1) < 1).build();


        Rule old_user_active_days_test = Rule.builder().name("old_user_active_days_test").behavior(task -> {
            UserInfo userInfo = task.getUserInfo();
            Map context = task.getContext();
            List<Map<String, Object>> mapList = rcUserRecordService.getActiveDaysByIdAndTime(userInfo.getId(), DateUtil.getLastNDayStartTime(3));
            mapList.stream().filter(
                    stringObjectMap -> stringObjectMap.getOrDefault("id", -1)
                            .equals(userInfo.getId())
            ).findAny().ifPresent(context::putAll);
            return task;
        }).checker(o -> (Integer) o.getContext().getOrDefault("activeCount", -1) > 2).build();

        Rule old_user_check_pay = Rule.builder().name("old_user_check_pay").checker(o -> 0 == o.getUserInfo().getPayStatus()).build();

        Rule old_user_paid_check_pay_by_hours = Rule.builder().name("old_user_paid_check_pay_by_hours").behavior(task -> {
            UserInfo userInfo = task.getUserInfo();
            Map context = task.getContext();
            List<Map<String, Object>> mapList = rcVideoChatService.checkPayStatusByHoursAndId(userInfo.getId(), DateUtil.getLastNHoursStartTime(24));
            mapList.stream().filter(
                    stringObjectMap -> stringObjectMap.getOrDefault("id", -1)
                            .equals(userInfo.getId())
            ).findAny().ifPresent(context::putAll);
            return task;
        }).checker(o -> (Integer) o.getContext().getOrDefault("payCount", -1) == 0).build();

        root.getBuilder().bindCheckRule(new_or_old_user_test);

        new_or_old_user_test.getBuilder()
                .bindTrueCheckRule(new_user_test_pay)
                .bindTrueCheckRule(new_user_test_match_count)
                .bindTrueCheckRule(new_user_saver);

        new_user_test_match_count.getBuilder()
                .bindFalseCheckRule(new_user_test_friends)
                .bindTrueCheckRule(new_user_saver);

        new_or_old_user_test.getBuilder()
                .bindFalseCheckRule(old_user_active_days_test)
                .bindTrueCheckRule(old_user_check_pay)
                .bindTrueCheckRule(old_user_unpaid_saver);

        old_user_check_pay.getBuilder()
                .bindFalseCheckRule(old_user_paid_check_pay_by_hours)
                .bindTrueCheckRule(old_user_paid_saver);
        log.info("RULE STRUCTURE : {}", root);
        root.gc();
    }


    @SuppressWarnings("unchecked")
    public void start(Task task) throws Exception {
        root.flow(task);
    }
}
