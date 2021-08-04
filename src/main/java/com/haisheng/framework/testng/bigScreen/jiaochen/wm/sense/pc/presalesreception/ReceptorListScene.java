package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.presalesreception;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 15.8. 销售接待员工列表（谢）v3.0 （2021-03-16）
 *
 * @author wangmin
 * @date 2021-03-31 12:01:33
 */
@Builder
public class ReceptorListScene extends BaseScene {
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
        return "/car-platform/pc/pre-sales-reception/receptor/list";
    }
}