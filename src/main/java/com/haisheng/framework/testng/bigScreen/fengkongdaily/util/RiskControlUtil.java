package com.haisheng.framework.testng.bigScreen.fengkongdaily.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.exception.DataException;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.fengkongdaily.scene.LoginPcScene;
import com.haisheng.framework.testng.bigScreen.jiaochen.ScenarioUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import org.springframework.util.StringUtils;

public class RiskControlUtil extends TestCaseCommon {
    private static volatile ScenarioUtil instance = null;
    private static final String shopId = EnumTestProduce.FK_DAILY.getShopId();
    public static String IpPort=EnumTestProduce.FK_DAILY.getAddress();
    private static final EnumTestProduce product = EnumTestProduce.FK_DAILY;
    public VisitorProxy visitor = new VisitorProxy(product);
    /**
     * 单例
     *
     * @return ScenarioUtil
     */
    public static ScenarioUtil getInstance() {
        if (instance == null) {
            synchronized (ScenarioUtil.class) {
                if (instance == null) {
                    instance = new ScenarioUtil();
                }
            }
        }
        return instance;
    }

    private JSONObject invokeApi(String path, JSONObject requestBody) {
        return invokeApi(path, requestBody, true);
    }

    /**
     * http请求方法调用
     *
     * @param path        路径
     * @param requestBody 请求体
     * @param checkCode   是否校验code
     * @return JSONObject response.data
     */
    public JSONObject invokeApi(String path, JSONObject requestBody, boolean checkCode) {
        if (StringUtils.isEmpty(path)) {
            throw new DataException("path不可为空");
        }
        String request = JSON.toJSONString(requestBody);
        String result = null;
        if (checkCode) {
            result = httpPostWithCheckCode(path, request, IpPort);
            return JSON.parseObject(result).getJSONObject("data");
        } else {
            try {
                result = httpPost(path, request, IpPort);
            } catch (Exception e) {
                appendFailReason(e.toString());
            }
            return JSON.parseObject(result);
        }
    }

    /**
     * @description:风控登录
     * @author:gly
     * @time:2021/4/1
     */
    public String pcLogin(String phone, String password) {
        IScene scene= LoginPcScene.builder().type(0).username(phone).password(password).build();
        String message=visitor.invokeApi(scene,false).getString("message");
        return message;
    }


}
