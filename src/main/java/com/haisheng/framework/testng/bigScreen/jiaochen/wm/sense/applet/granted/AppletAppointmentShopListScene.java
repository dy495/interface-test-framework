package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * /jiaochen/applet/granted/appointment/shop/list的接口
 *
 * @author wangmin
 * @date 2021-03-12 17:53:03
 */
@Builder
public class AppletAppointmentShopListScene extends BaseScene {
    /**
     * 描述 预约类型 MAINTAIN：保养，REPAIR：维修
     * 是否必填 true
     * 版本 v2.0
     */
    private final String type;

    /**
     * 描述 预约车辆id
     * 是否必填 true
     * 版本 v1.0
     */
    private final Long carId;

    /**
     * 描述 客户当前位置经纬度 [纬度,经度]
     * 是否必填 false
     * 版本 v1.0
     */
    private final JSONArray coordinate;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("type", type);
        object.put("car_id", carId);
        object.put("coordinate", coordinate);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/applet/granted/appointment/shop/list";
    }
}