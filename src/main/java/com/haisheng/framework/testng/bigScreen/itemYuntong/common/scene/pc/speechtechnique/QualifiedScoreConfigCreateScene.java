package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.speechtechnique;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 5.3. 合格分配置（谢）2021-05-26
 *
 * @author wangmin
 * @date 2021-05-31 16:28:11
 */
@Builder
public class QualifiedScoreConfigCreateScene extends BaseScene {
    /**
     * 描述 接待环节合格分数，环节使用"获取指定枚举值列表"接口获取 enum_type为 RECEPTION_LINKS
     * 是否必填 true
     * 版本 v1.0
     */
    private final JSONArray linkScores;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("link_scores", linkScores);
        return object;
    }

    @Override
    public String getPath() {
        return "/intelligent-control/pc/speech-technique/qualified-score-config/create";
    }
}