package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.store;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 20.13. 订单卡卷作废 v2.0(池)
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class OrderVolumeCancelScene extends BaseScene {
    /**
     * 描述 id 订单id
     * 是否必填 false
     * 版本 -
     */
    private final Long id;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        return object;
    }

    @Override
    public String getPath() {
        return "/account-platform/auth/store/order/volume-cancel";
    }
}