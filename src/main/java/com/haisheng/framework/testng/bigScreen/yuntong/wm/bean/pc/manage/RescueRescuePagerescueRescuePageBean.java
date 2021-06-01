package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.manage;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 15.1. 道路救援记录分页 （谢）（2020-12-18）
 *
 * @author wangmin
 * @date 2021-06-01 18:39:23
 */
@Data
public class RescueRescuePagerescueRescuePageBean implements Serializable {
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
     * 描述 救援记录id
     * 版本 v2.0
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 描述 拨号时间
     * 版本 v2.0
     */
    @JSONField(name = "dial_time")
    private String dialTime;

    /**
     * 描述 救援门店名称
     * 版本 v2.0
     */
    @JSONField(name = "shop_name")
    private String shopName;

    /**
     * 描述 客户vip类型描述
     * 版本 v2.0
     */
    @JSONField(name = "vip_type_name")
    private String vipTypeName;

    /**
     * 描述 客户名称
     * 版本 v2.0
     */
    @JSONField(name = "customer_name")
    private String customerName;

    /**
     * 描述 客户联系方式
     * 版本 v2.0
     */
    @JSONField(name = "customer_phone")
    private String customerPhone;

    /**
     * 描述 救援电话
     * 版本 v2.0
     */
    @JSONField(name = "rescue_tel")
    private String rescueTel;

}