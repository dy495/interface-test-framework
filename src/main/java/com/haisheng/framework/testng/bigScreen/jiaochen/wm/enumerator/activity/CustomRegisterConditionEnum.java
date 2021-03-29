package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.activity;

import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author : wangmin
 * @date :  2021/3/16  16:50
 */
public enum CustomRegisterConditionEnum {
    /**
     * 多选
     */
    CHECK_BOX(1, "复选框"),
    /**
     * 填空
     */
    INPUT(2, "输入框");


    private Integer id;

    private String desc;

    public Integer getId() {
        return id;
    }

    public String getDesc() {
        return desc;
    }

    CustomRegisterConditionEnum(Integer id, String desc) {
        this.id = id;
        this.desc = desc;
    }

    public static CustomRegisterConditionEnum findById(Integer id) {
        Optional<CustomRegisterConditionEnum> any = Arrays.stream(values()).filter(c -> c.id.equals(id))
                .findAny();
        Preconditions.checkArgument(any.isPresent(), "自定义报名条件类型不存在");
        return any.get();
    }
}
