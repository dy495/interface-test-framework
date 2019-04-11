package com.haisheng.framework.util;

public class StringUtil {



    public static String changeUnderLineToLittleCamel(String para) {
        String[] arr = para.split("_");
        String camel = arr[0];
        for (int i=1;i<arr.length;i++) {
            camel +=  arr[i].substring(0, 1).toUpperCase() + arr[i].substring(1);
        }

        return camel;
    }
}
