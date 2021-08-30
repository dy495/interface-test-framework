package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.afterspeechtechnique;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 7.4. 合格分详情（华成裕）2021-05-26
 *
 * @author wangmin
 * @date 2021-08-30 14:39:23
 */
@Builder
public class QualifiedScoreConfigDetailScene extends BaseScene {
    /**
     * 描述 适用品牌id 通过获取品牌列表接口获取
     * 是否必填 true
     * 版本 v1.0
     */
    private final Long brandId;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("brand_id", brandId);
        return object;
    }

    @Override
    public String getPath() {
        return "/intelligent-control/pc/after-speech-technique/qualified-score-config/detail";
    }
}