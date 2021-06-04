package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.app.departmentdata;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 4.5. 部门平均分值趋势（谢）
 *
 * @author wangmin
 * @date 2021-06-02 17:56:15
 */
@Data
public class AppReceptionAverageScoreTrendBean implements Serializable {
    /**
     * 描述 数据列表
     * 版本 v1.0
     */
    @JSONField(name = "list")
    private JSONArray list;

    /**
     * 描述 日期
     * 版本 v1.0
     */
    @JSONField(name = "day")
    private String day;

    /**
     * 描述 总平均分
     * 版本 v1.0
     */
    @JSONField(name = "total_average_score")
    private Integer totalAverageScore;

}