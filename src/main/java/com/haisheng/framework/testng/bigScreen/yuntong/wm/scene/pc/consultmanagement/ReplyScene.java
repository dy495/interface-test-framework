package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.consultmanagement;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 10.9. 回复（通用）（池）(2021-03-12)
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class ReplyScene extends BaseScene {
    /**
     * 描述 id
     * 是否必填 true
     * 版本 -
     */
    private final Long id;

    /**
     * 描述 回复内容
     * 是否必填 true
     * 版本 v3.0
     */
    private final String content;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("content", content);
        return object;
    }

    @Override
    public String getPath() {
        return "/account-platform/auth/consult-management/reply";
    }
}