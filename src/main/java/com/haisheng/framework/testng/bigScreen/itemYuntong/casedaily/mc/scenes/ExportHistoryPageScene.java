package com.haisheng.framework.testng.bigScreen.itemYuntong.casedaily.mc.scenes;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 6.3. 导出记录 v1.0 (池)
 * @date :2021/7/9 19:27
 **/

@Builder
public class ExportHistoryPageScene extends BaseScene {

    private final int page;
    private final int size;

    @Override
    protected JSONObject getRequestBody() {
        JSONObject obj = new JSONObject();
        obj.put("page",page);
        obj.put("size",size);
        return obj;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/record/export/page";
    }
}
