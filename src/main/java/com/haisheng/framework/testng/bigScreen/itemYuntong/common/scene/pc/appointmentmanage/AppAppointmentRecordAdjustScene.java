package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.appointmentmanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 16.7. pc变更预约时间段（谢）
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class AppAppointmentRecordAdjustScene extends BaseScene {
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
     * 是否必填 false
     * 版本 v3.0
     */
    private final String type;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("shop_id", shopId);
        object.put("time_id", timeId);
        object.put("type", type);
        return object;
    }

    @Override
    public String getPath() {
        return "/account-platform/auth/appointment-manage/appointment-record/adjust";
    }
}