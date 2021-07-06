package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.manage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 1.4. 配置预约时间段（谢）v3.0（2021-03-22）
 *
 * @author wangmin
 * @date 2021-03-31 12:29:35
 */
@Builder
public class AppointmentTimeRangeEditScene extends BaseScene {
    /**
     * 描述 预约类型 取值见字典表《预约类型》 v3.0增加试驾
     * 是否必填 true
     * 版本 v2.0
     */
    private final String type;

    /**
     * 描述 日期类型 WEEKDAY：工作提，WEEKEND：周末
     * 是否必填 true
     * 版本 v1.0
     */
    private final String dateType;

    /**
     * 描述 上午时段配置
     * 是否必填 true
     * 版本 v1.0
     */
    private final JSONObject morning;

    /**
     * 描述 下午时段配置
     * 是否必填 true
     * 版本 v1.0
     */
    private final JSONObject afternoon;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("type", type);
        object.put("date_type", dateType);
        object.put("morning", morning);
        object.put("afternoon", afternoon);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/manage/appointment/time-range/edit";
    }
}