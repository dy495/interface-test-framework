package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.operation;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 27.8. 内容运营 : 活动-开启/关闭
 *
 * @author wangmin
 * @date 2021-05-18 17:04:36
 */
@Builder
public class StatusChangeScene extends BaseScene {
    /**
     * 描述 文章ID
     * 是否必填 true
     * 版本 -
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
        return "/account-platform/auth/operation/status/change";
    }
}