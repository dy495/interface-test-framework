package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.Integral;

/**
 * @author wangmin
 * @date 2020/12/30 10:25 AM
 * @desc
 */
public enum IntegralExchangeStatusEnum {
    /**
     * 未开始
     */
    NOT_START("未开始"),
    WORKING("进行中"),
    CLOSE("已关闭"),
    EXPIRED("已过期"),
    INVALID("已失效");

    private String desc;

    IntegralExchangeStatusEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
