package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.followup;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 4.3. app 跟进列详情 v4 (池)(2020-03-11)
 *
 * @author wangmin
 * @date 2021-08-06 16:38:23
 */
@Builder
public class AppFollowUpFollowDetailScene extends BaseScene {
    /**
     * 描述 唯一id
     * 是否必填 true
     * 版本 v2.0
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
        return "/car-platform/m-app/follow-up/follow-detail";
    }
}