package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.presalesreception;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 21.4. 查询手机号客户信息（谢）v3.0 （2021-03-16）
 *
 * @author wangmin
 * @date 2021-08-30 14:26:55
 */
@Builder
public class PreSalesReceptionAdmitScene extends BaseScene {
    /**
     * 描述 手机号
     * 是否必填 true
     * 版本 v1.0
     */
    private final String phone;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("phone", phone);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/pre-sales-reception/admit";
    }
}