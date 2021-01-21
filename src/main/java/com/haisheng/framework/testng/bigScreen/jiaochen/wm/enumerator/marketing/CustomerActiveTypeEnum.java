package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing;

import org.apache.commons.collections.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wangmin
 * @date 2020/11/12 20:27
 */
public enum CustomerActiveTypeEnum {

    /**
     * 高活
     */
    HIGH(1, "高活"),
    CENTER(2, "中活"),
    LOW(3, "低活"),
    ;

    private Integer code;

    private String value;

    CustomerActiveTypeEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Integer getCode() {
        return code;
    }

    public static String findValueByCode(Integer code) {
        List<CustomerActiveTypeEnum> collect = Arrays.stream(values()).filter(t -> t.code.equals(code)).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(collect)) {
            return collect.get(0).getValue();
        }
        return "";
    }

}
