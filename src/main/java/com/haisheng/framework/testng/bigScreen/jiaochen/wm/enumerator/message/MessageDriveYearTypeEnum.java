package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.message;

/**
 * @author wangmin
 * @date 2021/3/9 5:34 PM
 * @desc
 */
public enum MessageDriveYearTypeEnum {
    /**
     * 1年
     */
    ONE_YEAR("1年"),
    /**
     * 2年
     */
    TWO_YEAR("2年"),
    /**
     * 3年
     */
    THREE_YEAR("3年"),
    /**
     * 4年
     */
    THIRD_YEAR("4年"),
    /**
     * 5年
     */
    FIVE_YEAR("5年"),
    /**
     * 6年
     */
    SIX_YEAR("6年"),
    /**
     * 6年以上
     */
    UP_SIX_YEAR("6年以上");

    private String desc;

    MessageDriveYearTypeEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
