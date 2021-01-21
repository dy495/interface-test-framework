package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing;

import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author wangmin
 * @date 2020/11/16 11:07 AM
 * @desc 发出渠道
 */
public enum SendChannelEnum {
    AUTO_PUSH(0, "自动推送"),
    OFFLINE_PURCHASE(1, "线下售卖"),
    ACTIVITY_SEND(2, "活动发放"),
    ONLINE_PURCHASE(3, "线上售卖"),
    INTEGRAL_MALL(4, "积分商城"),
    ;

    private Integer id;

    private String name;

    SendChannelEnum(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static SendChannelEnum findById(Integer id) {
        Preconditions.checkArgument(null != id, "核销类型不存在");
        Optional<SendChannelEnum> any = Arrays.stream(values()).filter(v -> v.getId().equals(id)).findAny();
        Preconditions.checkArgument(any.isPresent(), "类型类型不存在");
        return any.get();
    }
}
