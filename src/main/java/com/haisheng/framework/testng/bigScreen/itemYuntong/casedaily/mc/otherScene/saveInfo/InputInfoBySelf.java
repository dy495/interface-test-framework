package com.haisheng.framework.testng.bigScreen.itemYuntong.casedaily.mc.otherScene.saveInfo;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

@Builder
public class InputInfoBySelf extends BaseScene {
    /**
     * 姓名
     * Required : true
     **/
    private final String name;
    /**
     * 手机号
     * Required : false
     **/
    private final String phone;
    /**
     * 车牌记录ID
     * Required : false
     **/
    private final int plateNumberRecordId;

    @Override
    protected JSONObject getRequestBody() {
        JSONObject obj = new JSONObject();
        obj.put("name",name);
        obj.put("phone",phone);
        obj.put("plate_number_record_id",plateNumberRecordId);
        return obj;
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/retention/reid-customer-add";
    }
}
