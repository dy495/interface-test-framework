package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.speechtechnique;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 3.4. 合格分详情（谢）2021-05-26
 *
 * @author wangmin
 * @date 2021-06-01 16:41:05
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
        return "/intelligent-control/pc/speech-technique/qualified-score-config/detail";
    }
}