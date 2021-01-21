package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.common;

import com.google.common.base.Preconditions;

/**
 * 是or否机制
 *
 * @author liujiachun
 * @date  2020/5/21  13:04
 */
public enum YesOrNoEnum {
    /**
     * 否
     */
    NO("否"),
    /**
     * 是
     */
    YES("是"),
    ;

    private String name;

    YesOrNoEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


    public static YesOrNoEnum findById(Integer id) {
        Preconditions.checkArgument(id != null, "判断类型不存在");
        if (id == NO.ordinal()) {
            return NO;
        }
        if (id == YES.ordinal()) {
            return YES;
        }
        throw new IllegalArgumentException("判断类型不存在");
    }

    public static String findDescByBool(Boolean b) {
        if (b == null) {
            return null;
        }
        if (b) {
            return YES.getName();
        }
        return NO.getName();
    }

    public static YesOrNoEnum transform2Enum(Boolean b) {
        if (b == null) {
            return null;
        }
        if (b) {
            return YES;
        }
        return NO;
    }
}
