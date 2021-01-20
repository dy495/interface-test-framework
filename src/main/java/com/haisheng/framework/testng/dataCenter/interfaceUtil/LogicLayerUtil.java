package com.haisheng.framework.testng.dataCenter.interfaceUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.arronlong.httpclientutil.builder.HCB;
import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.util.CommonUtil;
import org.apache.http.client.HttpClient;
import org.testng.annotations.BeforeClass;

public class LogicLayerUtil extends TestCaseCommon {
    /**
     * 单利，确保多个类共用一份类
     * 此部分不变，后面的方法自行更改
     */
    private static volatile LogicLayerUtil instance = null;

    private LogicLayerUtil() {
    }
    public static LogicLayerUtil getInstance() {

        if (null == instance) {
            synchronized (LogicLayerUtil.class) {
                if (null == instance) {
                    //这里
                    instance = new LogicLayerUtil();
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
    public String IpPort = "http://47.95.69.163";

    public String httpPost1(String path, String json, String IpPort) throws Exception {
        String requestId = "127c81fd-d0b1-4c77-adad"+ CommonUtil.getRandom(5);
        String res = getRequest(json,requestId,IpPort);
        initHttpConfig();
        String queryUrl = IpPort + path;
        config.url(queryUrl).json(res);
        logger.info("{} json param: {}", path, res);
        long start = System.currentTimeMillis();
        response = HttpClientUtil.post(config);
        logger.info("response: {}", response);
        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);
        caseResult.setResponse(response);
        return response;
    }

    public String getRequest(String data, String requestId, String service){
        JSONObject jsonObject=new JSONObject();
        JSONObject system=new JSONObject();
        JSONArray resource = new JSONArray();
        JSONArray scope = new JSONArray();
        scope.add(0,"22728");
        system.put("app_id","88590052b177");
        system.put("scope",scope);
        system.put("service",service);
        jsonObject.put("data",data);
        jsonObject.put("system",system);
        jsonObject.put("request_id",requestId);
        jsonObject.put("resource",resource);
        return jsonObject.toJSONString();
    }

    /**
     * @author qingqing
     * @description 1.1. 特殊人物注册、新增人脸
     */
    /**
     * app checks submit 1. 特殊人物注册、新增人脸
     */
    public JSONObject special_register(String group_name, String user_id, JSON shop_user, String business_type,Boolean is_quality_limit,String pic_url
            ,Boolean is_after_detect,Boolean is_choose_biggest_face,Float face_quality,int[] axis,Float yaw,Float pitch,Float roll,Float sunglasses,Float illumination
            ,Float blur,Float mask
    ) throws Exception {
        String url = "/scenario/gate/SYSTEM_REGISTER_FACE/v1.0";
        JSONObject json = new JSONObject();
        json.put("group_name", group_name);
        json.put("user_id", user_id);
        json.put("shop_user", shop_user);
        json.put("business_type", business_type);
        json.put("is_quality_limit", is_quality_limit);
        json.put("pic_url", pic_url);
        json.put("is_after_detect", is_after_detect);
        json.put("is_choose_biggest_face", is_choose_biggest_face);
        json.put("face_quality", face_quality);
        json.put("axis", axis);
        json.put("yaw", yaw);
        json.put("pitch", pitch);
        json.put("roll", roll);
        json.put("sunglasses", sunglasses);
        json.put("illumination", illumination);
        json.put("blur", blur);
        json.put("mask", mask);
        String res = httpPost1(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res);
    }


    /**
     * A. 特定人物库查询
     */
    public JSONObject specialKu_serach(String group_name) throws Exception {
        String url = "/scenario/gate/SYSTEM_QUERY_GROUP/v1.0";
        JSONObject json = new JSONObject();
        json.put("group_name", group_name);
        String res = httpPost1(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res);
    }
    /**
     * B. 特定人物查询
     */
    public JSONObject specialMan_serach(String group_name,String user_id) throws Exception {
        String url = "/scenario/gate/SYSTEM_QUERY_USER/v1.0";
        JSONObject json = new JSONObject();
        json.put("group_name", group_name);
        json.put("user_id", user_id);
        String res = httpPost1(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res);
    }
    /**
     *3. 特殊人物校验（人脸检索）
     */
    public JSONObject specialFace_serach(String group_name,Boolean is_threshold,String pic_url,Integer result_num,Float score_threshold,Boolean is_choose_biggest_face) throws Exception {
        String url = "/scenario/gate/SYSTEM_SEARCH_FACE/v1.0";
        JSONObject json = new JSONObject();
        json.put("group_name", group_name);
        json.put("is_threshold", is_threshold);
        json.put("pic_url", pic_url);
        json.put("result_num", result_num);
        json.put("score_threshold", score_threshold);
        json.put("is_choose_biggest_face", is_choose_biggest_face);
        String res = httpPost1(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res);
    }

    /**
     *4. 特殊人物删除
     */
    public JSONObject delete_man(String group_name,String user_id) throws Exception {
        String url = "/scenario/gate/SYSTEM_DELETE_USER/v1.0";
        JSONObject json = new JSONObject();
        json.put("group_name", group_name);
        json.put("user_id", user_id);
        String res = httpPost1(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res);
    }

    /**
     *5. 特殊人物人脸删除
     */
    public JSONObject specialFace_delete(String group_name,String user_id,String face_id) throws Exception {
        String url = "/scenario/gate/SYSTEM_DELETE_FACE/v1.0";
        JSONObject json = new JSONObject();
        json.put("group_name", group_name);
        json.put("user_id", user_id);
        json.put("face_id", face_id);
        String res = httpPost1(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res);
    }

    /**
     *6. 自定义组删除
     */
    public JSONObject self_delete(String group_name) throws Exception {
        String url = "/scenario/gate/SYSTEM_DELETE_GROUP/v1.0";
        JSONObject json = new JSONObject();
        json.put("group_name", group_name);
        String res = httpPost1(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res);
    }

    /**
     *7. 消费者身份转变
     */
    public JSONObject changeUser(String shop_id,Integer from_user_id,String to_group_name,String to_user_id,String is_check_same) throws Exception {
        String url = "/scenario/gate/SYSTEM_CHANGE_USER/v1.0";
        JSONObject json = new JSONObject();
        json.put("shop_id", shop_id);
        json.put("from_user_id", from_user_id);
        json.put("to_group_name", to_group_name);
        json.put("to_user_id", to_user_id);
        json.put("is_check_same", is_check_same);
        String res = httpPost1(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res);
    }

    /**
     *三. 客流人物管理
     * 1. 默认组人脸检索
     */
    public JSONObject default_seearch(String shop_id,Integer from_user_id,String to_group_name,String to_user_id,String is_check_same) throws Exception {
        String url = "/scenario/gate/SYSTEM_SEARCH_DEFAULT/v1.0";
        JSONObject json = new JSONObject();
        json.put("shop_id", shop_id);
        json.put("from_user_id", from_user_id);
        json.put("to_group_name", to_group_name);
        json.put("to_user_id", to_user_id);
        json.put("is_check_same", is_check_same);
        String res = httpPost1(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res);
    }

    /**
     *2. 默认组用户查询
     * 只返回最优结果
     */
    public JSONObject default_userSearch(String shop_id,String user_id) throws Exception {
        String url = "/scenario/gate/SYSTEM_QUERY_DEFAULT_USER/v1.0";
        JSONObject json = new JSONObject();
        json.put("shop_id", shop_id);
        json.put("user_id", user_id);
        String res = httpPost1(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res);
    }

    /**
     *3.  删除默认组用户
     * 只返回最优结果
     */
    public JSONObject delete_default_user(String shop_id,String user_id) throws Exception {
        String url = "/scenario/gate/SYSTEM_DELETE_DEFAULT_USER/v1.0";
        JSONObject json = new JSONObject();
        json.put("shop_id", shop_id);
        json.put("user_id", user_id);
        String res = httpPost1(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res);
    }

    /**
     *4.  删除默认组用户的人脸
     */
    public JSONObject delete_default_face(String shop_id,String user_id,String face_id) throws Exception {
        String url = "/scenario/gate/SYSTEM_DELETE_DEFAULT_FACE/v1.0";
        JSONObject json = new JSONObject();
        json.put("shop_id", shop_id);
        json.put("user_id", user_id);
        json.put("face_id", face_id);
        String res = httpPost1(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res);
    }

    /**
     *5.  人脸检测
     */
    public JSONObject face_check(Boolean is_deep_analysis,String pic_url) throws Exception {
        String url = "/scenario/gate/DETECT_FACE/v1.0";
        JSONObject json = new JSONObject();
        json.put("is_deep_analysis", is_deep_analysis);
        json.put("pic_url", pic_url);
        String res = httpPost1(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res);
    }

    /**
     *14. 人脸注册（算法能力输出）
     */
    public JSONObject face_regin(String group_name,String user_id,String pic_url,String face_url,JSONArray axis,Boolean is_choose_biggest_face,Float yaw
    ,Float pitch,Float roll,Float sunglasses,Float illumination,Float blur,Float mask,Float quality) throws Exception {
        String url = "/scenario/gate/REGISTER_FACE/v1.0";
        JSONObject json = new JSONObject();
        json.put("group_name", group_name);
        json.put("user_id", user_id);
        json.put("face_url", face_url);
        json.put("axis", axis);
        json.put("is_choose_biggest_face", is_choose_biggest_face);
        json.put("yaw", yaw);
        json.put("pitch", pitch);
        json.put("roll", roll);
        json.put("sunglasses", sunglasses);
        json.put("illumination", illumination);
        json.put("blur", blur);
        json.put("mask", mask);
        json.put("quality", quality);
        String res = httpPost(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res);
    }

    /**
     *15. 人脸查询（算法能力输出）
     */
    public JSONObject face_search(String group_name,String user_id,String pic_url,String face_url,JSONArray axis,Boolean is_choose_biggest_face,Float yaw
            ,Float pitch,Float roll,Float sunglasses,Float illumination,Float blur,Float mask,Float quality) throws Exception {
        String url = "/scenario/gate/REGISTER_FACE/v1.0";
        JSONObject json = new JSONObject();
        json.put("group_name", group_name);
        json.put("user_id", user_id);
        json.put("face_url", face_url);
        json.put("axis", axis);
        json.put("is_choose_biggest_face", is_choose_biggest_face);
        json.put("yaw", yaw);
        json.put("pitch", pitch);
        json.put("roll", roll);
        json.put("sunglasses", sunglasses);
        json.put("illumination", illumination);
        json.put("blur", blur);
        json.put("mask", mask);
        json.put("quality", quality);
        String res = httpPost(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res);
    }

    /**
     16. 人脸比较（算法能力输出）
     */
    public JSONObject face_than(String group_name,String user_id,String pic_url,String face_url,JSONArray axis,Boolean is_choose_biggest_face,Float yaw
            ,Float pitch,Float roll,Float sunglasses,Float illumination,Float blur,Float mask,Float quality) throws Exception {
        String url = "/scenario/gate/REGISTER_FACE/v1.0";
        JSONObject json = new JSONObject();
        json.put("group_name", group_name);
        json.put("user_id", user_id);
        json.put("face_url", face_url);
        json.put("axis", axis);
        json.put("is_choose_biggest_face", is_choose_biggest_face);
        json.put("yaw", yaw);
        json.put("pitch", pitch);
        json.put("roll", roll);
        json.put("sunglasses", sunglasses);
        json.put("illumination", illumination);
        json.put("blur", blur);
        json.put("mask", mask);
        json.put("quality", quality);
        String res = httpPost(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res);
    }

}
