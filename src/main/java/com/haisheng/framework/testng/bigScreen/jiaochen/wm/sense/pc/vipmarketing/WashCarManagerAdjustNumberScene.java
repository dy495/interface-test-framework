package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vipmarketing;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.base.scene.BaseScene;
import lombok.Builder;

/**
 * /jiaochen/pc/vip-marketing/wash-car-manager/adjust-number的接口
 *
 * @author wangmin
 * @date 2021-03-12 17:23:17
 */
@Builder
public class WashCarManagerAdjustNumberScene extends BaseScene {
    /**
     * 描述 调整次数
     * 是否必填 false
     * 版本 v2.0
     */
    private final String adjustNumber;

    /**
     * 描述 客户手机号
     * 是否必填 false
     * 版本 v2.0
     */
    private final String customerPhone;

    /**
     * 描述 备注
     * 是否必填 false
     * 版本 v2.0
     */
    private final String remark;


    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("adjust_number", adjustNumber);
        object.put("customer_phone", customerPhone);
        object.put("remark", remark);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/vip-marketing/wash-car-manager/adjust-number";
    }
}