package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.activity;

import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author : wangmin
 * @date :  2021/3/16  16:05
 */
public enum ActionPointEnum {
    /**
     * 预约试驾
     */
    APPOINTMENT_TEST_DRIVE(1, "预约试驾"),
    APPOINTMENT_MAINTAIN(2, "预约保养"),
    APPOINTMENT_REPAIR(3, "预约维修"),
    SECOND_HAND_CAR_CONSULTATION(4, "二手车咨询"),
    RENEWAL_CONSULTATION(5, "续保咨询");


    private Integer id;

    private String desc;

    ActionPointEnum(Integer id, String desc) {
        this.id = id;
        this.desc = desc;
    }

    public Integer getId() {
        return id;
    }

    public String getDesc() {
        return desc;
    }

    public static ActionPointEnum findById(Integer id) {
        Optional<ActionPointEnum> any = Arrays.stream(values()).filter(f -> f.id.equals(id))
                .findAny();
        Preconditions.checkArgument(any.isPresent(), "客户行动点不存在");
        return any.get();
    }
}
