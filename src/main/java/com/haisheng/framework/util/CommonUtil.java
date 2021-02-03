package com.haisheng.framework.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.exception.DataException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wangmin
 * @date 2020/7/27 14:17
 */
public class CommonUtil {
    private static final Logger logger = LoggerFactory.getLogger(CommonUtil.class);

    public static String replace(String str, String subChar) {
        int s = str.indexOf(subChar);
        return str.replace(str.substring(0, s + 1), "");
    }

    /**
     * 判断是否包含中文
     *
     * @param str str
     * @return result
     */
    public static boolean isContainChinese(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        return m.find();
    }

    /**
     * 时间格式判断
     *
     * @param date   实际日期
     * @param format 比较的日期格式
     * @return boolean
     */
    public static boolean isLegalDate(String date, String format) {
        if (StringUtils.isEmpty(date) || date.length() < format.length()) {
            return false;
        }
        DateFormat dateFormat = new SimpleDateFormat(format);
        try {
            Date date1 = dateFormat.parse(date);
            return date.equals(dateFormat.format(date1));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断数组中是否包含一个数
     *
     * @param str  一个数
     * @param strs 一个数组
     * @return boolean 结果
     */
    public static boolean isContainStr(String str, String[] strs) {
        for (String s : strs) {
            if (s.equals(str)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 结果展示
     *
     * @param value value
     * @param <T>   T
     */
    @SafeVarargs
    public static <T> void valueView(T... value) {
        Arrays.stream(value).forEach(e -> logger.info("value:{}", e));
    }

    /**
     * 错误提示
     *
     * @param w 警告
     */
    public static void warning(String w) {
        System.err.println();
        System.err.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<" + w + ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.err.println();
    }

    /**
     * 日志打印
     *
     * @param v v
     */
    public static void log(String v) {
        String type = "---------------------------------------{}---------------------------------------";
        logger.info(type, v);
    }

    /**
     * 日志打印
     *
     * @param s s
     */
    public static <T> void logger(T s) {
        log("[" + s + "]" + "跑完");
    }

    public static String getStrField(JSONObject response, int index, String field) {
        String value = response.getJSONArray("list").getJSONObject(index).getString(field);
        return value == null ? "" : value;
    }

    public static Integer getIntField(JSONObject response, int index, String field) {
        return response.getJSONArray("list").getJSONObject(index).getInteger(field);
    }

    public static String getStrField(JSONArray jsonArray, int index, String field) {
        String value = jsonArray.getJSONObject(index).getString(field);
        return value == null ? "" : value;
    }

    public static List<String> getMoreParam(JSONObject object, String... paramName) {
        List<String> list = new ArrayList<>();
        Arrays.stream(paramName).forEach(e -> {
            if (StringUtils.isEmpty(e)) {
                throw new DataException("param类型应为String类型且不能为空");
            } else {
                if (!object.containsKey(e)) {
                    throw new DataException("object中不包含此key");
                }
                list.add(object.getString(e));
            }
        });
        return list;
    }

    /**
     * 取小数点后位数
     *
     * @param a     数值
     * @param digit 位数
     * @return double b
     */
    public static double getDecimal(double a, int digit) {
        int cardinal = (int) Math.pow(10, digit);
        return (double) Math.round(a * cardinal) / cardinal;
    }

    /**
     * 特殊计算
     */
    public static String getPercent(double a, double b) {
        if (a == 0 && b == 0) {
            return "0.0%";
        }
        if (b == 0) {
            return "100.0%";
        } else {
            return getPercent(a, b, 4);
        }
    }

    /**
     * 获取百分比
     *
     * @param a     a
     * @param b     b
     * @param scale 保留小数点后位数
     * @return result
     */
    public static String getPercent(double a, double b, int scale) {
        if (b == 0) {
            return "0.0%";
        }
        double c = new BigDecimal(a / b).divide(new BigDecimal(1), scale, BigDecimal.ROUND_HALF_UP).doubleValue();
        StringBuilder stringBuilder = new StringBuilder();
        NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMinimumFractionDigits(2);
        String str = nf.format(c);
        if (String.valueOf(str.charAt(str.length() - 2)).equals("0")) {
            stringBuilder.append(str);
            stringBuilder.replace(stringBuilder.length() - 2, stringBuilder.length() - 1, "");
            String s = stringBuilder.toString();
            return getS(s);
        }
        return getS(str);
    }

    private static String getS(final String y) {
        String result = y;
        if (y.contains(",")) {
            result = y.replace(y.substring(y.indexOf(","), y.indexOf(",") + 1), "");
        }
        return result;
    }

    public static String humpToLine(String str) {
        Pattern humpPattern = Pattern.compile("[A-Z]");
        Matcher matcher = humpPattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString().replaceFirst("_", "");
    }

    /**
     * 获取页面跳转页数
     * 当接口每页只能传入pageSize时，获取接口的访问次数来得到list.size()
     *
     * @param listSize list的尺寸
     * @param pageSize 接口要求的size
     * @return a
     */
    public static int getTurningPage(double listSize, double pageSize) {
        if (listSize < 0) {
            throw new DataException("listSize不可为负数");
        }
        if (pageSize < 0) {
            throw new DataException("pageSize不可为负数");
        }
        double a;
        a = listSize > pageSize ? listSize % pageSize == 0 ? listSize / pageSize : Math.ceil(listSize / pageSize) + 1 : 2;
        return (int) a;
    }

    /**
     * 获取随机数
     *
     * @param digitNumber 位数
     * @return 结果
     */
    public static String getRandom(int digitNumber) {
        return digitNumber == 0 ? "" : String.valueOf((int) ((Math.random() * 9 + 1) * (Math.pow(10, digitNumber - 1))));
    }

    /**
     * 获取随机数
     *
     * @param initial  初始
     * @param eventual 结束
     * @return 结果
     */
    public static Integer getRandom(int initial, int eventual) {
        Random random = new Random();
        return random.nextInt(eventual - initial) + initial;
    }

    /**
     * 集合去重
     *
     * @param arr 集合
     * @return 一个新集合
     */
    private static List<String> removeDuplicates(List<String> arr) {
        List<String> list = new ArrayList<>();
        Iterator<String> it = arr.iterator();
        while (it.hasNext()) {
            String a = it.next();
            if (list.contains(a)) {
                it.remove();
            } else {
                list.add(a);
            }
        }
        return list;
    }

    /**
     * 判断字符串列表每一项均不为空，包括空字符串与null
     *
     * @param strList 字符串列表
     * @return boolean 有一个为空则返回false，全不为空返回true
     */
    public static boolean strListNotNull(String[] strList) {
        boolean notNull = true;
        for (String str : strList) {
            if (StringUtils.isEmpty(str)) {
                notNull = false;
                break;
            }
        }
        return notNull;
    }

    /**
     * 对比两个数组
     *
     * @param obj1 第一个数组
     * @param obj2 第二个数组
     * @return boolean 数量、内容和顺序都一致返回true，有任意一项不满足返回false
     */
    public static boolean arrayEquals(Object[] obj1, Object[] obj2) {
        if (obj1 == null && obj2 == null) {
            return true;
        }
        if (obj1 != null && obj1.length == 0 && obj2 != null && obj2.length == 0) {
            return true;
        }
        if (obj1 != null && obj2 != null && obj1.length == obj2.length) {
            for (int i = 0; i < obj1.length; i++) {
                if (!obj1[i].equals(obj2[i])) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Contract(pure = true)
    public static <T> void checkResult(String key, T value, T expect, T actual) {
        String reason = key + "为：" + value + " 预期结果：" + expect + " 实际结果：" + actual;
        check(expect, actual, reason);
    }

    public static <T> void checkResult(String key, T expect, T actual) {
        String reason = key + " 预期结果：" + expect + " 实际结果：" + actual;
        check(expect, actual, reason);
    }

    public static <T> void checkResultPlus(String key1, T value1, String key2, T value2) {
        String reason = key1 + ": " + value1 + " " + key2 + ": " + value2;
        check(value1, value2, reason);
    }

    private static <T> void check(T expect, T actual, String reason) {
        valueView(expect, actual);
        if (expect == null || actual == null) {
            Preconditions.checkArgument(actual == expect, reason);
        } else {
            Preconditions.checkArgument(expect.equals(actual), reason);
        }
    }

    @NotNull
    @Contract(pure = true)
    public static <T> String result(T expect, T actual) {
        return " 预期结果：" + expect + " 实际结果：" + actual;
    }

    /**
     * 检查接口是否返回1000，且指定data下的字段是否存在
     * yu， 2020.10.22
     */
    public static void checkResult(String result, String... checkColumnNames) throws Exception {
        logger.info("result = {}", result);
        StringUtil.checkNull(result, "response");
        JSONObject res = JSONObject.parseObject(result);
        if (!res.getInteger("code").equals(1000)) {
            throw new Exception("result code is " + res.getInteger("code") + ", request id: " + res.getString("request_id"));
        }
        Preconditions.checkArgument(res.containsKey("data"), "response中未包含data字段, request id: " + res.getString("request_id"));
        for (String checkColumn : checkColumnNames) {
            Object column = res.getJSONObject("data").get(checkColumn);
            logger.info("{} : {}", checkColumn, column);
            if (column == null) {
                throw new Exception("result does not contains column " + checkColumn + ", request id: " + res.getString("request_id"));
            }
        }
    }
}
