package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.packagemanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 套餐开启/关闭
 */
@Builder
public class PackageFormSwitchPackageStatusScene extends BaseScene {

    /**
     * 卡券id
     */
    private final Long id;

    /**
     * 是否关闭
     */
    private final Boolean status;

    @Override
    public JSONObject getRequestBody() {
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
