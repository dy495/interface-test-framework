package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.app;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 跟进
 *
 * @author wangmin
 * @date 2021/1/29 15:11
 */
@Builder
public class FollowUpCompleteScene extends BaseScene {
    private final Integer id;
    private final Integer shopId;
    private final String remark;

    @Override
    public JSONObject getRequestBody() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("shop_id", shopId);
        jsonObject.put("remark", remark);
        return jsonObject;
    }

    @Override
    public String getPath() {
        return "/jiaochen/m-app/follow-up/complete";
    }
}
