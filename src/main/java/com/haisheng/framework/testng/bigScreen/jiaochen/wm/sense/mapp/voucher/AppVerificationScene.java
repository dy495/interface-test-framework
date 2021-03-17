package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.voucher;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * /jiaochen/m-app/voucher/verification的接口
 *
 * @author wangmin
 * @date 2021-03-12 18:09:47
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
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("card_number", cardNumber);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/m-app/voucher/verification";
    }
}