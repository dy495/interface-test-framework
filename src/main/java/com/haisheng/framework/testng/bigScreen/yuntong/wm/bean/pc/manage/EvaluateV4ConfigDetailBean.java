package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.manage;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 2.5. 评价配置详情（谢）
 *
 * @author wangmin
 * @date 2021-06-01 18:10:53
 */
@Data
public class EvaluateV4ConfigDetailBean implements Serializable {
    /**
     * 描述 数据列表
     * 版本 v1.0
     */
    @JSONField(name = "list")
    private JSONArray list;

    /**
     * 描述 接待环节 见字典《接待环节》
     * 版本 v1.0
     */
    @JSONField(name = "type")
    private Integer type;

    /**
     * 描述 接待环节
     * 版本 v1.0
     */
    @JSONField(name = "type_name")
    private String typeName;

    /**
     * 描述 评价项列表
     * 版本 v1.0
     */
    @JSONField(name = "items")
    private JSONArray items;

    /**
     * 描述 评价title
     * 版本 v4.0
     */
    @JSONField(name = "title")
    private String title;

    /**
     * 描述 答案数量
     * 版本 v4.0
     */
    @JSONField(name = "answer_number")
    private Integer answerNumber;

    /**
     * 描述 答案选项列表
     * 版本 v4.0
     */
    @JSONField(name = "answers")
    private JSONArray answers;

    /**
     * 描述 答案得分
     * 版本 v1.0
     */
    @JSONField(name = "score")
    private Integer score;

    /**
     * 描述 答案内容
     * 版本 v1.0
     */
    @JSONField(name = "content")
    private String content;

}