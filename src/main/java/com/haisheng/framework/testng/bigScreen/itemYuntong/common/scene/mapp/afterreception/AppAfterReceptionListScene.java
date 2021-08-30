package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.afterreception;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 2.3. 客户售后接待记录列表（刘）v7.0
 *
 * @author wangmin
 * @date 2021-08-30 14:35:38
 */
@Builder
public class AppAfterReceptionListScene extends BaseScene {
    /**
     * 描述 客户id
     * 是否必填 true
     * 版本 v3.0
     */
    private final Long customerId;

    /**
     * 描述 查询数量
     * 是否必填 true
     * 版本 v3.0
     */
    private final Integer size;

    /**
     * 描述 门店id 不传则查询全集团的接待记录
     * 是否必填 false
     * 版本 v3.0
     */
    private final Long shopId;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("customer_id", customerId);
        object.put("size", size);
        object.put("shop_id", shopId);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/after-reception/list";
    }
}