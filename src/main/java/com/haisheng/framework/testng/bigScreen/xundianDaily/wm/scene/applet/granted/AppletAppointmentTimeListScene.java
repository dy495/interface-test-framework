package com.haisheng.framework.testng.bigScreen.xundianDaily.wm.scene.applet.granted;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 7.3. 预约时间段列表 （谢）v3.0（2021-03-30）
 *
 * @author wangmin
 * @date 2021-03-31 13:03:22
 */
@Builder
public class AppletAppointmentTimeListScene extends BaseScene {
    /**
     * 描述 预约类型 详见字典表《预约类型》v3.0（2021-03-12）
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
     * 描述 预约日期
     * 是否必填 true
     * 版本 v1.0
     */
    private final String day;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("type", type);
        object.put("shop_id", shopId);
        object.put("car_id", carId);
        object.put("car_style_id", carStyleId);
        object.put("day", day);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/applet/granted/appointment/time/list";
    }
}