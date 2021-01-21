package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.appointment;

import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.evaluate.EvaluateTypeEnum;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author wangmin
 * @date 2020/7/1 4:06 PM
 * @desc
 */
public enum AppointmentTypeEnum {
    /**
     * 试驾
     */
    TEST_DRIVE("试驾", null, "预约试驾", false),
    /**
     * 维修
     */
    REPAIR("维修", EvaluateTypeEnum.APPOINTMENT, "预约维修", true),
    /**
     * 保养
     */
    MAINTAIN("保养", EvaluateTypeEnum.APPOINTMENT, "预约保养", true),
    /**
     * 活动
     */
    ACTIVITY("活动", null, "活动预约", false);

    private String value;

    private EvaluateTypeEnum evaluateType;

    private String title;

    private boolean isAfterSales;

    AppointmentTypeEnum(String value, EvaluateTypeEnum evaluateType, String title, boolean isAfterSales) {
        this.value = value;
        this.evaluateType = evaluateType;
        this.title = title;
        this.isAfterSales = isAfterSales;
    }

    public String getValue() {
        return value;
    }

    public EvaluateTypeEnum getEvaluateType() {
        return evaluateType;
    }

    public String getTitle() {
        return title;
    }

    public boolean isAfterSales() {
        return isAfterSales;
    }

    public static AppointmentTypeEnum findByName(String name) {
        Optional<AppointmentTypeEnum> any = Arrays.stream(values()).filter(t -> t.name().equals(name))
                .findAny();
        Preconditions.checkArgument(any.isPresent(), "预约类型不存在");
        return any.get();
    }

    public static List<String> afterSalesTypes() {
        return Arrays.stream(values()).filter(t -> t.isAfterSales)
                .map(AppointmentTypeEnum::name).collect(Collectors.toList());
    }

}
