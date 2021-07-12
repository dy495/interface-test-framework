package com.haisheng.framework.testng.dataCenter.interfaceUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.arronlong.httpclientutil.builder.HCB;
import com.arronlong.httpclientutil.common.HttpConfig;
import com.arronlong.httpclientutil.common.HttpHeader;
import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import lombok.Data;
import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.testng.annotations.BeforeClass;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

public class DataLayerUtil extends TestCaseCommon {
    /**
     * 单利，确保多个类共用一份类
     * 此部分不变，后面的方法自行更改
     */
    private static volatile DataLayerUtil instance = null;

    private DataLayerUtil() {
    }
    public static DataLayerUtil getInstance() {

        if (null == instance) {
            synchronized (DataLayerUtil.class) {
                if (null == instance) {
                    //这里
                    instance = new DataLayerUtil();
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
    public String IpPort1 = "http://dev.api.winsenseos.com/retail/api/data/biz";
    public String IpPort = "http://47.95.69.163:9086";
    public  String IpPort2 ="http://47.95.69.163:9083";
    public JSONObject http(String requestUrl ,Object requestJson,String router) throws NoSuchAlgorithmException, InvalidKeyException {
        final String NUMBER = ".";
        final String ALGORITHM = "HmacSHA256";
        String nonce = UUID.randomUUID().toString();
        String timestamp = "" + System.currentTimeMillis();
        String uid = "uid_ef6d2de5";//日常
        String appId = "49998b971ea0";
        String ak = "3fdce1db0e843ee0";
        String requestId = "89374293847";
        String version = "v1.0";
        String sk = "5036807b1c25b9312116fd4b22c351ac";
        JSONObject data = (JSONObject) requestJson;
        // 1. 将以下参数(uid、app_id、ak、router、timestamp、nonce)的值之间使用顿号(.)拼接成一个整体字符串
        String signStr = uid + NUMBER + appId + NUMBER + ak + NUMBER + router + NUMBER + timestamp + NUMBER + nonce;
        // 2. 使用HmacSHA256加密算法, 使用平台分配的sk作为算法的密钥. 对上面拼接后的字符串进行加密操作,得到byte数组
        Mac sha256Hmac = Mac.getInstance(ALGORITHM);
        SecretKeySpec encodeSecretKey = new SecretKeySpec(sk.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        sha256Hmac.init(encodeSecretKey);
        byte[] hash = sha256Hmac.doFinal(signStr.getBytes(StandardCharsets.UTF_8));
        // 3. 对2.中的加密结果,再进行一次base64操作, 得到一个字符串
        String auth = Base64.getEncoder().encodeToString(hash);
        Header[] headers = HttpHeader.custom()
                .other("Accept", "application/json")
                .other("Content-Type", "application/json;charset=utf-8")
                .other("timestamp", timestamp)
                .other("nonce", nonce)
                .other("ExpiredTime", "50 * 1000")
                .other("Authorization", auth)
                .build();
        GatewayBO gatewayBO = new GatewayBO(uid, appId, requestId, version, router, JSONObject.toJSONString(requestJson));
        String json = JSON.toJSONString(gatewayBO);
        HttpConfig config = HttpConfig.custom().headers(headers).url(requestUrl).json(json).client(client);
        try {
            String post = HttpClientUtil.post(config);
            return JSON.parseObject(post);
        } catch (HttpProcessException e) {
            // logger.error("http调用失败 requestUrl =  {} , requestJson =  {} , headers =  {}" ,requestUrl,requestJson,JSON.toJSONString(headers),e);
        }
        return null;
    }
    @Data
    public static class  GatewayBO{
        private String uid;
        @JSONField(name = "app_id")
        private String appId;
        @JSONField(name = "request_id")
        private String requestId;
        @JsonProperty("version")
        private String version;
        @JsonProperty("router")
        private String router;
        @JsonProperty("data")
        private DataDTO data;
        public GatewayBO() {
        }
        public GatewayBO(String uid, String appId, String requestId, String version, String router, String data) {
            this.uid = uid;
            this.appId = appId;
            this.requestId = requestId;
            this.version = version;
            this.router = router;
            this.data = new DataDTO(JSONObject.parseObject(data));
        }
        @Data
        public static class DataDTO {
            @JSONField(name = "biz_data")
            private JSONObject bizData;
            public DataDTO() {
            }
            public DataDTO(JSONObject bizData) {
                this.bizData = bizData;
            }
        }
    }
    /**
     * @author qingqing
     * @description 1.客户交易数据接收接口
     */
    public JSONObject customer_dealData(String shop_id, String trans_id, String trans_time, List trans_type,String user_id,double total_price
            ,double real_price,String openid,String orderNumber,String memberName,String receipt_type,String posId,JSONArray commodityList
    ) throws Exception {
        String url = "/business/precipitation/TRANS_INFO_RECEIVE/v1.0";
        JSONObject json = new JSONObject();
        json.put("shop_id", shop_id);
        json.put("trans_id", trans_id);
        json.put("trans_time", trans_time);
        json.put("trans_type", trans_type);
        json.put("user_id", user_id);
        json.put("total_price", total_price);
        json.put("real_price", real_price);
        json.put("openid", openid);
        json.put("orderNumber", orderNumber);
        json.put("memberName", memberName);
        json.put("receipt_type", receipt_type);
        json.put("posId", posId);
        json.put("commodityList", commodityList);
        JSONObject http = this.http(IpPort1 , json,url);
        return http;
    }

    /**
     * @author qingqing
     * @description 2.识别付款人物候选，上传图像（接受边缘端数据接口）
     */
    public JSONObject receive_data(String shop_id, String trans_id, String trans_time, JSONArray trans_type,String user_id,Integer total_price
            ,Integer real_price,String openid,String orderNumber,String memberName,String receipt_type,String posId,JSONArray commodityList
    ) throws Exception {
        String url = "/business/bind/RECEIVE_EDGE_DATA/v1.0";
        JSONObject json = new JSONObject();
        json.put("shop_id", shop_id);
        json.put("trans_id", trans_id);
        json.put("trans_time", trans_time);
        json.put("trans_type", trans_type);
        json.put("user_id", user_id);
        json.put("total_price", total_price);
        json.put("real_price", real_price);
        json.put("openid", openid);
        json.put("orderNumber", orderNumber);
        json.put("memberName", memberName);
        json.put("receipt_type", receipt_type);
        json.put("posId", posId);
        json.put("commodityList", commodityList);
        JSONObject http = this.http(IpPort1, json,url);
        return http.getJSONObject("data");
    }

    /**
     * app checks submit 4.1. 会员注册
     */
    public JSONObject memberRegisted(String requestId,String app_id,String scope,String face_url,String user_id,String define_identify,
    boolean is_detect_face,boolean is_choose_biggest,boolean is_quality_limit,boolean is_check_same,boolean is_cross_store,JSON mappings) throws Exception {
        String url = "/business/member/REGISTER/"+requestId;
        JSONObject json = new JSONObject();
        json.put("requestId", requestId);
        json.put("app_id",app_id);
        json.put("scope", scope);
        json.put("face_url", face_url);
        json.put("user_id",user_id);
        json.put("define_identify", define_identify);
        json.put("is_detect_face", is_detect_face);
        json.put("is_choose_biggest",is_choose_biggest);
        json.put("is_quality_limit", is_quality_limit);
        json.put("is_check_same", is_check_same);
        json.put("is_cross_store",is_cross_store);
        json.put("mappings", mappings);
        String res = httpPost(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res);
    }
    /**
     * app checks submit 4.2. 会员删除(删除人脸)
     */
    public JSONObject deleteFace(String requestId,String app_id,String scope,String define_identify,String user_id,String face_id,boolean is_cross_store) throws Exception {
        String url = "/business/member/DELETE_FACE/"+requestId;
        JSONObject json = new JSONObject();
        json.put("requestId", requestId);
        json.put("app_id",app_id);
        json.put("scope", scope);
        json.put("define_identify", define_identify);
        json.put("user_id",user_id);
        json.put("face_id", face_id);
        json.put("is_cross_store", is_cross_store);
        String res = httpPost(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res);
    }
    /**
     * app checks submit 4.2. 会员删除(删除指定身份的会员)
     */
    public JSONObject deleteUser(String requestId,String app_id,String scope,String define_identify,String user_id,boolean is_cross_store) throws Exception {
        String url = "/business/member/DELETE_USER/"+requestId;
        JSONObject json = new JSONObject();
        json.put("requestId", requestId);
        json.put("app_id",app_id);
        json.put("scope", scope);
        json.put("define_identify", define_identify);
        json.put("user_id",user_id);
        json.put("is_cross_store", is_cross_store);
        String res = httpPost(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res);
    }
    /**
     * app checks submit 4.2. 会员删除(删除指定身份组)
     */
    public JSONObject deleteClear_identify(String requestId,String app_id,String scope,String define_identify,boolean is_cross_store) throws Exception {
        String url = "/business/member/CLEAR_IDENTIFY/"+requestId;
        JSONObject json = new JSONObject();
        json.put("requestId", requestId);
        json.put("app_id",app_id);
        json.put("scope", scope);
        json.put("define_identify", define_identify);
        json.put("is_cross_store", is_cross_store);
        String res = httpPost(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res);
    }
    /**
     * app checks submit 4.3 查询身份下的会员
     */
    public JSONObject qyery_userSearch(String requestId,String app_id,String scope,String define_identify,boolean is_cross_store) throws Exception {
        String url = "/business/member/QUERY_USER/"+requestId;
        JSONObject json = new JSONObject();
        json.put("requestId", requestId);
        json.put("app_id",app_id);
        json.put("scope", scope);
        json.put("define_identify", define_identify);
        json.put("is_cross_store", is_cross_store);
        String res = httpPost(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res);
    }
    /**
     * app checks submit 4.3 查询一个门店下包含的身份
     */
    public JSONObject qyery_identifySearch(String requestId,String app_id,String scope,boolean is_cross_store) throws Exception {
        String url = "/business/member/QUERY_IDENTIFY/"+requestId;
        JSONObject json = new JSONObject();
        json.put("requestId", requestId);
        json.put("app_id",app_id);
        json.put("scope", scope);
        json.put("is_cross_store", is_cross_store);
        String res = httpPost(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res);
    }
    /**
     * app checks submit 4.3 检索图片中的人脸在一个门店下的身份
     */
    public JSONObject identify_search(String requestId,String app_id,String scope,String face_url,boolean is_detect_face,boolean is_choose_biggest,boolean is_cross_store) throws Exception {
        String url = "/business/member/SEARCH_IDENTIFY/"+requestId;
        JSONObject json = new JSONObject();
        json.put("requestId", requestId);
        json.put("app_id",app_id);
        json.put("scope", scope);
        json.put("face_url", face_url);
        json.put("is_detect_face", is_detect_face);
        json.put("is_choose_biggest", is_choose_biggest);
        json.put("is_cross_store", is_cross_store);
        String res = httpPost(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res);
    }
    /**
     * app checks submit 4.4 添加一个门店下某种身份的配置
     */
    public JSONObject add_identify(String requestId,String app_id,String scope,String identify,boolean is_register_staff,boolean is_cross_store) throws Exception {
        String url = "/business/member/ADD_IDENTIFY_CONFIG/"+requestId;
        JSONObject json = new JSONObject();
        json.put("requestId", requestId);
        json.put("app_id",app_id);
        json.put("scope", scope);
        json.put("identify", identify);
        json.put("is_register_staff", is_register_staff);
        json.put("is_cross_store", is_cross_store);
        String res = httpPost(url, json.toJSONString(), IpPort);
        return JSON.parseObject(res);
    }

    /**
     * @author qingqing
     * @description 1.实时热力图
     */
    public JSONObject thermal_map(String request_id,String method_type,JSONObject data,JSONObject system) throws Exception {
        String url = "/business/customer/QUERY_THERMAL_MAP/v1.1";
        JSONObject json = new JSONObject();
        json.put("request_id", request_id);
        json.put("method_type", method_type);
        json.put("data", data);
        json.put("system", system);
        String res = httpPost(url, json.toJSONString(), IpPort2);
        return JSON.parseObject(res);
    }
    /**
     * @author qingqing
     * @description 2. 当日统计查询
     * A. 进出人次统计查询：空间上可以按照全店、区域、出入口统计，查询今天到目前的统计数据
     *
     * B. 进出人数统计查询：空间上可以按照全店、区域、出入口统计，查询今天到目前的统计数据
     *
     * C. 在店人数统计查询：空间上可以按照全店、区域统计，查询今天到目前的统计数据
     *
     * D. 男女统计：空间上可以按照全店、区域统计，查询今天到目前的统计数据
     *
     * E. 年龄统计：空间上可以按照全店、区域统计，查询今天到目前的统计数据
     */
    public JSONObject customer_currData(String request_id,String method_type,JSONObject data,JSONObject system) throws Exception {
        String url = "/business/customer/QUERY_CURRENT_CUSTOMER_STATISTICS/v1.1";
        JSONObject json = new JSONObject();
        json.put("request_id", request_id);
        json.put("method_type", method_type);
        json.put("data", data);
        json.put("system", system);
        String res = httpPost(url, json.toJSONString(), IpPort2);
        return JSON.parseObject(res);
    }
    /**
     * @author qingqing
     * @description 4. 当日商铺PV/UV统计
    A. 进出人次统计查询：空间上可以按照全店统计，查询今天到当前的统计数据

    B. 进出人数统计查询：空间上可以按照全店统计，查询今天到当前的统计数据
     */
    public JSONObject curr_shopPv_Uv(String request_id,String method_type,JSONObject data,JSONObject system) throws Exception {
        String url = "/business/customer/QUERY_CURRENT_CUSTOMER_STATISTICS_SCOPE_PUV_DAY/v1.1";
        JSONObject json = new JSONObject();
        json.put("request_id", request_id);
        json.put("method_type", method_type);
        json.put("data", data);
        json.put("system", system);
        String res = httpPost(url, json.toJSONString(), IpPort2);
        return JSON.parseObject(res);
    }
    /**
     * @author qingqing
     * @description 5. 当日商铺PV/UV小时统计
    A. 进出人次统计查询：空间上可以按照全店统计，查询今天到当前小时的统计数据

    B. 进出人数统计查询：空间上可以按照全店统计，查询今天到当前小时的统计数据
     */
    public JSONObject curr_shopHourPv_Uv(String request_id,String method_type,JSONObject data,JSONObject system) throws Exception {
        String url = "/business/customer/QUERY_CURRENT_CUSTOMER_STATISTICS_SCOPE_PUV_HOUR/v1.1";
        JSONObject json = new JSONObject();
        json.put("request_id", request_id);
        json.put("method_type", method_type);
        json.put("data", data);
        json.put("system", system);
        String res = httpPost(url, json.toJSONString(), IpPort2);
        return JSON.parseObject(res);
    }
    /**
     * @author qingqing
     * @description 6. 当日商铺男女统计
    A. 男女统计：空间上可以按照全店统计，查询今天到目前的统计数据
     */
    public JSONObject curr_shopSex(String request_id,String method_type,JSONObject data,JSONObject system) throws Exception {
        String url = "/business/customer/QUERY_CURRENT_CUSTOMER_STATISTICS_SCOPE_SEX_DAY/v1.1";
        JSONObject json = new JSONObject();
        json.put("request_id", request_id);
        json.put("method_type", method_type);
        json.put("data", data);
        json.put("system", system);
        String res = httpPost(url, json.toJSONString(), IpPort2);
        return JSON.parseObject(res);
    }

    /**
     * @author qingqing
     * @description 7. 当日商铺年龄统计
    A. 年龄统计：空间上可以按照全店统计，查询今天到目前的统计数据
     */
    public JSONObject curr_shopAge(String request_id,String method_type,JSONObject data,JSONObject system) throws Exception {
        String url = "/business/customer/QUERY_CURRENT_CUSTOMER_STATISTICS_SCOPE_AGE_DAY/v1.1";
        JSONObject json = new JSONObject();
        json.put("request_id", request_id);
        json.put("method_type", method_type);
        json.put("data", data);
        json.put("system", system);
        String res = httpPost(url, json.toJSONString(), IpPort2);
        return JSON.parseObject(res);
    }

    /**
     * @author qingqing
     * @description 8. 当日商铺内区域PV/UV统计
    A. 进出人次统计查询：空间上可以按照商铺内区域统计，查询今天到当前的统计数据

    B. 进出人数统计查询：空间上可以按照商铺内区域统计，查询今天到当前的统计数据
     */
    public JSONObject curr_shopRegin_PvUv(String request_id,String method_type,JSONObject data,JSONObject system) throws Exception {
        String url = "/business/customer/QUERY_CURRENT_CUSTOMER_STATISTICS_REGION_PUV_DAY/v1.1";
        JSONObject json = new JSONObject();
        json.put("request_id", request_id);
        json.put("method_type", method_type);
        json.put("data", data);
        json.put("system", system);
        String res = httpPost(url, json.toJSONString(), IpPort2);
        return JSON.parseObject(res);
    }
    /**
     * @author qingqing
     * @description 9. 当日商铺内区域男女统计
    A. 男女统计：空间上可以按照商铺内区域统计，查询今天到目前的统计数据
     */
    public JSONObject curr_shopRegin_sex(String request_id,String method_type,JSONObject data,JSONObject system) throws Exception {
        String url = "/business/customer/QUERY_CURRENT_CUSTOMER_STATISTICS_REGION_SEX_DAY/v1.1";
        JSONObject json = new JSONObject();
        json.put("request_id", request_id);
        json.put("method_type", method_type);
        json.put("data", data);
        json.put("system", system);
        String res = httpPost(url, json.toJSONString(), IpPort2);
        return JSON.parseObject(res);
    }
    /**
     * @author qingqing
     * @description 10. 当日商铺内区域年龄统计
    A. 年龄统计：空间上可以按照商铺内区域统计，查询今天到目前的统计数据
     */
    public JSONObject curr_shopRegin_age(String request_id,String method_type,JSONObject data,JSONObject system) throws Exception {
        String url = "/business/customer/QUERY_CURRENT_CUSTOMER_STATISTICS_REGION_AGE_DAY/v1.1";
        JSONObject json = new JSONObject();
        json.put("request_id", request_id);
        json.put("method_type", method_type);
        json.put("data", data);
        json.put("system", system);
        String res = httpPost(url, json.toJSONString(), IpPort2);
        return JSON.parseObject(res);
    }
    /**
     * @author qingqing
     * @description 11. 当日商铺内进出口PV/UV统计
    A. 进出人次统计查询：空间上可以按照商铺内进出口统计，查询今天到当前的统计数据

    B. 进出人数统计查询：空间上可以按照商铺内进出口统计，查询今天到当前的统计数据
     */
    public JSONObject curr_shopEntrance_PvUv(String request_id,String method_type,JSONObject data,JSONObject system) throws Exception {
        String url = "/business/customer/QUERY_CURRENT_CUSTOMER_STATISTICS_ENTRANCE_PUV_DAY /v1.1";
        JSONObject json = new JSONObject();
        json.put("request_id", request_id);
        json.put("method_type", method_type);
        json.put("data", data);
        json.put("system", system);
        String res = httpPost(url, json.toJSONString(), IpPort2);
        return JSON.parseObject(res).getJSONObject("data");
    }

    /**
     * @author qingqing
     * @description 6.1. 历史设备出现人物
      支持查询历史(今天之前)的某台设备次下出现的人物信息
     */
    public JSONObject date_device_customer(String shop_id, String method_type, Object data,Object system) throws Exception {
        String url = "/business/customer/QUERY_SCOPE_DATE_DEVICE_CUSTOMER/v1.1";
        JSONObject json = new JSONObject();
        json.put("shop_id", shop_id);
        json.put("method_type", method_type);
        json.put("data", data);
        json.put("system", system);
        String res = httpPost(url, json.toJSONString(), IpPort2);
        return JSON.parseObject(res);
    }

}
