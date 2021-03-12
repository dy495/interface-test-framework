package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.packagemanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * /jiaochen/pc/package-manage/package-form/switch-package-status的接口
 *
 * @author wangmin
 * @date 2021-03-12 17:23:18
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
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("status", status);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/package-manage/package-form/switch-package-status";
    }
}