package com.haisheng.framework.testng.yu;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;

public class ScenarioUtil extends TestCaseCommon {

    /**
     * 单利，确保多个类共用一份类
     * 此部分不变，后面的方法自行更改
     *
     * */

    private static volatile ScenarioUtil instance = null;

    private ScenarioUtil() {}


    public static ScenarioUtil getInstance() {

        if (null == instance) {
            synchronized (ScenarioUtil.class) {
                if (null == instance) {
                    instance = new ScenarioUtil();
                }
            }
        }

        return instance;
    }


    /***
     * 方法区，不同产品的测试场景各不相同，自行更改
     */

    public JSONObject scopeAdd(String scopeName, String scopeType, String parentID) throws Exception {
        String url = "/business/passage/SCOPE_ADD/v1.0";
        String json = "{\n" +
                "   \"scope_name\":\"" + scopeName + "\",\n";
        if (!parentID.equals("")){

            json = json + "   \"parent_id\":" + Long.parseLong(parentID) + ",\n";

        }
        json = json + "   \"scope_type\":" + Integer.parseInt(scopeType) + "\n}";
        String res = apiCustomerRequest(url, json);

        return JSON.parseObject(res);
    }
}
