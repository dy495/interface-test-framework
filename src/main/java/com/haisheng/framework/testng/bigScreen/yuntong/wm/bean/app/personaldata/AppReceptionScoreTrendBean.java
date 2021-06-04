package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.app.personaldata;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 2.4. 各环节得分趋势（谢）
 *
 * @author wangmin
 * @date 2021-06-03 17:30:49
 */
@Data
public class AppReceptionScoreTrendBean implements Serializable {
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
     * 描述 欢迎接待
     * 版本 v1.0
     */
    @JSONField(name = "100")
    private Integer one;

    /**
     * 描述 新车推荐
     * 版本 v1.0
     */
    @JSONField(name = "200")
    private Integer two;

    /**
     * 描述 车辆提案
     * 版本 v1.0
     */
    @JSONField(name = "300")
    private Integer three;

    /**
     * 描述 试乘试驾
     * 版本 v1.0
     */
    @JSONField(name = "400")
    private Integer four;

    /**
     * 描述 个性需求
     * 版本 v1.0
     */
    @JSONField(name = "500")
    private Integer five;

}