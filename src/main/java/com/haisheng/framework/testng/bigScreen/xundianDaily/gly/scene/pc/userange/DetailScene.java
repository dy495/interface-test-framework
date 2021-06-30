package com.haisheng.framework.testng.bigScreen.xundianDaily.gly.scene.pc.userange;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 27.2. 
 *
 * @author wangmin
 * @date 2021-06-29 14:11:44
 */
@Builder
public class DetailScene extends BaseScene {
    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String referer;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final String appId;

    /**
     * 描述 nullCURRENT -(0,"集团","集团管理") BRAND -(1,"品牌","品牌管理") REGION -(2,"区域","区域管理") STORE -(3,"门店","门店管理")
     * 是否必填 false
     * 版本 -
     */
    private final String subjectKey;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("referer", referer);
        object.put("appId", appId);
        object.put("subject_key", subjectKey);
        return object;
    }

    @Override
    public String getPath() {
        return "/shop/pc/use-range/detail";
    }
}