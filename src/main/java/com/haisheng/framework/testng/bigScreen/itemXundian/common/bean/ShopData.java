package com.haisheng.framework.testng.bigScreen.itemXundian.common.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class ShopData implements Serializable {

    /**
     * 门店名称
     */
    private String shopName;

    /**
     * 门店名称
     */
    private String shopId;

    /**
     * 实时pvuv数据
     */
    private RealTimeShopPvUvBean realTimeShopPvUvBean;

    /**
     * 实时过店pvuv数据
     */
    private RealTimeShopPassPvUvBean realTimeShopPassPvUvBean;
}
