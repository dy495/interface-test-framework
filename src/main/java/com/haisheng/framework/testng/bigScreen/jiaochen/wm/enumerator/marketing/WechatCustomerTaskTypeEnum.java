package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing;

/**
 * 小程序客户任务类型枚举
 * @author wangmin
 * @date 2020/12/22 11:49
 */
public enum WechatCustomerTaskTypeEnum {

    /**
     * 分享任务
     */
    SHARE("分享任务"),

    /**
     * 个人签到任务
     */
    PERSON("个人任务");


    private String value;


    WechatCustomerTaskTypeEnum(String value ){
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}
