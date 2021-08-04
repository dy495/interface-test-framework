package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.activity;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 活动管理-报名列表
 */
@Builder
public class ManageRegisterPageScene extends BaseScene  {
    private final Integer page;
    private final Integer size;
    private final Long activityId;
    private final Integer status;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page",page );
        object.put("size",size );
        object.put("activity_id",activityId );
        object.put("status",status );
        return object;
    }
    @Override
    public String getPath() {
        return "/car-platform/pc/activity/manage/register/page";
    }


}
