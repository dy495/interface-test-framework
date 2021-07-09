package com.haisheng.framework.testng.bigScreen.itemYuntong.common.bean.pc.specialaudio;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 5.1. 特殊音频记录分页（谢）
 *
 * @author wangmin
 * @date 2021-06-02 17:56:15
 */
@Data
public class SpecialAudioPageBean implements Serializable {
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
    private Long id;

    /**
     * 描述 接待顾问姓名
     * 版本 v1.0
     */
    @JSONField(name = "receptor_name")
    private String receptorName;

    /**
     * 描述 音频时长
     * 版本 v1.0
     */
    @JSONField(name = "audio_duration")
    private String audioDuration;

    /**
     * 描述 接待开始时间
     * 版本 v1.0
     */
    @JSONField(name = "reception_time")
    private String receptionTime;

    /**
     * 描述 得分
     * 版本 v1.0
     */
    @JSONField(name = "score")
    private Integer score;

    /**
     * 描述 是否区域合规
     * 版本 v1.0
     */
    @JSONField(name = "is_region_compliance")
    private Boolean isRegionCompliance;

    /**
     * 描述 审核状态 详见《审核状态》
     * 版本 v1.0
     */
    @JSONField(name = "approval_status")
    private Integer approvalStatus;

    /**
     * 描述 审核状态描述
     * 版本 v1.0
     */
    @JSONField(name = "approval_status_name")
    private String approvalStatusName;

}