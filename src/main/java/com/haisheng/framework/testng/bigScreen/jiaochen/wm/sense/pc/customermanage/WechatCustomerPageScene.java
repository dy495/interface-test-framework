package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.customermanage;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 客户管理 -> 小程序客户
 */
@Builder
public class WechatCustomerPageScene extends BaseScene {
    private final String customerPhone;
    private final String activeType;
    private final String startTime;
    private final String endTime;
    private final String vipType;
    @Builder.Default
    private Integer page = 1;
    @Builder.Default
    private Integer size = 10;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("customer_phone", customerPhone);
        object.put("active_type", activeType);
        object.put("page", page);
        object.put("size", size);
        object.put("start_time", startTime);
        object.put("end_time", endTime);
        object.put("vip_type", vipType);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/customer-manage/wechat-customer/page";
    }

    @Override
    public void setSize(Integer size) {
        this.size = size;
    }

    @Override
    public void setPage(Integer page) {
        this.page = page;
    }

}
