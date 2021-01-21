package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.wechat;

import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author wangmin
 * @date  2020/12/4  17:58
 */
public enum WaitTaskTypeEnum {
    /**
     * 任务类型
     */
    APPOINTMENT("预约"),
    RECEPTION("接待"),
    FOLLOW_UP("跟进"),
    VERIFICATION("核销");


    private String typeName;

    WaitTaskTypeEnum(String typeName) {
        this.typeName = typeName;
    }


    public String getTypeName() {
        return typeName;
    }

    public static WaitTaskTypeEnum findByType(String type) {
        Optional<WaitTaskTypeEnum> any = Arrays.stream(values()).filter(t -> t.name().equals(type))
                .findAny();
        Preconditions.checkArgument(any.isPresent(), "任务类型不存在");
        return any.get();
    }
}
