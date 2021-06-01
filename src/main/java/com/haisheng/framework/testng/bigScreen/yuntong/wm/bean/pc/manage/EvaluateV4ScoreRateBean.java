package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.manage;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 2.7. 星级比例（谢）
 *
 * @author wangmin
 * @date 2021-06-01 18:10:53
 */
@Data
public class EvaluateV4ScoreRateBean implements Serializable {
    /**
     * 描述 数据列表
     * 版本 v1.0
     */
    @JSONField(name = "list")
    private JSONArray list;

    /**
     * 描述 接待环节
     * 版本 v1.0
     */
    @JSONField(name = "type")
    private Integer type;

    /**
     * 描述 接待环节描述
     * 版本 v1.0
     */
    @JSONField(name = "type_name")
    private String typeName;

    /**
     * 描述 1星
     * 版本 v1.0
     */
    @JSONField(name = "one")
    private String one;

    /**
     * 描述 2星
     * 版本 v1.0
     */
    @JSONField(name = "two")
    private String two;

    /**
     * 描述 3星
     * 版本 v1.0
     */
    @JSONField(name = "three")
    private String three;

    /**
     * 描述 4星
     * 版本 v1.0
     */
    @JSONField(name = "four")
    private String four;

    /**
     * 描述 5星
     * 版本 v1.0
     */
    @JSONField(name = "five")
    private String five;

}