package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.integralcenter;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 修改库存
 */
@Builder
public class EditExchangeStockScene extends BaseScene {
    private final String changeStockType;
    private final String num;
    private final Long id;
    private final String goodsName;
    private final String type;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("change_stock_type", changeStockType);
        object.put("num", num);
        object.put("id", id);
        object.put("goods_name", goodsName);
        object.put("type", type);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/integral-center/edit-exchange-stock";
    }
}
