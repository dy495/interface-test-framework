package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.activity;

import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author : wangmin
 * @date :  2020/11/24  14:02
 */
public enum ActivityRegisterStatusEnum {
    /**
     * 待审批
     */
    APPROVAL_PENDING(1, "待审批", "待审批", true, true, false),
    APPROVAL_CONFIRM(101, "已通过", "已通过", true, true, false),
    APPROVAL_REJECT(201, "已拒绝", "未通过", true, true, false),
    CANCELED(401, "已取消", "已取消", true, false, true),
    COMPLETE(501, "已结束", "已结束", false, false, true);

    private Integer id;

    private String statusName;

    private String appletStatusName;

    private Boolean isShow;

    private boolean isCancel;

    private boolean isCanDelete;

    ActivityRegisterStatusEnum(Integer id, String statusName, String appletStatusName, Boolean isShow, boolean isCancel, boolean isCanDelete) {
        this.id = id;
        this.statusName = statusName;
        this.appletStatusName = appletStatusName;
        this.isShow = isShow;
        this.isCancel = isCancel;
        this.isCanDelete = isCanDelete;
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

    public boolean isCancel() {
        return isCancel;
    }

    public boolean isCanDelete() {
        return isCanDelete;
    }

    public static ActivityRegisterStatusEnum findById(Integer id) {
        Optional<ActivityRegisterStatusEnum> any = Arrays.stream(values()).filter(t -> t.getId().equals(id)).findAny();
        Preconditions.checkArgument(any.isPresent(), "活动状态不存在");
        return any.get();
    }
}
