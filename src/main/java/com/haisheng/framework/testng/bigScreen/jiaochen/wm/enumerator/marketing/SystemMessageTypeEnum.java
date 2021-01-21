package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.marketing;

/**
 * @author wangmin
 * @date 2020/11/25 4:48 PM
 * @desc 系统消息类型
 */
public enum SystemMessageTypeEnum {
    /**
     * 我的卡券
     */
    VOUCHER_LIST("我的卡券"),
    ARTICLE_DETAIL("文章详情"),
    PACKAGE_LIST("套餐列表"),
    MAINTAIN("保养"),
    SYSTEM("系统消息");

    private String name;

    SystemMessageTypeEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
