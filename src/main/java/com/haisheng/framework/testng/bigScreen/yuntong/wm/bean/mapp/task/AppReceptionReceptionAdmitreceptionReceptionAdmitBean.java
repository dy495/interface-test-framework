package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.mapp.task;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 9.1. 查询接待车辆车主信息（谢）
 *
 * @author wangmin
 * @date 2021-06-01 18:39:24
 */
@Data
public class AppReceptionReceptionAdmitreceptionReceptionAdmitBean implements Serializable {
    /**
     * 描述 小程序码
     * 版本 v1.0
     */
    @JSONField(name = "er_code_url")
    private String erCodeUrl;

    /**
     * 描述 接待车牌号
     * 版本 v1.0
     */
    @JSONField(name = "plate_number")
    private String plateNumber;

    /**
     * 描述 接待车辆车主列表
     * 版本 v1.0
     */
    @JSONField(name = "customers")
    private JSONArray customers;

    /**
     * 描述 车主id
     * 版本 v1.0
     */
    @JSONField(name = "customer_id")
    private Long customerId;

    /**
     * 描述 车主姓名
     * 版本 v1.0
     */
    @JSONField(name = "customer_name")
    private String customerName;

    /**
     * 描述 车主手机号
     * 版本 v1.0
     */
    @JSONField(name = "customer_phone")
    private String customerPhone;

}