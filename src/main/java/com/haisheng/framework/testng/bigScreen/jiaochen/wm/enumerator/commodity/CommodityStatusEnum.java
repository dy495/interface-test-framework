package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.commodity;

import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author wangmin
 * @date 2021/1/11 15:57
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



    private String name;

    CommodityStatusEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }



    public static CommodityStatusEnum findByType(String type) {
        Optional<CommodityStatusEnum> any = Arrays.stream(values()).filter(t -> t.name().equals(type))
                .findAny();
        Preconditions.checkArgument(any.isPresent(), "商品状态不存在");
        return any.get();
    }
}
