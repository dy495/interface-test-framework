package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.activity;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 26.17. 下架活动 （谢）v3.0（2021-04-02）
 *
 * @author wangmin
 * @date 2021-04-13 20:17:12
 */
@Builder
public class ManageOfflineScene extends BaseScene {
    /**
     * 描述 活动id
     * 是否必填 true
     * 版本 v2.0
     */
    private final Long id;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/activity/manage/offline";
    }
}