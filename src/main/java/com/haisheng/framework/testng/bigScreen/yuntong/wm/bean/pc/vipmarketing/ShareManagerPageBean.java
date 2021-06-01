package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.vipmarketing;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 4.18. 分享管理 (池) v2.0
 *
 * @author wangmin
 * @date 2021-06-01 18:10:54
 */
@Data
public class ShareManagerPageBean implements Serializable {
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
     * 版本 v2.0
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 描述 类型
     * 版本 v2.0
     */
    @JSONField(name = "type_name")
    private String typeName;

    /**
     * 描述 任务名称
     * 版本 v2.0
     */
    @JSONField(name = "taskName")
    private String taskName;

    /**
     * 描述 奖励积分
     * 版本 v2.0
     */
    @JSONField(name = "award_score")
    private Integer awardScore;

    /**
     * 描述 奖励卡卷
     * 版本 v2.0
     */
    @JSONField(name = "award_card_volume")
    private String awardCardVolume;

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
     * 描述 业务类型 06("二维码分享") 05("活动分享") 04("签到分享") 0A("售后维修") 07("完善资料") 08("预约保养") 09("预约维修") 0K("介绍购车") 0M("预约试驾")
     * 版本 v2.0
     */
    @JSONField(name = "business_type")
    private String businessType;

    /**
     * 描述 业务类型名称
     * 版本 v2.0
     */
    @JSONField(name = "business_type_name")
    private String businessTypeName;

    /**
     * 描述 天数
     * 版本 v3.0
     */
    @JSONField(name = "day")
    private Integer day;

    /**
     * 描述 天数是否可编辑
     * 版本 -
     */
    @JSONField(name = "day_edit_flag")
    private Boolean dayEditFlag;

}