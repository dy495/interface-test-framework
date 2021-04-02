package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.shop;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 25.3. 门店详情 （杨）（2021-03-23）
 *
 * @author wangmin
 * @date 2021-04-02 14:47:46
 */
@Data
public class DetailBean implements Serializable {
    /**
     * 描述 门店id
     * 版本 v1.0
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 描述 门店名称
     * 版本 v1.0
     */
    @JSONField(name = "name")
    private String name;

    /**
     * 描述 门店简称
     * 版本 v1.0
     */
    @JSONField(name = "simple_name")
    private String simpleName;

    /**
     * 描述 门店头像
     * 版本 v1.0
     */
    @JSONField(name = "avatar_url")
    private String avatarUrl;

    /**
     * 描述 门店所属区划
     * 版本 v1.0
     */
    @JSONField(name = "district_code")
    private String districtCode;

    /**
     * 描述 门店品牌列表
     * 版本 v1.0
     */
    @JSONField(name = "brand_list")
    private JSONArray brandList;

    /**
     * 描述 门店详细地址
     * 版本 v1.0
     */
    @JSONField(name = "address")
    private String address;

    /**
     * 描述 门店销售电话
     * 版本 v1.0
     */
    @JSONField(name = "sale_tel")
    private String saleTel;

    /**
     * 描述 门店售后电话
     * 版本 v1.0
     */
    @JSONField(name = "service_tel")
    private String serviceTel;

    /**
     * 描述 门店救援电话
     * 版本 v2.1
     */
    @JSONField(name = "rescue_tel")
    private String rescueTel;

    /**
     * 描述 门店经度
     * 版本 v1.0
     */
    @JSONField(name = "longitude")
    private String longitude;

    /**
     * 描述 门店纬度
     * 版本 v1.0
     */
    @JSONField(name = "latitude")
    private String latitude;

    /**
     * 描述 区划名称
     * 版本 v1.0
     */
    @JSONField(name = "district_name")
    private String districtName;

    /**
     * 描述 预约状态
     * 版本 v1.0
     */
    @JSONField(name = "appointment_status")
    private String appointmentStatus;

    /**
     * 描述 洗车状态
     * 版本 v1.0
     */
    @JSONField(name = "washing_status")
    private String washingStatus;

    /**
     * 描述 门店状态
     * 版本 v1.0
     */
    @JSONField(name = "status")
    private String status;

    /**
     * 描述 创建时间
     * 版本 v1.0
     */
    @JSONField(name = "create_time")
    private String createTime;

    /**
     * 描述 客服电话
     * 版本 v3.0
     */
    @JSONField(name = "customer_service_tel")
    private String customerServiceTel;

}