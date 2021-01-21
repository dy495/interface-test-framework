package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.customer;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author wangmin
 * @date 2020/12/2 16:27
 */
public enum CustomerCreateSourceEnum {

    /**
     * 小程序创建
     */
    WECHAT_CREATE("小程序客户"),
    /**
     * 工单创建
     */
    AFTER_CREATE("售后客户"),
    /**
     * 潜客创建
     */
    POTENTIAL_CREATE("潜客创建"),
    /**
     * 商城发卷创建
     */
    SEND_VOUCHER_CREATE("商城发卷创建");

    private String name;

    CustomerCreateSourceEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


    public static CustomerCreateSourceEnum findByName(String name) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(name), "客户来源不存在");
        Optional<CustomerCreateSourceEnum> any = Arrays.stream(values()).filter(s -> s.name().equals(name)).findAny();
        return any.orElse(null);
    }
}
