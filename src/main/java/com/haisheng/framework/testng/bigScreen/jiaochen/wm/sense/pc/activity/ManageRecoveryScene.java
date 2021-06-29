package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.activity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 26.16. 恢复活动 （谢）v3.0（2021-04-02）
 *
 * @author wangmin
 * @date 2021-04-13 20:17:12
 */
@Builder
public class ManageRecoveryScene extends BaseScene {
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
        return "/jiaochen/pc/activity/manage/recovery";
    }
}