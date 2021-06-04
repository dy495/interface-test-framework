package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.specialaudio;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 4.5. 创建条件配置（谢）
 *
 * @author wangmin
 * @date 2021-05-31 16:28:11
 */
@Builder
public class ConfigCreateScene extends BaseScene {
    /**
     * 描述 是否时长限制
     * 是否必填 true
     * 版本 v1.0
     */
    private final Boolean limitDuration;

    /**
     * 描述 最低时长 单位min 限制时长时必填
     * 是否必填 false
     * 版本 v1.0
     */
    private final Integer duration;

    /**
     * 描述 是否分数限制
     * 是否必填 true
     * 版本 v1.0
     */
    private final Boolean limitScore;

    /**
     * 描述 最低分数 限制分数时必填
     * 是否必填 false
     * 版本 v1.0
     */
    private final Integer score;

    /**
     * 描述 是否区域合规限制
     * 是否必填 true
     * 版本 v1.0
     */
    private final Boolean limitRegion;

    /**
     * 描述 区域合规配置列表 限制区域合规时必填
     * 是否必填 false
     * 版本 v1.0
     */
    private final JSONArray regionLimits;

    /**
     * 描述 适用品牌id列表 通过获取品牌列表接口获取
     * 是否必填 true
     * 版本 v1.0
     */
    private final JSONArray brandIds;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("limit_duration", limitDuration);
        object.put("duration", duration);
        object.put("limit_score", limitScore);
        object.put("score", score);
        object.put("limit_region", limitRegion);
        object.put("regionLimits", regionLimits);
        object.put("brand_ids", brandIds);
        return object;
    }

    @Override
    public String getPath() {
        return "/intelligent-control/pc/special-audio/config/create";
    }
}