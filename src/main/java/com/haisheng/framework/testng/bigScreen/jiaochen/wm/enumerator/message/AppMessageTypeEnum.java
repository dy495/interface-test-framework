package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.message;

import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author : wangmin
 * @date :  2021/1/29  15:15
 */
public enum AppMessageTypeEnum {
    /**
     * 预约确认超时
     */
    APPOINTMENT_CONFIRM_OVERTIME(1, "预约确认超时提醒"),

    /**
     * 咨询回复超时提醒
     */
    CONSULT_REPLY_OVERTIME(2, "咨询回复超时提醒");

    private Integer id;

    private String typeName;

    AppMessageTypeEnum(Integer id, String typeName) {
        this.id = id;
        this.typeName = typeName;
    }

    public Integer getId() {
        return id;
    }

    public String getTypeName() {
        return typeName;
    }

    public static AppMessageTypeEnum findById(Integer id) {
        Optional<AppMessageTypeEnum> any = Arrays.stream(values()).filter(t -> t.getId().equals(id)).findAny();
        Preconditions.checkArgument(any.isPresent(), "app消息类型不正确");
        return any.get();
    }
}
