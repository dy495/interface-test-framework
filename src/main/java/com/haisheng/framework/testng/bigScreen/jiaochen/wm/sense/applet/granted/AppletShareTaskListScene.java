package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 任务奖励
 *
 * @author 王敏
 * @date 2021-02-01
 */
@Builder
public class AppletShareTaskListScene extends BaseScene {

    @Override
    public JSONObject getRequestBody() {
        return new JSONObject();
    }

    @Override
    public String getPath() {
        return "/car-platform/applet/granted/member-center/share-task/list";
    }
}
