package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.manage;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 3.3. 预约时间段详情（谢）v3.0（2021-03-22）
 *
 * @author wangmin
 * @date 2021-04-02 14:47:46
 */
@Data
public class AppointmentTimeRangeDetailBean implements Serializable {
    /**
     * 描述 上午时段配置
     * 版本 v1.0
     */
    @JSONField(name = "morning")
    private JSONObject morning;

    /**
     * 描述 时段名称
     * 版本 v1.0
     */
    @JSONField(name = "title")
    private String title;

    /**
     * 描述 正常应答开始时间
     * 版本 v1.0
     */
    @JSONField(name = "reply_start")
    private String replyStart;

    /**
     * 描述 正常应答结束时间
     * 版本 v1.0
     */
    @JSONField(name = "reply_end")
    private String replyEnd;

    /**
     * 描述 预约时间段配置
     * 版本 v1.0
     */
    @JSONField(name = "list")
    private JSONArray list;

    /**
     * 描述 预约时间段开始时间
     * 版本 v1.0
     */
    @JSONField(name = "start_time")
    private String startTime;

    /**
     * 描述 预约时间段结束时间
     * 版本 v1.0
     */
    @JSONField(name = "end_time")
    private String endTime;

    /**
     * 描述 可预约人数
     * 版本 v1.0
     */
    @JSONField(name = "num")
    private Integer num;

    /**
     * 描述 此时段预约折扣
     * 版本 v1.0
     */
    @JSONField(name = "discount")
    private Double discount;

    /**
     * 描述 上午时段配置
     * 版本 v1.0
     */
    @JSONField(name = "afternoon")
    private JSONObject afternoon;

}