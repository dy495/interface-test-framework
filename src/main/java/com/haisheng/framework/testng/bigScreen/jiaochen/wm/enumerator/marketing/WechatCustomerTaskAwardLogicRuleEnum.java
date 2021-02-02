package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing;

/**
 * 任务逻辑规则
 *
 * @author wangmin
 * @date 2020/12/21 14:34
 */
public enum WechatCustomerTaskAwardLogicRuleEnum {


    /**
     * 单次完成
     */
    ONCE("新用户单次奖励"),

    /**
     * 每次完成
     */
    EVERY_TIME("老用户每次");


    WechatCustomerTaskAwardLogicRuleEnum(String value) {
    }
}
