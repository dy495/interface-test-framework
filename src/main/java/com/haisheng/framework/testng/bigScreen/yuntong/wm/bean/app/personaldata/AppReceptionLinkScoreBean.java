package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.app.personaldata;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 2.3. 各环节得分（谢）
 *
 * @author wangmin
 * @date 2021-06-02 17:56:15
 */
@Data
public class AppReceptionLinkScoreBean implements Serializable {
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
     * 描述 集团平均分
     * 版本 v1.0
     */
    @JSONField(name = "group_average_score")
    private Integer groupAverageScore;

    /**
     * 描述 个人平均分
     * 版本 v1.0
     */
    @JSONField(name = "person_average_score")
    private Integer personAverageScore;

    /**
     * 描述 部门平均分
     * 版本 v1.0
     */
    @JSONField(name = "department_average_score")
    private Integer departmentAverageScore;

}