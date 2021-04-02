package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.auth;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 34.1. 
 *
 * @author wangmin
 * @date 2021-04-02 14:47:46
 */
@Data
public class TreeBean implements Serializable {
    /**
     * 描述 No comments found.
     * 版本 -
     */
    @JSONField(name = "children")
    private JSONArray children;

    /**
     * 描述 值
     * 版本 -
     */
    @JSONField(name = "value")
    private Integer value;

    /**
     * 描述 标签
     * 版本 -
     */
    @JSONField(name = "label")
    private String label;

    /**
     * 描述 描述
     * 版本 -
     */
    @JSONField(name = "description")
    private String description;

}