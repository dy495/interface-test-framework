package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.customermanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 13.7. 车系列表 (池)v3.0 （2021-3-15）
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class PreSaleCustomerStyleListScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final Long shopId;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("shop_id", shopId);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/customer-manage/pre-sale-customer/style-list";
    }
}