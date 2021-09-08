package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.integralcenter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 38.13. 兑换商品规格详情列表 (张小龙) v2.0 （2021-01-11）
 *
 * @author wangmin
 * @date 2021-08-30 14:26:55
 */
@Builder
public class IntegralCenterExchangeCommoditySpecificationsListScene extends BaseScene {
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
        return "/car-platform/pc/integral-center/exchange-commodity-specifications-list";
    }
}