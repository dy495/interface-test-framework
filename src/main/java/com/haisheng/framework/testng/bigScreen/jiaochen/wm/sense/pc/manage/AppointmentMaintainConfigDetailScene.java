package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.manage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * /jiaochen/pc/manage/appointment/maintain-config/detail的接口
 *
 * @author wangmin
 * @date 2021-03-12 17:23:17
 */
@Builder
public class AppointmentMaintainConfigDetailScene extends BaseScene {
    /**
     * 描述 预约类型 REPAIR：维修，MAINTAIN：保养
     * 是否必填 true
     * 版本 v2.0
     */
    private final String type;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("type", type);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/manage/appointment/maintain-config/detail";
    }
}