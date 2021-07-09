package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.consultmanagement;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 10.11. 专属服务说明配置（池）（2021-03-08）
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class DedicatedServiceExplainEditScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 true
     * 版本 -
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
        return "/account-platform/auth/consult-management/dedicated-service/explain-edit";
    }
}