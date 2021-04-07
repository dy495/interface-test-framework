package com.haisheng.framework.testng.bigScreen.xundianDaily.wm.scene.pc.integralcenter;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 积分明细
 */
@Builder
public class ExchangeDetailedScene extends BaseScene {
    @Builder.Default
    private Integer page = 1;
    @Builder.Default
    private Integer size = 10;
    private final String phone;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("phone", phone);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/pc/integral-center/exchange-detailed";
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
