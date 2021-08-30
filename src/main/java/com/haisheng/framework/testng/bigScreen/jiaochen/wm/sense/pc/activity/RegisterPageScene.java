package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.activity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 30.22. 活动报名列表 （谢）（2020-12-23）
 *
 * @author wangmin
 * @date 2021-08-30 14:26:55
 */
@Builder
public class RegisterPageScene extends BaseScene {
    /**
     * 描述 页码 大于0
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer page;

    /**
     * 描述 页大小 范围为[1,100]
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer size;

    /**
     * 描述 活动id
     * 是否必填 true
     * 版本 v2.0
     */
    private final Long activityId;

    /**
     * 描述 报名状态 1：待审批，101：已通过，201：已拒绝，401：已取消，501：已结束
     * 是否必填 false
     * 版本 v2.0
     */
    private final Integer status;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("activity_id", activityId);
        object.put("status", status);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/activity/manage/register/page";
    }
}