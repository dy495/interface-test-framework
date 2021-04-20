package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.message;

/**
 * @author wangmin
 * @date 2021/3/10 10:30 AM
 * @desc
 */
public enum AfterCustomerLevelEnum {
    /**
     * A级：一年内进场2次及以上，平均消费${变量}元及以上
     */
    A("A级"),
    /**
     * B级：一年内进场2次及以上，平均消费1000元以下
     */
    B("B级"),
    /**
     * C级：一年内进场2次以下，平均消费1000元及以上
     */
    C("C级"),
    /**
     * D级：一年内进场2次以下，平均消费1000元以下
     */
    D("D级"),
    /**
     * E级：一年内未进场
     */
    E("E级");

    private String desc;

    AfterCustomerLevelEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
