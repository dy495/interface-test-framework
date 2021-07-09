package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.speechtechnique;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 5.1. 获取标准话术列表（谢）
 *
 * @author wangmin
 * @date 2021-05-31 16:28:11
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