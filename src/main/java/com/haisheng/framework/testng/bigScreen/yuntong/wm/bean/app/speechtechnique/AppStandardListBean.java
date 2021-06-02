package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.app.speechtechnique;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 1.1. 获取标准话术列表（谢）
 *
 * @author wangmin
 * @date 2021-06-02 17:56:15
 */
@Data
public class AppStandardListBean implements Serializable {
    /**
     * 描述 数据列表
     * 版本 v1.0
     */
    @JSONField(name = "list")
    private JSONArray list;

    /**
     * 描述 话术环节
     * 版本 v1.0
     */
    @JSONField(name = "type")
    private String type;

    /**
     * 描述 话术分组
     * 版本 v1.0
     */
    @JSONField(name = "techniques")
    private JSONArray techniques;

    /**
     * 描述 话术类型标题
     * 版本 v1.0
     */
    @JSONField(name = "title")
    private String title;

    /**
     * 描述 术语列表
     * 版本 v1.0
     */
    @JSONField(name = "terms")
    private String terms;

}