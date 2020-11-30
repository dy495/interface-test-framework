package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.packagemanager;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.scene.BaseScene;
import lombok.Builder;

/**
 * 预约管理 -> 预约记录
 */
@Builder
public class PackageFormPage extends BaseScene {
    private final String packageStatus;
    private final String packageName;
    private final String creator;
    private final String startTime;
    private final String endTime;
    private final String shopName;
    private final Integer page;
    private final Integer size;

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
