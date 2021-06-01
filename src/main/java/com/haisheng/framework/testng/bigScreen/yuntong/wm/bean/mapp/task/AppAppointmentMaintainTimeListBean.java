package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.mapp.task;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 6.4. app获取可变更预约日期时间段列表（谢）v3.0（2021-03-16）
 *
 * @author wangmin
 * @date 2021-06-01 18:10:55
 */
@Data
public class AppAppointmentMaintainTimeListBean implements Serializable {
    /**
     * 描述 数据列表
     * 版本 v1.0
     */
    @JSONField(name = "list")
    private JSONArray list;

    /**
     * 描述 可预约日期
     * 版本 v1.0
     */
    @JSONField(name = "day")
    private String day;

    /**
     * 描述 预约时间段id
     * 版本 v1.0
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 描述 预约时间段
     * 版本 v1.0
     */
    @JSONField(name = "time")
    private String time;

    /**
     * 描述 预约价格
     * 版本 v1.0
     */
    @JSONField(name = "price")
    private String price;

    /**
     * 描述 是否约满
     * 版本 v1.0
     */
    @JSONField(name = "is_full")
    private Boolean isFull;

}