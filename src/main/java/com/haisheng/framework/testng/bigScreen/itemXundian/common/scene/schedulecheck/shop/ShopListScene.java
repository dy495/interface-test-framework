package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.schedulecheck.shop;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 20.2. 获取可巡查门店
 *
 * @author wangmin
 * @date 2021-07-30 10:55:36
 */
@Builder
public class ShopListScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String referer;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String appId;

    /**
     * 描述 巡检员id
     * 是否必填 true
     * 版本 -
     */
    private final String inspectorId;

    /**
     * 描述 区划编码
     * 是否必填 true
     * 版本 -
     */
    private final String districtCode;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("inspector_id", inspectorId);
        object.put("district_code", districtCode);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/schedule-check/shop/list";
    }
}