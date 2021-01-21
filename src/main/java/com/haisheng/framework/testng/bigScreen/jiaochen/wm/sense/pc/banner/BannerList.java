package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.banner;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

@Builder
public class BannerList extends BaseScene {
    @Override
    public JSONObject getJSONObject() {
        return new JSONObject();
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/banner/list";
    }
}
