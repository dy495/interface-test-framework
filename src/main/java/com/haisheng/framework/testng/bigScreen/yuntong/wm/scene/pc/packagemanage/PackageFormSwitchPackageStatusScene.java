package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.packagemanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 17.2. 改变套餐状态 v1.0
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class PackageFormSwitchPackageStatusScene extends BaseScene {
    /**
     * 描述 套餐id
     * 是否必填 false
     * 版本 v1.0
     */
    private final Long id;

    /**
     * 描述 套餐状态
     * 是否必填 false
     * 版本 v1.0
     */
    private final Boolean status;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("status", status);
        return object;
    }

    @Override
    public String getPath() {
        return "/yt/pc/package-manage/package-form/switch-package-status";
    }
}