package com.haisheng.framework.testng.bigScreen.itemPorsche.common.scene.app;

import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 展厅客户查询接口
 *
 * @author wangmin
 */
@Builder
public class CustomerPageScene extends BaseScene {
    private final String customerName;
    private final String customerPhone;
    private final String startTime;
    private final String endTime;
    @Builder.Default
    private final String page = "1";
    @Builder.Default
    private final String size = "10";
    private final String vehicleChassisCode;

    @Override
    public JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("customer_name", customerName);
        object.put("customer_phone", customerPhone);
        object.put("end_time", endTime);
        object.put("page", page);
        object.put("size", size);
        object.put("start_time", startTime);
        object.put("vehicle_chassis_code", vehicleChassisCode);
        return object;
    }

    @Override
    public String getPath() {
        return "/porsche/app/customer/page";
    }


}
