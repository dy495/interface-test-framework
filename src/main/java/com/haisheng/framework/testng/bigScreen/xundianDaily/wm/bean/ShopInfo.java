package com.haisheng.framework.testng.bigScreen.xundianDaily.wm.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ShopInfo implements Serializable {

    /**
     * 门店名称
     */
    private String shopName;

    /**
     * 门店数据
     */
    private List<RealTimeShopPvUvBean> realTimeShopPvUvBeanList;
}
