package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.customer;

import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author wangmin
 * @date 2020/7/4 2:45 PM
 */
public enum CustomerLevelTypeEnum {
    /**
     * 正常
     */
    NORMAL(0, "普通等级", false, true, true, 0),
    /**
     * 战败
     */
    FAILURE(1, "战败等级", false, false, false, 0),
    /**
     * 公海
     */
    PUBLIC(2, "公海等级", false, false, false, 0),
    /**
     * 默认
     */
    DEFAULT(3, "默认等级", false, true, true, 0),
    /**
     * 订单客户
     */
    ORDER(4, "订单等级", true, false, true, 50),
    /**
     * 成交客户
     */
    DEAL(5, "成交等级", true, false, true, 100),
    ;

    private Integer id;
    private String description;

    private boolean isOrderLevel;

    private boolean dccLevel;

    private boolean isImportLevel;

    private int priority;

    CustomerLevelTypeEnum(Integer id, String description, boolean isOrderLevel, boolean dccLevel, boolean isImportLevel, int priority) {
        this.id = id;
        this.description = description;
        this.isOrderLevel = isOrderLevel;
        this.dccLevel = dccLevel;
        this.isImportLevel = isImportLevel;
        this.priority = priority;
    }

    public boolean isImportLevel() {
        return isImportLevel;
    }

    public String getDescription() {
        return description;
    }

    public Integer getId() {
        return id;
    }

    public boolean notDccLevel() {
        return !dccLevel;
    }

    public boolean isOrderLevel() {
        return isOrderLevel;
    }

    public static CustomerLevelTypeEnum findById(Integer id) {
        Optional<CustomerLevelTypeEnum> any = Arrays.stream(values()).filter(riskStatusEnum -> riskStatusEnum.id.equals(id)).findAny();
        Preconditions.checkArgument(any.isPresent(), "该id不存在");
        return any.get();
    }

    public static CustomerLevelTypeEnum findWithNullById(Integer id) {
        return Arrays.stream(values()).filter(e -> e.id.equals(id)).findAny().orElse(null);
    }


    public static boolean isCanChange(Integer originLevel, Integer targetLevel) {
        Preconditions.checkArgument(null != targetLevel, "目标等级不存在");
        if (originLevel == null) {
            return true;
        }
        CustomerLevelTypeEnum typeEnum = CustomerLevelTypeEnum.findById(originLevel);
        if (!typeEnum.isOrderLevel) {
            return true;
        }
        CustomerLevelTypeEnum targetType = CustomerLevelTypeEnum.findById(targetLevel);
        return targetType.priority > typeEnum.priority;
    }
}
