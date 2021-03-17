package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * /jiaochen/m-app/task/appointment/modify-time的接口
 *
 * @author wangmin
 * @date 2021-03-12 18:09:47
 */
@Builder
public class AppAppointmentModifyTimeScene extends BaseScene {
    /**
     * 描述 预约记录id
     * 是否必填 true
     * 版本 v1.0
     */
    private final Long id;

    /**
     * 描述 预约门店id
     * 是否必填 true
     * 版本 v1.0
     */
    private final Long shopId;

    /**
     * 描述 预约时间段id
     * 是否必填 true
     * 版本 v1.0
     */
    private final Long timeId;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("shop_id", shopId);
        object.put("time_id", timeId);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/m-app/task/appointment/modify-time";
    }
}