package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.testdrive;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 15.7. 试乘试驾审核操作（刘）v5.0
 *
 * @author wangmin
 * @date 2021-08-06 16:38:24
 */
@Builder
public class AppTestDriveAuditScene extends BaseScene {
    /**
     * 描述 唯一id
     * 是否必填 true
     * 版本 5.0
     */
    private final Long id;

    /**
     * 描述 客户名称
     * 是否必填 true
     * 版本 5.0
     */
    private final String customerName;

    /**
     * 描述 2/1/3 REFUSE/PASSED/CANCEL
     * 是否必填 true
     * 版本 v5.0
     */
    private final Integer status;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("customerName", customerName);
        object.put("status", status);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/test-drive/audit";
    }
}