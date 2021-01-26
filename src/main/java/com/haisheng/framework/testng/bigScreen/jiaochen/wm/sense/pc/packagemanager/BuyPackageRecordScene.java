package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.packagemanager;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 卡券管理 -> 套餐购买记录
 */
@Builder
public class BuyPackageRecordScene extends BaseScene {
    private final String packageName;
    private final String sender;
    private final String startTime;
    private final String endTime;
    private final Integer sendType;
    @Builder.Default
    private Integer page = 1;
    @Builder.Default
    private Integer size = 10;

    @Override
    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        object.put("package_name", packageName);
        object.put("sender", sender);
        object.put("start_time", startTime);
        object.put("end_time", endTime);
        object.put("send_type", sendType);
        object.put("page", page);
        object.put("size", size);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/package-manage/buy-package-record";
    }

    @Override
    public void setPage(Integer page) {
        this.page = page;
    }

    @Override
    public void setSize(Integer size) {
        this.size = size;
    }
}
