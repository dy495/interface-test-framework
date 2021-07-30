package com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.punchclockandsignrule;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 24.4. 签到或打卡
 *
 * @author wangmin
 * @date 2021-07-30 10:55:36
 */
@Builder
public class PunchClockAndSignRuleGoScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String referer;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String appId;

    /**
     * 描述 打卡或签到类型 PUNCH_CLOCK 打卡 SIGN_IN 签到
     * 是否必填 false
     * 版本 -
     */
    private final String type;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("type", type);
        return object;
    }

    @Override
    public String getPath() {
        return "/patrol/punch-clock-and-sign-rule/go";
    }
}