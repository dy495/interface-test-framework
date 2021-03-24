package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 6.1. 获取banner通用（池）（2021-03-15）的接口
 *
 * @author wangmin
 * @date 2021-03-24 14:46:43
 */
@Builder
public class AppletConsultBannerScene extends BaseScene {
    /**
     * 描述 type banner类型 HOME_PAGE 首页 ONLINE_EXPERTS 在线专家 RENEW_CONSULT 续保咨询 USED_CAR 二手车 USED_CAR_ASSESS 二手车评估
     * 是否必填 false
     * 版本 v3.0
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
        return "/jiaochen/applet/granted/consult/banner";
    }
}