package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.activity;

import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.appointment.ActivityRegisterStatusEnum;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author : wangmin
 * @date :  2020/11/24  14:02
 */
public enum ActivityRegisterApprovalStatusEnum {
    /**
     * 待审批
     */
    PENDING(1, ActivityRegisterStatusEnum.APPROVAL_PENDING, "待审批"),
    CONFIRM(101, ActivityRegisterStatusEnum.APPROVAL_CONFIRM, "已通过"),
    REJECT(201, ActivityRegisterStatusEnum.APPROVAL_REJECT, "已拒绝"),
    CANCELED(401, ActivityRegisterStatusEnum.CANCELED, "已取消");

    private Integer id;

    private ActivityRegisterStatusEnum registerStatus;

    private String statusName;

    ActivityRegisterApprovalStatusEnum(Integer id, ActivityRegisterStatusEnum registerStatus, String statusName) {
        this.id = id;
        this.registerStatus = registerStatus;
        this.statusName = statusName;
    }

    public Integer getId() {
        return id;
    }

    public String getStatusName() {
        return statusName;
    }

    public ActivityRegisterStatusEnum getRegisterStatus() {
        return registerStatus;
    }

    public static ActivityRegisterApprovalStatusEnum findById(Integer id) {
        Optional<ActivityRegisterApprovalStatusEnum> any = Arrays.stream(values()).filter(t -> t.getId().equals(id)).findAny();
        Preconditions.checkArgument(any.isPresent(), "活动状态不存在");
        return any.get();
    }
}
