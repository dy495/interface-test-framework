package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.voucher;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 6.1. 卡券审批申请分页 （张小龙）
 *
 * @author wangmin
 * @date 2021-04-02 14:47:46
 */
@Data
public class ApplyPageBean implements Serializable {
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
     * 版本 v3.0
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 描述 优惠券id
     * 版本 v3.0
     */
    @JSONField(name = "voucher_id")
    private Long voucherId;

    /**
     * 描述 卡券名称
     * 版本 v1.0
     */
    @JSONField(name = "name")
    private String name;

    /**
     * 描述 申请人名称
     * 版本 v1.0
     */
    @JSONField(name = "apply_name")
    private String applyName;

    /**
     * 描述 申请账号
     * 版本 v1.0
     */
    @JSONField(name = "apply_account")
    private String applyAccount;

    /**
     * 描述 申请时间
     * 版本 v3.0
     */
    @JSONField(name = "apply_time")
    private String applyTime;

    /**
     * 描述 所属主体名称
     * 版本 v3.0
     */
    @JSONField(name = "subject_name")
    private String subjectName;

    /**
     * 描述 发出数量
     * 版本 v1.0
     */
    @JSONField(name = "num")
    private Integer num;

    /**
     * 描述 成本单价
     * 版本 v1.0
     */
    @JSONField(name = "price")
    private String price;

    /**
     * 描述 成本累计
     * 版本 v1.0
     */
    @JSONField(name = "total_price")
    private String totalPrice;

    /**
     * 描述 申请类型
     * 版本 v1.0
     */
    @JSONField(name = "apply_type")
    private String applyType;

    /**
     * 描述 申请类型名称
     * 版本 v1.0
     */
    @JSONField(name = "apply_type_name")
    private String applyTypeName;

    /**
     * 描述 状态枚举
     * 版本 v1.0
     */
    @JSONField(name = "status")
    private String status;

    /**
     * 描述 状态展示名称
     * 版本 v1.0
     */
    @JSONField(name = "status_name")
    private String statusName;

    /**
     * 描述 申请门店
     * 版本 v1.0
     */
    @JSONField(name = "apply_group")
    private String applyGroup;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "cost_center")
    private String costCenter;

    /**
     * 描述 申请项目
     * 版本 v1.0
     */
    @JSONField(name = "apply_item")
    private String applyItem;

    /**
     * 描述 申请项目名称
     * 版本 v1.0
     */
    @JSONField(name = "apply_item_name")
    private String applyItemName;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "send_time")
    private String sendTime;

}