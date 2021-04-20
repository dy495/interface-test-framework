package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.message;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author zhangxiaolong
 * @date 2021/3/9 4:40 PM
 * @desc
 */
public enum MessageCustomerTypeEnum {
    /**
     * 小程序创建
     */
    WECHAT_CUSTOMER("小程序客户"),
    /**
     * 工单创建
     */
    AFTER_CUSTOMER("售后客户"),
    /**
     * 潜客创建
     */
    PRE_CUSTOMER("销售客户"),
    /**
     * 自定义客户
     */
    CUSTOM_CUSTOMER("自定义客户");

    private String desc;

    MessageCustomerTypeEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public static MessageCustomerTypeEnum findByType(String type) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(type), "客户类型不存在");
        Optional<MessageCustomerTypeEnum> any = Arrays.stream(values()).filter(s -> s.name().equals(type)).findAny();
        Preconditions.checkArgument(any.isPresent(), "客户类型不存在");
        return any.get();
    }
}

