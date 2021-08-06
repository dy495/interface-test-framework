package com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.mapp.testdrive;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.BaseScene;
import lombok.Builder;

/**
 * 15.1. 试乘试驾申请（刘）v5.0
 *
 * @author wangmin
 * @date 2021-08-06 16:38:24
 */
@Builder
public class TestDriveApplyScene extends BaseScene {
    /**
     * 描述 请求系统
     * 是否必填 true
     * 版本 5.0
     */
    private final String apiSource;

    /**
     * 描述 唯一id
     * 是否必填 false
     * 版本 5.0
     */
    private final Integer id;

    /**
     * 描述 接待id
     * 是否必填 true
     * 版本 v5.0
     */
    private final Long receptionId;

    /**
     * 描述 证件正面地址路
     * 是否必填 true
     * 版本 5.0
     */
    private final String frontCardPath;

    /**
     * 描述 证件反面地址路
     * 是否必填 true
     * 版本 5.0
     */
    private final String backCardPath;

    /**
     * 描述 客户名称
     * 是否必填 true
     * 版本 v5.0
     */
    private final String customerName;

    /**
     * 描述 客户性别
     * 是否必填 true
     * 版本 v5.0
     */
    private final String gender;

    /**
     * 描述 证件号
     * 是否必填 true
     * 版本 v5.0
     */
    private final String cardNumber;

    /**
     * 描述 联系方式
     * 是否必填 true
     * 版本 v5.0
     */
    private final String contact;

    /**
     * 描述 城市
     * 是否必填 true
     * 版本 v5.0
     */
    private final String city;

    /**
     * 描述 地址
     * 是否必填 true
     * 版本 v5.0
     */
    private final String address;

    /**
     * 描述 试驾车系
     * 是否必填 true
     * 版本 v5.0
     */
    private final Long carTypeId;

    /**
     * 描述 试驾车型
     * 是否必填 true
     * 版本 v5.0
     */
    private final Long carModeId;

    /**
     * 描述 试驾日期
     * 是否必填 true
     * 版本 v5.0
     */
    private final String driveTime;

    /**
     * 描述 签订日期
     * 是否必填 true
     * 版本 v5.0
     */
    private final String protocolTime;

    /**
     * 描述 协议
     * 是否必填 false
     * 版本 v5.0
     */
    private final String protocol;


    @Override
    protected JSONObject getRequestBody() {
        JSONObject object = new JSONObject();
        object.put("api_source", apiSource);
        object.put("id", id);
        object.put("reception_id", receptionId);
        object.put("front_card_path", frontCardPath);
        object.put("backCardPath", backCardPath);
        object.put("customer_name", customerName);
        object.put("gender", gender);
        object.put("card_number", cardNumber);
        object.put("contact", contact);
        object.put("city", city);
        object.put("address", address);
        object.put("car_type_id", carTypeId);
        object.put("car_mode_id", carModeId);
        object.put("drive_time", driveTime);
        object.put("protocol_time", protocolTime);
        object.put("protocol", protocol);
        return object;
    }

    @Override
    public String getPath() {
        return "/car-platform/m-app/test-drive/apply";
    }
}