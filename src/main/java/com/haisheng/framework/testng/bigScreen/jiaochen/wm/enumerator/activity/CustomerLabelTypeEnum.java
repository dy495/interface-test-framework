package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.activity;

import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing.VipTypeEnum;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author : wangmin
 * @date :  2021/1/19  16:36
 */
public enum CustomerLabelTypeEnum {
    /**
     * 总人数
     */
    TOTAL(0, null, "总人数"),
    /**
     * vip会员
     */
    COMMON(1, VipTypeEnum.COMMON.getId(), "普通会员"),
    VIP(100, VipTypeEnum.VIP.getId(), "vip会员"),
    APPLET(1000, null, "小程序用户"),
    PRE_SALES(2000, null, "销售用户"),
    AFTER_SALES(3000, null, "售后用户");

    private Integer id;

    private Integer subTypeId;

    private String typeName;

    public String getTypeName() {
        return typeName;
    }

    public Integer getId() {
        return id;
    }

    public Integer getSubTypeId() {
        return subTypeId;
    }

    CustomerLabelTypeEnum(Integer id, Integer subTypeId, String typeName) {
        this.id = id;
        this.subTypeId = subTypeId;
        this.typeName = typeName;
    }

    public static CustomerLabelTypeEnum findById(Integer id) {
        Optional<CustomerLabelTypeEnum> any = Arrays.stream(values()).filter(t -> t.getId().equals(id))
                .findAny();
        Preconditions.checkArgument(any.isPresent(), "客户标签不存在");
        return any.get();
    }
}
