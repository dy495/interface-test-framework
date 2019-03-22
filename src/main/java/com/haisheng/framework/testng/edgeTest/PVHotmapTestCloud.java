package com.haisheng.framework.testng.edgeTest;

import ai.winsense.ApiClient;
import ai.winsense.common.Credential;
import ai.winsense.constant.SdkConstant;
import ai.winsense.model.ApiRequest;
import ai.winsense.model.ApiResponse;
import com.alibaba.fastjson.JSON;
import com.haisheng.framework.testng.CommonDataStructure.*;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.DingChatbot;
import com.haisheng.framework.util.FileUtil;
import org.apache.commons.io.FileUtils;
import org.apache.ibatis.session.SqlSession;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.ObjectInputStream;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.zip.GZIPInputStream;

public class PVHotmapTestCloud {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private SqlSession sqlSession = null;
    private LogMine logMine       = new LogMine(logger);
    private String START_TIME     = "123456789";
    private String END_TIME       = "987654321";
    private String UID            = "uid_e0d1ebec";
    private String APP_ID         = "a4d4d18741a8";
    private String SHOP_ID        = "159";
    private String DEVICE_ID      = "6274064685499392";
    private int SLEEP_MS          = 3*1000;
    private int SLEEP_LONG        = 5*1000;

    private String HOTMAP_GET_ROUTER  = "/business/customer/QUERY_THERMAL_MAP/v1.1";
    private String PV_UPLOAD_ROUTER  = "/scenario/who/ANALYSIS_PERSON/v1.0";


    @Test
    public void testStatisticHotmap() throws Exception{
        String requestId = "";
        logMine.logCase("testStatisticHotmap");

        try {
            String jsonDir = "src/main/resources/test-res-repo/pv-post/cloud-pv-valid-scenario";
            FileUtil fileUtil = new FileUtil();
            List<File> files = fileUtil.getFiles(jsonDir, ".json");
            for (File file : files) {
                logger.info("test scenario file: " + file.getName());
                String startTime = getCurrentHourBegin();
                String endTime = getCurrentHourEnd();
                requestId = UUID.randomUUID().toString();
                HotmapQuery hotmapQuery = new HotmapQuery("166", startTime, endTime, true);
                HotmapInfo hotmapBefore = apiCustomerRequest(HOTMAP_GET_ROUTER, hotmapQuery);
                HotmapInfo hotmapAdd    = getCurrentTestPvInfo(file, hotmapQuery);

                apiSdkPostToCloud(file, PV_UPLOAD_ROUTER, startTime, requestId);
                Thread.sleep(SLEEP_MS);

                HotmapInfo hotmapLatest = apiCustomerRequest(HOTMAP_GET_ROUTER, hotmapQuery);
                boolean result = checkTestResult(hotmapBefore, hotmapAdd, hotmapLatest);
                if (!result) {
                    dingdingAlarm("热力图统计测试", "上传的数据和最新获取的数据不同", requestId, "@刘峤");
                    String msg = "request id: " + requestId + "\n热力图统计测试, 上传的数据和最新获取的数据不同";
                    throw new Exception(msg);
                }
            }

            logger.info("test " + files.size() + " scenario-files.");
        } catch (Exception e) {
            logger.error(e.toString());
            dingdingAlarm("热力图统计测试", "出现Exception", requestId, "@刘峤");
            throw e;
        }

    }

    private String getCurrentHourBegin() throws Exception {
        DateTime dateTime = new DateTime();
        int year = dateTime.getYear();
        int month = dateTime.getMonthOfYear();
        int day = dateTime.getDayOfMonth();
        int hour = dateTime.getHourOfDay();
        DateTimeUtil dt = new DateTimeUtil();
        String time = year + "/" + month + "/" + day + " " + hour +":00:00:000";
        return dt.dateToTimestamp(time);
    }

    private String getCurrentHourEnd() throws Exception {
        DateTime dateTime = new DateTime();
        int year = dateTime.getYear();
        int month = dateTime.getMonthOfYear();
        int day = dateTime.getDayOfMonth();
        int hour = dateTime.getHourOfDay()+1;
        DateTimeUtil dt = new DateTimeUtil();
        String time = year + "/" + month + "/" + day + " " + hour +":00:00:000";
        return dt.dateToTimestamp(time);
    }

    private HotmapInfo getCurrentTestPvInfo(File file, HotmapQuery hotmapQuery) {
        logMine.logStep("get current add hotmap info");
        HotmapInfo hotmapInfo = new HotmapInfo();
        try {
            String content= null;
            content = FileUtils.readFileToString(file,"UTF-8");
            JSONArray trace = new JSONObject(content).getJSONArray("trace");
            int minX  = Integer.MAX_VALUE;
            int minY  = Integer.MAX_VALUE;
            int maxX  = 0;
            int maxY  = 0;
            int count = 0;
            for (int i=0; i<trace.length(); i++) {
                JSONObject position = trace.getJSONObject(i).getJSONObject("position");
                if (position.isNull("map_axis")) {
                    continue;
                }
                JSONArray mapAxis = position.getJSONArray("map_axis");
                int x = mapAxis.getInt(0);
                int y = mapAxis.getInt(1);
                JSONArray region = trace.getJSONObject(i).getJSONObject("position").getJSONArray("region");
                for (int j=0; j<region.length(); j++) {
                    String regionId = region.getJSONObject(j).getString("region_id");
                    if (regionId.equals(hotmapQuery.regionId)) {
                        count++;
                        if (x < minX) {
                            minX = x;
                        } else if (x > maxX) {
                            maxX = x;
                        }
                        if (y < minY) {
                            minY = y;
                        } else if (y > maxY) {
                            maxY = y;
                        }
                    }
                }
            }
            hotmapInfo.setStartX(String.valueOf(minX));
            hotmapInfo.setStartY(String.valueOf(minY));
            hotmapInfo.setWidth(String.valueOf(maxX-minX));
            hotmapInfo.setHeight(String.valueOf(maxY-minY));
            logger.info("current test json data get done.");
        } catch (Exception e) {
            logger.error(e.toString());
            Assert.assertTrue(false);
        }
        return hotmapInfo;
    }

    private HotmapInfo apiCustomerRequest(String router, HotmapQuery hotmapQuery) throws Exception {
        logMine.logStep("get latest hotmap info from cloud");
        HotmapInfo hotmapInfo = null;
        try {
            String json = "{" +
                    "\"shop_id\":\""+SHOP_ID+"\"," +
                    "\"region_id\":\""+hotmapQuery.regionId+"\"," +
                    "\"start_time\":" + hotmapQuery.startTime + "," +
                    "\"end_time\":"+ hotmapQuery.endTime + "," +
                    "\"is_real_time\":" + hotmapQuery.isRealTime +
                    "}";

            Credential credential = new Credential("e0709358d368ee13", "ef4e751487888f4a7d5331e8119172a3");
            // 封装request对象
            String requestId = UUID.randomUUID().toString();
            ApiRequest apiRequest = new ApiRequest.Builder()
                    .uid(UID)
                    .appId(APP_ID)
                    .version(SdkConstant.API_VERSION)
                    .requestId(requestId)
                    .router(router)
                    .dataResource(new String[]{})
                    .dataBizData(JSON.parseObject(json))
                    .build();

            // client 请求
            ApiClient apiClient = new ApiClient("http://dev.api.winsenseos.com/retail/api/data/biz", credential);
            ApiResponse apiResponse = apiClient.doRequest(apiRequest);
            logMine.printImportant(JSON.toJSONString(apiResponse));
            if(! apiResponse.isSuccess()) {
                String msg = "request id: " + requestId + ", gateway: /retail/api/data/biz, router: " + router + "\nresponse: " + JSON.toJSONString(apiResponse);
                throw new Exception(msg);
            }
            hotmapInfo = getExistedPvInfo(JSON.toJSONString(apiResponse));
        } catch (Exception e) {
            throw e;
        }

        return hotmapInfo;
    }

    private HotmapInfo getExistedPvInfo(String response) throws Exception{
        HotmapInfo hotmapInfo = new HotmapInfo();
        JSONObject map = new JSONObject(response).getJSONObject("data").getJSONObject("thermal_map");
        hotmapInfo.setStartX(map.getString("start_x"));
        hotmapInfo.setGenerateTime(map.getString("generate_time"));
        hotmapInfo.setThermalMapData(map.getString("thermal_map_data"));
        hotmapInfo.setStartY(map.getString("start_y"));
        hotmapInfo.setWidth(map.getString("width"));
        hotmapInfo.setTime(map.getString("time"));
        hotmapInfo.setHeight(map.getString("height"));
        hotmapInfo.setMaxValue(map.getString("max_value"));

        ByteArrayInputStream bi = null;
        GZIPInputStream gi = null;
        ObjectInputStream is = null;
        boolean isException = false;

        try {
            byte[] array = Base64.getDecoder().decode(hotmapInfo.getThermalMapData());

            bi = new ByteArrayInputStream(array);
            gi = new GZIPInputStream(bi);
            is = new ObjectInputStream(gi);

            hotmapInfo.setThermalMapPoint((int[]) is.readObject());
        } catch (Exception e) {
            logger.error(e.toString());
            isException = true;
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (gi != null) {
                    gi.close();
                }
                if (bi != null) {
                    bi.close();
                }
            } catch (Exception e) {
                logger.error(e.toString());

            } finally {
                if (isException) {
                    throw new Exception("解析ThermalMapPoint失败");
                }
            }
        }

        return hotmapInfo;
    }

    private void apiSdkPostToCloud(File file, String router, String startTime, String requestId) throws Exception {
        try {
            logMine.logStep("post pv info");
            // 传入签名参数
            Credential credential = new Credential("e0709358d368ee13", "ef4e751487888f4a7d5331e8119172a3");
            String json = FileUtils.readFileToString(file,"UTF-8");
            json = json.replaceAll("\n\\s*", "")
                    .replace(START_TIME, startTime)
                    .replace(END_TIME, String.valueOf(System.currentTimeMillis()));
            // 封装request对象
            ApiRequest apiRequest = new ApiRequest.Builder()
                    .uid(UID)
                    .appId(APP_ID)
                    .version(SdkConstant.API_VERSION)
                    .requestId(requestId)
                    .dataDeviceId(DEVICE_ID)
                    .router(router)
                    .dataResource(new String[]{})
                    .dataBizData(JSON.parseObject(json))
                    .build();

            // client 请求
            String gateway = "http://dev.api.winsenseos.com/retail/api/data/device";
            ApiClient apiClient = new ApiClient(gateway, credential);
            ApiResponse apiResponse = apiClient.doRequest(apiRequest);
            logMine.printImportant(JSON.toJSONString(apiResponse));
            if(! apiResponse.isSuccess()) {
                String msg = "request id: " + requestId + ", gateway: /retail/api/data/device, router: " + router + ". \nresponse: " + JSON.toJSONString(apiResponse);
                throw new Exception(msg);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    private boolean checkTestResult(HotmapInfo before, HotmapInfo add, HotmapInfo latest) {

        return true;
    }

    private void dingdingAlarm(String summary, String detail, String requestId, String atPerson) {
        detail = "请求requestid: " + requestId + " \n" + detail;
        //screenshot do not support local pic, must use pic in web
        String bugPic = "http://i01.lw.aliimg.com/media/lALPBbCc1ZhJGIvNAkzNBLA_1200_588.png";
        String linkUrl = "http://192.168.50.2:8080/view/云端测试/job/pv-cloud-test/Test_20Report/";
        String msg = DingChatbot.getMarkdown(summary, detail, bugPic, linkUrl, atPerson);
//        DingChatbot.sendMarkdown(msg);
    }


}
