package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 接待记录列表
 *
 * @author wangmin
 * @date 2021/1/26 17:07
 */
@Data
public class ReceptionPage implements Serializable {

    /**
     * 接待顾客id
     */
    @JSONField(name = "customer_id")
    private Long customerId;

    /**
     * 接待车辆车牌号
     */
    @JSONField(name = "plate_number")
    private String plateNumber;

    /**
     * 接待id （2.0版本由reception_id改为id）
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 接待状态
     */
    @JSONField(name = "reception_status_name")
    private String receptionStatusName;

    /**
     * 注册状态
     */
    @JSONField(name = "registration_status_name")
    private String registrationStatusName;

    /**
     * 接待人
     */
    @JSONField(name = "reception_sale_name")
    private String receptionSaleName;

    /**
     * 接待时间
     */
    @JSONField(name = "reception_time")
    private String receptionTime;


    /**
     * 完成时间
     */
    @JSONField(name = "finish_time")
    private String finishTime;

    /**
     * 接待类型
     */
    @JSONField(name = "reception_type_name")
    private String receptionTypeName;

    /**
     * 门店
     */
    @JSONField(name = "shop_id")
    private Integer shopId;

    /**
     * 产值
     */
    @JSONField(name = "output_value")
    private String outputValue;

    /**
     * 里程数
     */
    @JSONField(name = "newest_miles")
    private Integer newestMiles;

    /**
     * 送修人姓名
     */
    @JSONField(name = "repair_customer_name")
    private String repairCustomerName;

}
