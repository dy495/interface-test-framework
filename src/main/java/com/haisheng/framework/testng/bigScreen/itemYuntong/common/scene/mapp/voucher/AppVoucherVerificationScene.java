package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.voucher;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 16.1. app 卡券核销（张小龙）
 *
 * @author wangmin
 * @date 2021-08-06 16:38:24
 */
@Builder
public class AppVoucherVerificationScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String cardNumber;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("card_number", cardNumber);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/voucher/verification";
    }
}