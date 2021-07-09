package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.sensitivewords;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 3.8. 敏感词开启/关闭（谢）
 *
 * @author wangmin
 * @date 2021-05-31 16:28:11
 */
@Builder
public class ChangeStatusScene extends BaseScene {
    /**
     * 描述 敏感词id
     * 是否必填 true
     * 版本 v1.0
     */
    private final Long id;

    /**
     * 描述 是否开启
     * 是否必填 true
     * 版本 v1.0
     */
    private final Boolean open;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("open", open);
        return object;
    }

    @Override
    public String getPath() {
        return "/intelligent-control/pc/sensitive-words/change-status";
    }
}