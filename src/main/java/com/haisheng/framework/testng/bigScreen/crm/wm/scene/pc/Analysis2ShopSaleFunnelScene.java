package com.haisheng.framework.testng.bigScreen.crm.wm.scene.pc;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 漏斗接口
 *
 * @author wangmin
 */
@Builder
public class Analysis2ShopSaleFunnelScene extends BaseScene {
    private final String month;
    private final String cycleType;
    private final String saleId;

    @Override
    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        object.put("cycle_type", cycleType);
        object.put("month", month);
        object.put("sale_id", saleId);
        return object;
    }

    @Override
    public String getPath() {
        return "/porsche/analysis2/shop/sale-funnel";
    }

    @Override
    public String getIpPort() {
        return null;
    }
}
