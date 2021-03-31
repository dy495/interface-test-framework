package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.followup;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 1.5. app 跟进列表备注 v3 (池)(2020-03-11)
 *
 * @author wangmin
 * @date 2021-03-31 13:03:23
 */
@Builder
public class AppRemarkV3Scene extends BaseScene {
    /**
     * 描述 跟进id
     * 是否必填 true
     * 版本 v3.0
     */
    private final Long followId;

    /**
     * 描述 备注内容
     * 是否必填 true
     * 版本 v3.0
     */
    private final String remark;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("follow_id", followId);
        object.put("remark", remark);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/m-app/follow-up/remark-v3";
    }
}