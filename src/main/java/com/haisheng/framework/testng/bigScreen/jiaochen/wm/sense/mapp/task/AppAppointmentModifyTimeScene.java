package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.task;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 3.3. app变更预约时间段（谢）
 *
 * @author wangmin
 * @date 2021-03-31 13:03:23
 */
@Builder
public class AppAppointmentModifyTimeScene extends BaseScene {
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
     * 描述 预约时间段id
     * 是否必填 true
     * 版本 v1.0
     */
    private final Long timeId;

    /**
     * 描述 预约类型 见字典表《预约类型》
     * 是否必填 true
     * 版本 v3.0
     */
    private final String type;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("shop_id", shopId);
        object.put("time_id", timeId);
        object.put("type", type);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/m-app/task/appointment/modify-time";
    }
}