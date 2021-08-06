package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.reid;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 12.1. 前台人脸列表（华）v5.0（2021-07-09）
 *
 * @author wangmin
 * @date 2021-08-06 16:38:23
 */
@Builder
public class AppReidReidListScene extends BaseScene {
    /**
     * 描述 页大小 范围为[1,100]
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer size;

    /**
     * 描述 上次请求最后值
     * 是否必填 false
     * 版本 v1.0
     */
    private final JSONObject lastValue;

    /**
     * 描述 人脸开关，若为false，则不会返回人脸图片，防止抓包
     * 是否必填 true
     * 版本 v5.0
     */
    private final Boolean isFaceOpen;

    /**
     * 描述 客户进入门类型（PRE_SALE/AFTER_SALE 销售门/售后门)
     * 是否必填 true
     * 版本 v5.0
     */
    private final String enterType;

    /**
     * 描述 查询日期
     * 是否必填 false
     * 版本 v5.0
     */
    private final String day;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("size", size);
        object.put("last_value", lastValue);
        object.put("is_face_open", isFaceOpen);
        object.put("enter_type", enterType);
        object.put("day", day);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/reid/reid-list";
    }
}