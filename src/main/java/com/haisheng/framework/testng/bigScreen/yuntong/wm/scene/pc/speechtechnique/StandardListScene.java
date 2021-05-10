package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.speechtechnique;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 3.1. 获取标准话术列表（谢）
 *
 * @author wangmin
 * @date 2021-05-08 20:23:16
 */
@Builder
public class StandardListScene extends BaseScene {
    /**
     * 描述 门店id
     * 是否必填 true
     * 版本 v1.0
     */
    private final Long shopId;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("shop_id", shopId);
        return object;
    }

    @Override
    public String getPath() {
        return "/intelligent-control/pc/speech-technique/standard-list";
    }
}