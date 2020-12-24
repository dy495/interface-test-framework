package com.haisheng.framework.testng.managePlatform.manageToOutDaily.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
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
import org.testng.annotations.DataProvider;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ToOutUtil extends TestCaseCommon {
    /**
     * 单利，确保多个类共用一份类
     * 此部分不变，后面的方法自行更改
     */
    private static volatile ToOutUtil instance = null;

    private ToOutUtil() {
    }

    public static ToOutUtil getInstance() {
        if (null == instance) {
            synchronized (ToOutUtil.class) {
                if (null == instance) {
                    //这里
                    instance = new ToOutUtil();
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
    public String os_data = "http://39.105.225.12";
    public String os_device = "http://39.105.225.12:8888";


    public JSONObject http(String requestUrl ,Object requestJson){

        Header[] headers = HttpHeader.custom()
                .other("Accept", "application/json")
                .other("Content-Type", "application/json;charset=utf-8")
                .other("request_id", UUID.randomUUID().toString())
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
     * @description 查询摄像头设备列表（分页）(2020.10.14)
     */
    public JSONObject device_seach(Integer page, Integer size, List shop_ids, String device_type, Long device_id, String device_status) throws Exception {
        String url = "/admin/inner/data/device/list";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        json.put("shop_ids", shop_ids);
        json.put("device_type", device_type);
        json.put("device_id", device_id);
        json.put("device_status", device_status);
        JSONObject http = this.http(os_data + url, json);
        return http.getJSONObject("data");
    }

   /**
     * @author qingqing
     * @description 查询服务器设备列表（分页）(2020.10.14)
     */
    public JSONObject cluster_seach(Integer page, Integer size, List shop_ids, String device_name, Long device_id, String device_status,String shop_name,String device_model) throws Exception {
        String url = "/admin/inner/cluster/node/list";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        json.put("shop_ids", shop_ids);
        json.put("device_name", device_name);
        json.put("device_id", device_id);
        json.put("device_status", device_status);
        json.put("shop_name", shop_name);
        json.put("device_model", device_model);
        JSONObject http = this.http(os_data + url, json);
        return http.getJSONObject("data");
    }

    /**
     * @author qingqing
     * @description 查询门店收银设备对应的摄像头列表（分页）(2020.10.14)
     */
    public JSONObject cashier_seach(Integer page, Integer size, JSONArray shop_ids) throws Exception {
        String url = "/admin/inner/data/device/cashier/list";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        json.put("shop_ids", shop_ids);
        JSONObject http = this.http(os_data + url, json);
        return http.getJSONObject("data");
    }

    /**
     * @author qingqing
     * @description 查询服务器型号枚举(2020.10.14)
     */
    public JSONObject cluster_model() throws Exception {
        String url = "/admin/inner/cluster/node/model/list";
        JSONObject json = new JSONObject();
        JSONObject http = this.http(os_data + url, json);
        return http.getJSONObject("data");
    }

    /**
     * @author qingqing
     * @description 查询摄像头状态枚举(2020.10.14)
     */
    public JSONObject device_status() throws Exception {
        String url = "/admin/inner/data/device/status/list";
        JSONObject json = new JSONObject();
        JSONObject http = this.http(os_data + url, json);
        return http.getJSONObject("data");
    }

    /**
     * @author qingqing
     * @description 查询服务器状态枚举(2020.10.14)
     */
    public JSONObject cluster_status() throws Exception {
        String url = "/admin/inner/cluster/node/status/list";
        JSONObject json = new JSONObject();
        JSONObject http = this.http(os_data + url, json);
        return http.getJSONObject("data");
    }

    /**
     * @author qingqing
     * @description 查询门店信息列表（分页）(2020.10.14)
     */
    public JSONObject storeInfo_seach(Integer page, Integer size, String app_id) throws Exception {
        String url = "/admin/inner/data/subject/list";
        JSONObject json = new JSONObject();
        json.put("page", page);
        json.put("size", size);
        json.put("app_id", app_id);
        JSONObject http = this.http(os_data + url, json);
        return http.getJSONObject("data");
    }

    /**
     * @author qingqing
     * @description 上传录像（给业务模块调用）
     */
    public JSONObject video_upload(String shop_id, String oss_bucket, String oss_key, String oss_secret, String oss_uploadendpoint, JSON command_list) throws Exception {
        String url = "/admin/inner/device/video/upload";
        JSONObject json = new JSONObject();
        json.put("shop_id", shop_id);
        json.put("oss_bucket", oss_bucket);
        json.put("oss_key", oss_key);
        json.put("oss_secret", oss_secret);
        json.put("oss_uploadendpoint", oss_uploadendpoint);
        json.put("command_list", command_list);
        JSONObject http = this.http(os_data + url, json);
        return http.getJSONObject("data");
    }

    /**
     * @author qingqing
     * @description 上传录像（给业务模块调用）
     */
    public JSONObject video_upload_status(JSONArray task_ids) throws Exception {
        String url = "/admin/inner/device/video/upload/status";
        JSONObject json = new JSONObject();
        json.put("task_ids", task_ids);
        JSONObject http = this.http(os_data + url, json);
        return http.getJSONObject("data");
    }

    /**
     * @author qingqing
     * @description 摄像头云台控制（给业务模块调用）
     */
    public JSONObject device_control(String terminal_device_id, Integer command, Integer duration) throws Exception {
        String url = "/admin/inner/device/ptz/control";
        JSONObject json = new JSONObject();
        json.put("terminal_device_id", terminal_device_id);
        json.put("command", command);
        json.put("duration", duration);
        JSONObject http = this.http(os_data + url, json);
        return http;
    }

    @DataProvider(name = "COMMAND")
    public static Object[] command() {
        return new Integer[]{
                0,
                1,
                2,
                3,
                8,
                9
        };
    }
}