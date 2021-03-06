package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vipmarketing;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * /car-platform/pc/vip-marketing/wash-car-manager/equity-detail的接口
 *
 * @author wangmin
 * @date 2021-03-12 17:23:17
 */
@Builder
public class WashCarManagerEquityDetailScene extends BaseScene {

    @Override
    public JSONObject getRequestBody() {
        return new JSONObject();
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/vip-marketing/wash-car-manager/equity-detail";
    }
}