package com.haisheng.framework.util;

import com.alibaba.fastjson.JSONArray;
import org.apache.tomcat.util.http.Parameters;

import java.text.DecimalFormat;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    public String genPhoneNum() {
        Random random = new Random();
        String num = "177" + (random.nextInt(89999999) + 10000000);

        return num;
    }

    public String genRandom7() {

        String tmp = UUID.randomUUID() + "";

        return tmp.substring(tmp.length() - 7);
    }

    public String genRandom(int num) {

        String tmp = UUID.randomUUID() + "";

        return tmp.substring(tmp.length() - num);
    }


    public String genRandom() {

        String tmp = UUID.randomUUID().toString();

        return tmp;
    }

    public String trimStr(String str) {

        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            str = m.replaceAll("");
        }
        return str;
    }

    public boolean compareJsonArray(JSONArray jsonArray1,JSONArray jsonArray2){

        boolean result = true;

        if (jsonArray1.size()!=jsonArray2.size()){
            result  = false;
        }else {
            if (!jsonArray1.equals(jsonArray2)){
                result  = false;
            }
        }

        return result;

    }

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
