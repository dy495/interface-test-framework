package com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.tarot.enumerator;

import com.google.common.base.Preconditions;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Optional;

public enum EnumAppointmentRepairRecord implements EnumMapping<EnumAppointmentRepairRecord, String> {

    SHOP_NAME("shop_name", "归属门店"),

    BRAND_NAME("brand_name", "品牌");

    EnumAppointmentRepairRecord(String rearField, String frontField) {
        this.frontField = frontField;
        this.rearField = rearField;
    }

    @Getter
    private final String rearField;
    @Getter
    private final String frontField;

    @NotNull
    @Override
    public EnumAppointmentRepairRecord findByField(String rearField) {
        Optional<EnumAppointmentRepairRecord> any = Arrays.stream(values()).filter(f -> f.rearField.equals(rearField)).findAny();
        Preconditions.checkArgument(any.isPresent(), "映射字段不存在");
        return any.get();
    }
}
