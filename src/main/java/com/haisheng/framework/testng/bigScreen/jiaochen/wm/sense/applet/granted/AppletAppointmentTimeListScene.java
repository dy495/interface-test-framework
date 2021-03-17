package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * /jiaochen/applet/granted/appointment/time/list的接口
 *
 * @author wangmin
 * @date 2021-03-12 17:53:03
 */
@Builder
public class AppletAppointmentTimeListScene extends BaseScene {
    /**
     * 描述 预约类型 MAINTAIN：保养，REPAIR：维修
     * 是否必填 true
     * 版本 v2.0
     */
    private final String type;

    /**
     * 描述 预约门店id
     * 是否必填 true
     * 版本 v1.0
     */
    private final Long shopId;

    /**
     * 描述 预约车辆id
     * 是否必填 true
     * 版本 v1.0
     */
    private final Long carId;

    /**
     * 描述 预约日期
     * 是否必填 true
     * 版本 v1.0
     */
    private final String day;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("type", type);
        object.put("shop_id", shopId);
        object.put("car_id", carId);
        object.put("day", day);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/applet/granted/appointment/time/list";
    }
}