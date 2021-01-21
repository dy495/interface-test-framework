package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.activity;

import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author wangmin
 * @date  2020/12/23  16:26
 */
public enum ActivityStatusEnum {
    /**
     * 已撤销
     */
    REVOKE(0, "已撤销", true, false, true, false, false),
    PENDING(101, "待审核", false, true, false, false, false),
    PASSED(201, "进行中", false, false, true, true, true),
    CANCELED(301, "已取消", false, false, false, false, false),
    REJECT(401, "审核未通过", true, false, true, false, false),
    WAITING_START(501, "未开始", false, false, true, true, true),
    FINISH(601, "已结束", false, false, false, false, false);

    private Integer id;

    private String statusName;

    private boolean isCanDelete;

    private boolean isCanRevoke;

    private boolean isCanEdit;

    private boolean isCanCancel;

    private boolean isCanPromotion;

    ActivityStatusEnum(Integer id, String statusName, boolean isCanDelete, boolean isCanRevoke, boolean isCanEdit, boolean isCanCancel, boolean isCanPromotion) {
        this.id = id;
        this.statusName = statusName;
        this.isCanDelete = isCanDelete;
        this.isCanRevoke = isCanRevoke;
        this.isCanEdit = isCanEdit;
        this.isCanCancel = isCanCancel;
        this.isCanPromotion = isCanPromotion;
    }

    public Integer getId() {
        return id;
    }

    public String getStatusName() {
        return statusName;
    }

    public boolean isCanDelete() {
        return isCanDelete;
    }

    public boolean isCanRevoke() {
        return isCanRevoke;
    }

    public boolean isCanEdit() {
        return isCanEdit;
    }

    public boolean isCanCancel() {
        return isCanCancel;
    }

    public boolean isCanPromotion() {
        return isCanPromotion;
    }

    public static ActivityStatusEnum findById(Integer id) {
        Optional<ActivityStatusEnum> any = Arrays.stream(values()).filter(s -> s.getId().equals(id)).findAny();
        Preconditions.checkArgument(any.isPresent(), "活动审批状态不正确");
        return any.get();
    }
}
