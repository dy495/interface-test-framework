package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 车型列表
 *
 * @author wangmin
 * @date 2021-03-31 13:03:22
 */
@Builder
public class AppletPlateNumberProvinceListScene extends BaseScene {

    @Override
    public JSONObject getRequestBody() {
        return new JSONObject();
    }

    @Override
    public String getPath() {
        return "/car-platform/applet/plate-number-province-list";
    }
}