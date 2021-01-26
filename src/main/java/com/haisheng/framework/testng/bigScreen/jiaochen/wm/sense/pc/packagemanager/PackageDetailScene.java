package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.packagemanager;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * @author wangmin
 * @date 2021/1/25 20:04
 */
@Builder
public class PackageDetailScene extends BaseScene {
    private final Long id;

    @Override
    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/package-manage/package-detail";
    }
}
