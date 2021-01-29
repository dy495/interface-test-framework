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
    private Integer id;
}
