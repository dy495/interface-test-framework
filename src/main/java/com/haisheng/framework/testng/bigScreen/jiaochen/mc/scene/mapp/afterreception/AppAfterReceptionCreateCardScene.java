package com.haisheng.framework.testng.bigScreen.jiaochen.mc.scene.mapp.afterreception;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 2.1. 创建销售接待--虚拟卡片（刘）v7.0
 *
 */
@Builder
public class AppAfterReceptionCreateCardScene extends BaseScene {
    @Override
    protected JSONObject getRequestBody() {
        return null;
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/after-reception/detail";
    }
}