package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.appointmentmanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * /jiaochen/pc/appointment-manage/maintain/time/list的接口
 *
 * @author wangmin
 * @date 2021-03-15 14:05:12
 */
@Builder
public class MaintainTimeListScene extends BaseScene {
    /**
     * 描述 预约类型 REPAIR：维修，MAINTAIN：保养
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

    /**
     * 描述 预约日期
     * 是否必填 true
     * 版本 v1.0
     */
    private final String day;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("type", type);
        object.put("shop_id", shopId);
        object.put("day", day);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/appointment-manage/maintain/time/list";
    }
}