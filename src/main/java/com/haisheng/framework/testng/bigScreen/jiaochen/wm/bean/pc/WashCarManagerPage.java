package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 洗车记录
 *
 * @author wangmin
 * @date 2021/1/27 20:02
 */
@Data
public class WashCarManagerPage implements Serializable {

    /**
     * 增发数量
     */
    @JSONField(name = "customer_vip_type_name")
    private String customerVipTypeName;

    /**
     * 电话
     */
    @JSONField(name = "phone")
    private String phone;

    /**
     * 客户类型
     */
    @JSONField(name = "customer_vip_type")
    private String customerVipType;

    /**
     * 剩余洗车次数
     */
    @JSONField(name = "wash_number")
    private Integer washNumber;

}
