package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.mapp.followup;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 3.3. app 跟进列详情 v4 (池)(2020-03-11)
 *
 * @author wangmin
 * @date 2021-06-01 18:39:24
 */
@Data
public class AppFollowDetailfollowDetailBean implements Serializable {
    /**
     * 描述 跟进类型
     * 版本 -
     */
    @JSONField(name = "type")
    private String type;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "pre_reception_evaluate")
    private JSONObject preReceptionEvaluate;

    /**
     * 描述 客户名称
     * 版本 v4.0
     */
    @JSONField(name = "customer_name")
    private String customerName;

    /**
     * 描述 客户手机号
     * 版本 v4.0
     */
    @JSONField(name = "customer_phone")
    private String customerPhone;

    /**
     * 描述 客户头像
     * 版本 v4.0
     */
    @JSONField(name = "face_url")
    private String faceUrl;

    /**
     * 描述 接待顾问名称
     * 版本 v4.0
     */
    @JSONField(name = "reception_sales_name")
    private String receptionSalesName;

    /**
     * 描述 接待开始时间
     * 版本 v4.0
     */
    @JSONField(name = "reception_start_date")
    private String receptionStartDate;

    /**
     * 描述 接待结束时间
     * 版本 v4.0
     */
    @JSONField(name = "reception_end_date")
    private String receptionEndDate;

    /**
     * 描述 评价星级
     * 版本 v4.0
     */
    @JSONField(name = "evaluate_level")
    private Integer evaluateLevel;

    /**
     * 描述 评价时间
     * 版本 v4.0
     */
    @JSONField(name = "evaluate_date")
    private String evaluateDate;

    /**
     * 描述 跟进内容
     * 版本 v4.0
     */
    @JSONField(name = "follow_content")
    private String followContent;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "evaluate_items")
    private JSONArray evaluateItems;

    /**
     * 描述 评价title
     * 版本 v4.0
     */
    @JSONField(name = "title")
    private String title;

    /**
     * 描述 评价分数
     * 版本 v4.0
     */
    @JSONField(name = "score")
    private Integer score;

    /**
     * 描述 评价内容
     * 版本 v4.0
     */
    @JSONField(name = "content")
    private String content;

}