package com.haisheng.framework.testng.CommonDataStructure;

import org.testng.annotations.DataProvider;

public class DmpScope {


    @DataProvider(name = "DMP_DAILY_SCOPE")
    public static Object[][] dmpDailyScope() {
        return new String[][] {
                // uid, node-id/品牌id, shop-id/业务id, com
                {"uid_7fc78d24","55","8","赢识日常"}
        };
    }


    @DataProvider(name = "DMP_ONLINE_SCOPE")
    public static Object[][] dmpOnlineScope() {
        return new String[][] {
                {"uid_09845c0e","241","242","万达广场丰科店线上"},
                {"uid_7fc78d24","668","669","购物中心(demo)线上"}
        };
    }
}
