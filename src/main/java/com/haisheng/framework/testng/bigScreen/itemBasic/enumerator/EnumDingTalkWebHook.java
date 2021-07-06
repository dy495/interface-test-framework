package com.haisheng.framework.testng.bigScreen.itemBasic.enumerator;

import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import lombok.Getter;

/**
 * 钉钉群机器人
 *
 * @author wangmin
 * @date 2020/7/24 15:17
 */
public enum EnumDingTalkWebHook {

    XMX("https://oapi.dingtalk.com/robot/send?access_token=6fd0c40f21aa0f06cce8bbd94daf20875659b19f17fa84446deef5b9810d870a"),

    YLLZ("https://oapi.dingtalk.com/robot/send?access_token=d6a85c28f4f829b622fb2206b75338b46f9b567421258152bcdb6d4afb2d7d60"),
    QA_TEST_GRP(DingWebhook.QA_TEST_GRP),
    CAR_OPEN_MANAGEMENT_PLATFORM_GRP(DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP),
    ONLINE_CAR_CAR_OPEN_MANAGEMENT_PLATFORM_GRP(DingWebhook.ONLINE_CAR_CAR_OPEN_MANAGEMENT_PLATFORM_GRP),
    COMMODITY_SHELF_GRP(DingWebhook.COMMODITY_SHELF_GRP),
    PV_UV_ACCURACY_GRP(DingWebhook.PV_UV_ACCURACY_GRP),
    DAILY_PV_UV_ACCURACY_GRP(DingWebhook.DAILY_PV_UV_ACCURACY_GRP),
    CLOUD_ALARM_GRP(DingWebhook.CLOUD_ALARM_GRP),
    APP_CLOUD_ALARM_GRP(DingWebhook.APP_CLOUD_ALARM_GRP),
    APP_BAIGUOYUAN_ALARM_GRP(DingWebhook.APP_BAIGUOYUAN_ALARM_GRP),
    DAILY_EDGE(DingWebhook.DAILY_EDGE);

    EnumDingTalkWebHook(String webHook) {
        this.webHook = webHook;
    }

    @Getter
    private final String webHook;
}
