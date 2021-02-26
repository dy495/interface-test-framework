package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.vipmarketing;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.BaseScene;
import lombok.Builder;

/**
 * 增加洗车次数
 *
 * @author wangmin
 * @date 2021/2/1 17:02
 */
@Builder
public class AdjustNumberScene extends BaseScene {
    /**
     * 客户姓名
     */
    private final String customerPhone;

    /**
     * 电话
     */
    private final String adjustNumber;

    /**
     * 客户类型
     */
    private final String remark;

    @Override
    public JSONObject getRequest() {
        JSONObject object = new JSONObject();
        object.put("customer_phone", customerPhone);
        object.put("adjust_number", adjustNumber);
        object.put("remark", remark);
        return object;
    }

    @Override
    public String getPath() {
        return "/jiaochen/pc/vip-marketing/wash-car-manager/adjust-number";
    }
}
