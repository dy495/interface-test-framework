package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.afterspecialaudio;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 6.5. 创建条件配置（华成裕）
 *
 * @author wangmin
 * @date 2021-08-30 14:39:23
 */
@Builder
public class ConfigCreateScene extends BaseScene {
    /**
     * 描述 最低时长 单位min 限制时长时必填
     * 是否必填 false
     * 版本 v1.0
     */
    private final Integer duration;

    /**
     * 描述 最低分数 限制分数时必填
     * 是否必填 false
     * 版本 v1.0
     */
    private final Integer score;

    /**
     * 描述 区域合规配置列表 限制区域合规时必填
     * 是否必填 false
     * 版本 v1.0
     */
    private final JSONArray regionLimits;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("duration", duration);
        object.put("score", score);
        object.put("region_limits", regionLimits);
        return object;
    }

    @Override
    public String getPath() {
        return "/intelligent-control/pc/after-special-audio/config/create";
    }
}