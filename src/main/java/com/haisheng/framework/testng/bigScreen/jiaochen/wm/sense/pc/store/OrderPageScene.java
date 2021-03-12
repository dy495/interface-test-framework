package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.store;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * /jiaochen/pc/store/order/page的接口
 *
 * @author wangmin
 * @date 2021-03-12 17:23:18
 */
@Builder
public class OrderPageScene extends BaseScene {
    /**
     * 描述 页码 大于0
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer page;

    /**
     * 描述 页大小 范围为[1,100]
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer size;

    /**
     * 描述 绑定手机号
     * 是否必填 false
     * 版本 v2.0
     */
    private final String bindPhone;

    /**
     * 描述 商品名称
     * 是否必填 false
     * 版本 v2.0
     */
    private final String commodityName;

    /**
     * 描述 支付时间
     * 是否必填 false
     * 版本 v2.0
     */
    private final String startPayTime;

    /**
     * 描述 支付时间
     * 是否必填 false
     * 版本 v2.0
     */
    private final String endPayTime;

    /**
     * 描述 订单号
     * 是否必填 false
     * 版本 v2.0
     */
    private final String orderNumber;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("bind_phone", bindPhone);
        object.put("commodity_name", commodityName);
        object.put("start_pay_time", startPayTime);
        object.put("end_pay_time", endPayTime);
        object.put("order_number", orderNumber);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/store/order/page";
    }
}