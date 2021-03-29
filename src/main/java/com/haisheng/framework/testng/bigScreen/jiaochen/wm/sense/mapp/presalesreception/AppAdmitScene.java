package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.presalesreception;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 5.1. 查询手机号客户信息（谢）v3.0 （2021-03-16）的接口
 *
 * @author wangmin
 * @date 2021-03-24 14:44:47
 */
@Builder
public class AppAdmitScene extends BaseScene {
    /**
     * 描述 手机号
     * 是否必填 true
     * 版本 v1.0
     */
    private final String phone;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("phone", phone);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/m-app/pre-sales-reception/admit";
    }
}