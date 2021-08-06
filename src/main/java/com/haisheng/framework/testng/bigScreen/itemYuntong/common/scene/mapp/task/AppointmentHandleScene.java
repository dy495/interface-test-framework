package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 7.2. app预约确认/取消（谢）
 *
 * @author wangmin
 * @date 2021-08-06 16:38:23
 */
@Builder
public class AppointmentHandleScene extends BaseScene {
    /**
     * 描述 预约记录id
     * 是否必填 true
     * 版本 v1.0
     */
    private final Long id;

    /**
     * 描述 预约门店id
     * 是否必填 true
     * 版本 v1.0
     */
    private final Long shopId;

    /**
     * 描述 处理类型 10：确认，20取消
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer type;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("shop_id", shopId);
        object.put("type", type);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/task/appointment/handle";
    }
}