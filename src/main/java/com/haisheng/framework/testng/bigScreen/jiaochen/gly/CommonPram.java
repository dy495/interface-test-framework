package com.haisheng.framework.testng.bigScreen.jiaochen.gly;

import org.testng.annotations.DataProvider;

public class CommonPram {
    //正常的名字
    public String name="temporary";;
    //名字51个字
    @DataProvider(name = "Name")
    public static Object[][] name(){
        return new String[][]{
                {"名字51个字", "123456789012345678901234567890123456789012345678901"},
                {"名字为空",""},


        };
    }
    //手机号格式正常--现在的手机号是已存在的手机号
    public String phoneNumber="17611474518";
    //手机号格式异常
    @DataProvider(name = "PhoneFormat")
    public static Object[][] phoneNumberFormat(){
        return new String[][]{
                {"中文格式", "手机号呀呀呀呀呀呀呀呀"},
                {"英文格式", "GYGYHYYGYGY"},
                {"标点符号格式", "《》｛｝”？》：《）（（"},
                {"已存在的手机号","13373166806"},
                {"长度为10","1337316680"},
                {"长度为12","133731668066"},
                {"小程序未注册","18701300108"},
                {"空手机号",""},

        };
    }
    //性别格式
    @DataProvider(name = "Sex")
    public static Object[][] sex(){
        return new String[][]{
                {"性别：男", "1"},
                {"性别：女", "0"},
                {"不存在的性别参数", "hhhh"},
                {"性别为空",""}
        };
    }

    //车主类型格式
    @DataProvider(name = "CustomerType")
    public static Object[][] customerType(){
        return new String[][]{
                {"车主类型：个人", "PERSON"},
                {"车主类型：公司", "CORPORATION"},
                {"不存在的车主类型参数", "22222eee222"},
                {"车主类型为空", ""},
        };
    }
}
