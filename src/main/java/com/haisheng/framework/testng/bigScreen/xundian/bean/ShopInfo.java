package com.haisheng.framework.testng.bigScreen.xundian.bean;

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

    /**
     * 获取pv数据总和
     *
     * @return int
     */
    public Integer getPvSum() {
        return realTimeShopPvUvBeanList.stream().mapToInt(e -> e.getYesterdayPv() == null ? 0 : e.getYesterdayPv()).sum();
    }

    /**
     * 获取uv数据总和
     *
     * @return int
     */
    public Integer getUvSum() {
        return realTimeShopPvUvBeanList.stream().mapToInt(e -> e.getYesterdayUv() == null ? 0 : e.getYesterdayPv()).sum();
    }
}
