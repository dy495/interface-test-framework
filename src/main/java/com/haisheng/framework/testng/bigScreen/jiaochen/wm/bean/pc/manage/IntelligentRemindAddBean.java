package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.manage;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 21.1. 新增智能提醒项 （谢）（2021-01-05）
 *
 * @author wangmin
 * @date 2021-04-02 14:47:46
 */
@Data
public class IntelligentRemindAddBean implements Serializable {
    /**
     * 描述 id
     * 版本 v1.0
     */
    @JSONField(name = "id")
    private JSONObject id;

}