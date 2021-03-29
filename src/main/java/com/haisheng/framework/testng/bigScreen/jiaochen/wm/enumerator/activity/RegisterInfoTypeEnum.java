package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.activity;

import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author : wangmin
 * @date :  2021/1/21  17:14
 */
public enum RegisterInfoTypeEnum {


    /**
     * 姓名
     */
    NAME(1, "STRING", "姓名"),
    PHONE(2, "STRING", "手机号"),
    GENDER(3, "STRING", "性别"),
    AGE(4, "INTEGER", "年龄"),
    EMAIL(5, "STRING", "邮箱"),
    REGISTER_NUM(6, "INTEGER", "报名人数"),
    OTHER(9999, "STRING", "其他"),
    CUSTOM(10000, "OBJECT", "自定义");

    private Integer id;

    private String valueType;

    private String desc;

    RegisterInfoTypeEnum(Integer id, String valueType, String desc) {
        this.id = id;
        this.valueType = valueType;
        this.desc = desc;
    }

    public Integer getId() {
        return id;
    }

    public String getDesc() {
        return desc;
    }

    public String getValueType() {
        return valueType;
    }

    public static RegisterInfoTypeEnum findById(Integer id) {
        Optional<RegisterInfoTypeEnum> any = Arrays.stream(values()).filter(t -> t.getId().equals(id))
                .findAny();
        Preconditions.checkArgument(any.isPresent(), "报名信息类型不存在");
        return any.get();
    }

}
