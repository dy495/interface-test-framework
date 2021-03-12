package com.haisheng.framework.testng.bigScreen.jiaochen.wm.freemarker.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 格式转化工具
 *
 * @author wangmin
 * @date 2021/3/8 20:15
 */
public class TransformUtil {

    public static String firstCharUpper(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * 驼峰转下划线
     *
     * @param str str
     * @return result
     */
    public static String humpToLine(String str) {
        Pattern humpPattern = Pattern.compile("[A-Z]");
        Matcher matcher = humpPattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 下划线转驼峰
     *
     * @param str              str
     * @param firstIsUpperCase 首字母是否大写
     * @return result
     */
    public static String lineToHump(String str, Boolean firstIsUpperCase) {
        Pattern linePattern = Pattern.compile("[_|-](\\w)");
        str = str.toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        if (firstIsUpperCase) {
            String s = sb.toString().substring(0, 1).toUpperCase();
            return s + sb.toString().substring(1, sb.length());
        } else {
            return sb.toString();
        }
    }
}
