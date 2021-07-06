package com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.auth.staff;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 12.3. 删除员工
 *
 * @author wangmin
 * @date 2021-04-01 14:22:36
 */
@Builder
public class DeleteScene extends BaseScene {
    /**
     * 描述 账号id
     * 是否必填 true
     * 版本 v1.0
     */
    private final String id;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        return object;
    }

    @Override
    public String getPath() {
        return "/risk-control/auth/staff/delete";
    }
}