package com.haisheng.framework.util;

import com.alibaba.fastjson.JSONObject;

/**
 * @author wangmin
 * @date 2020/7/27 14:17
 */
public class CommonUtil {
    public static String getStrFieldByData(JSONObject response, String field) {
        return response.getJSONObject("data").getString(field);
    }

    public static Integer getIntFieldByData(JSONObject response, String field) {
        return response.getJSONObject("data").getInteger(field);
    }

}
