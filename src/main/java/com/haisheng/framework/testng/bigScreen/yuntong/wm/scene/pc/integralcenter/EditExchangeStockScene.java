package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.integralcenter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 38.11. 编辑积分兑换库存 (张小龙) v2.0
 *
 * @author wangmin
 * @date 2021-05-18 17:04:36
 */
@Builder
public class EditExchangeStockScene extends BaseScene {
    /**
     * 描述 库存商品id
     * 是否必填 false
     * 版本 v2.0
     */
    private final Long id;

    /**
     * 描述 商品类型( FICTITIOUS("虚拟商品"),REAL("实物");)
     * 是否必填 false
     * 版本 v2.0
     */
    private final String type;

    /**
     * 描述 库存商品名称
     * 是否必填 false
     * 版本 v2.0
     */
    private final String goodsName;

    /**
     * 描述 更改库存类型（ADD:增加 MINUS:减少）
     * 是否必填 true
     * 版本 v2.0
     */
    private final String changeStockType;

    /**
     * 描述 改变库存数量
     * 是否必填 true
     * 版本 v2.0
     */
    private final Integer num;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("type", type);
        object.put("goods_name", goodsName);
        object.put("change_stock_type", changeStockType);
        object.put("num", num);
        return object;
    }

    @Override
    public String getPath() {
        return "/yt/pc/integral-center/edit-exchange-stock";
    }
}