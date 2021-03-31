package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vipmarketing;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 20.14. 签到开启或关闭
 *
 * @author wangmin
 * @date 2021-03-31 12:47:27
 */
@Builder
public class SignInConfigStartOrCloseScene extends BaseScene {
    /**
     * 描述 状态
     * 是否必填 true
     * 版本 v2.0
     */
    private final String status;

    /**
     * 描述 id
     * 是否必填 true
     * 版本 v2.0
     */
    private final Long id;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("status", status);
        object.put("id", id);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/vip-marketing/sign_in_config/start-or-close";
    }
}