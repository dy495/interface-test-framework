package com.haisheng.framework.testng.CommonDataStructure;

import org.testng.annotations.DataProvider;

public class FaceTestInvalidPara {


    private String WHOLE_CHAR_CONTACT = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM1234567890"+
            "[]@-+~！#$^&()={}|;:'\\\"<>.?/"+
            "·！￥……（）——【】、；：”‘《》。？、,%*";
    private String GOOD_GRP_NAME = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM1234567890" +
            "[]@-+~！#$^&()={}|;:'\\\"<>.?/" +
            "·！￥……（）——【】、；：”‘《》。？、";
    private  String GOOD_SET_ID = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM1234567890@-_";

    @DataProvider(name = "BAD_SET_ID")
    public static Object[] createBadSetId() {
        return new String[] {
                "嗨", "", " ",
                //英文字符
                "~",  "！",  "#",  "$",  "%",
                "^",  "&",  "*",  "(",   ")",
                "=",  "{",  "}",  "|",   ";",
                "<",  ">",  ".",  "?",  "/",
                ":",  "'",  "\\\"",  ",", "[",
                "]",   "+",
                //中文字符
                "·",  "！",  "￥",  "……",  "（",
                "）",  "——",  "【",  "】",  "、",
                "；",  "：",  "”",  "‘",  "《",
                "，",  "》",  "。" ,  "？",  "、"
        };
    }

    @DataProvider(name = "GOOD_SET_ID")
    public static Object[] createGoodSetId() {
        return new String[] {
                "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM1234567890@-_",
        };
    }

    @DataProvider(name = "BAD_GRP_NAME")
    public static Object[] createBadGrpName() {

        return new String[] {
                //英文字符   group_name 入参校验 不能包含 ,%*
                ",",  "%", "*", "", " "
        };
    }

    @DataProvider(name = "GOOD_GRP_NAME")
    public static Object[] createGoodGrpName() {

        return new String[] {
                "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM1234567890",
                "[]@-+~！#$^&()={}|;:'\\\"<>.?/",
                "·！￥……（）——【】、；：”‘《》。？、"
        };
    }



    @DataProvider(name = "WHOLE_CHAR_CONTACT")
    public static Object[] wholeCharContact() {

        return new String[] {
                "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM1234567890",
                "[]@-+~！#$^&()={}|;:'\\\"<>.?/",
                "·！￥……（）——【】、；：”‘《》。？、,%*"
        };
    }

    @DataProvider(name = "EMPTY")
    public static Object[] empty() {

        return new String[] {
                "",
                " "
        };
    }

    @DataProvider(name = "DIGITAL")
    public static Object[] digital() {

        return new String[] {
                "-2147483648",
                "0",
                "214748365"
        };
    }

    @DataProvider(name = "BOOLEAN")
    public static Object[] boolValue() {

        return new Boolean[] {
                true,
                false
        };
    }
}