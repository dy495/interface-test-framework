package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.evaluate;

import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author wangmin
 * @date  2021/1/8  15:56
 */
public enum DefaultFavourableCycleEnum {
    /**
     * 3天
     */
    ONE(1, "3天"),
    /**
     * 5天
     */
    FIVE(5, "5天"),
    /**
     * 7天
     */
    SEVEN(7, "7天");


    private Integer days;

    private String cycleName;

    DefaultFavourableCycleEnum(Integer days, String cycleName) {
        this.days = days;
        this.cycleName = cycleName;
    }

    public Integer getDays() {
        return days;
    }

    public String getCycleName() {
        return cycleName;
    }

    public static DefaultFavourableCycleEnum findByDays(Integer days) {
        Optional<DefaultFavourableCycleEnum> any = Arrays.stream(values()).filter(c -> c.getDays().equals(days)).findAny();
        Preconditions.checkArgument(any.isPresent(), "默认好评周期不存在");
        return any.get();
    }
}
