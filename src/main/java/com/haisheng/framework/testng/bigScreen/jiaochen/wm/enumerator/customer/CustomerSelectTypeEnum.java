package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.customer;

import com.google.common.base.Preconditions;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 客户到店类型
 *
 * @author liujiachun
 * @date  2020/5/21  13:04
 */
public enum CustomerSelectTypeEnum {

    /**
     * 老客户重购
     */
    RE_BUY("老客户重购", "#005EC6", true),
    /**
     * 自然到访
     */
    COM_VISIT("自然到访", "#E9AA3C", true),
    /**
     * 亲友推荐
     */
    KIN_RECOMMEND("亲友推荐", "#97CB5D", true),
    /**
     * 线上垂媒
     */
    ONLINE_RECOMMEND("线上垂媒", "#E86487", true),

    /**
     * 小程序
     */
    WE_CHAT("小程序", "818BA8", true),

    ;


    private String name;

    private String color;

    private boolean isShow;

    CustomerSelectTypeEnum(String name, String color, boolean isShow) {
        this.name = name;
        this.color = color;
        this.isShow = isShow;
    }

    public String getColor() {
        return color;
    }

    public String getName() {
        return name;
    }


    public static CustomerSelectTypeEnum findById(Integer id) {
        Preconditions.checkArgument(id != null, "客户渠道不存在");
        Optional<CustomerSelectTypeEnum> any = Arrays.stream(values()).filter(e -> e.ordinal() == id)
                .findAny();
        Preconditions.checkArgument(any.isPresent(), "客户渠道不存在");
        return any.get();
    }

    public static CustomerSelectTypeEnum findByName(String name) {
        Optional<CustomerSelectTypeEnum> any = Arrays.stream(values()).filter(e -> e.getName().equals(name))
                .findAny();
        if (any.isPresent()) {
            return any.get();
        }
        return null;
    }

    public static List<Map<String, Object>> findList() {
        return Arrays.stream(values()).filter(a -> a.isShow).map(r -> {
            Map<String, Object> map = new HashMap<>(7);
            map.put("customer_select_type", r.ordinal());
            map.put("customer_select_type_name", r.getName());
            return map;
        }).collect(Collectors.toList());
    }

    public static boolean isCanChange(Integer sourceId){
        if(null == sourceId){
            return true;
        }
        if(CustomerSelectTypeEnum.WE_CHAT.ordinal() == sourceId){
            return false;
        }
        return true;
    }
}
