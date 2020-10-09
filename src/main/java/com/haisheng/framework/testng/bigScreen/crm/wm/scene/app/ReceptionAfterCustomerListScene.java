package com.haisheng.framework.testng.bigScreen.crm.wm.scene.app;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.model.experiment.enumerator.EnumAddress;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.base.BaseScene;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 售后我的接待列表接口
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class ReceptionAfterCustomerListScene extends BaseScene {
    private String searchCondition;
    private String searchDateEnd;
    private String searchDateStart;
    @Builder.Default
    private Integer page = 1;
    @Builder.Default
    private Integer size = 10;

    @Override
    public JSONObject getJSONObject() {
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
        return EnumAddress.PORSCHE.getAddress();
    }
}
