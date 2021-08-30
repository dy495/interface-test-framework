package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.shop;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 41.6. 门店状态变更 （谢）
 *
 * @author wangmin
 * @date 2021-08-30 14:26:55
 */
@Builder
public class StatusChangeScene extends BaseScene {
    /**
     * 描述 门店id
     * 是否必填 true
     * 版本 v1.0
     */
    private final Long id;

    /**
     * 描述 状态类型 APPOINTMENT：预约状态，WASHING：洗车状态，SHOP：门店状态
     * 是否必填 true
     * 版本 v1.0
     */
    private final String type;

    /**
     * 描述 状态 ENABLE：开启，DISABLE：关闭
     * 是否必填 true
     * 版本 v1.0
     */
    private final String status;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("type", type);
        object.put("status", status);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/shop/status/change";
    }
}