package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.vouchermanage;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 7.1. 卡券表单 （张小龙） v2.0
 *
 * @author wangmin
 * @date 2021-06-01 18:10:54
 */
@Data
public class VoucherFormVoucherPageBean implements Serializable {
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
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 描述 优惠券id
     * 版本 v2.0
     */
    @JSONField(name = "voucher_id")
    private Long voucherId;

    /**
     * 描述 卡券名称
     * 版本 v2.0
     */
    @JSONField(name = "voucher_name")
    private String voucherName;

    /**
     * 描述 创建者姓名
     * 版本 v2.0
     */
    @JSONField(name = "creator_name")
    private String creatorName;

    /**
     * 描述 创建者账号
     * 版本 v2.0
     */
    @JSONField(name = "creator_account")
    private String creatorAccount;

    /**
     * 描述 状态
     * 版本 v2.0
     */
    @JSONField(name = "voucher_status")
    private String voucherStatus;

    /**
     * 描述 状态名称
     * 版本 v2.0
     */
    @JSONField(name = "voucher_status_name")
    private String voucherStatusName;

    /**
     * 描述 所属主体名称
     * 版本 v2.0
     */
    @JSONField(name = "subject_name")
    private String subjectName;

    /**
     * 描述 剩余库存
     * 版本 v2.0
     */
    @JSONField(name = "surplus_inventory")
    private Integer surplusInventory;

    /**
     * 描述 累计发出
     * 版本 v2.0
     */
    @JSONField(name = "cumulative_delivery")
    private Integer cumulativeDelivery;

    /**
     * 描述 卡券描述
     * 版本 v2.0
     */
    @JSONField(name = "description")
    private String description;

    /**
     * 描述 是否显示查看
     * 版本 v2.0
     */
    @JSONField(name = "is_check")
    private Boolean isCheck;

    /**
     * 描述 是否显示推广
     * 版本 v2.0
     */
    @JSONField(name = "is_extension")
    private Boolean isExtension;

    /**
     * 描述 是否显示编辑
     * 版本 v2.0
     */
    @JSONField(name = "is_edit")
    private Boolean isEdit;

    /**
     * 描述 是否显示增发
     * 版本 v2.0
     */
    @JSONField(name = "is_additional")
    private Boolean isAdditional;

    /**
     * 描述 是否显示复制
     * 版本 v2.0
     */
    @JSONField(name = "is_copy")
    private Boolean isCopy;

    /**
     * 描述 是否显示作废
     * 版本 v2.0
     */
    @JSONField(name = "is_invalid")
    private Boolean isInvalid;

    /**
     * 描述 是否显示停止发放
     * 版本 v2.0
     */
    @JSONField(name = "is_stop")
    private Boolean isStop;

    /**
     * 描述 是否显示撤回
     * 版本 v2.0
     */
    @JSONField(name = "is_recall")
    private Boolean isRecall;

    /**
     * 描述 是否显示删除
     * 版本 v2.0
     */
    @JSONField(name = "is_delete")
    private Boolean isDelete;

    /**
     * 描述 是否开始发放
     * 版本 -
     */
    @JSONField(name = "begin_send")
    private Boolean beginSend;

    /**
     * 描述 可用库存
     * 版本 v3.0
     */
    @JSONField(name = "allow_use_inventory")
    private Integer allowUseInventory;

}