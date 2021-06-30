package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.staff;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 25.6. 员工状态修改 （杨航）
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
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
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("status", status);
        return object;
    }

    @Override
    public String getPath() {
        return "/yt/pc/staff/status/change";
    }
}