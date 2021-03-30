package com.haisheng.framework.testng.bigScreen.xundianDaily;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.xundianDaily.hqq.fucPackage.StoreFuncPackage;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import org.springframework.util.StringUtils;
import org.testng.annotations.DataProvider;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

public class WechatScenarioUtil extends TestCaseCommon {

    /**
     * 单利，确保多个类共用一份类
     * 此部分不变，后面的方法自行更改
     */

    private static volatile WechatScenarioUtil instance = null;

    private WechatScenarioUtil() {
    }

    public static WechatScenarioUtil getInstance() {

        if (null == instance) {
            synchronized (StoreFuncPackage.class) {
                if (null == instance) {
                    //这里
                    instance = new WechatScenarioUtil();
                }
            }
        }

        return instance;
    }

    public void loginApplet(String token) {
        logger.info("token is ", token);
        authorization = token;
    }


    /***
     * 方法区，不同产品的测试场景各不相同，自行更改
     */
    public String IpPort = "http://123.57.148.247";

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
     * -------------------------------------INS小程序首页相关接口-----------------------------------------------------------------------------------------
     */
    /**
     * @description: 1.1小程序端banner显示
     * @author:
     * @time:
     */
    public JSONObject bannerList() throws Exception {
        String url = "/car-platform/applet/banner";
        String json =
                "{} ";
        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 1.2小程序-首页-文章列表
     * @author:
     * @time:
     */
    public JSONObject ArticleList() throws Exception {
        String url = "/car-platform/applet/article/list";
        String json =
                "{} ";
        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 1.3小程序-首页-文章列表更多-分页
     * @author:
     * @time:
     */
    public JSONObject ArticlePage(Integer size, JSONObject last_value) throws Exception {
        String url = "/car-platform/applet/article/page";
        JSONObject json = new JSONObject();
        json.put("size", size);
        json.put("last_value", last_value);
        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 1.3小程序-文章详情
     * @author:
     * @time:
     */
    public JSONObject ArticleDetail(Integer id, String share_id) throws Exception {
        String url = "/car-platform/applet/article/detail";
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("share_id", share_id);
        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 1.3小程序-门店列表
     * @author:
     * @time:
     */
    public JSONObject shopList(JSONArray coordinate, String washingStatus) throws Exception {
        String url = "/car-platform/applet/shop-list";
        JSONObject json = new JSONObject();
        json.put("coordinate", coordinate);
        json.put("washingStatus", washingStatus);
        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 1.3小程序名称
     * @author:
     * @time:
     */
    public JSONObject AppletName(JSONArray coordinate, String washingStatus) throws Exception {
        String url = "/car-platform/applet/name";
        String json =
                "{} ";
        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 2.1门店消费记录列表
     * @author:
     * @time:
     */
    public JSONObject ConsumptionPage(Integer page, Integer size) throws Exception {
        String url = "/patrol/wechat/member/consumption/page";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 2.2订单详情
     * @author:
     * @time:
     */
    public JSONObject ConsumptionDetail(String order_number) throws Exception {
        String url = "/patrol/wechat/member/consumption/detail";
        JSONObject json = new JSONObject();
        json.put("order_number", order_number);
        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 3.1会员等级列表
     * @author:
     * @time:
     */
    public JSONObject levelList(String order_number) throws Exception {
        String url = "/patrol/wechat/member/level/list";
        JSONObject json = new JSONObject();
        json.put("order_number", order_number);
        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 4.1完善个人信息
     * @author:
     * @time:
     */
    public JSONObject memberRegister(Integer id, String name, Integer gender, String card_number, String phone, String birthday) throws Exception {
        String url = "/patrol/wechat/member/register";
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("name", name);
        json.put("gender", gender);
        json.put("card_number", card_number);
        json.put("phone", phone);
        json.put("birthday", birthday);
        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 4.2会员信息
     * @author:
     * @time:
     */
    public JSONObject memberInfo() throws Exception {
        String url = "/patrol/wechat/member/info";
        String json =
                "{} ";
        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 查看附近门店
     * @author:
     * @time:
     */
    public JSONObject nearShops(String shop_name, double longitude, double latitude) throws Exception {
        String url = "/store/m-app/auth/shop/find-near-shops";
        JSONObject json = new JSONObject();
        json.put("shop_name", shop_name);
        json.put("longitude", longitude);
        json.put("latitude", latitude);
        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 口味排行
     * @author:
     * @time:
     */
    public JSONObject tasteSort(Integer number) throws Exception {
        String url = "/store/m-app/auth/taste/sort";
        JSONObject json = new JSONObject();
        json.put("number", number);
        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 新品推荐
     * @author:
     * @time:
     */
    public JSONObject newProduct(Integer id) throws Exception {
        String url = "/store/m-app/auth/taste/new-product-recommend";
        JSONObject json = new JSONObject();
        json.put("id", id);
        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 提交反馈
     * @author:
     * @time:
     */
    public JSONObject submitFeedback(Integer feedback_type_id, Integer feedback_score, String feedback_message) throws Exception {
        String url = "/store/m-app/auth/feedback/submit-feedback";
        JSONObject json = new JSONObject();
        json.put("feedback_type_id", feedback_type_id);
        json.put("feedback_score", feedback_score);
        json.put("feedback_message", feedback_message);
        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 反馈奖励
     * @author:
     * @time:
     */
    public JSONObject awardFeedback(int size) throws Exception {
        String url = "/store/m-app/auth/feedback/award-feedback";
        JSONObject json = new JSONObject();
        json.put("size", size);
        String res = httpPostWithCheckCode(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @description: 获取所有反馈类型
     * @author:
     * @time:
     */
    public JSONObject queryAll() throws Exception {
        String url = "/store/m-app/auth/feedback/feedback-type/query-all";
        String json =
                "{} ";
        String res = httpPostWithCheckCode(url, json, IpPort);
        return JSON.parseObject(res).getJSONObject("data");
    }
}