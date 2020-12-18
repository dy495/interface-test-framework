package com.haisheng.framework.testng.bigScreen.xundianDaily.enumerator;

import org.apache.commons.lang.StringUtils;

import java.util.Arrays;

public enum EnumAppPageType {
    /**
     * 首页上面
     **/
    HOME_TOP,

    /**
     * 首页下面
     **/
    HOME_BELOW,

    /**
     * 关注页面
     **/
    FOLLOW_PAGE,

    /**
     * 门店列表页面
     **/
    DATA_LIST_PAGE,

    /**
     * 巡店中心页面
     **/
    PATROL_CENTER_PAGE,
    ;

    EnumAppPageType() {
    }

    public static EnumAppPageType getByType(String dataType) {
        if (StringUtils.isEmpty(dataType)) {
            return null;
        }
        return Arrays.stream(EnumAppPageType.values()).filter(appPageTypeEnum ->
                appPageTypeEnum.name().equals(dataType)).findAny().orElse(null);
    }
}
