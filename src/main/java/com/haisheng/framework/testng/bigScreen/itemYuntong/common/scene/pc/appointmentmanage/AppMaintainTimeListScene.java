package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.appointmentmanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 16.6. 获取可变更预约时间段列表（谢）（2020-12-28）
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class AppMaintainTimeListScene extends BaseScene {
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

    /**
     * 描述 预约日期
     * 是否必填 true
     * 版本 v1.0
     */
    private final String day;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("type", type);
        object.put("shop_id", shopId);
        object.put("day", day);
        return object;
    }

    @Override
    public String getPath() {
        return "/account-platform/auth/appointment-manage/maintain/time/list";
    }
}