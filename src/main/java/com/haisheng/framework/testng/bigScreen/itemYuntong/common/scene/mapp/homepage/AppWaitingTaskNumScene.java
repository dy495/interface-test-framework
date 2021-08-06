package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.homepage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 5.4. 代办数量 (谢) v3.0 （2021-03-27）
 *
 * @author wangmin
 * @date 2021-08-06 16:38:23
 */
@Builder
public class AppWaitingTaskNumScene extends BaseScene {
    /**
     * 描述 待办类型 见字典表《待办类型》
     * 是否必填 true
     * 版本 1.0
     */
    private final String type;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final Boolean isAllDataAuth;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("type", type);
        object.put("isAllDataAuth", isAllDataAuth);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/home-page/waiting-task/num";
    }
}