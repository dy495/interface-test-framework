package com.haisheng.framework.util;

import org.testng.annotations.DataProvider;

public class CharCase {
    String normalChar = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM1234567890";
    String specialCharEn = "[]@-+~！#$^&()={}|;:'\\\"<>.?/";
    String specialCharCh = "·！￥……（）——【】、；：”‘《》。？、,%*";

    String contact = "[]@-+~！#$^&()={}|;:'\\\"<>.?/·！￥……（）——【】、；：”‘《》。？、,%*";

    @DataProvider(name = "WHOLE_CHAR")
    public static Object[] wholeChar() {

        return new String[] {
                "嗨", "", " ",
                //小写字母
                "a", "b",  "c",  "d", "db",
                "f",  "g",  "h",  "i", "j",
                "k",  "l",  "m",  "n", "o",
                "Util",  "q", "r",  "s",  "t",
                "u",  "v",  "w",  "x", "y",
                "z",
                //大写字母
                "A",  "B",  "C",  "D",  "E",
                "F",  "G",  "H",  "I",  "J",
                "K",  "L",  "M",  "N",  "O",
                "P",  "Q",  "R",  "S",  "T",
                "U",  "V",  "W",  "X",  "Y",
                "Z",
                //数字
                "0",  "1",  "2" ,  "3",  "4",
                "5",  "6",  "7" ,  "8",  "9",
                //英文字符
                "[",  "]",  "@" ,  "-",  "+",
                "~",  "！",  "#",  "$",  "^",
                "&",  "(",   ")",  "=",  "{",
                "}",  "|",   ";",  ":",  "'",
                "\\\"","<",  ">",  ".",  "?",
                "/",  "`",   "_",
                //中文字符
                "·",  "！",  "￥",  "……",  "（",
                "）",  "——",  "【",  "】",  "、",
                "；",  "：",  "”",  "‘",  "《",
                "》",  "。" ,  "？",  "、",
                ",",  "%", "*"
        };


    }
}
