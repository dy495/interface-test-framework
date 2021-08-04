package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.staff;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 34.6. 员工状态修改 （杨航）
 *
 * @author wangmin
 * @date 2021-03-31 12:47:27
 */
@Builder
public class StatusChangeScene extends BaseScene {
    /**
     * 描述 账号uid
     * 是否必填 true
     * 版本 v1.0
     */
    private final String id;

    /**
     * 描述 账号状态
     * 是否必填 true
     * 版本 v1.0
     */
    private final String status;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("status", status);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/staff/status/change";
    }
}