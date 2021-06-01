package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.manage;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 2.3. 评价详情
 *
 * @author wangmin
 * @date 2021-06-01 18:10:53
 */
@Data
public class EvaluateV4DetailBean implements Serializable {
    /**
     * 描述 备注内容
     * 版本 v1.0
     */
    @JSONField(name = "remark_content")
    private String remarkContent;

    /**
     * 描述 评价详情
     * 版本 v1.0
     */
    @JSONField(name = "info")
    private JSONArray info;

    /**
     * 描述 评价title
     * 版本 v1.0
     */
    @JSONField(name = "title")
    private String title;

    /**
     * 描述 评价分数
     * 版本 v1.0
     */
    @JSONField(name = "score")
    private Integer score;

    /**
     * 描述 评价内容
     * 版本 v1.0
     */
    @JSONField(name = "content")
    private String content;

}