package com.haisheng.framework.testng.bigScreen.itemXundian.common.enumerator;


public enum TriggerRuleEnum {
    UNIFORM_MONITOR("UNIFORM_MONITOR","制服识别规则"),
    MASK_MONITOR("MASK_MONITOR","口罩识别规则"),
    HAT_MONITOR("HAT_MONITOR","");


    TriggerRuleEnum(String triggerRule, String triggerRuleName){
        this.triggerRule=triggerRule;
        this.triggerRuleName=triggerRuleName;
    };

    private  String triggerRule;

    private  String triggerRuleName;

    public String getTriggerRule(){
        return triggerRule;
    }
    public String getTriggerRuleName(){
        return triggerRule;
    }

}
