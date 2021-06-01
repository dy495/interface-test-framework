package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.integralcenter;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 37.21. 订单明细 (张小龙) v2.0
 *
 * @author wangmin
 * @date 2021-06-01 18:10:55
 */
@Data
public class OrderDetailBean implements Serializable {
    /**
     * 描述 总积分
     * 版本 v2.0
     */
    @JSONField(name = "integral_num")
    private Long integralNum;

    /**
     * 描述 商品明细
     * 版本 v2.0
     */
    @JSONField(name = "detailed_list")
    private JSONArray detailedList;

    /**
     * 描述 商品名称
     * 版本 v2.0
     */
    @JSONField(name = "commodity_name")
    private String commodityName;

    /**
     * 描述 商品积分
     * 版本 v2.0
     */
    @JSONField(name = "commodity_integral")
    private Long commodityIntegral;

    /**
     * 描述 商品数量
     * 版本 v2.0
     */
    @JSONField(name = "commodity_num")
    private Integer commodityNum;

}