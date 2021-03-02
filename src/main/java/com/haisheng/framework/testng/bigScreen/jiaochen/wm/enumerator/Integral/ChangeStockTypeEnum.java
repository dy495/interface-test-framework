package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.Integral;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author wangmin
 * @date 2021/1/6 3:17 PM
 */
public enum ChangeStockTypeEnum {
    /**
     *
     */
    ADD("增加", "积分发放"),
    MINUS("减少", "积分消耗");

    private String desc;

    private String description;

    ChangeStockTypeEnum(String desc, String description) {
        this.desc = desc;
        this.description = description;
    }

    public String getDesc() {
        return desc;
    }

    public String getDescription() {
        return description;
    }

    public static ChangeStockTypeEnum findByType(String type) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(type), "库存改变状态不存在");
        Optional<ChangeStockTypeEnum> any = Arrays.stream(values()).filter(v -> v.name().equals(type)).findAny();
        Preconditions.checkArgument(any.isPresent(), "库存改变状态不存在");
        return any.get();
    }
}
