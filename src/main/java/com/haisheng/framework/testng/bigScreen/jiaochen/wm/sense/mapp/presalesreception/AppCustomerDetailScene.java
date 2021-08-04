package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.presalesreception;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 5.10. 客户详情（谢）v3.0 （2021-03-16）
 *
 * @author wangmin
 * @date 2021-03-24 14:44:47
 */
@Builder
public class AppCustomerDetailScene extends BaseScene {
    /**
     * 描述 接待id
     * 是否必填 true
     * 版本 v3.0
     */
    private final String id;

    private final String shopId;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("shop_id", shopId);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/pre-sales-reception/detail";
    }
}