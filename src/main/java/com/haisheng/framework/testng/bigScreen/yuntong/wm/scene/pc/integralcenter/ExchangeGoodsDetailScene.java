package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.integralcenter;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 38.4. 积分兑换商品详情 (张小龙) v3.0 modify
 *
 * @author wangmin
 * @date 2021-05-18 17:04:36
 */
@Builder
public class ExchangeGoodsDetailScene extends BaseScene {
    /**
     * 描述 唯一id
     * 是否必填 true
     * 版本 v2.0
     */
    private final Long id;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        return object;
    }

    @Override
    public String getPath() {
        return "/account-platform/auth/integral-center/exchange-goods-detail";
    }
}