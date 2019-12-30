package com.haisheng.framework.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class ChkNotNullAndValid {


    private void checkJONotNull(String function, JSONObject jo, String key) throws Exception {

        if (jo.containsKey(key)) {
            JSONObject value = jo.getJSONObject(key);
            if (value == null || value.size() == 0) {
                throw new Exception(function + "返回结果中" + key + "对应的值为空");
            }
        } else {
            throw new Exception(function + "返回结果中不包含该key：" + key);
        }
    }

    private void checkJANotNull(String function, JSONObject jo, String key) throws Exception {

        if (jo.containsKey(key)) {
            JSONArray value = jo.getJSONArray(key);
            if (value == null || value.size() == 0) {
                throw new Exception(function + "返回结果中" + key + "对应的值为空");
            }
        } else {
            throw new Exception(function + "返回结果中不包含该key：" + key);
        }
    }

    private void checkStrNotNull(String function, JSONObject jo, String key) throws Exception {

        if (jo.containsKey(key)) {
            String value = jo.getString(key);
            if (value == null || "".equals(value)) {
                throw new Exception(function + "返回结果中" + key + "对应的值为空");
            }
        } else {
            throw new Exception(function + "返回结果中不包含该key：" + key);
        }
    }

    private void checkNotNull(String function, JSONObject jo, String key) throws Exception {

        if (key.contains("]-")) {
            String parentKey = key.substring(1, key.indexOf("]"));
            String childKey = key.substring(key.indexOf("-") + 1);
            checkDeepArrKeyNotNull(function, jo, parentKey, childKey);
        } else if (key.contains("}-")) {
            String parentKey = key.substring(1, key.indexOf("}"));
            String childKey = key.substring(key.indexOf("-") + 1);
            checkDeepObjectKeyNotNull(function, jo, parentKey, childKey);
        } else if (key.contains("]")) {
            key = key.substring(1, key.length() - 1);
            checkJANotNull(function, jo, key);
        } else if (key.contains("}")) {
            key = key.substring(1, key.length() - 1);
            checkJONotNull(function, jo, key);
        } else {
            checkStrNotNull(function, jo, key);
        }
    }

    private void checkDeepKeyValidity(String function, JSONObject jo, String key) throws Exception {

        if (key.contains("]-")) {
            String parentKey = key.substring(1, key.indexOf("]"));
            String childKey = key.substring(key.indexOf("-") + 1);
            checkArrKeyValidity(function, jo, parentKey, childKey);

        } else if (key.contains("}-")) {
            String parentKey = key.substring(1, key.indexOf("}"));
            String childKey = key.substring(key.indexOf("-") + 1);
            checkObjectKeyValidity(function, jo, parentKey, childKey);

        }
    }

    private void checkArrKeyValidity(String function, JSONObject jo, String parentKey, String childKey) throws Exception {

        JSONArray parent = jo.getJSONArray(parentKey);
        for (int i = 0; i < parent.size(); i++) {
            JSONObject single = parent.getJSONObject(i);
            checkKeyValues(function, single, childKey);
        }
    }

    private void checkObjectKeyValidity(String function, JSONObject jo, String parentKey, String childKey) throws Exception {

        JSONObject parent = jo.getJSONObject(parentKey);
        checkKeyValues(function, parent, childKey);
    }


    private void checkDeepArrKeyNotNull(String function, JSONObject jo, String parentKey, String childKey) throws Exception {

        checkJANotNull(function, jo, parentKey);
        JSONArray parent = jo.getJSONArray(parentKey);
        for (int i = 0; i < parent.size(); i++) {
            JSONObject single = parent.getJSONObject(i);
            checkKeyValue(function, single, childKey, "", false);
        }
    }

    private void checkDeepObjectKeyNotNull(String function, JSONObject jo, String parentKey, String childKey) throws Exception {

        checkJONotNull(function, jo, parentKey);

        JSONObject parent = jo.getJSONObject(parentKey);

        checkKeyValue(function, parent, childKey, "", false);
    }

    private void checkKeyValue(String function, JSONObject jo, String key, String value, boolean expectExactValue) throws Exception {

        if (!jo.containsKey(key)) {
            throw new Exception(function + "---没有返回" + key + "字段！");
        }

        String valueRes = jo.getString(key);

        if (expectExactValue) {
            Assert.assertEquals(valueRes, value, function + key + "字段值不相符：列表返回：" + valueRes + ", 期待：" + value);
        } else {
            if (valueRes == null || "".equals(valueRes)) {
                throw new Exception(function + key + "字段值为空！");
            }
        }
    }

    private void checkKeyMoreOrEqualValue(String function, JSONObject jo, String key, double value) throws Exception {

        if (!jo.containsKey(key)) {
            throw new Exception(function + "---没有返回" + key + "字段！");
        }

        double valueRes = jo.getDoubleValue(key);


        if (!(valueRes >= value)) {
            throw new Exception(key + "字段，期待>=" + value + "系统返回的value为：" + valueRes);
        }
    }

    private void checkKeyLessOrEqualValue(String function, JSONObject jo, String key, double value) throws Exception {

        if (!jo.containsKey(key)) {
            throw new Exception(function + "---没有返回" + key + "字段！");
        }

        double valueRes = jo.getDoubleValue(key);

        if (!(valueRes <= value)) {
            throw new Exception(key + "字段，期待>=" + value + "系统返回的value为：" + valueRes);
        }
    }

    private void checkKeyLessOrEqualKey(JSONObject jo, String key1, String key2, String function) throws Exception {

        checkNotNull(function, jo, key1);
        checkNotNull(function, jo, key2);

        double value1 = jo.getDoubleValue(key1);
        double value2 = jo.getDoubleValue(key2);

        //防止取值方面出现问题，value为空的时候也是不符合的
        if (!(value1 <= value2)) {
            throw new Exception(function + key1 + "：" + value1 + "，应该<=" + key2 + "：" + value2);
        }
    }

    private void checkKeyValues(String function, JSONObject jo, String... keyValues) throws Exception {

        for (String keyValue : keyValues) {
            //            注意其他判断与=判断的顺序
            if (keyValue.contains("[<=]")) {
                String key1 = keyValue.substring(0, keyValue.indexOf("["));
                String key2 = keyValue.substring(keyValue.indexOf("]") + 1);
                checkKeyLessOrEqualKey(jo, key1, key2, function);
            } else if (keyValue.contains(">=")) {
                String key = keyValue.substring(0, keyValue.indexOf(">"));
                String value = keyValue.substring(keyValue.indexOf("=") + 1);
                checkKeyMoreOrEqualValue(function, jo, key, Double.valueOf(value));
            } else if (keyValue.contains("<=")) {
                String key = keyValue.substring(0, keyValue.indexOf("<"));
                String value = keyValue.substring(keyValue.indexOf("=") + 1);
                checkKeyLessOrEqualValue(function, jo, key, Double.valueOf(value));
            } else if (keyValue.contains("=")) {
                String key = keyValue.substring(0, keyValue.indexOf("="));
                String value = keyValue.substring(keyValue.indexOf("=") + 1);

                checkKeyValue(function, jo, key, value, true);
            }
        }
    }
}
