package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.vipmarketing;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 2.6. 洗车规则说明(查看)
 *
 * @author wangmin
 * @date 2021-04-02 14:47:46
 */
@Data
public class WashCarManagerRuleDetailBean implements Serializable {
    /**
     * 描述 规则
     * 版本 v2.0
     */
    @JSONField(name = "rule_detail")
    private String ruleDetail;

}