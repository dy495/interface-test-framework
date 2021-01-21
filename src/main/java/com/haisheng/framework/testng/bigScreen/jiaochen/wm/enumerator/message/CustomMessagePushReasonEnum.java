package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.message;

import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author wangmin
 * @date 2020/11/24 14:23
 */
public enum CustomMessagePushReasonEnum {

    /**
     * 消息类型
     */
    CARD_VOLUME("卡券类"),
    ACTIVITY("活动类"),
    EVALUATE("评价类"),
    SETMEAL("套餐类"),
    SYSTEM("系统消息"),
    MAINTAIN("保养类");
    private String name;

    CustomMessagePushReasonEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static CustomMessagePushReasonEnum findByType(String type) {
        Optional<CustomMessagePushReasonEnum> any = Arrays.stream(values()).filter(e -> e.name().equals(type)).findAny();
        Preconditions.checkArgument(any.isPresent(), "消息类型不存在");
        return any.get();
    }

    public static CustomMessagePushReasonEnum findTemplateByType(String type) {
        Optional<CustomMessagePushReasonEnum> any = Arrays.stream(values()).filter(t -> t.name().equals(type)).findAny();
        Preconditions.checkArgument(any.isPresent(), "消息类型不存在");
        return any.get();
    }
}
