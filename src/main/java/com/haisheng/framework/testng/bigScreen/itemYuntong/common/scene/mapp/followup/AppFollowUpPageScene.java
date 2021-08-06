package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.followup;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 3.1. app评价跟进列表（谢）（2020-12-15）
 *
 * @author wangmin
 * @date 2021-08-06 16:38:23
 */
@Builder
public class AppFollowUpPageScene extends BaseScene {
    /**
     * 描述 页大小 范围为[1,100]
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer size;

    /**
     * 描述 上次请求最后值
     * 是否必填 false
     * 版本 v1.0
     */
    private final JSONObject lastValue;

    /**
     * 描述 跟进类型
     * 是否必填 false
     * 版本 v3.0
     */
    private final String type;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("size", size);
        object.put("last_value", lastValue);
        object.put("type", type);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/follow-up/page";
    }
}