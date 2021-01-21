package com.haisheng.framework.testng.bigScreen.crm.wm.base.container;

import lombok.Getter;

public enum EnumContainer {

    BUSINESS_PORSCHE(new DbContainer.Builder().driverName("com.mysql.cj.jdbc.Driver")
            .jdbcUrl("jdbc:mysql://rm-2zeg4an1kr1437xu6no.mysql.rds.aliyuncs.com/business-porsche")
            .password("read_only").username("read_only").build()),

    ONE_PIECE(new DbContainer.Builder().driverName("com.mysql.cj.jdbc.Driver")
            .jdbcUrl("jdbc:mysql://rm-2zeg4an1kr1437xu6no.mysql.rds.aliyuncs.com/onepiece")
            .password("qa_wr1234").username("qa_wr").build());

    EnumContainer(IContainer container) {
        this.container = container;
    }

    @Getter
    private final IContainer container;
}
