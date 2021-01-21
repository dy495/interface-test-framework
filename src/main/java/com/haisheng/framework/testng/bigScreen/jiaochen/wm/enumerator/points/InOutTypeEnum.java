package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.points;

import com.google.common.base.Preconditions;

/**
 * @author wangmin
 * @date 2020/12/7  16:44
 */
public enum InOutTypeEnum {

    /**
     * 收入
     */
    INCOME(1, "收入"),
    OUTCOME(2, "支出");

    private int id;

    private String typeName;

    InOutTypeEnum(int id, String typeName) {
        this.id = id;
        this.typeName = typeName;
    }

    public int getId() {
        return id;
    }

    public String getTypeName() {
        return typeName;
    }


    public static InOutTypeEnum findById(Integer id) {
        Preconditions.checkArgument(id != null, "收支类型不存在");
        if (INCOME.id == id) {
            return INCOME;
        }
        if (OUTCOME.id == id) {
            return OUTCOME;
        }

        throw new IllegalArgumentException("收支类型不存在");
    }
}
