package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.manage;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 4.4. 接待详情（谢）
 *
 * @author wangmin
 * @date 2021-06-02 17:56:15
 */
@Data
public class VoiceDetailBean implements Serializable {
    /**
     * 描述 记录id
     * 版本 v1.0
     */
    @JSONField(name = "id")
    private JSONObject id;

    /**
     * 描述 接待开始时间
     * 版本 v1.0
     */
    @JSONField(name = "start_time")
    private String startTime;

    /**
     * 描述 客户姓名
     * 版本 v1.0
     */
    @JSONField(name = "name")
    private String name;

    /**
     * 描述 客户电话
     * 版本 v1.0
     */
    @JSONField(name = "phone")
    private String phone;

    /**
     * 描述 客户id
     * 版本 v1.0
     */
    @JSONField(name = "customer_id")
    private Long customerId;

    /**
     * 描述 接待结束时间
     * 版本 v1.0
     */
    @JSONField(name = "end_time")
    private String endTime;

    /**
     * 描述 进店情况
     * 版本 v1.0
     */
    @JSONField(name = "enter_status_name")
    private String enterStatusName;

    /**
     * 描述 接待顾问
     * 版本 v1.0
     */
    @JSONField(name = "receptor_name")
    private String receptorName;

    /**
     * 描述 录音记录
     * 版本 v1.0
     */
    @JSONField(name = "voice_record")
    private String voiceRecord;

    /**
     * 描述 接待环节得分
     * 版本 v1.0
     */
    @JSONField(name = "scores")
    private JSONArray scores;

    /**
     * 描述 接待环节 见字典《接待环节》
     * 版本 v1.0
     */
    @JSONField(name = "type")
    private Integer type;

    /**
     * 描述 得分
     * 版本 v1.0
     */
    @JSONField(name = "score")
    private Integer score;

    /**
     * 描述 本次接待加权分
     * 版本 v1.0
     */
    @JSONField(name = "average_score")
    private Integer averageScore;

    /**
     * 描述 各环节得分标签
     * 版本 v1.0
     */
    @JSONField(name = "link_label_list")
    private JSONArray linkLabelList;

    /**
     * 描述 接待环节描述
     * 版本 v1.0
     */
    @JSONField(name = "type_name")
    private String typeName;

    /**
     * 描述 环节话术标签
     * 版本 v1.0
     */
    @JSONField(name = "labels")
    private JSONArray labels;

    /**
     * 描述 标签
     * 版本 v1.0
     */
    @JSONField(name = "label")
    private String label;

    /**
     * 描述 是否命中
     * 版本 v1.0
     */
    @JSONField(name = "is_hit")
    private Boolean isHit;

    /**
     * 描述 话术建议
     * 版本 v1.0
     */
    @JSONField(name = "advice_list")
    private JSONArray adviceList;

    /**
     * 描述 建议
     * 版本 v1.0
     */
    @JSONField(name = "advice")
    private String advice;

    /**
     * 描述 接待详情跳转url
     * 版本 v1.0
     */
    @JSONField(name = "reception_url")
    private String receptionUrl;

}