package com.haisheng.framework.model.experiment.enumerator;

import lombok.Getter;

/**
 * 测试项目枚举
 *
 * @author wangmin
 * @date 2020/7/24 15:11
 */
public enum EnumTestProduce {

    CRM_DAILY("CRM 日常-wm");

    EnumTestProduce(String name) {
        this.name = name;
    }

    @Getter
    private final String name;
}
