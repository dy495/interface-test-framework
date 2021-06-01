package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.receptionmanage;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 22.12. 套餐列表（张小龙）（2020-12-17）
 *
 * @author wangmin
 * @date 2021-06-01 18:10:54
 */
@Data
public class PackageListBean implements Serializable {
    /**
     * 描述 数据列表
     * 版本 v1.0
     */
    @JSONField(name = "list")
    private JSONArray list;

    /**
     * 描述 套餐id
     * 版本 v1.0
     */
    @JSONField(name = "package_id")
    private Long packageId;

    /**
     * 描述 套餐名
     * 版本 v1.0
     */
    @JSONField(name = "package_name")
    private String packageName;

    /**
     * 描述 套餐价格
     * 版本 -
     */
    @JSONField(name = "package_price")
    private String packagePrice;

    /**
     * 描述 客户使用有效期
     * 版本 -
     */
    @JSONField(name = "customer_use_validity")
    private Integer customerUseValidity;

    /**
     * 描述 卡券有效期类型 选择发送卡券时必填，1：时间段，2：有效天数
     * 版本 v2.0
     */
    @JSONField(name = "expire_type")
    private Integer expireType;

    /**
     * 描述 有效期
     * 版本 -
     */
    @JSONField(name = "expiry_date")
    private Integer expiryDate;

    /**
     * 描述 有效期开始时间
     * 版本 v2.2
     */
    @JSONField(name = "begin_use_time")
    private String beginUseTime;

    /**
     * 描述 有效期结束时间
     * 版本 v2.2
     */
    @JSONField(name = "end_use_time")
    private String endUseTime;

}