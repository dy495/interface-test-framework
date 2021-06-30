package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vipmarketing;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 权益列表
 *
 * @author wangmin
 * @date 2021/2/1 17:02
 */
@Builder
public class EquityPageScene extends BaseScene {
    @Builder.Default
    private final Integer size = 10;
    @Builder.Default
    private final Integer page = 1;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("size", size);
        object.put("page", page);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/vip-marketing/equity/page";
    }
}
