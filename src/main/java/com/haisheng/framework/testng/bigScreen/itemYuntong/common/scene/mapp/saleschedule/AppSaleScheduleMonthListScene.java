package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.saleschedule;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 14.4. 当月销售排班列表 (刘) v5.0
 *
 * @author wangmin
 * @date 2021-08-06 16:38:24
 */
@Builder
public class AppSaleScheduleMonthListScene extends BaseScene {
    /**
     * 描述 页大小 范围为[1,100]
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer size;

    /**
     * 描述 上次请求最后值
     * 是否必填 false
     * 版本 v1.0
     */
    private final JSONObject lastValue;

    /**
     * 描述 PRE/AFTER 销售/售后
     * 是否必填 true
     * 版本 v5.0
     */
    private final String type;

    /**
     * 描述 选择月份
     * 是否必填 true
     * 版本 v5.0
     */
    private final String date;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("size", size);
        object.put("last_value", lastValue);
        object.put("type", type);
        object.put("date", date);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/sale-schedule/month-list";
    }
}