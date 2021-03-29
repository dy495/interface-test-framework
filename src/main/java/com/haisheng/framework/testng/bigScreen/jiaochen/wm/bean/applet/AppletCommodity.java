package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.applet;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * @author wangmin
 * @date 2021/2/23 19:56
 */
@Data
public class AppletCommodity implements Serializable {
    /**
     * 市场价
     */
    @JSONField(name = "price")
    private String price;

    /**
     * 积分兑换id
     */
    @JSONField(name = "id")
    private Integer id;

    /**
     * 原始积分价格
     */
    @JSONField(name = "original_integral_price")
    private Integer originalIntegralPrice;

    /**
     * 现在积分价格
     */
    @JSONField(name = "present_integral_price")
    private Integer presentIntegralPrice;

    /**
     * 月份
     */
    @JSONField(name = "commodity_name")
    private String commodityName;
}
