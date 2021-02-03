package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator;

import lombok.Getter;

/**
 * 消息内容管理
 *
 * @author wangmin
 * @date 2021-02-03
 */
public enum MessageContentEnum {
    TRANSFER_VOUCHER("", "", "转移人消息"),
    RECEIVE_VOUCHER("", "", "接收人消息"),
    BUY_PACKAGE("豪横！您有新的套餐已到账！", "您在【发卡门店名称/集团】的购买的【套餐名称】已到账，点击查看详情！", "");

    MessageContentEnum(String title, String content, String desc) {
        this.title = title;
        this.content = content;
    }

    @Getter
    private final String content;

    @Getter
    private final String title;
}
