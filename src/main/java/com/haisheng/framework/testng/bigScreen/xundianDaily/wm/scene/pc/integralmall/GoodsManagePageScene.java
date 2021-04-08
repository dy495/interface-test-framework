package com.haisheng.framework.testng.bigScreen.xundianDaily.wm.scene.pc.integralmall;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
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

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("goods_status", goodsStatus);
        object.put("page", page);
        object.put("size", size);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/pc/integral-mall/goods-manage-page";
    }

}
