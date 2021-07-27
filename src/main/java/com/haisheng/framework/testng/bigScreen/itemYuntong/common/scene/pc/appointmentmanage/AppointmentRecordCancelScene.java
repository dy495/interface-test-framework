package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.appointmentmanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 20.4. 取消预约（谢）
 *
 * @author wangmin
 * @date 2021-07-27 18:26:28
 */
@Builder
public class AppointmentRecordCancelScene extends BaseScene {
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
        return "/car-platform/pc/appointment-manage/appointment-record/cancel";
    }
}