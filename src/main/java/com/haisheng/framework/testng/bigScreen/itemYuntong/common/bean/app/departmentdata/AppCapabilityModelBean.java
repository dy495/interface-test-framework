package com.haisheng.framework.testng.bigScreen.itemYuntong.common.bean.app.departmentdata;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 4.2. 销售接待能力模型（谢）
 *
 * @author wangmin
 * @date 2021-06-02 17:56:15
 */
@Data
public class AppCapabilityModelBean implements Serializable {
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
     * 描述 得分
     * 版本 v1.0
     */
    @JSONField(name = "score")
    private Integer score;

}