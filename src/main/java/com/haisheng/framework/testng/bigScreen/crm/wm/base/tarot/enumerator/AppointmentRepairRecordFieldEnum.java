package com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.enumerator;

import com.google.common.base.Preconditions;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

public enum AppointmentRepairRecordFieldEnum implements MappingEnum {

    SHOP_NAME("shop_name", "归属门店"),

    BRAND_NAME("brand_name", "品牌");

    AppointmentRepairRecordFieldEnum(String rearField, String frontField) {
        this.frontField = frontField;
        this.rearField = rearField;
    }

    @Getter
    private final String rearField;
    @Getter
    private final String frontField;

    public static AppointmentRepairRecordFieldEnum findByRearField(String rearField) {
        Optional<AppointmentRepairRecordFieldEnum> any = Arrays.stream(values()).filter(f -> f.rearField.equals(rearField)).findAny();
        Preconditions.checkArgument(any.isPresent(), "映射字段不存在");
        return any.get();
    }
}
