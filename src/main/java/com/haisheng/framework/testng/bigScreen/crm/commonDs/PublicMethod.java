package com.haisheng.framework.testng.bigScreen.crm.commonDs;

import com.alibaba.fastjson.JSONArray;
import com.haisheng.framework.testng.bigScreen.crm.CrmScenarioUtil;
import com.haisheng.framework.util.CommonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PublicMethod {
    CrmScenarioUtil crm = CrmScenarioUtil.getInstance();

    /**
     * 获取人员列表
     *
     * @return list
     */
    public List<Map<String, String>> getSaleList(String roleName) {
        List<Map<String, String>> array = new ArrayList<>();
        int total = crm.userUserPage(1, 10).getInteger("total");
        int s = CommonUtil.pageTurning(total, 100);
        for (int i = 1; i < s; i++) {
            JSONArray list = crm.userUserPage(i, 100).getJSONArray("list");
            for (int j = 0; j < list.size(); j++) {
                if (list.getJSONObject(j).getString("role_name").equals(roleName)) {
                    Map<String, String> map = new HashMap<>(16);
                    String userId = list.getJSONObject(j).getString("user_id");
                    String userName = list.getJSONObject(j).getString("user_name");
                    map.put("userId", userId);
                    map.put("userName", userName);
                    array.add(map);
                }
            }
        }
        Map<String, String> map = new HashMap<>();
        map.put("userId", "");
        map.put("userName", "总经理");
        array.add(map);
        return array;
    }
}
