package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.activity;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 28.1. 活动列表 （谢）v3.0（2021-04-02）
 *
 * @author wangmin
 * @date 2021-06-01 18:10:54
 */
@Data
public class ManagePageBean implements Serializable {
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
     * 描述 活动id
     * 版本 v2.0
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 描述 活动标题
     * 版本 v2.0
     */
    @JSONField(name = "title")
    private String title;

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
     * 描述 所属主体类型
     * 版本 v2.0
     */
    @JSONField(name = "subject_type")
    private String subjectType;

    /**
     * 描述 所属主体类型名称
     * 版本 v2.0
     */
    @JSONField(name = "subject_type_name")
    private String subjectTypeName;

    /**
     * 描述 所属主体id
     * 版本 v2.0
     */
    @JSONField(name = "subject_id")
    private Long subjectId;

    /**
     * 描述 所属主体名称
     * 版本 v2.0
     */
    @JSONField(name = "subject_name")
    private String subjectName;

    /**
     * 描述 活动状态
     * 版本 v2.0
     */
    @JSONField(name = "status")
    private Integer status;

    /**
     * 描述 活动状态描述
     * 版本 v2.0
     */
    @JSONField(name = "status_name")
    private String statusName;

    /**
     * 描述 活动审批状态
     * 版本 v2.0
     */
    @JSONField(name = "approval_status")
    private Integer approvalStatus;

    /**
     * 描述 活动审批状态描述
     * 版本 v2.0
     */
    @JSONField(name = "approval_status_name")
    private String approvalStatusName;

    /**
     * 描述 活动状态 1：裂变优惠券，2：活动招募
     * 版本 v2.0
     */
    @JSONField(name = "activity_type")
    private Integer activityType;

    /**
     * 描述 待审批数量
     * 版本 v2.0
     */
    @JSONField(name = "waiting_audit_num")
    private Long waitingAuditNum;

    /**
     * 描述 是否可撤销
     * 版本 v2.0
     */
    @JSONField(name = "is_can_revoke")
    private Boolean isCanRevoke;

    /**
     * 描述 是否可编辑
     * 版本 v2.0
     */
    @JSONField(name = "is_can_edit")
    private Boolean isCanEdit;

    /**
     * 描述 是否可删除
     * 版本 v2.0
     */
    @JSONField(name = "is_can_delete")
    private Boolean isCanDelete;

    /**
     * 描述 是否可取消
     * 版本 v2.0
     */
    @JSONField(name = "is_can_cancel")
    private Boolean isCanCancel;

    /**
     * 描述 是否可推广
     * 版本 v2.0
     */
    @JSONField(name = "is_can_promotion")
    private Boolean isCanPromotion;

    /**
     * 描述 是否可恢复
     * 版本 v3.0
     */
    @JSONField(name = "is_can_recovery")
    private Boolean isCanRecovery;

    /**
     * 描述 是否可下架
     * 版本 v3.0
     */
    @JSONField(name = "is_can_offline")
    private Boolean isCanOffline;

    /**
     * 描述 是否可上架
     * 版本 v3.0
     */
    @JSONField(name = "is_can_online")
    private Boolean isCanOnline;

    /**
     * 描述 是否可上架
     * 版本 v3.0
     */
    @JSONField(name = "is_can_top")
    private Boolean isCanTop;

}