package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.vipmarketing;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 2.1. 权益列表 (池) v2.0
 *
 * @author wangmin
 * @date 2021-04-02 14:47:46
 */
@Data
public class EquityPageBean implements Serializable {
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
     * 描述 服务类型 vip会员 普通会员
     * 版本 v2.0
     */
    @JSONField(name = "service_type_name")
    private String serviceTypeName;

    /**
     * 描述 服务类型 1 普通会员 10 vip 会员
     * 版本 v2.0
     */
    @JSONField(name = "service_type")
    private Integer serviceType;

    /**
     * 描述 权益id
     * 版本 v2.0
     */
    @JSONField(name = "equity_id")
    private Long equityId;

    /**
     * 描述 权益名称
     * 版本 v2.0
     */
    @JSONField(name = "equity_name")
    private String equityName;

    /**
     * 描述 奖励积分
     * 版本 v2.0
     */
    @JSONField(name = "award_count")
    private Integer awardCount;

    /**
     * 描述 单位
     * 版本 v2.0
     */
    @JSONField(name = "award_count_unit")
    private String awardCountUnit;

    /**
     * 描述 说明
     * 版本 v2.0
     */
    @JSONField(name = "description")
    private String description;

    /**
     * 描述 状态
     * 版本 v2.0
     */
    @JSONField(name = "status_name")
    private String statusName;

    /**
     * 描述 状态 ENABLE 开启 DISABLE 关闭;
     * 版本 v2.0
     */
    @JSONField(name = "status")
    private String status;

    /**
     * 描述 业务类型 00("洗车"), 01("生日积分"),
     * 版本 v2.0
     */
    @JSONField(name = "business_type")
    private String businessType;

}