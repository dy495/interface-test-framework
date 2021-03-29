package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.activity;

import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author : wangmin
 * @date :  2021/1/15  15:49
 */
public enum ActivityApprovalStatusEnum {
    /**
     * 已撤销
     */
    REVOKE(0, "已撤销", ActivityStatusEnum.REVOKE),
    PENDING(101, "待审核", ActivityStatusEnum.PENDING),
    PASSED(201, "审核通过", ActivityStatusEnum.PASSED),
    REJECT(301, "审核未通过", ActivityStatusEnum.REJECT);


    private Integer id;

    private String statusName;

    private ActivityStatusEnum activityStatus;


    ActivityApprovalStatusEnum(Integer id, String statusName, ActivityStatusEnum activityStatus) {
        this.id = id;
        this.statusName = statusName;
        this.activityStatus = activityStatus;
    }

    public Integer getId() {
        return id;
    }

    public String getStatusName() {
        return statusName;
    }

    public ActivityStatusEnum getActivityStatus() {
        return activityStatus;
    }

    public static ActivityApprovalStatusEnum findById(Integer id) {
        Optional<ActivityApprovalStatusEnum> any = Arrays.stream(values()).filter(s -> s.getId().equals(id)).findAny();
        Preconditions.checkArgument(any.isPresent(), "活动审核状态不存在");
        return any.get();
    }
}
