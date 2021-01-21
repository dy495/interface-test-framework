package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 开始/结束发放
 */
@Builder
public class ChangeProvideStatus extends BaseScene {
    private final Integer id;
    private final Boolean isStart;

    @Override
    public JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("isStart", isStart);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/voucher-manage/change-provide-status";
    }
}
