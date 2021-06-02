package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.app.personaldata;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 2.6. 给您的建议（谢）
 *
 * @author wangmin
 * @date 2021-06-02 17:56:15
 */
@Data
public class AppSpeechTechniqueAdviceBean implements Serializable {
    /**
     * 描述 提及最多的标签
     * 版本 v1.0
     */
    @JSONField(name = "most_advices")
    private JSONArray mostAdvices;

    /**
     * 描述 提及最少的标签
     * 版本 v1.0
     */
    @JSONField(name = "least_advices")
    private JSONArray leastAdvices;

    /**
     * 描述 波动最大环节
     * 版本 v1.0
     */
    @JSONField(name = "biggest_fluctuation_link")
    private String biggestFluctuationLink;

    /**
     * 描述 波动最大环节
     * 版本 v1.0
     */
    @JSONField(name = "lowest_score")
    private JSONObject lowestScore;

    /**
     * 描述 日期
     * 版本 v1.0
     */
    @JSONField(name = "day")
    private String day;

    /**
     * 描述 环节
     * 版本 v1.0
     */
    @JSONField(name = "link")
    private String link;

    /**
     * 描述 分数
     * 版本 v1.0
     */
    @JSONField(name = "score")
    private Integer score;

    /**
     * 描述 客户反馈最好环节
     * 版本 v1.0
     */
    @JSONField(name = "best_link")
    private JSONObject bestLink;

    /**
     * 描述 好评率
     * 版本 v1.0
     */
    @JSONField(name = "favorable_rate")
    private String favorableRate;

    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "favorableInt")
    private Integer favorableInt;

    /**
     * 描述 客户反馈最差环节
     * 版本 v1.0
     */
    @JSONField(name = "worst_link")
    private JSONObject worstLink;

}