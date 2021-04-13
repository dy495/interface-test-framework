package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 30. 全局提醒（华成裕）v5.3
 *
 * @author wangmin
 * @date 2021-03-31 12:47:27
 */
@Builder
public class NoticeMessagePullScene extends BaseScene {

    @Override
    public JSONObject getRequestBody() {
        return new JSONObject();
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/notice-message/pull";
    }
}