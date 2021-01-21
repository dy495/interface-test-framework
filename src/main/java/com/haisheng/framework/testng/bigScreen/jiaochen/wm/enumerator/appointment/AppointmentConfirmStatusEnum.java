package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.appointment;

import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author wangmin
 * @date  2020/11/23  16:48
 */
public enum AppointmentConfirmStatusEnum {
    /**
     * 确认通过
     */
    WAITING(0, "待确认", null, null),
    AGREE(10, "已确认", null, null),
    CANCEL(20, "已取消", MaintainStatusEnum.CANCELED, RepairStatusEnum.CANCELED);

    AppointmentConfirmStatusEnum(Integer id, String statusName, MaintainStatusEnum maintainStatusEnum, RepairStatusEnum repairStatusEnum) {
        this.id = id;
        this.statusName = statusName;
        this.maintainStatusEnum = maintainStatusEnum;
        this.repairStatusEnum = repairStatusEnum;
    }

    private Integer id;

    private String statusName;

    private MaintainStatusEnum maintainStatusEnum;

    private RepairStatusEnum repairStatusEnum;

    public MaintainStatusEnum getMaintainStatusEnum() {
        return maintainStatusEnum;
    }

    public RepairStatusEnum getRepairStatusEnum() {
        return repairStatusEnum;
    }

    public String getStatusName() {
        return statusName;
    }

    public Integer getId() {
        return id;
    }

    public static AppointmentConfirmStatusEnum findById(Integer id) {
        Optional<AppointmentConfirmStatusEnum> any = Arrays.stream(values()).filter(t -> t.getId().equals(id)).findAny();
        Preconditions.checkArgument(any.isPresent(), "确认状态不正确");
        return any.get();
    }
}
