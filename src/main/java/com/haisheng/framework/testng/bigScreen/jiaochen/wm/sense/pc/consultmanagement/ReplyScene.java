package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.consultmanagement;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 8.7. 回复（通用）（池）(2021-03-12)的接口
 *
 * @author wangmin
 * @date 2021-03-24 14:32:26
 */
@Builder
public class ReplyScene extends BaseScene {
    /**
     * 描述 id
     * 是否必填 false
     * 版本 -
     */
    private final Long id;

    /**
     * 描述 回复内容
     * 是否必填 false
     * 版本 v3.0
     */
    private final String content;


    @Override
    public JSONObject getRequestBody(){
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("content", content);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/consult-management/reply";
    }
}