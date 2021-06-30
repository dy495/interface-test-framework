package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.appointmentmanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 2.6. 获取可变更预约时间段列表（谢）（2020-12-28）
 *
 * @author wangmin
 * @date 2021-03-31 12:47:26
 */
@Builder
public class MaintainTimeListScene extends BaseScene {
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
    public JSONObject getRequestBody() {
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