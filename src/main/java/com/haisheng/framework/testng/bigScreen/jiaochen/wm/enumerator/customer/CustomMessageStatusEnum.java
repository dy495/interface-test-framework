package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.customer;

import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author wangmin
 * @date  2020/8/5  12:14
 */
public enum CustomMessageStatusEnum {
    /**
     * 排期中
     */
    SCHEDULING("排期中"),
    SUCCESS("发送成功");


    private final String statusName;

    CustomMessageStatusEnum(String statusName) {
        this.statusName = statusName;
    }

    public String getStatusName() {
        return statusName;
    }

    public static CustomMessageStatusEnum findByStatus(String status) {
        Optional<CustomMessageStatusEnum> any = Arrays.stream(values()).filter(s -> s.name().equals(status)).findAny();
        Preconditions.checkArgument(any.isPresent(), "消息状态不存在");
        return any.get();
    }
}
