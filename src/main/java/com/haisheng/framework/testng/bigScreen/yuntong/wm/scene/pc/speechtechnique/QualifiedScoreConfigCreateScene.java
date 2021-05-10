package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.speechtechnique;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 3.3. 话术分页（谢）
 *
 * @author wangmin
 * @date 2021-05-08 20:23:16
 */
@Builder
public class QualifiedScoreConfigCreateScene extends BaseScene {
    /**
     * 描述 总平均分合格分数
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer score;

    /**
     * 描述 接待环节合格分数，环节使用"获取指定枚举值列表"接口获取 enum_type为 RECEPTION_LINKS
     * 是否必填 true
     * 版本 v1.0
     */
    private final JSONArray linkScores;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("score", score);
        object.put("link_scores", linkScores);
        return object;
    }

    @Override
    public String getPath() {
        return "/intelligent-control/pc/speech-technique/qualified-score-config/create";
    }
}