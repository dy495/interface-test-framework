package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.vipmarketing;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 4.14. 签到开启或关闭
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
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
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("status", status);
        object.put("id", id);
        return object;
    }

    @Override
    public String getPath() {
        return "/yt/pc/vip-marketing/sign_in_config/start-or-close";
    }
}