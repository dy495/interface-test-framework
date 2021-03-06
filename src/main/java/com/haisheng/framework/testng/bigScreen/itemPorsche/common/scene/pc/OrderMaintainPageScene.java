package com.haisheng.framework.testng.bigScreen.itemPorsche.common.scene.pc;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 预约保养记录接口
 *
 * @author wangmin
 */
@Builder
public class OrderMaintainPageScene extends BaseScene {
    private final String startDay;
    private final String endDay;
    @Builder.Default
    private final int page = 1;
    @Builder.Default
    private final int size = 10;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("start_day", startDay);
        object.put("end_day", endDay);
        object.put("page", page);
        object.put("size", size);
        return object;
    }

    @Override
    public String getPath() {
        return "/porsche/order-manage/order/maintain/page";
    }


}
