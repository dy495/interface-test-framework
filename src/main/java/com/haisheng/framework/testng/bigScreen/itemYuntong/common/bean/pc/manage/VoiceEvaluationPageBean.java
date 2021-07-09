package com.haisheng.framework.testng.bigScreen.itemYuntong.common.bean.pc.manage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 4.1. 语音评鉴列表分页（谢）
 *
 * @author wangmin
 * @date 2021-06-02 17:56:15
 */
@Data
public class VoiceEvaluationPageBean implements Serializable {
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
    private Integer pageSize;

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
    private Long id;

    /**
     * 描述 接待顾问姓名
     * 版本 v1.0
     */
    @JSONField(name = "receptor_name")
    private String receptorName;

    /**
     * 描述 接待时间
     * 版本 v1.0
     */
    @JSONField(name = "reception_time")
    private String receptionTime;

    /**
     * 描述 接待时长
     * 版本 v1.0
     */
    @JSONField(name = "reception_duration")
    private String receptionDuration;

    /**
     * 描述 客户姓名
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
     * 描述 进店情况
     * 版本 v1.0
     */
    @JSONField(name = "enter_status_name")
    private String enterStatusName;

    /**
     * 描述 评分状态
     * 版本 v1.0
     */
    @JSONField(name = "evaluate_status_name")
    private String evaluateStatusName;

    /**
     * 描述 接待评分
     * 版本 v1.0
     */
    @JSONField(name = "evaluate_score")
    private Integer evaluateScore;

    /**
     * 描述 备注信息
     * 版本 v1.0
     */
    @JSONField(name = "remark")
    private String remark;

}