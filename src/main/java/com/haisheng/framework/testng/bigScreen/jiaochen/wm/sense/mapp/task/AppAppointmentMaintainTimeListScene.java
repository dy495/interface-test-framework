package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * /jiaochen/m-app/task/appointment/maintain/time/list的接口
 *
 * @author wangmin
 * @date 2021-03-12 18:09:47
 */
@Builder
public class AppAppointmentMaintainTimeListScene extends BaseScene {
    /**
     * 描述 预约类型 REPAIR：维修，MAINTAIN：保养
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


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("type", type);
        object.put("shop_id", shopId);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/m-app/task/appointment/maintain/time/list";
    }
}