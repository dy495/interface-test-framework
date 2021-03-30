package com.haisheng.framework.testng.bigScreen.xundianDaily.wm.scene.applet.grant;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 7.5. 授权用户信息（谢）car_platform_path: /jiaochen/applet/grant/user-detail
 *
 * @author wangmin
 * @date 2021-03-30 15:23:58
 */
@Builder
public class AppletUserDetailScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String referer;

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
        object.put("referer", referer);
        object.put("encrypted_data", encryptedData);
        object.put("iv", iv);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol-applet/grant/user-detail";
    }
}