package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.presalesreception;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 4.7. 销售接待员工列表（谢）v3.0 （2021-03-16）
 *
 * @author wangmin
 * @date 2021-03-31 13:03:23
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
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("shop_id", shopId);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/m-app/pre-sales-reception/receptor/list";
    }
}