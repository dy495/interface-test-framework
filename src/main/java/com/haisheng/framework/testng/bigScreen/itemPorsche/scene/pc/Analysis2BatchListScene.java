package com.haisheng.framework.testng.bigScreen.itemPorsche.scene.pc;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * 智能批次列表接口
 *
 * @author wangmin
 */
@Builder
public class Analysis2BatchListScene extends BaseScene {

    @Builder.Default
    private final int page = 1;
    @Builder.Default
    private final int size = 10;
    private final String month;
    private final String cycleType;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("month", month);
        object.put("cycle_type", cycleType);
        return object;
    }

    @Override
    public String getPath() {
        return "/porsche/analysis2/batch/list";
    }

    @Override
    public String getIpPort() {
        return null;
    }
}
