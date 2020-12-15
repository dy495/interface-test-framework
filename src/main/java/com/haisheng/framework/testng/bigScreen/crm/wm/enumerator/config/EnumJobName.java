package com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config;

import lombok.Getter;

/**
 * 项目名称
 * 统一管理
 * 不分项目
 *
 * @author wangmin
 * @date 2020/7/24 15:07
 */
public enum EnumJobName {

    CRM_DAILY_TEST("crm-daily-test"),

    CRM_ONLINE_TEST("crm-online-test"),

    CRM_DAY_DATA_STORE("crm-day-data-store"),

    JIAOCHEN_DAILY_TEST("jiaochen-daily-test"),
    ;

    EnumJobName(String jobName) {
        this.jobName = jobName;
    }

    @Getter
    private final String jobName;
}
