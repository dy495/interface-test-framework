package com.haisheng.framework.testng.bigScreen.xundian.scene.applet.granted;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 7.1. 预约门店列表 （谢）v3.0（2021-03-30）
 *
 * @author wangmin
 * @date 2021-03-31 13:03:22
 */
@Builder
public class AppletAppointmentShopListScene extends BaseScene {
    /**
     * 描述 预约类型 详见字典表《预约类型》v3.0（2021-03-12）
     * 是否必填 true
     * 版本 v2.0
     */
    private final String type;

    /**
     * 描述 预约车辆id type为保养维修时不能为空
     * 是否必填 false
     * 版本 v1.0
     */
    private final Long carId;

    /**
     * 描述 预约车系id type为试驾时不能为空
     * 是否必填 false
     * 版本 v3.0
     */
    private final Long carStyleId;

    /**
     * 描述 客户当前位置经纬度 [纬度,经度]
     * 是否必填 false
     * 版本 v1.0
     */
    private final JSONArray coordinate;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("type", type);
        object.put("car_id", carId);
        object.put("car_style_id", carStyleId);
        object.put("coordinate", coordinate);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol-applet/granted/appointment/shop/list";
    }
}