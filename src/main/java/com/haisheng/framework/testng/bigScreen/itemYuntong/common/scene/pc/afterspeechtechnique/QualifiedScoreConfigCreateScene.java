package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.afterspeechtechnique;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 7.3. 合格分配置（华成裕）2021-05-26
 *
 * @author wangmin
 * @date 2021-08-30 14:39:23
 */
@Builder
public class QualifiedScoreConfigCreateScene extends BaseScene {
    /**
     * 描述 适用品牌id 通过获取品牌列表接口获取
     * 是否必填 true
     * 版本 v1.0
     */
    private final Long brandId;

    /**
     * 描述 接待环节合格分数，环节使用"获取指定枚举值列表"接口获取 enum_type为 RECEPTION_LINKS 售后环节使用 enum_type为 AFTER_RECEPTION_LINK
     * 是否必填 true
     * 版本 v1.0
     */
    private final JSONArray linkScores;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("brand_id", brandId);
        object.put("link_scores", linkScores);
        return object;
    }

    @Override
    public String getPath() {
        return "/intelligent-control/pc/after-speech-technique/qualified-score-config/create";
    }
}