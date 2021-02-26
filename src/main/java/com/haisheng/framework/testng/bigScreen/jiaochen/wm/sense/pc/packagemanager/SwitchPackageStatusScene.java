package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.packagemanager;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 套餐开启/关闭
 */
@Builder
public class SwitchPackageStatusScene extends BaseScene {

    /**
     * 卡券id
     */
    private final Long id;

    /**
     * 是否关闭
     */
    private final Boolean status;

    @Override
    public JSONObject getRequest() {
        JSONObject object = new JSONObject();
        object.put("status", status);
        object.put("id", id);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/package-manage/package-form/switch-package-status";
    }
}
