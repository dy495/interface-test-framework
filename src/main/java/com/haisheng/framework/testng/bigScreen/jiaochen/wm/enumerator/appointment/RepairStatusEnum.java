package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.appointment;

import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author wangmin
 * @date  2020/11/24  14:02
 */
public enum RepairStatusEnum {
    /**
     * 待保养
     */
    WAITING(1, "待维修", true, false),
    CANCELED(101, "已取消", false, true),
    EXPIRED(201, "已过期", false, true),
    TO_BE_EVALUATED(301, "待评价", true, false),
    EVALUATED(401, "已完成", true, true);

    private Integer id;

    private String statusName;

    private Boolean isWaitConfirmStatus;

    private Boolean isEnd;

    RepairStatusEnum(Integer id, String statusName, Boolean isWaitConfirmStatus, Boolean isEnd) {
        this.id = id;
        this.statusName = statusName;
        this.isWaitConfirmStatus = isWaitConfirmStatus;
        this.isEnd = isEnd;
    }

    public Integer getId() {
        return id;
    }

    public String getStatusName() {
        return statusName;
    }

    public Boolean getIsEnd() {
        return isEnd;
    }


    public static RepairStatusEnum findById(Integer id) {
        Optional<RepairStatusEnum> any = Arrays.stream(values()).filter(t -> t.getId().equals(id)).findAny();
        Preconditions.checkArgument(any.isPresent(), "保养状态不存在");
        return any.get();
    }

    public static List<Integer> waitAuditStatus() {
        return Arrays.stream(values()).filter(s -> s.isWaitConfirmStatus)
                .map(RepairStatusEnum::getId).collect(Collectors.toList());
    }
}
