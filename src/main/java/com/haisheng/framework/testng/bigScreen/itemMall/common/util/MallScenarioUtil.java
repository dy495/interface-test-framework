package com.haisheng.framework.testng.bigScreen.itemMall.common.util;

import com.alibaba.fastjson.JSONObject;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.haisheng.framework.testng.bigScreen.itemXundian.casedaily.hqq.fucPackage.StoreFuncPackage;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import java.text.NumberFormat;
import java.util.Map;

public class MallScenarioUtil extends TestCaseCommon {

    /**
     * 单利，确保多个类共用一份类
     * 此部分不变，后面的方法自行更改
     */

    private static volatile MallScenarioUtil instance = null;

    private MallScenarioUtil() {
    }

    public static MallScenarioUtil getInstance() {

        if (null == instance) {
            synchronized (StoreFuncPackage.class) {
                if (null == instance) {
                    //这里
                    instance = new MallScenarioUtil();
                }
            }
        }

        return instance;
    }


    /***
     * 方法区，不同产品的测试场景各不相同，自行更改
     */
    public String IpPort = "http://dev.sso.mall.store.winsenseos.cn";

    public String httpGet(String path, Map<String, Object> paramMap, String IpPort) throws Exception {
        initHttpConfig();
        StringBuilder stringBuilder = new StringBuilder();
        String queryUrl = IpPort + path + "?";
        for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
            String key = entry.getKey();
            Object value = paramMap.get(key);
            stringBuilder.append("&").append(key).append("=").append(value);
        }
        String param = stringBuilder.toString().replaceFirst("&", "");
        config.url(queryUrl + param);
        logger.info("{} json param: {}", path.replace("?", ""), param);
        long start = System.currentTimeMillis();
        response = HttpClientUtil.get(config);
        logger.info("response: {}", response);
        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);
        caseResult.setResponse(response);
        return response;
    }

    /**
     * @description:登录
     * @author: qingqing
     * @time:
     */
    public void login(String userName, String passwd) {
        initHttpConfig();
        String path = "/account-platform/login-pc";
        String loginUrl = IpPort + path;
        String json = "{\"type\":0, \"username\":\"" + userName + "\",\"password\":\"" + passwd + "\"}";
        config.url(loginUrl)
                .json(json);
        logger.info("{} json param: {}", path, json);
        long start = System.currentTimeMillis();
        try {
            response = HttpClientUtil.post(config);
            authorization = JSONObject.parseObject(response).getJSONObject("data").getString("token");
            logger.info("authorization:" + authorization);
        } catch (Exception e) {
            appendFailReason(e.toString());
        }
        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);
        //saveData("登陆");
    }


    /**
     * double 格式化成百分比
     *
     * @param num1
     * @return String
     */
    public static String getPercentFormat(String num1) {
        NumberFormat nf = java.text.NumberFormat.getPercentInstance();
        nf.setMinimumFractionDigits(2);// 小数点后保留几位
        return nf.format(Double.parseDouble(num1));
    }
}

