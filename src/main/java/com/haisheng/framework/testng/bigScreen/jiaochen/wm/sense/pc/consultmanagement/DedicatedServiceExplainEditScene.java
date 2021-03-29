package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.consultmanagement;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 8.9. 专属服务页面配置（池）（2021-03-08）的接口
 *
 * @author wangmin
 * @date 2021-03-24 14:32:26
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
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("content", content);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/consult-management/dedicated-service/explain-edit";
    }
}