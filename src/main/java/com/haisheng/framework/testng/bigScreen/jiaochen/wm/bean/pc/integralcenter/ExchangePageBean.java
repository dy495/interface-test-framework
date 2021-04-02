package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.integralcenter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 35.1. 积分兑换列表 (张小龙) v2.0
 *
 * @author wangmin
 * @date 2021-04-02 14:47:46
 */
@Data
public class ExchangePageBean implements Serializable {
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
     * 描述 积分兑换品id
     * 版本 v2.0
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 描述 商品图
     * 版本 v2.0
     */
    @JSONField(name = "goods_pic")
    private String goodsPic;

    /**
     * 描述 商品名称
     * 版本 v2.0
     */
    @JSONField(name = "goods_name")
    private String goodsName;

    /**
     * 描述 商品价格
     * 版本 v2.0
     */
    @JSONField(name = "goods_price")
    private String goodsPrice;

    /**
     * 描述 兑换类型
     * 版本 v2.0
     */
    @JSONField(name = "exchange_type")
    private String exchangeType;

    /**
     * 描述 兑换类型名字
     * 版本 v2.0
     */
    @JSONField(name = "exchange_type_name")
    private String exchangeTypeName;

    /**
     * 描述 兑换价格
     * 版本 v2.0
     */
    @JSONField(name = "exchange_price")
    private Long exchangePrice;

    /**
     * 描述 已兑换/剩余
     * 版本 v2.0
     */
    @JSONField(name = "exchanged_and_surplus")
    private String exchangedAndSurplus;

    /**
     * 描述 限兑数量
     * 版本 v2.0
     */
    @JSONField(name = "exchange_number")
    private Integer exchangeNumber;

    /**
     * 描述 有效期开始时间
     * 版本 v2.0
     */
    @JSONField(name = "begin_use_time")
    private String beginUseTime;

    /**
     * 描述 有效期开始时间
     * 版本 v2.0
     */
    @JSONField(name = "end_use_time")
    private String endUseTime;

    /**
     * 描述 状态
     * 版本 v2.0
     */
    @JSONField(name = "status")
    private String status;

    /**
     * 描述 状态名称
     * 版本 v2.0
     */
    @JSONField(name = "status_name")
    private String statusName;

    /**
     * 描述 开关
     * 版本 v2.0
     */
    @JSONField(name = "switch_status")
    private Boolean switchStatus;

}