package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 会员权益列表
 *
 * @author 王敏
 * @date 2021-02-01
 */
@Builder
public class MemberCenterEquityListScene extends BaseScene {

    @Override
    public JSONObject getJSONObject() {
        return new JSONObject();
    }

    @Override
    public String getPath() {
        return "/jiaochen/applet/granted/member-center/equity/list";
    }
}
