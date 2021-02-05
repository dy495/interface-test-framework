package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing;

import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author wangmin
 * @date 2020/11/24 11:31 AM
 * @desc
 */
public enum AppletPushTargetEnum {
    ALL(0, "全部客户"),
    SHOP_CUSTOMER(1, "门店客户"),
    PERSONNEL_CUSTOMER(2, "个人客户"),
    ;

    private final String name;
    private final Integer id;

    AppletPushTargetEnum(int id, String name) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Integer getId() {
        return id;
    }

    public static AppletPushTargetEnum findById(Integer id) {
        Preconditions.checkArgument(id != null, "推送目标不存在");
        Optional<AppletPushTargetEnum> any = Arrays.stream(values()).filter(t -> t.ordinal() == id)
                .findAny();
        Preconditions.checkArgument(any.isPresent(), "推送目标不存在");
        return any.get();
    }

}
