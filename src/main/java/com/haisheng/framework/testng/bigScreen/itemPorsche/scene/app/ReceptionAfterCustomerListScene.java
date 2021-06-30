package com.haisheng.framework.testng.bigScreen.itemPorsche.scene.app;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 售后我的接待列表接口
 *
 * @author wangmin
 */
@Builder
public class ReceptionAfterCustomerListScene extends BaseScene {
    private final String searchCondition;
    private final String searchDateEnd;
    private final String searchDateStart;
    @Builder.Default
    private final Integer page = 1;
    @Builder.Default
    private final Integer size = 10;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("search_condition", searchCondition);
        object.put("search_date_start", searchDateStart);
        object.put("search_date_end", searchDateEnd);
        object.put("page", page);
        object.put("size", size);
        return object;
    }

    @Override
    public String getPath() {
        return "/porsche/app/after_sale/reception_after_customer_list";
    }

    @Override
    public String getIpPort() {
        return null;
    }
}
