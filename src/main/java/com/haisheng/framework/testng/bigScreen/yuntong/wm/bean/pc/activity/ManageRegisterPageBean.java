package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.activity;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 28.22. 活动报名列表 （谢）（2020-12-23）
 *
 * @author wangmin
 * @date 2021-06-01 18:10:54
 */
@Data
public class ManageRegisterPageBean implements Serializable {
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
     * 描述 报名id
     * 版本 v2.0
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 描述 活动id
     * 版本 v2.0
     */
    @JSONField(name = "activity_id")
    private Long activityId;

    /**
     * 描述 报名时间
     * 版本 v2.0
     */
    @JSONField(name = "register_time")
    private String registerTime;

    /**
     * 描述 顾客姓名
     * 版本 v2.0
     */
    @JSONField(name = "customer_name")
    private String customerName;

    /**
     * 描述 顾客类型
     * 版本 v2.0
     */
    @JSONField(name = "customer_type")
    private String customerType;

    /**
     * 描述 报名人数
     * 版本 v2.0
     */
    @JSONField(name = "register_num")
    private Integer registerNum;

    /**
     * 描述 客户手机号
     * 版本 v2.0
     */
    @JSONField(name = "customer_phone")
    private String customerPhone;

    /**
     * 描述 审核状态
     * 版本 v2.0
     */
    @JSONField(name = "status")
    private Integer status;

    /**
     * 描述 审核状态描述
     * 版本 v2.0
     */
    @JSONField(name = "status_name")
    private String statusName;

    /**
     * 描述 审批人名字
     * 版本 v2.0
     */
    @JSONField(name = "approval_name")
    private String approvalName;

    /**
     * 描述 审批人手机号
     * 版本 v2.0
     */
    @JSONField(name = "approval_phone")
    private String approvalPhone;

    /**
     * 描述 最新修改时间
     * 版本 v2.0
     */
    @JSONField(name = "modify_time")
    private String modifyTime;

}