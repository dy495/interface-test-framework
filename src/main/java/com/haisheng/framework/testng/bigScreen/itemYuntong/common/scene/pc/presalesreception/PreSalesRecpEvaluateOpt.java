package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.presalesreception;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 21.1. 接待分页（谢）v3.0 （2021-03-16）
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class PreSalesRecpEvaluateOpt extends BaseScene {

    /**
     * 描述 接待id
     * 是否必填 false
     * 版本 v3.0
     */
    private final Long reception_id;

    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();

        object.put("reception_id", reception_id);

        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/h5/pre-sales-reception/evaluate-option";
    }


}