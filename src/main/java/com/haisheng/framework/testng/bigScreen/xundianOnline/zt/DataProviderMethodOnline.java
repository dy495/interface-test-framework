package com.haisheng.framework.testng.bigScreen.xundianOnline.zt;

import org.testng.annotations.DataProvider;

public class DataProviderMethodOnline {
    @DataProvider(name = "device_id")
    public Object[] cameraId() {
        return new String[][]{
                {"8070127575729152","yushi150-3"},
                {"8070121884156928","yushipingtai(全功能)"},


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
                "uid_2cd5f8b4"
        };
    }

    @DataProvider(name = "memberName")
    public Object[] memberName() {
        return new String[]{
                "测试角色1",
                "小姐姐",
                "小2姐",
                "小姐姐2"
        };
    }

    @DataProvider(name = "memberPhone")
    public Object[] memberPhone() {
        return new String[]{
                "1234567790",
                "123123213213",
                "123123123123",
                "13604609869",
                "136"
        };
    }

    @DataProvider(name = "userId")
    public Object[] userId() {
        return new String[]{
                "wqd12er32ref122312dse21122",
                "11321rfdsf13r3r43gqwe23rf4",
                "12ede2f_3e2defqfec_13fsd2e",
                "2fd55722-9109-4fa8-b084-fcb9d93a4b2f"
        };
    }



    @DataProvider(name = "userIdName")
    public Object[] userIdName() {
        return new String[][]{
                {"uid_2cd5f8b4","小姐姐"},
                {"uid_2cd5f8b4","小姐姐2"},

        };
    }

    @DataProvider(name = "userList1")
    public Object[] userList1() {
        return new String[][]{
                {"uid_2cd5f8b4","小姐姐","13604609869"},
                {"uid_2cd5f8b4","小姐姐2","13604609869"},

        };
    }


    @DataProvider(name = "userList2")
    public Object[] userList2() {
        return new String[][]{
                {"uid_2cd5f8b4","小姐姐","13604609869","2fd55722-9109-4fa8-b084-fcb9d93a4b2f"},
                {"uid_2cd5f8b4","小姐姐2","13604609869","2fd55722-9109-4fa8-b084-fcb9d93a4b2f"},

        };
    }

    @DataProvider(name = "identity")
    public Object[] identity() {
        return new String[]{
                "123",
                "234",
                "345",
                "130"
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
                "src/main/java/com/haisheng/framework/testng/bigScreen/xundianDaily/pic/女人脸.jpg",

        };
    }

}