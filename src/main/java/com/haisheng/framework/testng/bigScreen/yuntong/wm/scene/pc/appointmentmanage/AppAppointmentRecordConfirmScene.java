package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.appointmentmanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 16.3. 确认预约（谢）
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class AppAppointmentRecordConfirmScene extends BaseScene {
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
     * 描述 预约类型 见字典表《预约类型》
     * 是否必填 true
     * 版本 v3.0
     */
    private final String type;


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
        return "/yt/pc/appointment-manage/appointment-record/confirm";
    }
}