package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.activity;

import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author wangmin
 * @date  2021/1/15  15:49
 */
public enum ActivityApprovalStatusEnum {
    /**
     * 已撤销
     */
    REVOKE(0, "已撤销", ActivityStatusEnum.REVOKE, false),
    PENDING(101, "待审核", ActivityStatusEnum.PENDING, true),
    PASSED(201, "审核通过", ActivityStatusEnum.PASSED, false),
    REJECT(301, "审核未通过", ActivityStatusEnum.REJECT, true);


    private Integer id;

    private String statusName;

    private ActivityStatusEnum activityStatus;

    private boolean isCanApproval;

    ActivityApprovalStatusEnum(Integer id, String statusName, ActivityStatusEnum activityStatus, boolean isCanApproval) {
        this.id = id;
        this.statusName = statusName;
        this.activityStatus = activityStatus;
        this.isCanApproval = isCanApproval;
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

    public boolean isCanApproval() {
        return isCanApproval;
    }

    public static ActivityApprovalStatusEnum findById(Integer id) {
        Optional<ActivityApprovalStatusEnum> any = Arrays.stream(values()).filter(s -> s.getId().equals(id)).findAny();
        Preconditions.checkArgument(any.isPresent(), "活动审核状态不存在");
        return any.get();
    }
}
