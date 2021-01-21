package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.customer;

import com.google.common.base.Preconditions;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 客户所属区域
 *
 * @author liujiachun
 * @date  2020/5/21  13:04
 */
public enum CustomerSourcesEnum {


    /**
     * 小程序
     */
    WECHART("小程序"),
    /**
     * 自然到店
     */
    NATURE_VISIT("自然到店"),
    ;

    private String name;

    CustomerSourcesEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


    public static CustomerSourcesEnum findById(Integer id) {
        Preconditions.checkArgument(id != null, "客户来源不存在");
        Optional<CustomerSourcesEnum> any = Arrays.stream(values()).filter(e -> e.ordinal() == id)
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
