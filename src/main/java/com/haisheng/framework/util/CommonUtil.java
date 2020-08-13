package com.haisheng.framework.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.model.experiment.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.crm.CrmScenarioUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @author wangmin
 * @date 2020/7/27 14:17
 */
public class CommonUtil {

    private static final Logger logger = LoggerFactory.getLogger(CommonUtil.class);

    public static String getStrFieldByData(JSONObject response, String field) {
        String value = response.getString(field);
        return value == null ? "" : value;
    }

    public static String getStrFieldByData(JSONObject response, int index, String field) {
        String value = response.getJSONArray("list").getJSONObject(index).getString(field);
        return value == null ? "" : value;
    }

    public static Integer getIntFieldByData(JSONObject response, String field) {
        return response.getInteger(field);
    }

    public static Integer getIntFieldByData(JSONObject response, int index, String field) {
        return response.getJSONArray("list").getJSONObject(index).getInteger(field);
    }

    /**
     * 删除垃圾客户
     */
    public static void deleteCustomer(String customerName) {
        CrmScenarioUtil crm = CrmScenarioUtil.getInstance();
        crm.login(EnumAccount.XSZJ.getUsername(), EnumAccount.XSZJ.getPassword());
        JSONObject response = crm.customerList(customerName, "", "", "", "", 1, 1000);
        JSONArray list = response.getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            int customerId = list.getJSONObject(i).getInteger("customer_id");
            crm.customerDelete(customerId);
        }
    }

    /**
     * 重复电话号数量
     *
     * @param response response
     * @return int
     */
    public static int phoneDuplicates(JSONObject response) {
        List<String> arr = new ArrayList<>();
        JSONArray list = response.getJSONArray("list");
        int a = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.getJSONObject(i).getString("user_status_name").equals("完成接待")
                    || list.getJSONObject(i).getString("user_status_name").equals("接待中")) {
                String customerPhone = list.getJSONObject(i).getString("customer_phone");
                if (customerPhone == null) {
                    arr.add("null");
                }
                arr.add(customerPhone);
                a++;
            }
        }
        int i = removeDuplicates(arr).size();
        logger.info("接待的总数量：{}", a);
        logger.info("电话号去重后接待数量：{}", i);
        logger.info("电话号重复的数量：{}", a - i);
        return a - i;
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
     * 结果展示
     *
     * @param value value
     * @param <T>   T
     */
    @SafeVarargs
    public static <T> void valueView(T... value) {
        Arrays.stream(value).forEach(e -> logger.info("value:{}", e));
    }
}
