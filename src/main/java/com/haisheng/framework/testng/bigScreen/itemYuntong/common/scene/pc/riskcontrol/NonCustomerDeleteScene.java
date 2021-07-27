package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.riskcontrol;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 10.3. 非客删除 v5.0
 *
 * @author wangmin
 * @date 2021-07-27 18:26:28
 */
@Builder
public class NonCustomerDeleteScene extends BaseScene {
    /**
     * 描述 唯一id
     * 是否必填 true
     * 版本 v5.0
     */
    private final Long id;

    /**
     * 描述 标记
     * 是否必填 false
     * 版本 v5.0
     */
    private final String signId;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("sign_id", signId);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/risk-control/non-customer/delete";
    }
}