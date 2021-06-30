package com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.vipmarketing;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemPorsche.casedaily.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 4.12. 洗车次数调整 (池) v2.0
 *
 * @author wangmin
 * @date 2021-05-18 17:04:35
 */
@Builder
public class WashCarManagerAdjustNumberScene extends BaseScene {
    /**
     * 描述 调整次数
     * 是否必填 false
     * 版本 v2.0
     */
    private final Integer adjustNumber;

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
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("adjust_number", adjustNumber);
        object.put("customer_phone", customerPhone);
        object.put("remark", remark);
        return object;
    }

    @Override
    public String getPath() {
        return "/yt/pc/vip-marketing/wash-car-manager/adjust-number";
    }
}