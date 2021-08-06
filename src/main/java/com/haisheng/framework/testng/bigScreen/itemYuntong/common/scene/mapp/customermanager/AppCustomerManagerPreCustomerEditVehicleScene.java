package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.customermanager;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 1.6. 客户车辆编辑（杨）v4.0 （2021-05-18）
 *
 * @author wangmin
 * @date 2021-08-06 16:38:23
 */
@Builder
public class AppCustomerManagerPreCustomerEditVehicleScene extends BaseScene {
    /**
     * 描述 车辆id
     * 是否必填 true
     * 版本 v4.0
     */
    private final Long id;

    /**
     * 描述 车辆底盘号
     * 是否必填 false
     * 版本 v3.0
     */
    private final String vin;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("vin", vin);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/customer-manager/pre-customer-edit-vehicle";
    }
}