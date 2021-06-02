package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.app.voicerecord;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 3.2. 个人接待详情-记录分页（谢）
 *
 * @author wangmin
 * @date 2021-06-02 17:56:15
 */
@Data
public class AppPersonalPageBean implements Serializable {
    /**
     * 描述 总数 首次查询或刷新时返回
     * 版本 v1.0
     */
    @JSONField(name = "total")
    private Long total;

    /**
     * 描述 本次查询最后一条数据主键
     * 版本 v1.0
     */
    @JSONField(name = "last_value")
    private JSONObject lastValue;

    /**
     * 描述 分数
     * 版本 v1.0
     */
    @JSONField(name = "score")
    private Long score;

    /**
     * 描述 接待id
     * 版本 v1.0
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 描述 返回的结果list
     * 版本 v1.0
     */
    @JSONField(name = "list")
    private JSONArray list;

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

}