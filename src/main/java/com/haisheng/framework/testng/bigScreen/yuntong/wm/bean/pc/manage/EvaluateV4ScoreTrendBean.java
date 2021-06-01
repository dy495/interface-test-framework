package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.manage;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 2.6. 星级评分趋势（谢）
 *
 * @author wangmin
 * @date 2021-06-01 18:10:53
 */
@Data
public class EvaluateV4ScoreTrendBean implements Serializable {
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
     * 描述 星级 [1,5]
     * 版本 v1.0
     */
    @JSONField(name = "score")
    private Integer score;

}