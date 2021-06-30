package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.activity;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 活动管理-报名活动数据
 */
@Builder
public class ManageRegisterDataScene extends BaseScene {
    private final Long  activityId;
    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("activity_id",activityId );
        return object;
    }
    @Override
    public String getPath() {
        return "/jiaochen/pc/activity/manage/register/data";
    }


}
