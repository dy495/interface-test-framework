package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 积分明细
 *
 * @author wangmin
 * @date 2021/02/23
 */
@Builder
public class AppletIntegralRecordScene extends BaseScene {
    private final Integer lastValue;
    private final Integer size;
    private final String type;
    private final String endTime;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("last_value", lastValue);
        object.put("size", size);
        object.put("type", type);
        object.put("end_time", endTime);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/applet/granted/integral-mall/integral-record";
    }
}
