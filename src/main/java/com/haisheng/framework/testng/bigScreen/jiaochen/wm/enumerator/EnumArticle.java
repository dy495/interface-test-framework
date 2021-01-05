package com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator;

import lombok.Getter;

public enum EnumArticle {

    ARTICLE_ONE("长期活动", 4144L, 238L),

    ARTICLE_TWO("报名页面领取卡券活动", 4205L, 249L),

    ARTICLE_THREE("批量报名活动", 4209L, 1L);

    EnumArticle(String name, Long dailyId, Long onlineId) {
        this.name = name;
        this.dailyId = dailyId;
        this.onlineId = onlineId;
    }

    @Getter
    private final String name;
    @Getter
    private final Long dailyId;

    @Getter
    private final Long onlineId;
}
