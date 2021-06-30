package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.mapp.homepage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 2.7. 读取代办 (谢) v3.0 （2021-03-27）
 *
 * @author wangmin
 * @date 2021-04-13 20:17:13
 */
@Builder
public class AppWaitingTaskReadv3Scene extends BaseScene {
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
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("type", type);
        object.put("isAllDataAuth", isAllDataAuth);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/m-app/home-page/waiting-task/readV3";
    }
}