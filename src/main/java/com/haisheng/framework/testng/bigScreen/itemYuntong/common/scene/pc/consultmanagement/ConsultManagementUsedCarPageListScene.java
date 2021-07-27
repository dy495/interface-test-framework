package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.consultmanagement;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 13.8. 二手车咨询列表 （池）(2021-03-12)
 *
 * @author wangmin
 * @date 2021-07-27 18:26:28
 */
@Builder
public class ConsultManagementUsedCarPageListScene extends BaseScene {
    /**
     * 描述 归属门店
     * 是否必填 false
     * 版本 v3.0
     */
    private final Long shopId;

    /**
     * 描述 跟进开始时间
     * 是否必填 false
     * 版本 v3.0
     */
    private final String followDateStart;

    /**
     * 描述 跟进结束时间
     * 是否必填 false
     * 版本 v3.0
     */
    private final String followDateEnd;

    /**
     * 描述 跟进账号
     * 是否必填 false
     * 版本 v3.0
     */
    private final String followLoginName;

    /**
     * 描述 商品名称
     * 是否必填 false
     * 版本 v3.0
     */
    private final String commodityName;

    /**
     * 描述 车型
     * 是否必填 false
     * 版本 v3.0
     */
    private final Long carModelId;

    /**
     * 描述 跟进人员
     * 是否必填 false
     * 版本 v3.0
     */
    private final String followSalesName;

    /**
     * 描述 咨询时间
     * 是否必填 false
     * 版本 v3.0
     */
    private final String consultDateStart;

    /**
     * 描述 咨询时间
     * 是否必填 false
     * 版本 v3.0
     */
    private final String consultDateEnd;

    /**
     * 描述 联系人
     * 是否必填 false
     * 版本 v3.0
     */
    private final String customerName;

    /**
     * 描述 联系电话
     * 是否必填 false
     * 版本 v3.0
     */
    private final String customerPhone;

    /**
     * 描述 是否超时
     * 是否必填 false
     * 版本 v3.0
     */
    private final Boolean isOverTime;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("shop_id", shopId);
        object.put("follow_date_start", followDateStart);
        object.put("follow_date_end", followDateEnd);
        object.put("follow_login_name", followLoginName);
        object.put("commodity_name", commodityName);
        object.put("car_model_id", carModelId);
        object.put("follow_sales_name", followSalesName);
        object.put("consult_date_start", consultDateStart);
        object.put("consult_date_end", consultDateEnd);
        object.put("customer_name", customerName);
        object.put("customer_phone", customerPhone);
        object.put("is_over_time", isOverTime);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/consult-management/used-car-page-list";
    }
}