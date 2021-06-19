package com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.enumerator;

import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.container.DbContainer;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.container.ExcelContainer;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.tarot.container.IContainer;
import lombok.Getter;

/**
 * 容器类型枚举
 */
public enum EnumContainer {

    DB_BUSINESS_PORSCHE(new DbContainer.Builder().driverName("com.mysql.cj.jdbc.Driver")
            .jdbcUrl("jdbc:mysql://rm-2zeg4an1kr1437xu6no.mysql.rds.aliyuncs.com/business-porsche")
            .password("read_only").username("read_only").build()),

    DB_ONE_PIECE(new DbContainer.Builder().driverName("com.mysql.cj.jdbc.Driver")
            .jdbcUrl("jdbc:mysql://rm-2zeg4an1kr1437xu6no.mysql.rds.aliyuncs.com/onepiece")
            .password("qa_wr1234").username("qa_wr").build()),

    EXCEL(new ExcelContainer.Builder().buildContainer()),
    ;

    EnumContainer(IContainer container) {
        this.container = container;
    }

    @Getter
    private final IContainer container;
}
