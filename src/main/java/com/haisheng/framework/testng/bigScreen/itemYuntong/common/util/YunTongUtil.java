package com.haisheng.framework.testng.bigScreen.itemYuntong.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import org.springframework.util.StringUtils;

public class YunTongUtil extends TestCaseCommon {
//    private static volatile YunTongUtil instance = null;
    private static String IpPort;
    private static EnumTestProduct product;
    private VisitorProxy visitor;

    /**
     * 单例
     *
     * @return YunTongUtil
     */
//    public static synchronized YunTongUtil getInstance(EnumTestProduce product) {
//        if (instance == null) {
//            instance = new YunTongUtil(product);
//            IpPort = product.getAddress();
//        } else {
//            if (YunTongUtil.product != product) {
//                instance = new YunTongUtil(product);
//                IpPort = product.getAddress();
//            }
//        }
//        return instance;
//    }
     public  YunTongUtil(EnumTestProduct product) {
        this.product = product;
        this.IpPort = product.getIp();
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
    public JSONObject preSalesCustomerPage(String page, String size, String pram, String result) {
        String url = "/car-platform/pc/customer-manage/pre-sale-customer/page";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        if (pram != null) {
            json.put(pram, result);
        }
        return invokeApi(url, json);
    }

    /**
     * 成交记录列表
     */
    public JSONObject preSalesBuyCarPage(String page, String size, String pram, String result) {
        String url = "/car-platform/pc/customer-manage/pre-sale-customer/buy-car/page";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        if (pram != null) {
            json.put(pram, result);
        }
        return invokeApi(url, json);
    }

    /**
     * 销售客户的门店名称和ID的列表
     */
    public JSONObject userShopList(){
        String url = "/pc/login-user/shop-list";
        JSONObject json = new JSONObject();
        return invokeApi(url, json);
    }

    /**
     * 销售客户的车系列表
     */
    public JSONObject preSaleStyleList(){
        String url = "/pc/customer-manage/pre-sale-customer/style-list";
        JSONObject json = new JSONObject();
        return invokeApi(url, json);
    }


    /**
     * @description :成交记录-时间筛选接口
     * * * @author: gly
     * @date :2020/06/08
     **/
    public JSONObject preSalesBuyCarPageTime(String page, String size, String startTime, String endTime) {
        String url = "/car-platform/pc/customer-manage/pre-sale-customer/buy-car/page";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        json.put("end_time", endTime);
        json.put("start_time", startTime);

        return invokeApi(url, json);
    }

    /**
     * 销售客户列表
     */
    public JSONObject preSalesCustomerPageTime(String page, String size, String startTime, String endTime) {
        String url = "/car-platform/pc/customer-manage/pre-sale-customer/page";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        json.put("end_time", endTime);
        json.put("start_time", startTime);
        return invokeApi(url, json);
    }

    /**
     * @description :销售接待记录
     *  @author: gly
     **/
    public JSONObject salesReceptionPage(String page, String size, String pram, String result) {
        String url = "/car-platform/pc/pre-sales-reception/page";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        if (pram != null) {
            json.put(pram, result);
        }
        return invokeApi(url, json);
    }

    /**
     * @description :销售接待记录--时间
     *  @author: gly
     **/
    public JSONObject salesReceptionPageTime(String page, String size, String reception_start, String reception_end) {
        String url = "/jiaochen/pc/pre-sales-reception/page";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        json.put("reception_end", reception_end);
        json.put("reception_start", reception_start);

        return invokeApi(url, json);
    }











}
