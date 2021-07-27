package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.presalesreception;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 26.5. 开始接待车主车辆（谢）v3.0 （2021-03-29）
 *
 * @author wangmin
 * @date 2021-07-27 18:26:28
 */
@Builder
public class PreSalesReceptionStartReceptionScene extends BaseScene {
    /**
     * 描述 接待顾客id
     * 是否必填 true
     * 版本 v3.0
     */
    private final Long customerId;

    /**
     * 描述 接待顾客手机号
     * 是否必填 true
     * 版本 v3.0
     */
    private final String customerPhone;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("customer_id", customerId);
        object.put("customer_phone", customerPhone);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/pre-sales-reception/start-reception";
    }
}