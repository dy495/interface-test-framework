package com.haisheng.framework.testng.bigScreen.jiaochen.mc.scene.mapp.aftercustomermanager;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 1.1. app销售售后客户列表（刘）
 *
 * @author wangmin
 * @date 2021-08-26 16:22:56
 */
@Builder
public class AppAfterCustomerManagerAfterCustomerScene extends BaseScene {
    /**
     * 描述 页大小 范围为[1,100]
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer size;

    /**
     * 描述 上次请求最后值
     * 是否必填 false
     * 版本 v1.0
     */
    private final JSONObject lastValue;

    /**
     * 描述 查询条件
     * 是否必填 false
     * 版本 v7.0
     */
    private final String qryValue;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("size", size);
        object.put("last_value", lastValue);
        object.put("qry_value", qryValue);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/after-customer-manager/after-customer";
    }
}