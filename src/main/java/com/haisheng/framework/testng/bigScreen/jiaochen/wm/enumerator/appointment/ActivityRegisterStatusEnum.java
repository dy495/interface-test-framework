package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.appointment;

import com.google.common.base.Preconditions;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wangmin
 * @date  2020/11/24  14:02
 */
public enum ActivityRegisterStatusEnum {
    /**
     * 待审批
     */
    APPROVAL_PENDING(1, "待审批", "待审批", true),
    APPROVAL_CONFIRM(101, "已通过", "已通过", true),
    APPROVAL_REJECT(201, "已拒绝", "未通过", true),
    CANCELED(401, "已取消", "已取消", true),
    COMPLETE(501, "已结束", "已结束", false);

    private Integer id;

    private String statusName;

    private String appletStatusName;

    private Boolean isShow;




    ActivityRegisterStatusEnum(Integer id, String statusName, String appletStatusName, Boolean isShow) {
        this.id = id;
        this.statusName = statusName;
        this.appletStatusName = appletStatusName;
        this.isShow = isShow;
    }

    public static List<Map<String, Object>> findNameList() {
        return Arrays.stream(values())
                .filter(ActivityRegisterStatusEnum::getShow)
                .map(r -> {
                    Map<String, Object> map = new HashMap<>(8);
                    map.put("key", r.name());
                    map.put("value", r.getStatusName());
                    return map;
                }).collect(Collectors.toList());
    }

    public Integer getId() {
        return id;
    }

    public String getStatusName() {
        return statusName;
    }

    public String getAppletStatusName() {
        return appletStatusName;
    }

    public Boolean getShow() {
        return isShow;
    }

    public static ActivityRegisterStatusEnum findById(Integer id) {
        Optional<ActivityRegisterStatusEnum> any = Arrays.stream(values()).filter(t -> t.getId().equals(id)).findAny();
        Preconditions.checkArgument(any.isPresent(), "活动状态不存在");
        return any.get();
    }

    public static ActivityRegisterStatusEnum findByName(String name) {
        Optional<ActivityRegisterStatusEnum> any = Arrays.stream(values()).filter(t -> t.name().equals(name)).findAny();
        Preconditions.checkArgument(any.isPresent(), "活动状态不存在");
        return any.get();
    }
}
