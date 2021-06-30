package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vipmarketing;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 签到配置修改
 *
 * @author wangmin
 * @date 2021/2/1 17:02
 */
@Builder
public class SignInConfigEditScene extends BaseScene {

    /**
     * id
     */
    private final Integer signInConfigId;

    /**
     * 说明
     */
    private final String explain;

    /**
     * 积分
     */
    private final Integer awardScore;

    /**
     * 图片url
     */
    private final String pictureUrl;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("sign_in_config_id", signInConfigId);
        object.put("explain", explain);
        object.put("award_score", awardScore);
        object.put("picture_url", pictureUrl);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/vip-marketing/sign_in_config/edit";
    }
}
