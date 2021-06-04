package com.haisheng.framework.testng.bigScreen.yuntong.gly.Util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import org.springframework.util.StringUtils;

public class YunTongUtil extends TestCaseCommon {
    private static volatile YunTongUtil instance = null;
    private static String IpPort;
    private static EnumTestProduce product;
    private VisitorProxy visitor;

    /**
     * 单例
     *
     * @return YunTongUtil
     */
    public static synchronized YunTongUtil getInstance(EnumTestProduce product) {
        if (instance == null) {
            instance = new YunTongUtil(product);
            IpPort = product.getAddress();
        } else {
            if (YunTongUtil.product != product) {
                instance = new YunTongUtil(product);
                IpPort = product.getAddress();
            }
        }
        return instance;
    }

    private YunTongUtil(EnumTestProduce product) {
        this.product = product;
        this.IpPort = product.getAddress();
        this.visitor = new VisitorProxy(product);
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
            throw new RuntimeException("path不可为空");
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

    public JSONObject invokeApi(IScene scene) {
        return invokeApi(scene, true);
    }

    public JSONObject invokeApi(IScene scene, boolean checkCode) {
        return invokeApi(scene.getPath(), scene.getBody(), checkCode);
    }

    private JSONObject invokeApi(String path, JSONObject requestBody) {
        return invokeApi(path, requestBody, true);
    }


    /**
     * 销售客户列表
     */
    public JSONObject preSalesReceptionPage(String page, String size, String pram, String result) {
        String url = "/car-platform/pc/customer-manage/pre-sale-customer/page";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        if (pram != null) {
            json.put(pram, result);
        }
        return invokeApi(url, json);
    }


}
