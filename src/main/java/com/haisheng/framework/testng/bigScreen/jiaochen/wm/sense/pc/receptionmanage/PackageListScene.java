package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.receptionmanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 套餐列表
 */
@Builder
public class PackageListScene extends BaseScene {

    @Override
    public JSONObject getRequestBody() {
        return new JSONObject();
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/reception-manage/package-list";
    }
}
