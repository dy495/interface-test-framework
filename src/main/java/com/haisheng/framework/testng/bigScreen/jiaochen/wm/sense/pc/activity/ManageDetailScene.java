package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.activity;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 活动管理-活动详情
 */
@Builder
public class ManageDetailScene extends BaseScene {
    private final Long  id;
    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id",id );
        return object;
    }
    @Override
    public String getPath() {
        return "/car-platform/pc/activity/manage/detail";
    }
}
