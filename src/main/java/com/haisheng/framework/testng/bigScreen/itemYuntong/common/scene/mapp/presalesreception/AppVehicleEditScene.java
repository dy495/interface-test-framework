package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.presalesreception;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 9.11. 客户车辆编辑（谢）v3.0 （2021-03-16）
 *
 * @author wangmin
 * @date 2021-08-06 16:38:23
 */
@Builder
public class AppVehicleEditScene extends BaseScene {
    /**
     * 描述 车辆id
     * 是否必填 true
     * 版本 v3.0
     */
    private final Long id;

    /**
     * 描述 购买车辆车型
     * 是否必填 true
     * 版本 v3.0
     */
    private final Long carModel;

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
        object.put("car_model", carModel);
        object.put("vin", vin);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/pre-sales-reception/vehicle/edit";
    }
}