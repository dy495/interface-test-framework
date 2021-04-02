package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.appointmentmanage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 14.6. 获取可变更预约时间段列表（谢）（2020-12-28）
 *
 * @author wangmin
 * @date 2021-04-02 14:47:46
 */
@Data
public class MaintainTimeListBean implements Serializable {
    /**
     * 描述 数据列表
     * 版本 v1.0
     */
    @JSONField(name = "list")
    private JSONArray list;

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