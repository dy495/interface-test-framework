package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.vipmarketing;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 4.22. 分享说明(池) v2.0
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
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
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("content", content);
        return object;
    }

    @Override
    public String getPath() {
        return "/account-platform/auth/vip-marketing/share-manager/explain_edit";
    }
}