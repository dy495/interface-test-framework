package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.record;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 6.7. 添加登陆日志（后端接口，前端无需处理）
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class LoginRecordAddScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 true
     * 版本 -
     */
    private final String saleId;

    /**
     * 描述 No comments found.
     * 是否必填 true
     * 版本 -
     */
    private final Long loginTime;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("sale_id", saleId);
        object.put("login_time", loginTime);
        return object;
    }

    @Override
    public String getPath() {
        return "/account-platform/auth/record/login-record/add";
    }
}