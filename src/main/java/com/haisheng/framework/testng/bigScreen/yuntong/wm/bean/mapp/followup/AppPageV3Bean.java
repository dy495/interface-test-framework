package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.mapp.followup;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 2.3. app跟进列表（池）V3（2020-03-09）
 *
 * @author wangmin
 * @date 2021-06-01 18:10:55
 */
@Data
public class AppPageV3Bean implements Serializable {
    /**
     * 描述 总数 首次查询或刷新时返回
     * 版本 v1.0
     */
    @JSONField(name = "total")
    private Long total;

    /**
     * 描述 本次查询最后一条数据主键
     * 版本 v1.0
     */
    @JSONField(name = "last_value")
    private JSONObject lastValue;

    /**
     * 描述 跟进id
     * 版本 v2.0
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "isComplete")
    private Boolean isComplete;

    /**
     * 描述 展示列（部分接口返回列按权限展示时需要）
     * 版本 v4.0
     */
    @JSONField(name = "key_list")
    private JSONArray keyList;

    /**
     * 描述 key名称（展示列名称）
     * 版本 v4.0
     */
    @JSONField(name = "key_name")
    private String keyName;

    /**
     * 描述 key值（实际取值key）
     * 版本 v4.0
     */
    @JSONField(name = "key_value")
    private String keyValue;

    /**
     * 描述 返回的结果list
     * 版本 v1.0
     */
    @JSONField(name = "list")
    private JSONArray list;

    /**
     * 描述 根据不同type取不同信息 RENEWAL_INSURANCE("续保咨询") renew_insurance SALES("专属服务咨询(专属销售顾问)") sales AFTER_SALES("专属服务咨询(专属售后顾问)") after_sales USED_CAR("二手车咨询") used_car USED_CAR_ASSESS("二手车评估") used_car_assess ONLINE_EXPERTS("在线专家咨询") online_experts BUY_CAR_EVALUATE ("购车评价") buy_car_evaluate_info MAINTAIN_EVALUATE("保养评价") maintain_evaluate_info REPAIR_EVALUATE("维修评价") repair_evaluate_info PRE_RECEPTION_EVALUATE("销售接待评价") pre_reception_evaluate_info
     * 版本 -
     */
    @JSONField(name = "type")
    private String type;

    /**
     * 描述 卡片title
     * 版本 v3.0
     */
    @JSONField(name = "title")
    private String title;

    /**
     * 描述 购车评价
     * 版本 v3.0
     */
    @JSONField(name = "buy_car_evaluate_info")
    private JSONObject buyCarEvaluateInfo;

    /**
     * 描述 门店id
     * 版本 v3.0
     */
    @JSONField(name = "shop_id")
    private Long shopId;

    /**
     * 描述 客户名称
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
     * 描述 评价分数
     * 版本 v3.0
     */
    @JSONField(name = "score")
    private Integer score;

    /**
     * 描述 评价标签
     * 版本 v3.0
     */
    @JSONField(name = "labels")
    private JSONArray labels;

    /**
     * 描述 评价内容
     * 版本 v3.0
     */
    @JSONField(name = "suggestion")
    private String suggestion;

    /**
     * 描述 备注
     * 版本 v3.0
     */
    @JSONField(name = "remark")
    private String remark;

    /**
     * 描述 评价时间
     * 版本 v2.0
     */
    @JSONField(name = "evaluate_time")
    private String evaluateTime;

    /**
     * 描述 预约车辆品牌logo图片地址
     * 版本 v1.0
     */
    @JSONField(name = "car_logo_url")
    private String carLogoUrl;

    /**
     * 描述 保养评价
     * 版本 v3.0
     */
    @JSONField(name = "maintain_evaluate_info")
    private JSONObject maintainEvaluateInfo;

    /**
     * 描述 车牌号
     * 版本 v3.0
     */
    @JSONField(name = "plate_number")
    private String plateNumber;

    /**
     * 描述 维修评价
     * 版本 v3.0
     */
    @JSONField(name = "repair_evaluate_info")
    private JSONObject repairEvaluateInfo;

    /**
     * 描述 故障描述
     * 版本 v3.0
     */
    @JSONField(name = "failure_description")
    private String failureDescription;

    /**
     * 描述 销售接待评价
     * 版本 v3.0
     */
    @JSONField(name = "pre_reception_evaluate_info")
    private JSONObject preReceptionEvaluateInfo;

    /**
     * 描述 续保咨询
     * 版本 v3.0
     */
    @JSONField(name = "renew_insurance")
    private JSONObject renewInsurance;

    /**
     * 描述 是否回复 true 为已回复 false 为未回复
     * 版本 v3.0
     */
    @JSONField(name = "is_reply")
    private Boolean isReply;

    /**
     * 描述 是否超时
     * 版本 v2.0
     */
    @JSONField(name = "is_overtime")
    private Boolean isOvertime;

    /**
     * 描述 咨询时间
     * 版本 v3.0
     */
    @JSONField(name = "consult_date")
    private String consultDate;

    /**
     * 描述 联系电话
     * 版本 v3.0
     */
    @JSONField(name = "customer_phone2")
    private String customerPhone2;

    /**
     * 描述 咨询内容
     * 版本 v3.0
     */
    @JSONField(name = "content")
    private String content;

    /**
     * 描述 品牌
     * 版本 v3.0
     */
    @JSONField(name = "brand_name")
    private String brandName;

    /**
     * 描述 车型
     * 版本 v3.0
     */
    @JSONField(name = "model_name")
    private String modelName;

    /**
     * 描述 购车日期
     * 版本 v3.0
     */
    @JSONField(name = "buy_car_time")
    private String buyCarTime;

    /**
     * 描述 在线专家
     * 版本 v3.0
     */
    @JSONField(name = "online_experts")
    private JSONObject onlineExperts;

    /**
     * 描述 系统电话
     * 版本 v3.0
     */
    @JSONField(name = "customer_phone1")
    private String customerPhone1;

    /**
     * 描述 车系名称
     * 版本 v3.0
     */
    @JSONField(name = "style_name")
    private String styleName;

    /**
     * 描述 专属售后顾问
     * 版本 v3.0
     */
    @JSONField(name = "after_sales")
    private JSONObject afterSales;

    /**
     * 描述 专属销售顾问
     * 版本 v3.0
     */
    @JSONField(name = "sales")
    private JSONObject sales;

    /**
     * 描述 意向车系
     * 版本 v3.0
     */
    @JSONField(name = "intention_car_style_name")
    private String intentionCarStyleName;

    /**
     * 描述 二手车咨询
     * 版本 v3.0
     */
    @JSONField(name = "used_car")
    private JSONObject usedCar;

    /**
     * 描述 车辆年款
     * 版本 v3.0
     */
    @JSONField(name = "year")
    private String year;

    /**
     * 描述 里程
     * 版本 v3.0
     */
    @JSONField(name = "mileage")
    private Integer mileage;

    /**
     * 描述 颜色
     * 版本 -
     */
    @JSONField(name = "color_name")
    private String colorName;

    /**
     * 描述 价格
     * 版本 v3.0
     */
    @JSONField(name = "price")
    private Double price;

    /**
     * 描述 首付
     * 版本 v3.0
     */
    @JSONField(name = "down_payment")
    private Double downPayment;

    /**
     * 描述 二手车评估
     * 版本 v3.0
     */
    @JSONField(name = "used_car_assess")
    private JSONObject usedCarAssess;

    /**
     * 描述 车辆品牌
     * 版本 v3.0
     */
    @JSONField(name = "car_brand_name")
    private String carBrandName;

    /**
     * 描述 评估车型
     * 版本 v3.0
     */
    @JSONField(name = "assess_model_name")
    private String assessModelName;

    /**
     * 描述 购车日期
     * 版本 v3.0
     */
    @JSONField(name = "buy_car_date")
    private String buyCarDate;

}