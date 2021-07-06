package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.pc.integralmall;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 商品页
 */
@Builder
public class GoodsManagePageScene extends BaseScene {
    private final String goodsStatus;
    @Builder.Default
    private final Integer page = 1;
    @Builder.Default
    private final Integer size = 10;

    private final String goodsName;
    private final Long goodsBrand;

    private final Long firstCategory;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("goods_status", goodsStatus);
        object.put("page", page);
        object.put("size", size);
        object.put("goods_name", goodsName);
        object.put("goods_brand", goodsBrand);
        object.put("first_category", firstCategory);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/pc/integral-mall/goods-manage-page";
    }

}
