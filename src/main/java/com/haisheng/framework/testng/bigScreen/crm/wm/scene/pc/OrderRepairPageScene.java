package com.haisheng.framework.testng.bigScreen.crm.wm.scene.pc;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 预约保养记录接口
 *
 * @author wangmin
 */
@Builder
public class OrderRepairPageScene extends BaseScene {
    private final String startDay;
    private final String endDay;
    @Builder.Default
    private final int page = 1;
    @Builder.Default
    private final int size = 10;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("start_day", startDay);
        object.put("end_day", endDay);
        object.put("page", page);
        object.put("size", size);
        return object;
    }

    @Override
    public String getPath() {
        return "/porsche/order-manage/order/repair/page";
    }

    @Override
    public String getIpPort() {
        return null;
    }
}
