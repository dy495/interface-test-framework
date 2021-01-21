package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.flow;

import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author wangmin
 * @date  2021/1/13  11:17
 */
public enum InformationFlowTypeEnum {
    /**
     *
     */
    ARTICLE(1, "文章"),
    ACTIVITY(2, "活动");

    private Integer id;

    private String typeName;

    InformationFlowTypeEnum(Integer id, String typeName) {
        this.id = id;
        this.typeName = typeName;
    }

    public Integer getId() {
        return id;
    }

    public String getTypeName() {
        return typeName;
    }


    public InformationFlowTypeEnum findById(Integer id) {
        Optional<InformationFlowTypeEnum> any = Arrays.stream(values()).filter(t -> t.id.equals(id)).findAny();
        Preconditions.checkArgument(any.isPresent(), "信息流类型不正确");
        return any.get();
    }
}
