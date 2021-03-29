package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.activity;

import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.activity.AbstractActivity;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.activity.PassedActivity;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.generate.activity.PendingActivity;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author : wangmin
 * @date :  2020/12/23  16:26
 */
public enum ActivityStatusEnum {
    /**
     * 已撤销
     */
    REVOKE(0, "已撤销", true, false, true, false, false, null),
    PENDING(101, "待审核", false, true, false, false, false, new PendingActivity.Builder()),
    PASSED(201, "进行中", false, false, true, true, true, new PassedActivity.Builder()),
    CANCELED(301, "已取消", false, false, false, false, false, null),
    REJECT(401, "审核未通过", true, false, true, false, false, null),
    WAITING_START(501, "未开始", false, false, true, true, true, null),
    FINISH(601, "已结束", false, false, false, false, false, null);

    @Getter
    private final Integer id;
    @Getter
    private final String statusName;
    @Getter
    private final boolean isCanDelete;
    @Getter
    private final boolean isCanRevoke;
    @Getter
    private final boolean isCanEdit;
    @Getter
    private final boolean isCanCancel;
    @Getter
    private final boolean isCanPromotion;
    @Getter
    public final AbstractActivity.BaseBuilder activityBuilder;

    ActivityStatusEnum(Integer id, String statusName, boolean isCanDelete, boolean isCanRevoke, boolean isCanEdit, boolean isCanCancel, boolean isCanPromotion, AbstractActivity.BaseBuilder activityBuilder) {
        this.id = id;
        this.statusName = statusName;
        this.isCanDelete = isCanDelete;
        this.isCanRevoke = isCanRevoke;
        this.isCanEdit = isCanEdit;
        this.isCanCancel = isCanCancel;
        this.isCanPromotion = isCanPromotion;
        this.activityBuilder = activityBuilder;
    }

    public static ActivityStatusEnum findById(Integer id) {
        Optional<ActivityStatusEnum> any = Arrays.stream(values()).filter(s -> s.getId().equals(id)).findAny();
        Preconditions.checkArgument(any.isPresent(), "活动审批状态不正确");
        return any.get();
    }
}
