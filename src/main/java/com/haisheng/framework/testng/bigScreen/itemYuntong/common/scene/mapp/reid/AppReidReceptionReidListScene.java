package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.reid;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 12.5. 接待中：人脸列表（华）v5.0（2021-07-09）
 *
 * @author wangmin
 * @date 2021-08-06 16:38:23
 */
@Builder
public class AppReidReceptionReidListScene extends BaseScene {
    /**
     * 描述 接待ID
     * 是否必填 true
     * 版本 v5.0
     */
    private final Long receptionId;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("reception_id", receptionId);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/reid/reception-reid-list";
    }
}