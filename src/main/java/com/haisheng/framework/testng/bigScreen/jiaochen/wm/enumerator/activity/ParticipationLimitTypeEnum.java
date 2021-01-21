package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.activity;

import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author wangmin
 * @date  2020/12/23  15:31
 */
public enum ParticipationLimitTypeEnum {

    /**
     * 全部可参加
     */
    ALL("全部可参加", false),
    /**
     * 部分可参加
     */
    PART_ALLOWED("部分可参加", true),
    /**
     * 部分不可参加
     */
    PART_NOT_ALLOWED("部分不可参加", true);


    private String typeName;

    private boolean isLimit;

    ParticipationLimitTypeEnum(String typeName, boolean isLimit) {
        this.typeName = typeName;
        this.isLimit = isLimit;
    }

    public String getTypeName() {
        return typeName;
    }

    public boolean isLimit() {
        return isLimit;
    }

    public static ParticipationLimitTypeEnum findById(Integer id) {
        Preconditions.checkArgument(id != null, "参与限制类型不存在");
        Optional<ParticipationLimitTypeEnum> any = Arrays.stream(values())
                .filter(t -> t.ordinal() == id).findAny();
        Preconditions.checkArgument(any.isPresent(), "参与限制类型不存在");
        return any.get();
    }
}
