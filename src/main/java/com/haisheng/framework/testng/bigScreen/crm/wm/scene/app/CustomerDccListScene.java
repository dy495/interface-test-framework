package com.haisheng.framework.testng.bigScreen.crm.wm.scene.app;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * dcc客户列表接口
 *
 * @author wangmin
 */
@Builder
public class CustomerDccListScene extends BaseScene {
    @Builder.Default
    private final String page = "1";
    @Builder.Default
    private final String size = "10";
    private final String endTime;
    private final String startTime;
    private final String customerLevel;
    private final String searchCondition;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("end_time", endTime);
        object.put("start_time", startTime);
        object.put("customer_level", customerLevel);
        object.put("search_condition", searchCondition);
        return object;
    }

    @Override
    public String getPath() {
        return "/porsche/app/customer/dcc-list";
    }

    @Override
    public String getIpPort() {
        return null;
    }
}
