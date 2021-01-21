package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing;

import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author wangmin
 * @date 2020/11/20 3:18 PM
 * @desc
 */
public enum ActivityChannelEnum {
    ONLINE(0, "线上"),
    OFFLINE(1, "线下"),
    ;
    private Integer id;

    private String name;

    ActivityChannelEnum(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static ActivityChannelEnum findById(Integer id) {
        Preconditions.checkArgument(null != id, "类型不能为空");
        Optional<ActivityChannelEnum> any = Arrays.stream(values()).filter(v -> v.getId().equals(id)).findAny();
        Preconditions.checkArgument(any.isPresent(), "类型不能为空");
        return any.get();
    }
}
