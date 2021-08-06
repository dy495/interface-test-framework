package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.followup;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 3.5. app 跟进列表备注 v3 (池)(2020-03-11)
 *
 * @author wangmin
 * @date 2021-08-06 16:38:23
 */
@Builder
public class AppFollowUpRemarkV3Scene extends BaseScene {
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
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("follow_id", followId);
        object.put("remark", remark);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/follow-up/remark-v3";
    }
}