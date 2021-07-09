package com.haisheng.framework.testng.bigScreen.itemYuntong.common.bean.pc.sensitivewords;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 6.3. 敏感行为详情（谢）
 *
 * @author wangmin
 * @date 2021-06-02 17:56:15
 */
@Data
public class SensitiveBehaviorDetailBean implements Serializable {
    /**
     * 描述 行为id
     * 版本 v1.0
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 描述 录音记录
     * 版本 v1.0
     */
    @JSONField(name = "voice_record")
    private String voiceRecord;

    /**
     * 描述 接待顾问姓名
     * 版本 v1.0
     */
    @JSONField(name = "receptor_name")
    private String receptorName;

    /**
     * 描述 开始接待时间
     * 版本 v1.0
     */
    @JSONField(name = "reception_start_time")
    private String receptionStartTime;

    /**
     * 描述 敏感词列表
     * 版本 v1.0
     */
    @JSONField(name = "sensitive_words")
    private JSONArray sensitiveWords;

    /**
     * 描述 敏感词出现时间段 相对时间
     * 版本 v1.0
     */
    @JSONField(name = "time")
    private String time;

    /**
     * 描述 敏感词
     * 版本 v1.0
     */
    @JSONField(name = "words")
    private String words;

}