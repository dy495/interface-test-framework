package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.presalesreception;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 21.4. 查询手机号客户信息（谢）v3.0 （2021-03-16）
 *
 * @author wangmin
 * @date 2021-06-01 18:10:54
 */
@Data
public class AdmitBean implements Serializable {
    /**
     * 描述 小程序码
     * 版本 v3.0
     */
    @JSONField(name = "er_code_url")
    private String erCodeUrl;

    /**
     * 描述 客户id
     * 版本 v3.0
     */
    @JSONField(name = "customer_id")
    private Long customerId;

    /**
     * 描述 客户姓名
     * 版本 v3.0
     */
    @JSONField(name = "customer_name")
    private String customerName;

    /**
     * 描述 客户手机号
     * 版本 v3.0
     */
    @JSONField(name = "customer_phone")
    private String customerPhone;

    /**
     * 描述 所属销售
     * 版本 v3.0
     */
    @JSONField(name = "service_sale_name")
    private String serviceSaleName;

    /**
     * 描述 上次到店时间
     * 版本 v3.0
     */
    @JSONField(name = "last_arrive_time")
    private String lastArriveTime;

    /**
     * 描述 客户级别
     * 版本 v3.0
     */
    @JSONField(name = "customer_level_name")
    private String customerLevelName;

}