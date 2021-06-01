package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.appointmentmanage;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 35.1. 预约看板 （谢）v3.0 （2021-03-22）
 *
 * @author wangmin
 * @date 2021-06-01 18:10:55
 */
@Data
public class AppTimeTableListBean implements Serializable {
    /**
     * 描述 数据列表
     * 版本 v1.0
     */
    @JSONField(name = "list")
    private JSONArray list;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "day")
    private Integer day;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "total_number")
    private Long totalNumber;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "appointment_number")
    private Long appointmentNumber;

}