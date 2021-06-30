package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vipmarketing;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 20.22. 分享说明(池) v2.0
 *
 * @author wangmin
 * @date 2021-03-31 12:47:27
 */
@Builder
public class ShareManagerExplainEditScene extends BaseScene {
    /**
     * 描述 内容
     * 是否必填 false
     * 版本 v2.0
     */
    private final String content;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("content", content);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/vip-marketing/share-manager/explain_edit";
    }
}