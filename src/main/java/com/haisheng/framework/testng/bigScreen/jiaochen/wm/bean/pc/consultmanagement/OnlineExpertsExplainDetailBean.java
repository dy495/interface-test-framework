package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.consultmanagement;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 8.11. 在线专家说明配置详情（池）（2021-03-08）
 *
 * @author wangmin
 * @date 2021-04-02 14:47:46
 */
@Data
public class OnlineExpertsExplainDetailBean implements Serializable {
    /**
     * 描述 内容详情
     * 版本 v3.0
     */
    @JSONField(name = "content")
    private String content;

}