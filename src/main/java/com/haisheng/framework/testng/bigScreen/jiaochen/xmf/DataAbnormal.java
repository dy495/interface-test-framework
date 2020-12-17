package com.haisheng.framework.testng.bigScreen.jiaochen.xmf;

import org.testng.annotations.DataProvider;

public class DataAbnormal {

    @DataProvider(name = "PLATE")
    public static Object[] plate() {
        return new String[]{
                "苏BJ123",   //6位
                "BJ12345",    //不含汉字
                "京1234567",  //不含英文
                "京bj12345", //含小写
                "京B@12345", //含字母
                "苏BJ123456",//9位
        };
    }

    @DataProvider(name = "ERR_PHONE")
    public static Object[] errPhone() {
        return new String[]{
                "1231234123",
                "aaaaaaaaaaa",
                "汉字汉字",
                "10：10",
                "!@#$%^&*()_+{}:",
                "123a123好*123",
                "1         1",
                "123123412345"
        };
    }

    @DataProvider(name = "CODE")
    public static Object[] code() {
        return new String[]{
                "123123",
                "00000",
                "aaaaaa",
                "汉字汉字",
                "10：10",
                "!@#$%^&*()_+{}:",
                "123a123好*123",
                "1    1",
                "0000000"
        };
    }

}
