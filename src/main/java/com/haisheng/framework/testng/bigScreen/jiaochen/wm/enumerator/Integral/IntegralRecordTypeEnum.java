package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.Integral;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author wangmin
 * @date 2020/12/15 5:18 PM
 * @desc 积分记录类型
 */
public enum IntegralRecordTypeEnum {
    /**
     * 全部
     */
    ALL("全部", null),

    /**
     * 获得
     */
    GAIN("获得", ChangeStockTypeEnum.ADD),

    /**
     * 消耗
     */
    CONSUME("消耗", ChangeStockTypeEnum.MINUS);

    private String desc;

    private ChangeStockTypeEnum stockType;

    IntegralRecordTypeEnum(String desc, ChangeStockTypeEnum stockType) {
        this.desc = desc;
        this.stockType = stockType;
    }

    public String getDesc() {
        return desc;
    }

    public ChangeStockTypeEnum getStockType() {
        return stockType;
    }

    public static IntegralRecordTypeEnum findByType(String type) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(type), "积分记录类型不存在");
        Optional<IntegralRecordTypeEnum> any = Arrays.stream(values()).filter(v -> v.name().equals(type)).findAny();
        Preconditions.checkArgument(any.isPresent(), "积分记录类型不存在");
        return any.get();
    }
}
