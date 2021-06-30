package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.applet.granted;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 14.10. 生成分享二维码 (池) v2.0
 *
 * @author wangmin
 * @date 2021-03-31 13:03:22
 */
@Builder
public class AppletMemberCenterShareTaskGeneratePosterScene extends BaseScene {
    /**
     * 描述 业务类型 ("0A", "售后维修"), ("04", "签到分享"), ("05", "活动分享"), ("06", "二维码分享"),
     * 是否必填 false
     * 版本 -
     */
    private final String businessType;

    /**
     * 描述 No comments found.
     * 是否必填 true
     * 版本 -
     */
    private final Long taskId;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("business_type", businessType);
        object.put("taskId", taskId);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/applet/granted/member-center/share-task/generate-Poster";
    }
}