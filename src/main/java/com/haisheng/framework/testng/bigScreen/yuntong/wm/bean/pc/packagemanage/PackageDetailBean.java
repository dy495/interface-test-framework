package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.packagemanage;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 17.6. 套餐详情 v1.0
 *
 * @author wangmin
 * @date 2021-06-01 18:10:54
 */
@Data
public class PackageDetailBean implements Serializable {
    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "package_id")
    private Long packageId;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "shop_id")
    private Long shopId;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "shop_name")
    private String shopName;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "brand_name")
    private String brandName;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "package_name")
    private String packageName;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "create_time")
    private String createTime;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "price")
    private String price;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "package_price")
    private String packagePrice;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "voucher_number")
    private Integer voucherNumber;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "validity")
    private Integer validity;

    /**
     * 描述 客户使用有效期
     * 版本 v2.0
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

    /**
     * 描述 是否被作废 true是
     * 版本 v2.2
     */
    @JSONField(name = "is_invalided")
    private Boolean isInvalided;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "sold_number")
    private Integer soldNumber;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "give_number")
    private Integer giveNumber;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "creator")
    private String creator;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "status")
    private Boolean status;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "package_description")
    private String packageDescription;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "shop_list")
    private JSONArray shopList;

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
     * 描述 客服电话
     * 版本 v3.0
     */
    @JSONField(name = "customer_service_tel")
    private String customerServiceTel;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "shop_ids")
    private JSONArray shopIds;

    /**
     * 描述 No comments found.
     * 版本 -
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
     * 描述 卡券名
     * 版本 v1.0
     */
    @JSONField(name = "voucher_name")
    private String voucherName;

    /**
     * 描述 卡券数量
     * 版本 v1.0
     */
    @JSONField(name = "voucher_count")
    private Integer voucherCount;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "send_time")
    private String sendTime;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "customer_name")
    private String customerName;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "customer_id")
    private Long customerId;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "plate_list")
    private JSONArray plateList;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "plate_number")
    private String plateNumber;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "customer_phone")
    private String customerPhone;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "vehicle_chassis_code")
    private String vehicleChassisCode;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "send_number")
    private Integer sendNumber;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "send_channel")
    private String sendChannel;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "send_channel_name")
    private String sendChannelName;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "extended_insurance_year")
    private Integer extendedInsuranceYear;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "extended_insurance_copies")
    private Integer extendedInsuranceCopies;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "pay_type_name")
    private String payTypeName;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "package_belongs")
    private String packageBelongs;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "use_time")
    private String useTime;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "remark")
    private String remark;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "recommender")
    private String recommender;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "recommender_account")
    private String recommenderAccount;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "cancel_time")
    private String cancelTime;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "cancel_account")
    private String cancelAccount;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "is_exist")
    private Boolean isExist;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "subject_type")
    private String subjectType;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "subject_id")
    private Long subjectId;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "is_show_plate_list")
    private Boolean isShowPlateList;

}