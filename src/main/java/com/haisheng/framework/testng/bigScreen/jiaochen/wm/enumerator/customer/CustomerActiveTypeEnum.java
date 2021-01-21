package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.customer;

import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.Optional;

/**
 * 客户身份
 * @author liujiachun
 * @date  2020/5/21  13:04
 */
public enum CustomerActiveTypeEnum {

    /**
     * 新客
     */
    NEW("新客"),
    /**
     * 老客
     */
    OLD("老客");

    private String name;

    CustomerActiveTypeEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }



    public static CustomerActiveTypeEnum findById(Integer id) {
        Preconditions.checkArgument(id != null, "客户类型不存在");
        Optional<CustomerActiveTypeEnum> any = Arrays.stream(values()).filter(e -> e.ordinal() == id)
                .findAny();
        Preconditions.checkArgument(any.isPresent(), "客户类型不存在");
        return any.get();
    }
}
