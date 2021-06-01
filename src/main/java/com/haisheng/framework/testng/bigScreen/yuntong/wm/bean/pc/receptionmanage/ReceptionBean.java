package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.receptionmanage;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 22.5. 接待车辆 （谢）
 *
 * @author wangmin
 * @date 2021-06-01 18:10:54
 */
@Data
public class ReceptionBean implements Serializable {
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
     * 描述 车辆到店次数
     * 版本 v1.0
     */
    @JSONField(name = "arrive_times")
    private Integer arriveTimes;

    /**
     * 描述 上次接待时间
     * 版本 v1.0
     */
    @JSONField(name = "last_arrive_time")
    private String lastArriveTime;

    /**
     * 描述 上次接待员工id
     * 版本 v1.0
     */
    @JSONField(name = "last_reception_sale_id")
    private String lastReceptionSaleId;

    /**
     * 描述 上次接待员工姓名
     * 版本 v1.0
     */
    @JSONField(name = "last_reception_sale_name")
    private String lastReceptionSaleName;

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

    /**
     * 描述 车主拥有卡券
     * 版本 v1.0
     */
    @JSONField(name = "voucher_list")
    private JSONArray voucherList;

    /**
     * 描述 卡券id
     * 版本 v1.0
     */
    @JSONField(name = "voucher_id")
    private Long voucherId;

    /**
     * 描述 卡券名称
     * 版本 v1.0
     */
    @JSONField(name = "voucher_name")
    private String voucherName;

    /**
     * 描述 卡券数量
     * 版本 v1.0
     */
    @JSONField(name = "voucher_count")
    private Long voucherCount;

}