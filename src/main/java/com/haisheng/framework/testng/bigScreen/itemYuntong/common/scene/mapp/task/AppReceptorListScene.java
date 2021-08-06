package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 11.7. 售后接待员工列表（谢）（2020-12-15）
 *
 * @author wangmin
 * @date 2021-08-06 16:38:23
 */
@Builder
public class AppReceptorListScene extends BaseScene {
    /**
     * 描述 门店id
     * 是否必填 true
     * 版本 v2.0
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
        return "/car-platform/m-app/task/reception/receptor/list";
    }
}