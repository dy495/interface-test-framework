package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.app.voicerecord;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 3.4. 部门接待分页（谢）
 *
 * @author wangmin
 * @date 2021-06-02 17:56:15
 */
@Data
public class AppDepartmentPageBean implements Serializable {
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
     * 描述 排序字段值
     * 版本 v1.0
     */
    @JSONField(name = "orderValue")
    private String orderValue;

    /**
     * 描述 员工id
     * 版本 v1.0
     */
    @JSONField(name = "id")
    private String id;

    /**
     * 描述 返回的结果list
     * 版本 v1.0
     */
    @JSONField(name = "list")
    private JSONArray list;

    /**
     * 描述 员工姓名
     * 版本 v1.0
     */
    @JSONField(name = "name")
    private String name;

    /**
     * 描述 接待次数
     * 版本 v1.0
     */
    @JSONField(name = "reception_times")
    private Integer receptionTimes;

    /**
     * 描述 接待时长单位min
     * 版本 v1.0
     */
    @JSONField(name = "reception_Duration")
    private Long receptionDuration;

    /**
     * 描述 总得分
     * 版本 v1.0
     */
    @JSONField(name = "total_score")
    private Long totalScore;

}