package com.haisheng.framework.testng.bigScreen.xundianDaily.zt;

import org.testng.annotations.DataProvider;

public class DataProviderMethod {

    @DataProvider(name = "device_id")
    public Object[] cameraId() {
        return new String[][]{
                {"8061349193417728"},
                {"8097818264503296"},
                {"8058611994690560"}

        };
    }

    @DataProvider(name = "type")
    public Object[] type_1() {
        return new String[][]{
                {"NORMAL"},
                {"COMMUNITY"},
                {"PLAZA"},
                {"FLAGSHIP"},

        };
    }

    @DataProvider(name = "is_read")
    public Object[] isread() {
        return new Boolean[][]{
                {null},
                {true},
                {false},
        };
    }


    @DataProvider(name = "memberId")
    public Object[] memberId() {

        return new String[]{
                "120",
                "110",
                "119",
                "11223344",
        };
    }

    @DataProvider(name = "memberName")
    public Object[] memberName() {
        return new String[]{
                "测试角色1",
                "测试角色2",
                "测试角色3",
        };
    }

    @DataProvider(name = "memberPhone")
    public Object[] memberPhone() {
        return new String[]{
                "1234567790",
                "123123213213",
                "123123123123",
        };
    }

    @DataProvider(name = "userId")
    public Object[] userId() {
        return new String[]{
                "wqd12er32ref122312dse21122",
                "11321rfdsf13r3r43gqwe23rf4",
                "12ede2f_3e2defqfec_13fsd2e",
        };
    }


    @DataProvider(name = "identity")
    public Object[] identity() {
        return new String[]{
                "123",
                "234",
                "345",
        };
    }



    @DataProvider(name = "visitId")
    public Object[] visitId() {
        return new String[]{
                "1234",
                "1232",
                "1233",
        };
    }

    @DataProvider(name = "visitName")
    public Object[] visitName() {
        return new String[]{
                "测试1",
                "测试2",
                "测试3",
        };
    }

    @DataProvider(name = "visitShopName")
    public Object[] visitShopName() {
        return new String[]{
                "测试1",
                "测试2",
                "AI-Test(门店订单录像)",
        };
    }

    @DataProvider(name = "FACE_URL")
    public static Object[] face_url(){
        return new String[]{
                "src/main/java/com/haisheng/framework/testng/bigScreen/xundianDaily/pic/正脸.jpg",

        };
    }
}
