package com.haisheng.framework.testng.bigScreen.itemPorsche.common.scene.pc;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 概览数据接口
 *
 * @author wangmin
 */
@Builder
public class Analysis2ShopPanelScene extends BaseScene {
    @Builder.Default
    private final int page = 1;
    @Builder.Default
    private final int size = 10;
    private final String month;
    private final String cycleType;
    private final String saleId;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("month", month);
        object.put("cycle_type", cycleType);
        object.put("sale_id", saleId);
        return object;
    }

    @Override
    public String getPath() {
        return "/porsche/analysis2/shop/pannel";
    }

    @Override
    public String getIpPort() {
        return null;
    }
}
