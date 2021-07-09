package com.haisheng.framework.testng.bigScreen.itemYuntong.common.bean.pc.specialaudio;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 5.2. 特殊音频详情（谢）
 *
 * @author wangmin
 * @date 2021-06-02 17:56:15
 */
@Data
public class DetailBean implements Serializable {
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
     * 描述 录音开始时间
     * 版本 v1.0
     */
    @JSONField(name = "recording_start_time")
    private String recordingStartTime;

    /**
     * 描述 录音结束时间
     * 版本 v1.0
     */
    @JSONField(name = "recording_end_time")
    private String recordingEndTime;

    /**
     * 描述 原因
     * 版本 v1.0
     */
    @JSONField(name = "reason")
    private String reason;

    /**
     * 描述 录音记录
     * 版本 v1.0
     */
    @JSONField(name = "voice_record")
    private String voiceRecord;

}