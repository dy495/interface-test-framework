package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.reid;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 12.11. 接待中：添加陪客（华）v5.0（2021-07-09）
 *
 * @author wangmin
 * @date 2021-08-06 16:38:24
 */
@Builder
public class AppReidReceptionAccompanyReidAddScene extends BaseScene {
    /**
     * 描述 接待ID
     * 是否必填 true
     * 版本 v5.0
     */
    private final Long receptionId;

    /**
     * 描述 人脸ID
     * 是否必填 true
     * 版本 v5.0
     */
    private final JSONArray reidList;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("reception_id", receptionId);
        object.put("reid_list", reidList);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/reid/reception-accompany-reid-add";
    }
}