package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.afterspecialaudio;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 6.4. 特殊音频翻译（华成裕）
 *
 * @author wangmin
 * @date 2021-08-30 14:39:23
 */
@Builder
public class AfterSpecialAudioTranslateScene extends BaseScene {
    /**
     * 描述 id
     * 是否必填 true
     * 版本 v1.0
     */
    private final Long id;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        return object;
    }

    @Override
    public String getPath() {
        return "/intelligent-control/pc/after-special-audio/translate";
    }
}