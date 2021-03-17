package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.grant;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * /jiaochen/applet/grant/user-detail的接口
 *
 * @author wangmin
 * @date 2021-03-12 17:53:04
 */
@Builder
public class AppletUserDetailScene extends BaseScene {
    /**
     * 描述 授权的加密数据
     * 是否必填 true
     * 版本 v1.0
     */
    private final String encryptedData;

    /**
     * 描述 加密向量
     * 是否必填 true
     * 版本 v1.0
     */
    private final String iv;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("encrypted_data", encryptedData);
        object.put("iv", iv);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/applet/grant/user-detail";
    }
}