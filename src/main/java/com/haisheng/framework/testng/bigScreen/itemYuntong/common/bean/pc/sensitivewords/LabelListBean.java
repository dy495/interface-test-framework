package com.haisheng.framework.testng.bigScreen.itemYuntong.common.bean.pc.sensitivewords;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 6.1. 敏感词标签统计列表（谢）
 *
 * @author wangmin
 * @date 2021-06-02 17:56:15
 */
@Data
public class LabelListBean implements Serializable {
    /**
     * 描述 数据列表
     * 版本 v1.0
     */
    @JSONField(name = "list")
    private JSONArray list;

    /**
     * 描述 记录id
     * 版本 v1.0
     */
    @JSONField(name = "id")
    private JSONObject id;

    /**
     * 描述 敏感词
     * 版本 v1.0
     */
    @JSONField(name = "words")
    private String words;

    /**
     * 描述 提及次数
     * 版本 v1.0
     */
    @JSONField(name = "count")
    private Integer count;

}