package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.saleschedule;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 14.6. 销售排班组列表 (刘) v5.0
 *
 * @author wangmin
 * @date 2021-08-06 16:38:24
 */
@Builder
public class AppSaleScheduleGroupListScene extends BaseScene {
    /**
     * 描述 组id
     * 是否必填 false
     * 版本 v5.0
     */
    private final Long groupId;

    /**
     * 描述 排班时间 2021-03
     * 是否必填 false
     * 版本 5.0
     */
    private final String time;

    /**
     * 描述 PRE 销售 AFTER 售后
     * 是否必填 true
     * 版本 5.0
     */
    private final String type;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("group_id", groupId);
        object.put("time", time);
        object.put("type", type);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/sale-schedule/group-list";
    }
}