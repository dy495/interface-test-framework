package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.customermanage;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 13.25. 流失客户维修记录 (杨) v3.0
 *
 * @author wangmin
 * @date 2021-06-01 18:10:54
 */
@Data
public class LossCustomerRepairPageBean implements Serializable {
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
     * 描述 唯一标识
     * 版本 v1.0
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 描述 开单日期
     * 版本 v1.0
     */
    @JSONField(name = "make_order_date")
    private String makeOrderDate;

    /**
     * 描述 服务单号
     * 版本 v1.0
     */
    @JSONField(name = "service_order_id")
    private String serviceOrderId;

    /**
     * 描述 工单类型
     * 版本 v1.0
     */
    @JSONField(name = "work_order_type")
    private String workOrderType;

    /**
     * 描述 维修类型
     * 版本 v1.0
     */
    @JSONField(name = "repair_type_name")
    private String repairTypeName;

    /**
     * 描述 服务顾问名称
     * 版本 v1.0
     */
    @JSONField(name = "sale_name")
    private String saleName;

    /**
     * 描述 送修人姓名
     * 版本 v1.0
     */
    @JSONField(name = "repair_customer_name")
    private String repairCustomerName;

    /**
     * 描述 送修人联系方式
     * 版本 v1.0
     */
    @JSONField(name = "repair_customer_phone")
    private String repairCustomerPhone;

    /**
     * 描述 送修人性别
     * 版本 v1.0
     */
    @JSONField(name = "sex")
    private String sex;

    /**
     * 描述 里程数/km
     * 版本 v1.0
     */
    @JSONField(name = "newest_miles")
    private Integer newestMiles;

    /**
     * 描述 产值
     * 版本 v1.0
     */
    @JSONField(name = "output_value")
    private String outputValue;

    /**
     * 描述 交车时间
     * 版本 v1.0
     */
    @JSONField(name = "deliver_date")
    private String deliverDate;

    /**
     * 描述 导入时间
     * 版本 v1.0
     */
    @JSONField(name = "import_date")
    private String importDate;

}