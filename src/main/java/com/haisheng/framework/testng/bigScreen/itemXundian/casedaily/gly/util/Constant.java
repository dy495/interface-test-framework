package com.haisheng.framework.testng.bigScreen.itemXundian.casedaily.gly.util;

import org.testng.annotations.DataProvider;

public class Constant {

    /**
     * @description:门店事件触发状态
     * @author: gly
     * @time: 2020-3-26
     */
    @DataProvider(name = "SELECT_tasksListFilter")
    public static Object[][] tasksListFilter(){
        return new String[][]{
                {"event_state", "event_state"},
                {"trigger_rule", "trigger_rule"},
        };
    }



}
