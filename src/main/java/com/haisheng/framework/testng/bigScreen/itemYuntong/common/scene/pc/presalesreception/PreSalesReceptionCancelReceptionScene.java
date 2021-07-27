package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.presalesreception;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 26.7. 取消接待（谢）v3.0 （2021-03-16）
 *
 * @author wangmin
 * @date 2021-07-27 18:26:28
 */
@Builder
public class PreSalesReceptionCancelReceptionScene extends BaseScene {
    /**
     * 描述 接待id
     * 是否必填 true
     * 版本 v1.0
     */
    private final Long id;

    /**
     * 描述 接待门店id
     * 是否必填 true
     * 版本 v1.0
     */
    private final Long shopId;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("shop_id", shopId);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/pre-sales-reception/cancel-reception";
    }
}