package com.haisheng.framework.testng.bigScreen.jiaochenDaily.wm.sense.pc.packagemanager;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crmDaily.wm.scene.BaseScene;
import lombok.Builder;

/**
 * 套餐管理 -> 套餐表单
 */
@Builder
public class PackageFormPage extends BaseScene {
    private final Boolean packageStatus;
    private final String packageName;
    private final String creator;
    private final String startTime;
    private final String endTime;
    private final String shopName;
    @Builder.Default
    private final Integer page = 1;
    @Builder.Default
    private final Integer size = 10;

    @Override
    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        object.put("package_status", packageStatus);
        object.put("package_name", packageName);
        object.put("creator", creator);
        object.put("start_time", startTime);
        object.put("end_time", endTime);
        object.put("shop_name", shopName);
        object.put("page", page);
        object.put("size", size);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/package-manage/package-form/page";
    }
}
