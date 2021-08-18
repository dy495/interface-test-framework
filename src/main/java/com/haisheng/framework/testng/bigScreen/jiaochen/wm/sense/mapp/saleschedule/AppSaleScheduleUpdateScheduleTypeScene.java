package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.saleschedule;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 14.13. 销售排班类型变更-按人、按月 (刘) v5.0
 *
 * @author wangmin
 * @date 2021-08-06 16:38:24
 */
@Builder
public class AppSaleScheduleUpdateScheduleTypeScene extends BaseScene {
    /**
     * 描述 排班类型： 按人 按组 公用enum --》SALE_SCHEDULE_TYPE
     * 是否必填 true
     * 版本 5.0
     */
    private final Integer scheduleType;

    /**
     * 描述 PRE 销售 * AFTER 售后
     * 是否必填 true
     * 版本 5.0
     */
    private final String type;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("schedule_type", scheduleType);
        object.put("type", type);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/sale-schedule/update-schedule-type";
    }
}