package com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config;

import lombok.Getter;

/**
 * 测试项目枚举
 *
 * @author wangmin
 * @date 2020/7/24 15:11
 */
public enum EnumTestProduce {

    CRM_DAILY("汽车 日常 王敏"),

    CRM_ONLINE("汽车 线上-赢识 王敏"),

    CRM_ONLINE_PORSCHE("汽车 线上-保时捷 王敏");

    EnumTestProduce(String name) {
        this.name = name;
    }

    @Getter
    private final String name;
}
