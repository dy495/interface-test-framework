package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.customermanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 6.20. 维修记录 (池) v1.0
 *
 * @author wangmin
 * @date 2021-03-31 12:32:56
 */
@Builder
public class AfterSaleCustomerRepairPageScene extends BaseScene {
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
     * 描述 车辆id
     * 是否必填 false
     * 版本 v1.0
     */
    private final Long carId;

    /**
     * 描述 门店id
     * 是否必填 false
     * 版本 v1.0
     */
    private final Long shopId;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("car_id", carId);
        object.put("shop_id", shopId);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/customer-manage/after-sale-customer/repair-page";
    }
}