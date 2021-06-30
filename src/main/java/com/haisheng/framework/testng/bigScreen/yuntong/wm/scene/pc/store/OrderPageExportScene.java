package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.store;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 20.9. 商城订单导出 (华成裕)
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class OrderPageExportScene extends BaseScene {
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

    /**
     * 描述 导出类型 ALL：导出全部，CURRENT_PAGE：导出当前页，SPECIFIED_DATA：导出特定数据
     * 是否必填 true
     * 版本 v2.0
     */
    private final String exportType;

    /**
     * 描述 导出数据id列表，特定数据时必填
     * 是否必填 false
     * 版本 v2.0
     */
    private final JSONArray ids;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("bind_phone", bindPhone);
        object.put("commodity_name", commodityName);
        object.put("start_pay_time", startPayTime);
        object.put("end_pay_time", endPayTime);
        object.put("order_number", orderNumber);
        object.put("export_type", exportType);
        object.put("ids", ids);
        return object;
    }

    @Override
    public String getPath() {
        return "/yt/pc/store/order/page/export";
    }
}