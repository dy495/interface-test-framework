package com.haisheng.framework.testng.managePlatform;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.util.FileUtil;
import com.haisheng.framework.util.HttpExecutorUtil;
import com.haisheng.framework.util.StatusCode;
import okhttp3.*;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

public class SetToDaily {

    //在调用时初始化authorization和header
    String authorization = "";
    HashMap<String,String> header = new HashMap();

    String URL_prefix ="http://39.106.253.190";

    public org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    public String uploadLayoutPic(String picA) throws Exception {

        String downloadImagePath = "src/main/java/com/haisheng/framework/testng/managePlatform/layoutPic";
        FileUtil fileUtil = new FileUtil();
        fileUtil.downloadImage(picA, downloadImagePath);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://39.106.253.190/admin/data/layout/layoutPicUpload");

        httpPost.addHeader("authorization", authorization);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();

        // 把文件加到HTTP的post请求中
        File pictureAFile = new File(downloadImagePath);
        builder.addBinaryBody(
                "layout_pic",
                new FileInputStream(pictureAFile),
                ContentType.APPLICATION_OCTET_STREAM,
                pictureAFile.getName()
        );

        HttpEntity multipart = builder.build();
        httpPost.setEntity(multipart);
        CloseableHttpResponse response = httpClient.execute(httpPost);
        HttpEntity responseEntity = response.getEntity();
        String res = EntityUtils.toString(responseEntity, "UTF-8");
        String fileUrl = JSON.parseObject(res).getJSONObject("data").getString("url");

        return fileUrl;
    }

    public String getDeviceId(String response) {
        JSONObject data = JSON.parseObject(response).getJSONObject("data");
        String deviceId = data.getString("device_id");

        return deviceId;
    }

    public String getDeviceIdByName(String response,String name) {
        JSONArray list = JSON.parseObject(response).getJSONObject("data").getJSONArray("list");

        String deviceId = "";

        for (int i = 0; i < list.size(); i++) {
            JSONObject jsonObject = list.getJSONObject(i);
            if (name.equals(jsonObject.getString("name"))){
                deviceId = jsonObject.getString("device_id");
            }
        }


        return deviceId;
    }

    public String getEntranceId(String response) {

        JSONObject data = JSON.parseObject(response).getJSONObject("data");
        String entranceId = data.getString("entrance_id");

        return entranceId;
    }

    public String getRegionId(String response) {
        JSONObject data = JSON.parseObject(response).getJSONObject("data");
        String regionId = data.getString("region_id");

        return regionId;
    }

    public String getLayoutId(String response) {
        JSONObject data = JSON.parseObject(response).getJSONObject("data");
        String layoutId = data.getString("layout_id");

        return layoutId;
    }

    public String getLayoutIdByName(String response,String name) {
        String layoutId = "";
        JSONArray jsonArray = JSON.parseObject(response).getJSONObject("data").getJSONArray("list");
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            if (name.equals(jsonObject.getString("name"))){
                layoutId = jsonObject.getString("layout_id");
            }

        }

        return layoutId;
    }

    public String enumDeviceType() throws Exception {
        String url = URL_prefix + "/admin/data/deviceType/list";

        String response = getRequest(url, header);

        return response;
    }

    public String addDevice(String name, String deviceType, String sceneType, String cloudSceneType, String url, String subjectId) throws Exception {

        String postUrl = URL_prefix + "/admin/data/device/";

        String json =
                "{\n" +
                        "    \"name\":\"" + name + "\",\n" +
                        "    \"device_type\":\"" + deviceType + "\",\n" +
                        "    \"scene_type\":\"" + sceneType + "\",\n" +
                        "    \"cloud_scene_type\":\"" + cloudSceneType + "\",\n" +
                        "    \"url\":\"" + url + "\",\n" +
                        "    \"subject_id\":\"" + subjectId + "\"\n" +
                        "}";

        String response = postRequest(postUrl, json, header);
        
        checkCode(response, StatusCode.SUCCESS, "");

        return response;
    }

    public String updateDevice(String name, String deviceId, String deviceType, int interval, int startTime, int endTime,
                               String ding, String email, String videoUrl) throws Exception {

        String url = URL_prefix + "/admin/data/device/" + deviceId;

        String json =
                "{\n" +
                        "    \"name\":\"" + name + "\",\n" +
                        "    \"device_type\":\"" + deviceType + "\",\n" +
                        "    \"scene_type\":\"COMMON\",\n" +
                        "    \"cloud_scene_type\":\"MALL_ENTRANCE_SNAP\",\n" +
                        "    \"monitor_config\":{\n" +
                        "        \"open\":true,\n" +
                        "        \"interval\":" + interval + ",\n" +
                        "        \"time\":[\n" +
                        "            " + startTime + ",\n" +
                        "            " + endTime + "\n" +
                        "        ],\n" +
                        "        \"ding_ding\":[\n" +
                        "            \"" + ding + "\"\n" +
                        "        ],\n" +
                        "        \"email\":[\n" +
                        "            \"" + email + "\"\n" +
                        "        ]\n" +
                        "    },\n" +
                        "    \"url\":\"" + videoUrl + "\"\n" +
                        "}";

        String response = putRequest(url, json, header);
        
        checkCode(response, StatusCode.SUCCESS, "");

        return response;
    }

    public String removeDevice(String deviceId, int expectCode) throws Exception {

        String url = URL_prefix + "/admin/device/remove/" + deviceId;

        String json = "{}";

        String response = deleteRequest(url, json, header);
        
        checkCode(response, expectCode, "");

        return response;
    }

    public String removeDevice(String deviceId) {

        String url = URL_prefix + "/admin/device/remove/" + deviceId;

        String json = "{}";

        String response = null;
        try {
            response = deleteRequest(url, json, header);
            checkCode(response, StatusCode.SUCCESS, "");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public String getDevice(String deviceId) throws Exception {

        String url = URL_prefix + "/admin/data/device/" + deviceId;

        String response = getRequest(url, header);
        
        checkCode(response, StatusCode.SUCCESS, "");

        return response;
    }

    public String listDevice(String subjectId) throws Exception {

        String url = URL_prefix + "/admin/data/device/list";

        String json =
                "{\n" +
                        "    \"subject_id\":\"" + subjectId + "\",\n" +
                        "    \"level\":2,\n" +
                        "    \"page\":1,\n" +
                        "    \"size\":100\n" +
                        "}";

        String response = postRequest(url, json, header);
        
        checkCode(response, StatusCode.SUCCESS, "");

        return response;
    }

    public String startDevice(String deviceId) throws Exception {

        String url = URL_prefix + "/admin/device/start/" + deviceId;

        String json =
                "{}";

        String response = postRequest(url, json, header);
        
        checkCode(response, StatusCode.SUCCESS, "");

        return response;
    }

    public String stopDevice(String deviceId) throws Exception {

        String url = URL_prefix + "/admin/device/stop/" + deviceId;

        String json =
                "{}";

        String response = postRequest(url, json, header);
        
        checkCode(response, StatusCode.SUCCESS, "");

        return response;
    }

    public String batchStartDevice(String deviceId1, String deviceId2) throws Exception {

        String url = URL_prefix + "/admin/batch/device/start";

        String json =
                "{\n" +
                        "    \"device_list\":[\n" +
                        "        \"" + deviceId1 + "\",\n" +
                        "        \"" + deviceId2 + "\"\n" +
                        "    ]\n" +
                        "}";

        String response = postRequest(url, json, header);
        
        checkCode(response, StatusCode.SUCCESS, "");

        return response;
    }

    public String batchStopDevice(String deviceId1, String deviceId2) throws Exception {

        String url = URL_prefix + "/admin/batch/device/stop";

        String json =
                "{\n" +
                        "    \"stop_list\":[\n" +
                        "        {\n" +
                        "            \"device_id\":\"" + deviceId1 + "\"\n" +
                        "        },\n" +
                        "        {\n" +
                        "            \"device_id\":\"" + deviceId2 + "\"\n" +
                        "        }\n" +
                        "    ]\n" +
                        "}";

        String response = postRequest(url, json, header);
        
        checkCode(response, StatusCode.SUCCESS, "");

        return response;
    }

    public String batchRemoveDevice(String deviceId1, String deviceId2) throws Exception {

        String url = URL_prefix + "/admin/batch/device/remove";

        String json =
                "{\n" +
                        "    \"remove_list\":[\n" +
                        "        {\n" +
                        "            \"device_id\":\"" + deviceId1 + "\"\n" +
                        "        },\n" +
                        "        {\n" +
                        "            \"device_id\":\"" + deviceId2 + "\"\n" +
                        "        }\n" +
                        "    ]\n" +
                        "}";

        String response = postRequest(url, json, header);
        
        checkCode(response, StatusCode.SUCCESS, "");

        return response;
    }

    public String batchMonitorDevice(String deviceId1, String deviceId2, int interval, int startTime, int endTime,
                                     String ding, String email) throws Exception {

        String url = URL_prefix + "/admin/batch/device/monitorConfig";

        String json =
                "{\n" +
                        "    \"list\":[\n" +
                        "        \"" + deviceId1 + "\",\n" +
                        "        \"" + deviceId2 + "\"\n" +
                        "    ],\n" +
                        "    \"monitor_config\":{\n" +
                        "        \"open\":true,\n" +
                        "        \"interval\":" + interval + ",\n" +
                        "        \"time\":[\n" +
                        "            " + startTime + "," +
                        "            " + endTime +
                        "        ],\n" +
                        "        \"ding_ding\":[\n" +
                        "            \"" + ding + "\"\n" +
                        "        ],\n" +
                        "        \"email\":[\n" +
                        "            \"" + email + "\"\n" +
                        "        ]\n" +
                        "    }\n" +
                        "}";

        String response = postRequest(url, json, header);
        
        checkCode(response, StatusCode.SUCCESS, "");

        return response;
    }


    public String findDetailOfEntrance(String deviceId) throws Exception {

        String url = URL_prefix + "/admin/data/layout/findDetail/device/" + deviceId;

        String response = getRequest(url, header);
        
        checkCode(response, StatusCode.SUCCESS, "");

        return response;
    }

    public String enumFloorList() throws Exception {
        System.out.println("dfdfd");

        String url = URL_prefix + "/admin/data/layout/floorEnum/list";

        String response = getRequest(url, header);
        checkCode(response, StatusCode.SUCCESS, "获取楼层");

        return response;
    }

    public String addLayout(String name, String description, String subjectId, String floorId, int expectCode) throws Exception {

        logger.info("\n");
        logger.info("------------------------1、创建新平面----------------------------");

        String url = URL_prefix + "/admin/data/layout/";

        String json =
                "{\n" +
                        "    \"name\":\"" + name + "\",\n" +
                        "    \"description\":\"" + description + "\",\n" +
                        "    \"subject_id\":\"" + subjectId + "\",\n" +
                        "    \"floor_id\":" + floorId +
                        "}";

        String response = postRequest(url, json, header);
        
        checkCode(response, expectCode, "新建平面");

        return response;
    }

    public String layoutPicUpload() throws IOException {

        logger.info("\n");
        logger.info("------------------------2、平面图片上传----------------------------");

        String url = URL_prefix + "/admin/data/layout/layoutPicUpload";

        String filePath = "src\\main\\java\\com\\haisheng\\framework\\testng\\managePlatform\\experimentLayout";
        filePath = filePath.replace("\\", File.separator);
        File file = new File(filePath);

        OkHttpClient client = new OkHttpClient();

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        builder.addFormDataPart("layout_pic", file.getName(), RequestBody.create
                (MediaType.parse("application/octet-stream"), file));

        //构建
        MultipartBody multipartBody = builder.build();

        //创建Request
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", authorization)
                .post(multipartBody)
                .build();

        Response response = client.newCall(request).execute();

        JSONObject data = JSON.parseObject(response.body().string()).getJSONObject("data");

        String layoutPic = data.getString("url");

        return layoutPic;

    }

    public String deleteLayoutPic(int layoutId) throws Exception {

        logger.info("\n");
        logger.info("------------------------3、平面图片删除----------------------------");

        String url = URL_prefix + "/admin/data/layout/layoutPicUpload/" + layoutId;

        String json = "{}";

        String response = deleteRequest(url, json, header);
        
        checkCode(response, StatusCode.SUCCESS, "平面图片删除");

        return response;
    }

    public String updateLayout(String layoutId, String name, String description, String layoutPic, String floorId) throws Exception {

        logger.info("\n");
        logger.info("------------------------5、平面编辑----------------------------");

        String url = URL_prefix + "/admin/data/layout/" + layoutId;

        String json =
                "{\n" +
                        "    \"name\":\"" + name + "\",\n" +
                        "    \"description\":\"" + description + "\",\n" +
                        "    \"layout_pic_oss\":\"" + layoutPic + "\",\n" +
                        "    \"floor_id\":" + floorId +
                        "}";

        String response = putRequest(url, json, header);
        
        checkCode(response, StatusCode.SUCCESS, "平面编辑");

        return response;
    }

    public String getLayout(int layoutId) throws Exception {
        logger.info("\n");
        logger.info("------------------------6、平面详情----------------------------");

        String url = URL_prefix + "/admin/data/layout/" + layoutId;

        String response = getRequest(url, header);
        
        checkCode(response, StatusCode.SUCCESS, "平面详情");
        return response;
    }

    public String listLayout(String subjectId) throws Exception {
        logger.info("\n");
        logger.info("------------------------7、平面列表（分页）----------------------------");

        String url = URL_prefix + "/admin/data/layout/list";
        String json =
                "{\n" +
                        "    \"subject_id\":\"" + subjectId + "\",\n" +
                        "    \"page\":1,\n" +
                        "    \"size\":50\n" +
                        "}";

        String response = postRequest(url, json, header);
        
        checkCode(response, StatusCode.SUCCESS, "平面列表（分页）");
        return response;
    }

    public String listAllLayout(String subjectId) throws Exception {
        logger.info("\n");
        logger.info("------------------------7、平面列表（不分页）----------------------------");

        String url = URL_prefix + "/admin/data/layout/listAll";
        String json =
                "{\n" +
                        "    \"subject_id\":\"" + subjectId + "\"" +
                        "}";

        String response = postRequest(url, json, header);
        
        checkCode(response, StatusCode.SUCCESS, "平面列表（不分页）");
        return response;
    }

    public String addLayoutDevice(String layoutId, String deviceId) throws Exception {
        logger.info("\n");
        logger.info("------------------------8、平面设备新增----------------------------");

        String url = URL_prefix + "/admin/data/layoutDevice/" + deviceId + "/" + layoutId;
        String json =
                "{}";

        String response = postRequest(url, json, header);
        
        checkCode(response, StatusCode.SUCCESS, "平面所属设备列表");
        return response;
    }

    //    平面设备删除
    public String deleteLayoutDevice(int layoutId, String deviceId) throws Exception {
        logger.info("\n");
        logger.info("------------------------9、平面设备删除----------------------------");

        String url = URL_prefix + "/admin/data/layoutDevice/" + deviceId + "/" + layoutId;
        String json =
                "{}";

        String response = deleteRequest(url, json, header);
        
        checkCode(response, StatusCode.SUCCESS, "平面设备删除");
        return response;
    }

    public String listLayoutDevice(String layoutId) throws Exception {
        logger.info("\n");
        logger.info("------------------------11、平面所属设备列表----------------------------");

        String url = URL_prefix + "/admin/data/layoutDevice/pagelist";
        String json =
                "{\n" +
                        "    \"layout_id\":\"" + layoutId + "\",\n" +
                        "    \"page\":1,\n" +
                        "    \"size\":50\n" +
                        "}";

        String response = postRequest(url, json, header);
        
        checkCode(response, StatusCode.SUCCESS, "平面所属设备列表");
        return response;
    }

    public String genMappingJson(String method, String deviceId, String layoutId) {

        String json =
                "{\n" +
                        "    \"device_id\":\"" + deviceId + "\",\n" +
                        "    \"layout_id\":\"" + layoutId + "\",\n" +
                        "    \"device_location\":{\n" +
                        "        \"x\":0.6482204361875191,\n" +
                        "        \"y\":0.6309841031580149,\n" +
                        "        \"degree\":0\n" +
                        "    },\n" +
                        "    \"layout_mapping\":{\n" +
                        "        \"a\":{\n" +
                        "            \"x\":0.886133611007495,\n" +
                        "            \"y\":0.622972179426388\n" +
                        "        },\n" +
                        "        \"b\":{\n" +
                        "            \"x\":0.8821125432640587,\n" +
                        "            \"y\":0.8704516013588629\n" +
                        "        },\n" +
                        "        \"c\":{\n" +
                        "            \"x\":0.6395081227434074,\n" +
                        "            \"y\":0.8704516013588629\n" +
                        "        },\n" +
                        "        \"d\":{\n" +
                        "            \"x\":0.6435291904868436,\n" +
                        "            \"y\":0.6514590193610613\n" +
                        "        }\n" +
                        "    },\n" +
                        "    \"device_mapping\":{\n" +
                        "        \"a\":{\n" +
                        "            \"x\":0.9476324751895118,\n" +
                        "            \"y\":0.05416666666666667\n" +
                        "        },\n" +
                        "        \"b\":{\n" +
                        "            \"x\":0.9523199179697514,\n" +
                        "            \"y\":0.9166666666666666\n" +
                        "        },\n" +
                        "        \"c\":{\n" +
                        "            \"x\":0.047643461383528034,\n" +
                        "            \"y\":0.9416666666666667\n" +
                        "        },\n" +
                        "        \"d\":{\n" +
                        "            \"x\":0.045299739993408285,\n" +
                        "            \"y\":0.041666666666666664\n" +
                        "              }";
        if ("analysisMatrix".equals(method)) {
            json += "}";
        } else if ("layoutMapping".equals(method) || "updateLayoutMapping".equals(method)) {
            json += "    },\n" +
                    "    \"matrix\":[\n" +
                    "        0.11883674069475003,\n" +
                    "        -0.004101835356844864,\n" +
                    "        0.6334838431213622,\n" +
                    "        -0.1387884257457738,\n" +
                    "        0.24273284991074442,\n" +
                    "        0.6427397244425025,\n" +
                    "        -0.1671104142576048,\n" +
                    "        0.001438933074163663,\n" +
                    "        1\n" +
                    "    ]\n";
        }

        json += "}";

        return json;
    }

    public String layoutMapping(String deviceId, String layoutId) throws Exception {
        logger.info("\n");
        logger.info("------------------------12、平面映射----------------------------");

        String url = URL_prefix + "/admin/data/layoutMapping/";
        String json = genMappingJson("layoutMapping", deviceId, layoutId);

        String response = postRequest(url, json, header);
        checkCode(response, StatusCode.SUCCESS, "平面映射");
        return response;
    }

    public String updateLayoutMapping(String deviceId, String layoutId) throws Exception {
        logger.info("\n");
        logger.info("------------------------13、平面映射编辑----------------------------");

        String url = URL_prefix + "/admin/data/layoutMapping/";
        String json = genMappingJson("updateLayoutMapping", deviceId, layoutId);

        String response = putRequest(url, json, header);
        checkCode(response, StatusCode.SUCCESS, "平面映射编辑");
        return response;
    }

    public String getLayoutMapping(String deviceId, String layoutId) throws Exception {
        logger.info("\n");
        logger.info("------------------------14、平面映射详情----------------------------");

        String url = URL_prefix + "/admin/data/layoutMapping/" + deviceId + "/" + layoutId;

        String response = getRequest(url, header);
        checkCode(response, StatusCode.SUCCESS, "映射详情");
        return response;
    }

    public String deleteLayoutMapping(String deviceId, int layoutId) throws Exception {
        logger.info("\n");
        logger.info("------------------------15、平面映射删除----------------------------");

        String url = URL_prefix + "/admin/data/layoutMapping/" + deviceId + "/" + layoutId;
        String json = "{}";

        String response = deleteRequest(url, json, header);
        
        checkCode(response, StatusCode.SUCCESS, "平面映射删除");
        return response;
    }

    //    测试模式的时候会调用这个接口
    public String analysisMatrix(String deviceId, String layoutId, JSONObject device_mapping, JSONArray layout_matrix,
                                 JSONObject device_location, JSONObject layout_mapping) throws Exception {
        logger.info("\n");
        logger.info("------------------------15、平面映射矩阵解析----------------------------");

        String url = URL_prefix + "/admin/data/layoutMapping/analysisMatrix";
        String json = genMappingJson("analysisMatrix", deviceId, layoutId);

        JSONObject jsonObject = JSON.parseObject(json);
        jsonObject.put("device_mapping",device_mapping);
        jsonObject.put("layout_matrix",layout_matrix);
        jsonObject.put("device_location",device_location);
        jsonObject.put("layout_mapping",layout_mapping);

        json = jsonObject.toJSONString();

        String response = postRequest(url, json, header);
        
        checkCode(response, StatusCode.SUCCESS, "平面映射矩阵解析");
        return response;
    }

    //    1、创建新区域
    public String addRegion(String shopId, String regionName, String regionType, String layoutId, boolean isLayoutRegion) throws Exception {
        String url = URL_prefix + "/admin/data/region/";

        String json =
                "{\n" +
                        "    \"region_name\":\"" + regionName + "\",\n" +
                        "    \"region_type\":\"" + regionType + "\",\n";

        if (isLayoutRegion) {
            json += "    \"layout_id\":" + layoutId + ",\n";
        }

        json += "    \"subject_id\":\"" + shopId + "\"\n" +
                "}";

        String response = postRequest(url, json, header);
        
        checkCode(response, StatusCode.SUCCESS, "创建新区域");
        return response;
    }

    //    3、编辑区域
    public String updateRegion(String regionId, JSONArray region_location) throws Exception {
        String url = URL_prefix + "/admin/data/region/" + regionId;

        String json =
                "{\n" +
                        "    \"region_location\":[\n" +
                        "        {\n" +
                        "            \"x\":0,\n" +
                        "            \"y\":1\n" +
                        "        },\n" +
                        "        {\n" +
                        "            \"x\":1,\n" +
                        "            \"y\":1\n" +
                        "        },\n" +
                        "        {\n" +
                        "            \"x\":1,\n" +
                        "            \"y\":0\n" +
                        "        },\n" +
                        "        {\n" +
                        "            \"x\":0,\n" +
                        "            \"y\":0\n" +
                        "        },\n" +
                        "        {\n" +
                        "            \"x\":0.1788026858773746,\n" +
                        "            \"y\":0.1295265389447236\n" +
                        "        },\n" +
                        "        {\n" +
                        "            \"x\":0.7155148842147973,\n" +
                        "            \"y\":0.27584798994974874\n" +
                        "        }\n" +
                        "    ]\n" +
                        "}";

        JSONObject jsonObject = JSON.parseObject(json);
        jsonObject.put("region_location",region_location);

        json = jsonObject.toJSONString();

        String response = putRequest(url, json, header);
        
        checkCode(response, StatusCode.SUCCESS, "编辑区域");
        return response;
    }

    //    4、区域详情
    public String getRegion(String regionId) throws Exception {
        String url = URL_prefix + "/admin/data/region/" + regionId;

        String response = getRequest(url, header);
        
        checkCode(response, StatusCode.SUCCESS, "区域详情");
        return response;
    }

    //    5、区域列表
    public String listRegion(String subjectId) throws Exception {
        String url = URL_prefix + "/admin/data/region/list";

        String json =
                "{\n" +
                        "    \"subject_id\":\"" + subjectId + "\",\n";

        json += "    \"page\":1,\n" +
                "    \"size\":50\n" +
                "}";

        String response = postRequest(url, json, header);
        
        checkCode(response, StatusCode.SUCCESS, "区域列表");
        return response;
    }

    //    6、新增区域设备
    public String addRegionDevice(String regionId, String deviceId) throws Exception {
        String url = URL_prefix + "/admin/data/regionDevice/";

        String json =
                "{\n" +
                        "    \"region_id\":" + regionId + ",\n" +
                        "    \"device_id\":\"" + deviceId + "\"\n" +
                        "}";

        String response = postRequest(url, json, header);
        
        checkCode(response, StatusCode.SUCCESS, "区域列表");
        return response;
    }

    public String addRegionDeviceCheckCode(String regionId, String deviceId, int expectCode) throws Exception {
        String url = URL_prefix + "/admin/data/regionDevice/";

        String json =
                "{\n" +
                        "    \"region_id\":" + regionId + ",\n" +
                        "    \"device_id\":\"" + deviceId + "\"\n" +
                        "}";


        String response = postRequest(url, json, header);
        
        checkCode(response, expectCode, "区域列表");
        return response;
    }

    //    7、区域设备删除
    public String deleteRegionDevice(String regionId, String deviceId) throws Exception {
        String url = URL_prefix + "/admin/data/regionDevice/" + regionId + "/" + deviceId;

        String json =
                "{}";

        String response = deleteRequest(url, json, header);
        
        checkCode(response, StatusCode.SUCCESS, "区域设备删除");
        return response;
    }

    //    8、区域所属设备列表
    public String listRegionDevice(String regionId) throws Exception {
        String url = URL_prefix + "/admin/data/regionDevice/list";

        String json =
                "{\n" +
                        "    \"region_id\":" + regionId + "\n" +
                        "}";

        String response = postRequest(url, json, header);
        
        checkCode(response, StatusCode.SUCCESS, "区域所属设备列表");
        return response;
    }

    //    9、区域类型
    public String enumRegionType() throws Exception {
        String url = URL_prefix + "/admin/data/region/regionEnum/list";

        String response = getRequest(url, header);
        checkCode(response, StatusCode.SUCCESS, "区域类型");
        return response;
    }

    public String[] getAllRegionType() throws Exception {
        String response = enumRegionType();
        JSONObject data = JSON.parseObject(response).getJSONObject("data");
        JSONArray list = data.getJSONArray("list");

        String[] regionTypes = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            JSONObject singleList = list.getJSONObject(i);
            String regionType = singleList.getString("region_type");
            regionTypes[i] = regionType;
        }

        return regionTypes;
    }

    //    1、新增出入口
    public String addEntrance(String name, String entranceType, String regionId, boolean useLine,
                              boolean bothDir,boolean isStair) throws Exception {
        String url = URL_prefix + "/admin/data/entrance/";
        String json =
                "{\n" +
                        "    \"entrance_name\":\"" + name + "\",\n" +
                        "    \"entrance_type\":\"" + entranceType + "\",\n" +
                        "    \"region_id\":\"" + regionId + "\",\n" +
                        "    \"use_line\":\"" + useLine + "\"," +
                        "    \"both_dir\":\"" + bothDir + "\"," +
                        "    \"is_stair\":\"" + isStair + "\"\n" +
                        "}";

        String response = postRequest(url, json, header);
        
        checkCode(response, StatusCode.SUCCESS, "");
        return response;
    }

    //    3、出入口编辑
    public String updateEntrance(String entranceId, JSONArray  entranceMapLoc) throws Exception {
        String url = URL_prefix + "/admin/data/entrance/" + entranceId;
        String json =
                "{\n" +
                        "    \"entrance_map_location\":[\n" +
                        "        {\n" +
                        "            \"x\":0.2573539730931068,\n" +
                        "            \"y\":0.3567917713567839\n" +
                        "        },\n" +
                        "        {\n" +
                        "            \"x\":0.686254987485021,\n" +
                        "            \"y\":0.38792399497487434\n" +
                        "        },\n" +
                        "        {\n" +
                        "            \"x\":0.4354767987422078,\n" +
                        "            \"y\":0.4937735552763819\n" +
                        "        }\n" +
                        "    ]\n" +
                        "}";

        JSONObject jsonObject = JSON.parseObject(json);
        jsonObject.put("entrance_map_location",entranceMapLoc);

        json = jsonObject.toJSONString();

        String response = putRequest(url, json, header);
        
        checkCode(response, StatusCode.SUCCESS, "");
        return response;
    }

    //    4、出入口详情
    public String getEntrance(String entranceId) throws Exception {
        String url = URL_prefix + "/admin/data/entrance/" + entranceId;

        String response = getRequest(url, header);
        
        checkCode(response, StatusCode.SUCCESS, "");
        return response;
    }

    //    5、出入口列表
    public String listEntrance(String regionId) throws Exception {
        String url = URL_prefix + "/admin/data/entrance/list";
        String json =
                "{\n" +
                        "    \"region_id\":\"" + regionId + "\",\n" +
                        "    \"page\":1,\n" +
                        "    \"size\":50\n" +
                        "}";

        String response = postRequest(url, json, header);
        
        checkCode(response, StatusCode.SUCCESS, "");
        return response;
    }

    //    6、绑定出入口设备
    public String bindEntranceDevice(String entranceId, String deviceId, JSONObject entrancePoint,
                                     JSONArray entranceLoc, JSONArray entranceDpLoc) throws Exception {
        String url = URL_prefix + "/admin/data/entranceDevice/";

        String json =
                "{\n" +
                        "    \"entrance_point\":null,\n" +
                        "    \"entrance_location\":null,\n" +
                        "    \"entrance_dp_location\":null,\n" +
                        "    \"entrance_id\":" + entranceId +   ",\n" +
                        "    \"device_id\":\"" + deviceId + "\"\n" +
                        "}";

        JSONObject jsonObject = JSON.parseObject(json);

        jsonObject.put("entrance_point",entrancePoint);
        jsonObject.put("entrance_location",entranceLoc);
        jsonObject.put("entrance_dp_location",entranceDpLoc);

        json = jsonObject.toJSONString();

        String response = postRequest(url, json, header);
        
        checkCode(response, StatusCode.SUCCESS, "绑定出入口设备");
        return response;
    }

    //    8、出入口设备解绑
    public String unbindEntranceDevice(String entranceId, String deviceId) throws Exception {
        String url = URL_prefix + "/admin/data/entranceDevice/" + entranceId + "/" + deviceId;
        String json =
                "{}";

        String response = deleteRequest(url, json, header);
        
        checkCode(response, StatusCode.SUCCESS, "出入口设备解绑");
        return response;
    }

    //    9、出入口所属设备列表（不分页）
    public String listEntranceDevice(String entranceId) throws Exception {
        String url = URL_prefix + "/admin/data/entranceDevice/list";
        String json =
                "{\n" +
                        "    \"entrance_id\":\"" + entranceId + "\"\n" +
                        "}";

        String response = postRequest(url, json, header);
        
        checkCode(response, StatusCode.SUCCESS, "");
        return response;
    }

    //    10、出入口类型
    public String enumEntranceType() throws Exception {
        String url = URL_prefix + "/admin/data/entrance/entranceEnum/list";

        String response = getRequest(url, header);
        checkCode(response, StatusCode.SUCCESS, "");
        return response;
    }



    public String addSubject(String brandId, String subjectName, int subjectTypeId) throws Exception {
        String url = URL_prefix + "/admin/data/subject/";
        String json =
                "{\n" +
                        "    \"brand_id\":\"" + brandId + "\",\n" +
                        "    \"subject_type\":" + subjectTypeId + ",\n" +
                        "    \"subject_name\":\"" + subjectName + "\",\n" +
                        "    \"region\":{\n" +
                        "        \"country\":\"中国\",\n" +
                        "        \"area\":\"华北区\",\n" +
                        "        \"province\":\"北京市\",\n" +
                        "        \"city\":\"北京市\",\n" +
                        "        \"district\":\"海淀区\"\n" +
                        "    },\n" +
                        "    \"local\":\"中关村soho\",\n" +
                        "    \"manager\":\"娜乌西卡\",\n" +
                        "    \"telephone\":\"17666666666\"\n" +
                        "}";

        String response = postRequest(url, json, header);
        
        checkCode(response, StatusCode.SUCCESS, "");
        return response;
    }

    public String updateSubject(String subjectId, String subjectName, String country, String area, String province,
                                String city, String district, String local, String manager, String phone) throws Exception {
        String url = URL_prefix + "/admin/data/subject/" + subjectId;
        String json =
                "{\n" +
                        "    \"subject_name\":\"" + subjectName + "\",\n" +
                        "    \"region\":{\n" +
                        "        \"country\":\"" + country + "\",\n" +
                        "        \"area\":\"" + area + "\",\n" +
                        "        \"province\":\"" + province + "\",\n" +
                        "        \"city\":\"" + city + "\",\n" +
                        "        \"district\":\"" + district + "\"\n" +
                        "    },\n" +
                        "    \"local\":\"" + local + "\",\n" +
                        "    \"manager\":\"" + manager + "\",\n" +
                        "    \"telephone\":\"" + phone + "\"\n" +
                        "}";

        String response = putRequest(url, json, header);
        
        checkCode(response, StatusCode.SUCCESS, "");
        return response;
    }

    public String detailSubject(String subjectId) throws Exception {
        String url = URL_prefix + "/admin/data/subject/" + subjectId;

        String response = getRequest(url, header);

        
        checkCode(response, StatusCode.SUCCESS, "");
        return response;
    }

    public String listSubject(String appId, String brandId) throws Exception {
        String url = URL_prefix + "/admin/data/subject/list";
        String json =
                "{\n" +
                        "    \"app_id\":\"" + appId + "\",\n" +
                        "    \"brand_id\":\"" + brandId + "\",\n" +
                        "    \"page\":1,\n" +
                        "    \"size\":10\n" +
                        "}";

        String response = postRequest(url, json, header);
        checkCode(response, StatusCode.SUCCESS, "");
        return response;
    }

    public String getSubjectIdByList(String res, String subjectName) throws Exception {

        String subjectId = checkIsExistByListSubject(res, subjectName, true);

        return subjectId;
    }

    public String checkIsExistByListSubject(String response, String subjectName, boolean isExist) {
        String subjectId = "";
        boolean isExistRes = false;
        JSONArray list = JSON.parseObject(response).getJSONObject("data").getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            JSONObject listSingle = list.getJSONObject(i);
            String subjectNameRes = listSingle.getString("subject_name");
            if (subjectName.equals(subjectNameRes)) {
                isExistRes = true;
                subjectId = listSingle.getString("subject_id");
                break;
            }
        }

        Assert.assertEquals(isExistRes, isExist, "expect isExist: " + isExist + ", acatual: " + isExistRes);
        return subjectId;
    }


    //    业务节点解除关联机器，即解绑定机器
    public String removeNode(String nodeId, String clusterNodeId) throws Exception {
        String url = URL_prefix + "/admin/cluster/node/remove";
        String json =
                "{\n" +
                        "    \"node_id\":\"" + nodeId + "\",\n" +
                        "    \"cluster_node_id\":\"" + clusterNodeId + "\"" +
                        "}";

        String response = deleteRequest(url, json, header);
        
        checkCode(response, StatusCode.SUCCESS, "");
        return response;
    }

    //    查询业务节点是否有绑定集群
//    即已绑定机器列表
    public String bindedList(String nodeId) throws Exception {
        String url = URL_prefix + "/admin/cluster/bind/list";
        String json =
                "{\n" +
                        "    \"node_id\":\"" + nodeId + "\",\n" +
                        "    \"page\":1,\n" +
                        "    \"size\":10\n" +
                        "}";

        String response = postRequest(url, json, header);
        
        checkCode(response, StatusCode.SUCCESS, "");
        return response;
    }

    //    根据业务节点，获取可绑定机器列表
    public String unbindedList(String nodeId) throws Exception {
        String url = URL_prefix + "/admin/cluster/node/un_bind/list";
        String json =
                "{\n" +
                        "    \"node_id\":\"" + nodeId + "\",\n" +
                        "    \"page\":1,\n" +
                        "    \"size\":10\n" +
                        "}";

        String response = postRequest(url, json, header);
        
        checkCode(response, StatusCode.SUCCESS, "");
        return response;
    }

    //    业务节点关联机器，即绑定机器
    public String bindNode(String nodeId, String clusterNodeId, String alias) throws Exception {
        String url = URL_prefix + "/admin/cluster/node/bind";
        String json =
                "{\n" +
                        "    \"node_id\":\"" + nodeId + "\",\n" +
                        "    \"cluster_node_id\":" + clusterNodeId + ",\n" +
                        "    \"alias\":\"" + alias + "\"\n" +
                        "}";

        String response = postRequest(url, json, header);
        
        checkCode(response, StatusCode.SUCCESS, "");
        return response;
    }

    public String enumNodeType() throws Exception {
        String url = URL_prefix + "/admin/data/nodeType/list";
        String json =
                "{\n" +
                        "    \"page\":1,\n" +
                        "    \"size\":10\n" +
                        "}";

        String response = postRequest(url, json, header);
        return response;
    }

    //    1、删除服务配置
    public String deleteConfig(String serviceId) throws Exception {
        String url = URL_prefix + "/admin/data/nodeServiceConfig/" + serviceId;
        String json =
                "{}";

        String response = deleteRequest(url, json, header);
        
        checkCode(response, StatusCode.SUCCESS, "");
        return response;
    }

    //    2、根据节点ID查询服务详情
    public String queryByNodeId(String nodeId) throws Exception {
        String url = URL_prefix + "/admin/data/nodeServiceConfig/queryByNodeId/" + nodeId;

        String response = getRequest(url, header);
        
        checkCode(response, StatusCode.SUCCESS, "");
        return response;
    }

    //    3、增加节点对应服务&配置
    public String nodeServiceConfig(String nodeId,String serviceId, JSONObject cloudConfig) throws Exception {
        String url = URL_prefix + "/admin/data/nodeServiceConfig/";
        String json =
                "{\n" +
                        "    \"service_id\":" + serviceId + ",\n" +
                        "    \"node_id\":\"" + nodeId + "\"" +
                        "}";

        JSONObject jsonObject = JSON.parseObject(json);

        jsonObject.put("cloud_config", cloudConfig);

        json = jsonObject.toJSONString();

        String response = postRequest(url, json, header);
        
        checkCode(response, StatusCode.SUCCESS, "");
        return response;
    }

    //4、更新服务配置
    public String updateConfig(String serviceId, String subjectId, JSONObject cloudConfig) throws Exception {

        logger.info("===========================更新服务配置==================================");
        logger.info("\n\n");
        String url = URL_prefix + "/admin/data/nodeServiceConfig/" + serviceId;
        String json =
                "{\n" +
                        "    \"cloud_config\":{},\n" +
                        "    \"node_id\":\"" + subjectId + "\",\n" +
                        "    \"service_id\":" + serviceId +  "\n" +
                        "}";

        JSONObject jsonObject = JSON.parseObject(json);
        jsonObject.put("cloud_config",cloudConfig);

        json = jsonObject.toJSONString();

        String response = putRequest(url, json, header);
        
        checkCode(response, StatusCode.SUCCESS, "");
        return response;
    }


    //    查询服务配置详情
    public String getConfig(int serviceId) throws Exception {
        String url = URL_prefix + "/admin/data/nodeServiceConfig/" + serviceId;

        String response = getRequest(url, header);
        
        checkCode(response, StatusCode.SUCCESS, "");
        return response;
    }

    //    1、新建品牌
    public String addBrand(String appId, String brandName, String manager, String phone) throws Exception {
        String url = URL_prefix + "/admin/data/brand/";
        String json =
                "{\n" +
                        "    \"brand_name\":\"" + brandName + "\",\n" +
                        "    \"app_id\":\"" + appId + "\",\n" +
                        "    \"manager\":\"" + manager + "\",\n" +
                        "    \"telephone\":\"" + phone + "\"\n" +
                        "}";

        String response = postRequest(url, json, header);
        
        checkCode(response, StatusCode.SUCCESS, "");
        return response;
    }

    //    3、更新品牌
    public String updateBrand(String brandId, String brandName, String manager, String phone) throws Exception {
        String url = URL_prefix + "/admin/data/brand/" + brandId;
        String json =
                "{\n" +
                        "    \"brand_name\":\"" + brandName + "\",\n" +
                        "    \"manager\":\"" + manager + "\",\n" +
                        "    \"telephone\":\"" + phone + "\"\n" +
                        "}";

        String response = putRequest(url, json, header);
        
        checkCode(response, StatusCode.SUCCESS, "");
        return response;
    }

    //    4、品牌详情
    public String getBrand(String brandId) throws Exception {
        String url = URL_prefix + "/admin/data/brand/" + brandId;

        String response = getRequest(url, header);
        
        checkCode(response, StatusCode.SUCCESS, "");
        return response;
    }

    //    5、品牌列表
    public String listBrand(String appId) throws Exception {
        String url = URL_prefix + "/admin/data/brand/list";

        String json = genListBrandJson(appId);
        String response = postRequest(url, json, header);
        
        checkCode(response, StatusCode.SUCCESS, "");
        return response;
    }

    public String genListBrandJson(String appId) {
        String json =
                "{\n" +
                        "    \"app_id\":\"" + appId + "\",\n" +
                        "    \"page\":1,\n" +
                        "    \"size\":100\n" +
                        "}";
        return json;
    }

    public String listBrandForBrandId(String appId) throws Exception {
        String url = URL_prefix + "/admin/data/brand/list";

        String json = genListBrandJson(appId);
        String response = postRequest(url, json, header);
        checkCode(response, StatusCode.SUCCESS, "");
        return response;
    }

    public String getCloud() throws Exception {
        String url  = "http://39.106.253.190/admin/data/sceneDevice/list";

        String json = "{}";

        String response = postRequest(url, json, header);
        checkCode(response, StatusCode.SUCCESS, "");
        return response;
    }

    public void checkCode(String response, int expect, String message) throws Exception {
        JSONObject resJo = JSON.parseObject(response);
        int code = resJo.getInteger("code");
        message = resJo.getString("message");

        if (expect != code) {
            throw new Exception(message + " expect code: " + expect + ",actual: " + code);
        }
    }

    public String postRequest(String url, String json, HashMap header) throws Exception {
        HttpExecutorUtil executor = new HttpExecutorUtil();
        executor.doPostJsonWithHeaders(url, json, header);
        return executor.getResponse();
    }

    public String putRequest(String url, String json, HashMap header) throws Exception {
        HttpExecutorUtil executor = new HttpExecutorUtil();
        executor.doPutJsonWithHeaders(url, json, header);
        return executor.getResponse();
    }

    public String getRequest(String url, HashMap header) throws Exception {
        HttpExecutorUtil executor = new HttpExecutorUtil();
        executor.doGetJsonWithHeaders(url, header);
        return executor.getResponse();
    }

    public String deleteRequest(String url, String json, HashMap header) throws Exception {
        HttpExecutorUtil executor = new HttpExecutorUtil();

        executor.doDeleteJsonWithHeaders(url, json, header);
        return executor.getResponse();
    }
}
