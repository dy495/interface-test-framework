package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 7.4. app获取可变更预约日期时间段列表（谢）v3.0（2021-03-16）
 *
 * @author wangmin
 * @date 2021-08-06 16:38:23
 */
@Builder
public class AppTimeListScene extends BaseScene {
    /**
     * 描述 预约类型 取值见字典表《预约类型》v3.0
     * 是否必填 true
     * 版本 v2.0
     */
    private final String type;

    /**
     * 描述 预约门店id
     * 是否必填 true
     * 版本 v1.0
     */
    private final Long shopId;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("type", type);
        object.put("shop_id", shopId);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/task/appointment/maintain/time/list";
    }
}