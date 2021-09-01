package com.haisheng.framework.testng.bigScreen.itemMall.common.enumerator;

import com.google.common.base.Preconditions;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Optional;

public enum TimeTableEnum {
    A("09", "【09:00 ~ 09:59】"),
    B("10", "【10:00 ~ 10:59】"),
    C("11", "【11:00 ~ 11:59】"),
    D("12", "【12:00 ~ 12:59】"),
    E("13", "【13:00 ~ 13:59】"),
    F("14", "【14:00 ~ 14:59】"),
    G("15", "【15:00 ~ 15:59】"),
    H("16", "【16:00 ~ 16:59】"),
    I("17", "【17:00 ~ 17:59】"),
    J("18", "【18:00 ~ 18:59】"),
    K("19", "【19:00 ~ 19:59】"),
    L("20", "【20:00 ~ 20:59】"),
    M("21", "【21:00 ~ 21:59】"),
    N("22", "【22:00 ~ 22:59】"),
    ;

    TimeTableEnum(String hour, String section) {
        this.hour = hour;
        this.section = section;
    }

    @Getter
    private final String hour;
    @Getter
    private final String section;

    @NotNull
    public static TimeTableEnum findSectionByHour(String hour) {
        Preconditions.checkNotNull(hour, "时间段不可为空");
        Optional<TimeTableEnum> any = Arrays.stream(values()).filter(e -> e.getHour().equals(hour)).findAny();
        Preconditions.checkArgument(any.isPresent(), "时间段不存在");
        return any.get();
    }
}
