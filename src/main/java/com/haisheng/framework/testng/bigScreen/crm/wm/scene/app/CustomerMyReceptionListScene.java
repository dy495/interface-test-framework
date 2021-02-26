package com.haisheng.framework.testng.bigScreen.crm.wm.scene.app;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 我的接待列表接口
 *
 * @author wangmin
 */
@Builder
public class CustomerMyReceptionListScene extends BaseScene {
    private final String searchCondition;
    private final String searchDateStart;
    private final String searchDateEnd;
    @Builder.Default
    private int page = 1;
    @Builder.Default
    private int size = 10;

    @Override
    public JSONObject getRequest() {
        JSONObject object = new JSONObject();
        object.put("search_condition", searchCondition);
        object.put("search_date_start", searchDateStart);
        object.put("search_date_end", searchDateEnd);
        object.put("size", size);
        object.put("page", page);
        return object;
    }

    @Override
    public String getPath() {
        return "/porsche/app/customer/my-reception-list";
    }

    @Override
    public void setPage(Integer page) {
        this.page = page;
    }

    @Override
    public void setSize(Integer size) {
        this.size = size;
    }

}
