package com.haisheng.framework.util;

import java.text.DecimalFormat;

public class StringUtil {



    public static String changeUnderLineToLittleCamel(String para) {
        String[] arr = para.split("_");
        String camel = arr[0];
        for (int i=1;i<arr.length;i++) {
            camel +=  arr[i].substring(0, 1).toUpperCase() + arr[i].substring(1);
        }

        return camel;
    }

    public static String calAccuracyString(int numerator, int denominator) {

        DecimalFormat df = new DecimalFormat("#.00");
        float per = (float) numerator * 100 / (float) denominator;

        return df.format(per) + "%";
    }

    public static float calAccuracyFloat(int numerator, int denominator) {

        DecimalFormat df = new DecimalFormat("#.00");
        float per = (float) numerator / (float) denominator;

        return Float.parseFloat(df.format(per));
    }
}
