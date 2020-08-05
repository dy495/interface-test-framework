package com.haisheng.framework.util;

import com.alibaba.fastjson.JSONObject;

/**
 * @author wangmin
 * @date 2020/7/27 14:17
 */
public class CommonUtil {
    public static String getStrFieldByData(JSONObject response, String field) {
        return response.getString(field);
    }

    public static String getStrFieldByData(JSONObject response, int index, String field) {
        return response.getJSONArray("list").getJSONObject(index).getString(field);
    }

    public static Integer getIntFieldByData(JSONObject response, String field) {
        return response.getInteger(field);
    }

    public static Integer getIntFieldByData(JSONObject response, int index, String field) {
        return response.getJSONArray("list").getJSONObject(index).getInteger(field);
    }
}
