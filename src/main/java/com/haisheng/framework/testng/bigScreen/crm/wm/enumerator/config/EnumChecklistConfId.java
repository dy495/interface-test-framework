package com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config;

import lombok.Getter;

/**
 * 项目id
 *
 * @author wangmin
 * @date 2020/7/24 14:45
 */
public enum EnumChecklistConfId {
    /**
     * CRM_日常
     */
    DB_SERVICE_ID_CRM_DAILY_SERVICE(21),

    /**
     * CRM_线上
     */
    DB_SERVICE_ID_CRM_ONLINE_SERVICE(26),

    /**
     * 轿辰日常
     */
    DB_SERVICE_ID_JIAOCHEN_DAILY_SERVICE(27),
    ;

    EnumChecklistConfId(int id) {
        this.id = id;
    }

    @Getter
    private final int id;
}
