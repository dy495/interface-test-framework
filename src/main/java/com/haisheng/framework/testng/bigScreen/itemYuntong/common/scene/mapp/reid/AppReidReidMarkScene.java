package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.reid;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 12.2. 前台人脸身份标记（华）v5.0（2021-07-09）
 *
 * @author wangmin
 * @date 2021-08-06 16:38:23
 */
@Builder
public class AppReidReidMarkScene extends BaseScene {
    /**
     * 描述 人脸ID
     * 是否必填 true
     * 版本 v5.0
     */
    private final JSONArray reidList;

    /**
     * 描述 标记类型，参考通用枚举(REID_TYPE)
     * 是否必填 true
     * 版本 v5.0
     */
    private final String reidType;

    /**
     * 描述 客户进入门类型（PRE_SALE/AFTER_SALE 销售门/售后门)
     * 是否必填 true
     * 版本 v5.0
     */
    private final String enterType;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("reid_list", reidList);
        object.put("reid_type", reidType);
        object.put("enter_type", enterType);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/reid/reid-mark";
    }
}