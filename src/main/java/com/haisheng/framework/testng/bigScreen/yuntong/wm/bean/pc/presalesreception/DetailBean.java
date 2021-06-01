package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.presalesreception;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 21.3. 接待详情（谢）v3.0 （2021-03-16）
 *
 * @author wangmin
 * @date 2021-06-01 18:10:54
 */
@Data
public class DetailBean implements Serializable {
    /**
     * 描述 接待id
     * 版本 v3.0
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 描述 接待门店id
     * 版本 v3.0
     */
    @JSONField(name = "shop_id")
    private Long shopId;

    /**
     * 描述 顾客归属 见字典表《顾客归属类型》
     * 版本 v3.0
     */
    @JSONField(name = "belong_type")
    private Integer belongType;

    /**
     * 描述 接待顾客Id
     * 版本 v4.0
     */
    @JSONField(name = "customer_id")
    private Long customerId;

    /**
     * 描述 接待顾客名称
     * 版本 v3.0
     */
    @JSONField(name = "customer_name")
    private String customerName;

    /**
     * 描述 顾客头像
     * 版本 v3.0
     */
    @JSONField(name = "customer_avatar")
    private String customerAvatar;

    /**
     * 描述 接待顾客电话
     * 版本 v3.0
     */
    @JSONField(name = "customer_phone")
    private String customerPhone;

    /**
     * 描述 接待顾客性别 0女 1男
     * 版本 v3.0
     */
    @JSONField(name = "customer_gender")
    private Integer customerGender;

    /**
     * 描述 所属顾问
     * 版本 v3.0
     */
    @JSONField(name = "belong_sale_name")
    private String belongSaleName;

    /**
     * 描述 售后接待员工id
     * 版本 v3.0
     */
    @JSONField(name = "receptor_id")
    private String receptorId;

    /**
     * 描述 接待顾问
     * 版本 v3.0
     */
    @JSONField(name = "reception_sale_name")
    private String receptionSaleName;

    /**
     * 描述 接待时间
     * 版本 v3.0
     */
    @JSONField(name = "reception_time")
    private String receptionTime;

    /**
     * 描述 意向车型
     * 版本 v3.0
     */
    @JSONField(name = "car_model")
    private String carModel;

    /**
     * 描述 预计购车时间
     * 版本 v3.0
     */
    @JSONField(name = "estimated_buy_time")
    private String estimatedBuyTime;

    /**
     * 描述 客户等级
     * 版本 v3.0
     */
    @JSONField(name = "customer_level_name")
    private String customerLevelName;

    /**
     * 描述 是否超时
     * 版本 v3.0
     */
    @JSONField(name = "is_overtime")
    private Boolean isOvertime;

    /**
     * 描述 预约客户标识，空则不展示
     * 版本 v3.0
     */
    @JSONField(name = "label")
    private String label;

    /**
     * 描述 是否可变更接待
     * 版本 v3.0
     */
    @JSONField(name = "is_can_change_receptor")
    private Boolean isCanChangeReceptor;

    /**
     * 描述 是否完成，完成后、确认购车、取消、完成按钮不可点击（或不出现）
     * 版本 v3.0
     */
    @JSONField(name = "is_finish")
    private Boolean isFinish;

    /**
     * 描述 客户创建日期
     * 版本 v3.0
     */
    @JSONField(name = "create_time")
    private String createTime;

    /**
     * 描述 意向品牌id
     * 版本 v3.0
     */
    @JSONField(name = "brand_id")
    private Long brandId;

    /**
     * 描述 意向车系id
     * 版本 v3.0
     */
    @JSONField(name = "car_style_id")
    private Long carStyleId;

    /**
     * 描述 意向车型id
     * 版本 v3.0
     */
    @JSONField(name = "car_model_id")
    private Long carModelId;

    /**
     * 描述 意向品牌名
     * 版本 v3.0
     */
    @JSONField(name = "brand_name")
    private String brandName;

    /**
     * 描述 意向车系名
     * 版本 v3.0
     */
    @JSONField(name = "car_style_name")
    private String carStyleName;

    /**
     * 描述 意向车型名
     * 版本 v3.0
     */
    @JSONField(name = "car_model_name")
    private String carModelName;

    /**
     * 描述 顾客智能标签
     * 版本 v4.0
     */
    @JSONField(name = "customer_labels")
    private JSONArray customerLabels;

    /**
     * 描述 标签类型
     * 版本 v4.0
     */
    @JSONField(name = "label_type")
    private String labelType;

    /**
     * 描述 标签值
     * 版本 v4.0
     */
    @JSONField(name = "label_value")
    private String labelValue;

    /**
     * 描述 标签名称
     * 版本 -
     */
    @JSONField(name = "label_name")
    private String labelName;

    /**
     * 描述 顾客备注记录
     * 版本 v3.0
     */
    @JSONField(name = "remarks")
    private JSONArray remarks;

    /**
     * 描述 备注信息
     * 版本 v3.0
     */
    @JSONField(name = "remark")
    private String remark;

    /**
     * 描述 备注时间
     * 版本 v3.0
     */
    @JSONField(name = "time")
    private String time;

    /**
     * 描述 销售顾问
     * 版本 V4.0
     */
    @JSONField(name = "sales_name")
    private String salesName;

    /**
     * 描述 门店名称
     * 版本 V4.0
     */
    @JSONField(name = "shop_name")
    private String shopName;

    /**
     * 描述 购车记录
     * 版本 v3.0
     */
    @JSONField(name = "order_list")
    private JSONArray orderList;

    /**
     * 描述 记录创建时间
     * 版本 v4.0
     */
    @JSONField(name = "record_create_date")
    private Long recordCreateDate;

    /**
     * 描述 销售顾问
     * 版本 v3.0
     */
    @JSONField(name = "sale_name")
    private String saleName;

    /**
     * 描述 购车时间
     * 版本 v3.0
     */
    @JSONField(name = "buy_car_time")
    private String buyCarTime;

    /**
     * 描述 底盘号
     * 版本 v3.0
     */
    @JSONField(name = "vin")
    private String vin;

    /**
     * 描述 车牌号列表
     * 版本 v4.0
     */
    @JSONField(name = "plate_numbers")
    private JSONArray plateNumbers;

}