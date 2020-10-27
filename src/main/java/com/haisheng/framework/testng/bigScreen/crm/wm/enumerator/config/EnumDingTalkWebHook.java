package com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config;

import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import lombok.Getter;

/**
 * 钉钉群机器人
 *
 * @author wangmin
 * @date 2020/7/24 15:17
 */
public enum EnumDingTalkWebHook {
    BA("https://oapi.dingtalk.com/robot/send?access_token=cd982c04607c4f502d984b5bea63534ecfc3bf73b94e6dd2402b108f9736a13a"),
    QA_TEST_GRP(DingWebhook.QA_TEST_GRP),
    OPEN_MANAGEMENT_PLATFORM_GRP(DingWebhook.OPEN_MANAGEMENT_PLATFORM_GRP),
    ONLINE_OPEN_MANAGEMENT_PLATFORM_GRP(DingWebhook.ONLINE_OPEN_MANAGEMENT_PLATFORM_GRP),
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
