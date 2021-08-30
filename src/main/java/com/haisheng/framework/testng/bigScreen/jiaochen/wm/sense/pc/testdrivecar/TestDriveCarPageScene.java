package com.haisheng.framework.testng.bigScreen.jiaochen.wm.sense.pc.testdrivecar;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 27.1. 试驾车分页 （刘）
 *
 * @author wangmin
 * @date 2021-08-30 14:26:55
 */
@Builder
public class TestDriveCarPageScene extends BaseScene {
    /**
     * 描述 页码 大于0
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer page;

    /**
     * 描述 页大小 范围为[1,100]
     * 是否必填 true
     * 版本 v1.0
     */
    private final Integer size;

    /**
     * 描述 车辆name
     * 是否必填 false
     * 版本 5.0
     */
    private final String carName;

    /**
     * 描述 车系
     * 是否必填 false
     * 版本 v5.0
     */
    private final Long carStyleId;

    /**
     * 描述 车型id
     * 是否必填 false
     * 版本 5.0
     */
    private final Long modelId;

    /**
     * 描述 车牌号
     * 是否必填 false
     * 版本 v5.0
     */
    private final String plateNumber;

    /**
     * 描述 剩余天数枚举型（传下拉选择值） 通过通用枚举接口获取，key为 REMAIN_DAY
     * 是否必填 false
     * 版本 v5.0
     */
    private final String remainingDaysEnum;

    /**
     * 描述 剩余天数
     * 是否必填 false
     * 版本 v5.0
     */
    private final Integer remainingDays;

    /**
     * 描述 底盘号
     * 是否必填 false
     * 版本 v5.0
     */
    private final String vehicleChassisCode;

    /**
     * 描述 服役开始时间（2021-07-09）
     * 是否必填 false
     * 版本 v5.0
     */
    private final String svcStartTime;

    /**
     * 描述 服役结束时间（2021-07-09）
     * 是否必填 false
     * 版本 v5.0
     */
    private final String svcEndTime;

    /**
     * 描述 状态
     * 是否必填 false
     * 版本 v5.0
     */
    private final String status;

    /**
     * 描述 No comments found.
     * 是否必填 false
     * 版本 -
     */
    private final Boolean isAllDataAuth;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("page", page);
        object.put("size", size);
        object.put("car_name", carName);
        object.put("car_style_id", carStyleId);
        object.put("model_id", modelId);
        object.put("plate_number", plateNumber);
        object.put("remaining_days_enum", remainingDaysEnum);
        object.put("remaining_days", remainingDays);
        object.put("vehicle_chassis_code", vehicleChassisCode);
        object.put("svc_start_time", svcStartTime);
        object.put("svc_end_time", svcEndTime);
        object.put("status", status);
        object.put("isAllDataAuth", isAllDataAuth);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/pc/test-drive-car/page";
    }
}