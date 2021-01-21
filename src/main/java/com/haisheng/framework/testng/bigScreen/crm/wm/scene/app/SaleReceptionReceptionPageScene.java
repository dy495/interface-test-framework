package com.haisheng.framework.testng.bigScreen.crm.wm.scene.app;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 前台展厅接待接口
 *
 * @author wangmin
 */
@Builder
public class SaleReceptionReceptionPageScene extends BaseScene {
    private final String customerNamePhone;
    private final String startTime;
    private final String endTime;
    @Builder.Default
    private final String page = "1";
    @Builder.Default
    private final String size = "10";

    @Override
    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        object.put("customer_name_phone", customerNamePhone);
        object.put("end_time", endTime);
        object.put("start_time", startTime);
        object.put("page", page);
        object.put("size", size);
        return object;
    }

    @Override
    public String getPath() {
        return "/porsche/app/sale-reception/reception-page";
    }

    @Override
    public String getIpPort() {
        return null;
    }
}
