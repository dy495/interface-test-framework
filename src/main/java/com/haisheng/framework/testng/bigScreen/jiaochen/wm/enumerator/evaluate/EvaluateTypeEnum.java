package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.evaluate;

import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author wangmin
 * @date  2020/8/7  12:12
 */
public enum EvaluateTypeEnum {
    /**
     * 预约
     */
    APPOINTMENT(1, "预约评价"),
    BUY_NEW_CAR(2, "新车评价");


    private Integer id;

    private String typeName;

    EvaluateTypeEnum(Integer id, String typeName) {
        this.id = id;
        this.typeName = typeName;
    }

    public Integer getId() {
        return id;
    }

    public String getTypeName() {
        return typeName;
    }

    public static EvaluateTypeEnum findById(Integer id) {
        Optional<EvaluateTypeEnum> any = Arrays.stream(values()).filter(t -> t.id.equals(id)).findAny();
        Preconditions.checkArgument(any.isPresent(), "评价类型不存在");
        return any.get();
    }
}
