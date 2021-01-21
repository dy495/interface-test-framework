package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.customer;

import com.google.common.base.Preconditions;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wangmin
 * @date 2020/12/2 14:34
 */
public enum CustomerTypeEnum {



    /**
     * 潜在客户
     */
    POTENTIAL_CUSTOMER("潜在客户"),
    /**
     * 成交客户
     */
    SUCCESS_CUSTOMER("成交客户"),
    ;

    private String name;

    CustomerTypeEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


    public static CustomerTypeEnum findById(Integer id) {
        Preconditions.checkArgument(id != null, "客户来源不存在");
        Optional<CustomerTypeEnum> any = Arrays.stream(values()).filter(e -> e.ordinal() == id)
                .findAny();
        Preconditions.checkArgument(any.isPresent(), "客户来源不存在");
        return any.get();
    }



    public static List<Map<String, Object>> findList() {
        return Arrays.stream(values()).map(r -> {
            Map<String, Object> map = new HashMap<>(5);
            map.put("customer_source", r.ordinal());
            map.put("customer_source_name", r.getName());
            return map;
        }).collect(Collectors.toList());
    }
}
