package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vouchermanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 开始/结束发放
 */
@Builder
public class ChangeProvideStatusScene extends BaseScene {
    private final Long id;

    /**
     * 是否开始发放
     */
    private final Boolean isStart;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("is_start", isStart);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/voucher-manage/change-provide-status";
    }
}
