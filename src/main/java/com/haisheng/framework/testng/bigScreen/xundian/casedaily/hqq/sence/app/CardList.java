package com.haisheng.framework.testng.bigScreen.xundian.casedaily.hqq.sence.app;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 卡片列表接口
 */
@Builder
public class CardList extends BaseScene {
    private final String pageType;
    private final Integer lastValue;
    private final Integer size;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page_type", pageType);
        object.put("last_value", lastValue);
        object.put("size", size);
        return object;
    }

    @Override
    public String getPath() {
        return "/store/m-app/auth/card/card-list";
    }
}
