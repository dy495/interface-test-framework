package com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config;

import lombok.Getter;

/**
 * 测试项目枚举
 *
 * @author wangmin
 * @date 2020/7/24 15:11
 */
public enum EnumTestProduce {

    CRM_DAILY("汽车-保时捷 日常 "),

    CRM_ONLINE("汽车-保时捷 赢识线上 "),

    CRM_ONLINE_PORSCHE("汽车-保时捷 保时捷线上 "),

    JIAOCHEN_DAILY("汽车-轿辰 日常 "),

    JIAOCHEN_ONLINE("汽车-轿辰 赢识线上 "),
    ;

    EnumTestProduce(String name) {
        this.name = name;
    }

    @Getter
    private final String name;
}
