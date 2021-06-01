package com.haisheng.framework.testng.bigScreen.yuntong.wm.bean.pc.consultmanagement;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 10.12. 专属服务说明配置详情（池）（2021-03-08）
 *
 * @author wangmin
 * @date 2021-06-01 18:10:54
 */
@Data
public class DedicatedServiceExplainDetailBean implements Serializable {
    /**
     * 描述 内容详情
     * 版本 v3.0
     */
    @JSONField(name = "content")
    private String content;

}