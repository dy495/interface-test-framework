package com.haisheng.framework.testng.commonDataStructure;

import org.testng.annotations.DataProvider;

public class InvalidPara {

    @DataProvider(name = "PUNCTUATION")
    public static Object[] punctuation() {

        //中文字符！、人体可以注册，删除时失败
        return new String[] {
                "嗨",
                //英文字符
                "~",  "！", "@",  "#",  "$",
                "%",  "^",  "&",  "*",  "(",
                ")",  "-",  "+",  "=",  "{",
                "}",  "[",  "]", "|",
                ";",  ":",  "'",  "\\\"",  ",",
                "<",  ">",  ".",  "?",  "/",
                //中文字符
                "·",  "！",  "￥",  "……",  "（",
                "）",  "——",  "【",  "】",  "、",
                "；",  "：",  "”",  "‘",  "《",
                "，",  "》",  "。" ,  "？",  "、",
                //前后带空格的，看查询中不带空格能查出来吗?其实最好能查出来，前后的空格希望可以trim掉
                " a4d4d18741a8",
                "a4d4d18741a8 ",
                " 00000",
                //特殊数字
                "11",  "0",   "-1",   "01",  "0.1",
                "2.2", "-0.1","-2.2", "1.0"
        };
    }

    @DataProvider(name = "BLANK")
    public static Object[] blank() {

        return new String[] {
                "",  " "
        };
    }

    @DataProvider(name = "PUNCTUATION_CHAR")
    public static Object[] punctuationChar() {

        return new String[] {
                "嗨",
                //英文字符
                "~",  "！", "@",  "#",  "$",
                "%",  "^",  "&",  "*",  "(",
                ")",  "-",  "+",  "=",  "{",
                "}",  "[",  "]", "|",
                ";",  ":",  "'",  "\\\"",  ",",
                "<",  ">",  ".",  "?",  "/",
                //中文字符
                "·",  "！",  "￥",  "……",  "（",
                "）",  "——",  "【",  "】",  "、",
                "；",  "：",  "”",  "‘",  "《",
                "，",  "》",  "。" ,  "？",  "、",
                //前后带空格的，看查询中不带空格能查出来吗?其实最好能查出来，前后的空格希望可以trim掉
                " a",
                "a ",
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
