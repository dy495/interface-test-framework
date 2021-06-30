package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.activity;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 22.1. 活动审批数据 （谢）（2021-01-19）
 *
 * @author wangmin
 * @date 2021-03-31 12:50:51
 */
@Builder
public class ManageDataScene extends BaseScene {

    @Override
    public JSONObject getRequestBody() {

        return new JSONObject();
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/activity/manage/data";
    }

}