package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.specialaudio;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 5.2. 特殊音频详情（谢）
 *
 * @author wangmin
 * @date 2021-05-08 20:23:16
 */
@Builder
public class DetailScene extends BaseScene {
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
        return "/intelligent-control/pc/special-audio/detail";
    }
}