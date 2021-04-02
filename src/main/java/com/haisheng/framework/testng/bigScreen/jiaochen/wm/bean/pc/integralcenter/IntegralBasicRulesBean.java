package com.haisheng.framework.testng.bigScreen.jiaochen.wm.bean.pc.integralcenter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 35.22. 积分基础规则设置 (张小龙) v2.0
 *
 * @author wangmin
 * @date 2021-04-02 14:47:46
 */
@Data
public class IntegralBasicRulesBean implements Serializable {
    /**
     * 描述 数据列表
     * 版本 v1.0
     */
    @JSONField(name = "list")
    private JSONArray list;

    /**
     * 描述 规则id
     * 版本 v2.0
     */
    @JSONField(name = "id")
    private Long id;

    /**
     * 描述 规则名称
     * 版本 v2.0
     */
    @JSONField(name = "rule_name")
    private String ruleName;

    /**
     * 描述 单笔发送积分
     * 版本 v2.0
     */
    @JSONField(name = "single_send")
    private Long singleSend;

    /**
     * 描述 已发放积分
     * 版本 v2.0
     */
    @JSONField(name = "all_send")
    private Long allSend;

    /**
     * 描述 规则详情
     * 版本 v2.0
     */
    @JSONField(name = "rule_detail")
    private String ruleDetail;

    /**
     * 描述 规则更新者
     * 版本 v2.0
     */
    @JSONField(name = "rule_update_sale")
    private String ruleUpdateSale;

    /**
     * 描述 更新时间
     * 版本 v2.0
     */
    @JSONField(name = "update_time")
    private String updateTime;

    /**
     * 描述 规则类型
     * 版本 v2.0
     */
    @JSONField(name = "rule_type")
    private String ruleType;

    /**
     * 描述 积分有效年份
     * 版本 v2.0
     */
    @JSONField(name = "year")
    private Integer year;

    /**
     * 描述 积分规则描述
     * 版本 v2.0
     */
    @JSONField(name = "description")
    private String description;

}