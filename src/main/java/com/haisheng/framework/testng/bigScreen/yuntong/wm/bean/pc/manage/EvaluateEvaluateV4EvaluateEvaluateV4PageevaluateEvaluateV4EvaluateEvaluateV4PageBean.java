package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.manage;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 2.1. 评价记录分页
 *
 * @author wangmin
 * @date 2021-06-01 18:39:23
 */
@Data
public class EvaluateEvaluateV4EvaluateEvaluateV4PageevaluateEvaluateV4EvaluateEvaluateV4PageBean implements Serializable {
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
     * 描述 记录id
     * 版本 v1.0
     */
    @JSONField(name = "id")
    private JSONObject id;

    /**
     * 描述 归属门店id
     * 版本 v1.0
     */
    @JSONField(name = "shop_id")
    private Long shopId;

    /**
     * 描述 归属门店名称
     * 版本 v1.0
     */
    @JSONField(name = "shop_name")
    private String shopName;

    /**
     * 描述 客户名称
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
     * 描述 评价星级 （1-5）
     * 版本 v1.0
     */
    @JSONField(name = "score")
    private Integer score;

    /**
     * 描述 评价时间
     * 版本 v1.0
     */
    @JSONField(name = "evaluate_time")
    private String evaluateTime;

    /**
     * 描述 接待时间
     * 版本 v1.0
     */
    @JSONField(name = "reception_time")
    private String receptionTime;

    /**
     * 描述 接待顾问
     * 版本 v1.0
     */
    @JSONField(name = "receptor_name")
    private String receptorName;

    /**
     * 描述 进店状况
     * 版本 v1.0
     */
    @JSONField(name = "enter_status_name")
    private String enterStatusName;

    /**
     * 描述 跟进备注
     * 版本 v1.0
     */
    @JSONField(name = "follow_remark")
    private String followRemark;

    /**
     * 描述 跟进时间
     * 版本 v1.0
     */
    @JSONField(name = "follow_time")
    private String followTime;

    /**
     * 描述 欢迎接待
     * 版本 v1.0
     */
    @JSONField(name = "link1")
    private Integer link1;

    /**
     * 描述 新车推荐
     * 版本 v1.0
     */
    @JSONField(name = "link2")
    private Integer link2;

    /**
     * 描述 试乘试驾
     * 版本 v1.0
     */
    @JSONField(name = "link3")
    private Integer link3;

    /**
     * 描述 车辆提及
     * 版本 v1.0
     */
    @JSONField(name = "link4")
    private Integer link4;

    /**
     * 描述 个性需求
     * 版本 v1.0
     */
    @JSONField(name = "link5")
    private Integer link5;

}