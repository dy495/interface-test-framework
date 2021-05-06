package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.reception.after;

import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author wangmin
 * @date 2020/12/5  15:54
 */
public enum ReceptionStatusEnum {
    /**
     * 接待中
     */
    IN_RECEPTION(1000, "接待中", false),
    CANCEL(2000, "已取消", true),
    FINISH(4000, "完成", true);


    private final Integer id;

    private final String statusName;

    private final boolean isEndStatus;

    ReceptionStatusEnum(Integer id, String statusName, boolean isEndStatus) {
        this.id = id;
        this.statusName = statusName;
        this.isEndStatus = isEndStatus;
    }

    public String getStatusName() {
        return statusName;
    }

    public Integer getId() {
        return id;
    }

    public boolean isEndStatus() {
        return isEndStatus;
    }

    public static ReceptionStatusEnum findById(Integer id) {
        Optional<ReceptionStatusEnum> any = Arrays.stream(values()).filter(s -> s.id.equals(id))
                .findAny();
        Preconditions.checkArgument(any.isPresent(), "接待状态不存在");
        return any.get();
    }
}
