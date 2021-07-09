package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.receptionmanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 23.8. 取消接待 （谢）
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class CancelReceptionScene extends BaseScene {
    /**
     * 描述 接待id
     * 是否必填 true
     * 版本 v1.0
     */
    private final Long id;

    /**
     * 描述 接待门店id
     * 是否必填 true
     * 版本 v1.0
     */
    private final Long shopId;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("shop_id", shopId);
        return object;
    }

    @Override
    public String getPath() {
        return "/account-platform/auth/reception-manage/cancel-reception";
    }
}