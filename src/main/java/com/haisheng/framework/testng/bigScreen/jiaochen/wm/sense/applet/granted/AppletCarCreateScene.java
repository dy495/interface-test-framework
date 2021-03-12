package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * /jiaochen/applet/granted/car/create的接口
 *
 * @author wangmin
 * @date 2021-03-12 17:53:03
 */
@Builder
public class AppletCarCreateScene extends BaseScene {
    /**
     * 描述 车型id
     * 是否必填 false
     * 版本 1.0
     */
    private final Long modelId;

    /**
     * 描述 车牌号
     * 是否必填 false
     * 版本 1.0
     */
    private final String plateNumber;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("model_id", modelId);
        object.put("plate_number", plateNumber);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/applet/granted/car/create";
    }
}