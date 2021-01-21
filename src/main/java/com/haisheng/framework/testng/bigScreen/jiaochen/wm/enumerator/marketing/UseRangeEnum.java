package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wangmin
 * @date 2020/11/11 4:24 PM
 * @desc 卡券和套餐使用范围枚举
 */
public enum UseRangeEnum {
    /**
     * 集团
     */
    CURRENT(0, "集团", "集团管理"),
    BRAND(1, "品牌", "品牌管理"),
    REGION(2, "区域", "区域管理"),
    STORE(3, "门店", "门店管理"),
    ;

    private final Integer id;

    private final String name;

    private final String desc;

    UseRangeEnum(Integer id, String name, String desc) {
        this.id = id;
        this.name = name;
        this.desc = desc;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public static UseRangeEnum findByType(String type) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(type), "主体类型不存在");
        Optional<UseRangeEnum> any = Arrays.stream(values()).filter(v -> v.name().equals(type)).findAny();
        Preconditions.checkArgument(any.isPresent(), "主体类型不存在");
        return any.get();
    }

    public static List<Map<String, Object>> findList() {
        return Arrays.stream(values()).map(v -> {
            Map<String, Object> map = new HashMap<>(16);
            map.put("key", v.name());
            map.put("value", v.getName());
            return map;
        }).collect(Collectors.toList());
    }
}
