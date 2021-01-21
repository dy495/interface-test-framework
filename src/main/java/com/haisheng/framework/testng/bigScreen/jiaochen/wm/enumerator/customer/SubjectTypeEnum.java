package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.customer;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wangmin
 * @date  2020/9/8  10:26
 */
public enum SubjectTypeEnum {
    /**
     *
     */
    PERSON("个人"),

    CORPORATION("公司");


    private final String name;

    SubjectTypeEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static SubjectTypeEnum findByName(String name) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(name), "客户类型不存在");
        Optional<SubjectTypeEnum> any = Arrays.stream(values()).filter(s -> s.getName().equals(name)).findAny();
        return any.orElse(null);
    }

    public static SubjectTypeEnum findByType(String name) {
        Optional<SubjectTypeEnum> any = Arrays.stream(values()).filter(s -> s.name().equals(name)).findAny();
        Preconditions.checkArgument(any.isPresent(), "客户类型不存在");
        return any.get();
    }

    public static List<Map<String, Object>> findNameList() {
        return Arrays.stream(values()).map(r -> {
            Map<String, Object> map = new HashMap<>(8);
            map.put("id", r.name());
            map.put("name", r.getName());
            return map;
        }).collect(Collectors.toList());
    }
}
