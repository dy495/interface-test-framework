package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.activity;

import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author : wangmin
 * @date :  2020/12/23  15:31
 */
public enum ReceiveLimitTypeEnum {

    /**
     * 全部可参加
     */
    NO_LIMIT("不限制", false),
    PERIOD("总次数", true),
    PER_DAY("每日次数", true);


    private String typeName;

    private boolean isLimit;

    ReceiveLimitTypeEnum(String typeName, boolean isLimit) {
        this.typeName = typeName;
        this.isLimit = isLimit;
    }

    public String getTypeName() {
        return typeName;
    }

    public boolean isLimit() {
        return isLimit;
    }

    public static ReceiveLimitTypeEnum findById(Integer id) {
        Preconditions.checkArgument(id != null, "领取限制类型不存在");
        Optional<ReceiveLimitTypeEnum> any = Arrays.stream(values())
                .filter(t -> t.ordinal() == id).findAny();
        Preconditions.checkArgument(any.isPresent(), "参与限制类型不存在");
        return any.get();
    }
}
