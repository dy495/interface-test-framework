package com.haisheng.framework.testng.bigScreen.itemXundian.common.enumerator;

import com.google.common.base.Preconditions;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Optional;

public enum TimeTableEnum {
    A("09", "【08:00 ~ 08:59】"),
    B("10", "【09:00 ~ 09:59】"),
    C("11", "【10:00 ~ 10:59】"),
    D("12", "【11:00 ~ 11:59】"),
    E("13", "【12:00 ~ 12:59】"),
    F("14", "【13:00 ~ 13:59】"),
    G("15", "【14:00 ~ 14:59】"),
    H("16", "【15:00 ~ 15:59】"),
    I("17", "【16:00 ~ 16:59】"),
    J("18", "【17:00 ~ 17:59】"),
    K("19", "【18:00 ~ 18:59】"),
    L("20", "【19:00 ~ 19:59】"),
    M("21", "【20:00 ~ 20:59】"),
    N("22", "【21:00 ~ 21:59】"),
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
