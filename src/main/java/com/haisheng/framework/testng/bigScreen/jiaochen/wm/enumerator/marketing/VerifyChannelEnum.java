package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing;

import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author wangmin
 * @date 2020/11/14 5:06 PM
 * @desc
 */
public enum VerifyChannelEnum {
    ACTIVE(0, "主动核销"),
    PASSIVE(1, "被动核销"),
    ;

    private Integer id;

    private String name;

    VerifyChannelEnum(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static VerifyChannelEnum findById(Integer id){
        Preconditions.checkArgument(null != id, "核销类型不存在");
        Optional<VerifyChannelEnum> any = Arrays.stream(values()).filter(v -> v.getId().equals(id)).findAny();
        Preconditions.checkArgument(any.isPresent(), "类型类型不存在");
        return any.get();
    }
}
