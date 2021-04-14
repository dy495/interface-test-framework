package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.Integral;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author wangmin
 * @date 2021/1/4 10:53 AM

 */
public enum CommodityStatusEnum {
    /**
     * 上架
     */
    UP("上架"),
    /**
     * 下架
     */
    DOWN("下架");

    private String desc;

    CommodityStatusEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public static CommodityStatusEnum findByType(String type) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(type), "商品状态不存在");
        Optional<CommodityStatusEnum> any = Arrays.stream(values()).filter(v -> v.name().equals(type)).findAny();
        Preconditions.checkArgument(any.isPresent(), "商品状态不存在");
        return any.get();
    }
}
