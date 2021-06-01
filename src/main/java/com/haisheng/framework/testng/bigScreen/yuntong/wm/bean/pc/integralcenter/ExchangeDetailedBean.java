package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.integralcenter;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 37.15. 积分兑换明细 (张小龙) v2.0
 *
 * @author wangmin
 * @date 2021-06-01 18:10:55
 */
@Data
public class ExchangeDetailedBean implements Serializable {
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
     * 描述 唯一id
     * 版本 v2.0
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 描述 客户名称
     * 版本 v2.0
     */
    @JSONField(name = "exchange_customer_name")
    private String exchangeCustomerName;

    /**
     * 描述 兑换类型（ADD:增加 MINUS:减少）
     * 版本 v2.0
     */
    @JSONField(name = "exchange_type")
    private String exchangeType;

    /**
     * 描述 兑换类型名称（ADD:增加 MINUS:减少）
     * 版本 v2.0
     */
    @JSONField(name = "exchange_type_name")
    private String exchangeTypeName;

    /**
     * 描述 操作人
     * 版本 v2.0
     */
    @JSONField(name = "operate_sale_name")
    private String operateSaleName;

    /**
     * 描述 手机号
     * 版本 v2.0
     */
    @JSONField(name = "phone")
    private String phone;

    /**
     * 描述 库存明细
     * 版本 v2.0
     */
    @JSONField(name = "stock_detail")
    private Long stockDetail;

    /**
     * 描述 操作时间
     * 版本 v2.0
     */
    @JSONField(name = "operate_time")
    private String operateTime;

    /**
     * 描述 变动原因
     * 版本 v2.0
     */
    @JSONField(name = "change_reason")
    private String changeReason;

    /**
     * 描述 订单号
     * 版本 v2.0
     */
    @JSONField(name = "order_code")
    private String orderCode;

}