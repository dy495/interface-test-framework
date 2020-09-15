package com.haisheng.framework.model.experiment.enumerator;

import lombok.Getter;

/**
 * 项目名称
 *
 * @author wangmin
 * @date 2020/7/24 15:07
 */
public enum EnumJobName {

    CRM_DAILY_TEST("crm-daily-test"),

    CRM_ONLINE_TEST("crm-online-test");

    EnumJobName(String jobName) {
        this.jobName = jobName;
    }

    @Getter
    private final String jobName;
}
