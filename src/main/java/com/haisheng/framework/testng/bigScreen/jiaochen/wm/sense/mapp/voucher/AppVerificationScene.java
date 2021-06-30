package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.voucher;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 6.1. app 卡券核销（张小龙）
 *
 * @author wangmin
 * @date 2021-03-31 13:03:23
 */
@Builder
public class AppVerificationScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String cardNumber;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("card_number", cardNumber);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/m-app/voucher/verification";
    }
}