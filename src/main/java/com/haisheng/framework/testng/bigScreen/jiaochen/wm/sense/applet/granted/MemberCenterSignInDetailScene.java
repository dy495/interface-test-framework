package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 小程序签到详情
 *
 * @author wangmin
 * @date 2021-02-02
 */
@Builder
public class MemberCenterSignInDetailScene extends BaseScene {

    @Override
    public JSONObject getRequest() {
        return new JSONObject();
    }

    @Override
    public String getPath() {
        return "/jiaochen/applet/granted/member-center/sign-in-detail";
    }
}
