package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.specialaudio;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 4.4. 特殊音频翻译（谢）
 *
 * @author wangmin
 * @date 2021-05-31 16:28:11
 */
@Builder
public class SpecialAudioTranslateScene extends BaseScene {
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
        return "/intelligent-control/pc/special-audio/translate";
    }
}