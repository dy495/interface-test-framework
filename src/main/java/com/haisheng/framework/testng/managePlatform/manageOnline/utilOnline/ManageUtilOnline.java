package com.haisheng.framework.testng.managePlatform.manageOnline.utilOnline;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.arronlong.httpclientutil.builder.HCB;
import com.arronlong.httpclientutil.common.HttpConfig;
import com.arronlong.httpclientutil.common.HttpHeader;
import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.testng.annotations.BeforeClass;

import java.util.Objects;
import java.util.UUID;

public class ManageUtilOnline extends TestCaseCommon {
    /**
     * 单利，确保多个类共用一份类
     * 此部分不变，后面的方法自行更改
     */
    private static volatile ManageUtilOnline instance = null;

    private ManageUtilOnline() {
    }

    public static ManageUtilOnline getInstance() {
        if (null == instance) {
            synchronized (ManageUtilOnline.class) {
                if (null == instance) {
                    //这里
                    instance = new ManageUtilOnline();
                }
            }
        }
        return instance;
    }

    HttpClient client = null;


    @BeforeClass
    public  void init() throws HttpProcessException {
        client = HCB.custom()
                .pool(50, 10)
                .retry(3).build();
    }

    /***
     * 方法区，不同产品的测试场景各不相同，自行更改
     */
    public String os_data = "http://39.106.253.135";
    //public String os_device = "http://39.105.225.12:8888";
    /**
     * @description:登录
     * @author: qingqing
     * @time:
     */
    public String login(String userName, String passwd) {
        initHttpConfig();
        String path = "/administrator/login";
        String loginUrl = os_data + path;
        String json = "{\"type\":0, \"email\":\"" + userName + "\",\"password\":\"" + passwd + "\"}";
        config.url(loginUrl)
                .json(json);
        logger.info("{} json param: {}", path, json);
        long start = System.currentTimeMillis();
        try {
            response = HttpClientUtil.post(config);
            JSONObject jsonObject = JSONObject.parseObject(response);
            JSONObject data = jsonObject.getJSONObject("data");
            if (Objects.nonNull(data)){
                authorization = data.getString("token");
                logger.info("authorization: {}", authorization);
                return authorization;
            }
        } catch (Exception e) {
            appendFailReason(e.toString());
        }
        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);
        //saveData("登陆");
        return authorization;
    }

    public JSONObject http(String requestUrl ,Object requestJson){

        Header[] headers = HttpHeader.custom()
                .other("Accept", "application/json")
                .other("Content-Type", "application/json;charset=utf-8")
                .other("request_id", UUID.randomUUID().toString())
                .other("Authorization", login("huangqingqing@winsense.ai", "1a972efae9dba34da19d48fc94fe008c"))
                .other("api_source", "BUSINESS_PATROL")
                .build();

        HttpConfig config = HttpConfig.custom().headers(headers).url(requestUrl).json(JSON.toJSONString(requestJson)).client(client);
        try {
            String post = HttpClientUtil.post(config);
            return JSON.parseObject(post);
        } catch (HttpProcessException e) {
            // logger.error("http调用失败 requestUrl =  {} , requestJson =  {} , headers =  {}" ,requestUrl,requestJson,JSON.toJSONString(headers),e);
        }
        return null;
    }


    /**
     * @author qingqing
     * @description 门店管理(2020.10.14)
     */
    public JSONObject store_manage(Integer page, Integer size, Long subject_id, Long subject_name, Long subject_type, String app_id,String uid) throws Exception {
        String url = "/admin/data/subject/list";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        json.put("subject_id", subject_id);
        json.put("subject_name", subject_name);
        json.put("subject_type", subject_type);
        json.put("app_id", app_id);
        json.put("uid", uid);
        JSONObject http = this.http(os_data + url, json);
        return http.getJSONObject("data");
    }

    /**
     * @author qingqing
     * @description 设备管理(2020.10.14)
     */
    public JSONObject decice_manage(Long subject_id) throws Exception {
        String url = "/admin/data/device/list";
        JSONObject json = new JSONObject();
        json.put("subject_id", subject_id);
        JSONObject http = this.http(os_data + url, json);
        return http.getJSONObject("data");
    }

}