package com.haisheng.framework.testng.bigScreen.crm.wm.scene.app;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 售后-我的客户列表接口
 *
 * @author wangmin
 */
@Builder
public class AfterSaleCustomerListScene extends BaseScene {
    @Builder.Default
    private final String page = "1";
    @Builder.Default
    private final String size = "10";
    private final String searchCondition;
    private final String searchDateStart;
    private final String searchDateEnd;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("search_condition", searchCondition);
        object.put("search_date_start", searchDateStart);
        object.put("search_date_end", searchDateEnd);
        return object;
    }

    @Override
    public String getPath() {
        return "/porsche/app/after_sale/after-sale-customer-list";
    }

    @Override
    public String getIpPort() {
        return null;
    }
}
