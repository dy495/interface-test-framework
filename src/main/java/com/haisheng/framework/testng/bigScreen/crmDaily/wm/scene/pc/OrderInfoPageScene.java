package com.haisheng.framework.testng.bigScreen.crmDaily.wm.scene.pc;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crmDaily.wm.scene.BaseScene;
import lombok.Builder;

/**
 * 智能批次列表接口
 *
 * @author wangmin
 */
@Builder
public class OrderInfoPageScene extends BaseScene {

    @Builder.Default
    private final int page = 1;
    @Builder.Default
    private final int size = 10;

    @Override
    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        return object;
    }

    @Override
    public String getPath() {
        return "/porsche/order-info/page";
    }

    @Override
    public String getIpPort() {
        return null;
    }
}
