package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.insurancemanagement;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 17.1. 投保客户列表 （池）（2021-03-05）
 *
 * @author wangmin
 * @date 2021-04-02 14:47:46
 */
@Data
public class CustomerPageListBean implements Serializable {
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
     * 描述 归属门店
     * 版本 v3.0
     */
    @JSONField(name = "shop_name")
    private String shopName;

    /**
     * 描述 销售顾问
     * 版本 v3.0
     */
    @JSONField(name = "sales_name")
    private String salesName;

    /**
     * 描述 客户经理
     * 版本 v3.0
     */
    @JSONField(name = "sales_name2")
    private String salesName2;

    /**
     * 描述 创建时间
     * 版本 v3.0
     */
    @JSONField(name = "create_date")
    private String createDate;

    /**
     * 描述 联系方式1
     * 版本 v3.0
     */
    @JSONField(name = "phone1")
    private String phone1;

    /**
     * 描述 联系方式2
     * 版本 v3.0
     */
    @JSONField(name = "phone2")
    private String phone2;

    /**
     * 描述 车型
     * 版本 v3.0
     */
    @JSONField(name = "model_name")
    private String modelName;

    /**
     * 描述 客户经理
     * 版本 v3.0
     */
    @JSONField(name = "sales_name1")
    private String salesName1;

    /**
     * 描述 车牌号码
     * 版本 v3.0
     */
    @JSONField(name = "plate_number")
    private String plateNumber;

    /**
     * 描述 购车年限
     * 版本 v3.0
     */
    @JSONField(name = "buy_car_year")
    private String buyCarYear;

    /**
     * 描述 底盘号
     * 版本 v3.0
     */
    @JSONField(name = "vehicle_chassis_code")
    private String vehicleChassisCode;

    /**
     * 描述 购车日期
     * 版本 v3.0
     */
    @JSONField(name = "buy_car_date")
    private String buyCarDate;

    /**
     * 描述 客户经理
     * 版本 v3.0
     */
    @JSONField(name = "insurance_date")
    private String insuranceDate;

    /**
     * 描述 投保公司
     * 版本 v3.0
     */
    @JSONField(name = "insurance_company")
    private String insuranceCompany;

    /**
     * 描述 投保金额
     * 版本 v3.0
     */
    @JSONField(name = "insurance_money")
    private String insuranceMoney;

    /**
     * 描述 唯一标识id
     * 版本 -
     */
    @JSONField(name = "id")
    private Long id;

}