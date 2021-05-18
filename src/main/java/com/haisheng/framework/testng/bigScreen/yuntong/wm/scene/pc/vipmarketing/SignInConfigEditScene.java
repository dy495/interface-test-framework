package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.vipmarketing;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 4.15. 签到配置修改 (池) v2.0
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class SignInConfigEditScene extends BaseScene {
    /**
     * 描述 id
     * 是否必填 true
     * 版本 v2.0
     */
    private final Long signInConfigId;

    /**
     * 描述 说明
     * 是否必填 true
     * 版本 v2.0
     */
    private final String explain;

    /**
     * 描述 积分
     * 是否必填 true
     * 版本 v2.0
     */
    private final Integer awardScore;

    /**
     * 描述 图片url
     * 是否必填 false
     * 版本 v2.0
     */
    private final String pictureUrl;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("sign_in_config_id", signInConfigId);
        object.put("explain", explain);
        object.put("award_score", awardScore);
        object.put("picture_url", pictureUrl);
        return object;
    }

    @Override
    public String getPath() {
        return "/yt/pc/vip-marketing/sign_in_config/edit";
    }
}