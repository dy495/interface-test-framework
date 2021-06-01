package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.receptionmanage;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 22.1. 接待分页 （池）（2021-03-29）
 *
 * @author wangmin
 * @date 2021-06-01 18:10:54
 */
@Data
public class PageBean implements Serializable {
    /**
     * 描述 当前页
     * 版本 v1.0
     */
    @JSONField(name = "page")
    private Integer page;

    /**
     * 描述 当前页的数量
     * 版本 v1.0
     */
    @JSONField(name = "size")
    private Integer size;

    /**
     * 描述 每页的数量
     * 版本 v1.0
     */
    @JSONField(name = " page_size")
    private Integer  pageSize;

    /**
     * 描述 总数
     * 版本 v1.0
     */
    @JSONField(name = "total")
    private Long total;

    /**
     * 描述 总页数
     * 版本 v1.0
     */
    @JSONField(name = "pages")
    private Integer pages;

    /**
     * 描述 详细数据列表
     * 版本 v1.0
     */
    @JSONField(name = "list")
    private JSONArray list;

    /**
     * 描述 接待id （2.0版本由reception_id改为id）
     * 版本 v2.0
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 描述 门店id
     * 版本 v1.0
     */
    @JSONField(name = "shop_id")
    private Long shopId;

    /**
     * 描述 接待顾客id
     * 版本 v1.0
     */
    @JSONField(name = "customer_id")
    private Long customerId;

    /**
     * 描述 门店名称
     * 版本 v1.0
     */
    @JSONField(name = "shop_name")
    private String shopName;

    /**
     * 描述 接待车辆品牌名称
     * 版本 v1.0
     */
    @JSONField(name = "brand_name")
    private String brandName;

    /**
     * 描述 接待车辆车系名
     * 版本 v1.0
     */
    @JSONField(name = "car_style_name")
    private String carStyleName;

    /**
     * 描述 接待车辆车牌号
     * 版本 v1.0
     */
    @JSONField(name = "plate_number")
    private String plateNumber;

    /**
     * 描述 客户姓名
     * 版本 v1.0
     */
    @JSONField(name = "customer_name")
    private String customerName;

    /**
     * 描述 客户联系方式
     * 版本 v1.0
     */
    @JSONField(name = "customer_phone")
    private String customerPhone;

    /**
     * 描述 接待状态
     * 版本 v1.0
     */
    @JSONField(name = "reception_status")
    private Integer receptionStatus;

    /**
     * 描述 接待状态名称
     * 版本 v1.0
     */
    @JSONField(name = "reception_status_name")
    private String receptionStatusName;

    /**
     * 描述 注册状态
     * 版本 v1.0
     */
    @JSONField(name = "registration_status")
    private Integer registrationStatus;

    /**
     * 描述 注册状态名称
     * 版本 v1.0
     */
    @JSONField(name = "registration_status_name")
    private String registrationStatusName;

    /**
     * 描述 接待人
     * 版本 v1.0
     */
    @JSONField(name = "reception_sale_name")
    private String receptionSaleName;

    /**
     * 描述 接待人id
     * 版本 v2.0
     */
    @JSONField(name = "reception_sale_id")
    private String receptionSaleId;

    /**
     * 描述 接待时间
     * 版本 v2.0
     */
    @JSONField(name = "reception_time")
    private String receptionTime;

    /**
     * 描述 完成时间
     * 版本 v1.0
     */
    @JSONField(name = "finish_time")
    private String finishTime;

    /**
     * 描述 取消时间
     * 版本 v1.0
     */
    @JSONField(name = "cancel_time")
    private String cancelTime;

    /**
     * 描述 取消账号
     * 版本 v1.0
     */
    @JSONField(name = "cancel_account")
    private String cancelAccount;

    /**
     * 描述 接待类型
     * 版本 v1.0
     */
    @JSONField(name = "reception_type")
    private Integer receptionType;

    /**
     * 描述 接待类型描述
     * 版本 v1.0
     */
    @JSONField(name = "reception_type_name")
    private String receptionTypeName;

    /**
     * 描述 预约到店时间
     * 版本 v1.0
     */
    @JSONField(name = "appointment_time")
    private String appointmentTime;

    /**
     * 描述 评价时间
     * 版本 v1.0
     */
    @JSONField(name = "evaluate_time")
    private String evaluateTime;

    /**
     * 描述 是否可完成接待
     * 版本 v2.1
     */
    @JSONField(name = "is_can_finish")
    private Boolean isCanFinish;

    /**
     * 描述 接待来源类型
     * 版本 -
     */
    @JSONField(name = "reception_source_type_name")
    private String receptionSourceTypeName;

    /**
     * 描述 接待车辆车型名
     * 版本 v3.0
     */
    @JSONField(name = "car_model_name")
    private String carModelName;

}