package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.consultmanagement;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 5.9. 专属服务说明配置（池）（2021-03-08）
 *
 * @author wangmin
 * @date 2021-03-31 12:36:16
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
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("content", content);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/consult-management/dedicated-service/explain-edit";
    }
}