package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.integralcenter;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
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
        return "/car-platform/pc/integral-center/exchange-detailed";
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
