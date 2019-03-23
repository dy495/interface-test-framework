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
import com.haisheng.framework.util.MQYun;
import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.ObjectInputStream;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.GZIPInputStream;

public class PVHotmapTestCloud {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private LogMine logMine       = new LogMine(logger);
    private String START_TIME     = "123456789";
    private String END_TIME       = "987654321";
    private String UID            = "uid_e0d1ebec";
    private String APP_ID         = "a4d4d18741a8";
    private String SHOP_ID        = "159";
    private String DEVICE_ID      = "6274064685499392";
    private String INVALID_ID     = "11111111";

    private String HOTMAP_GET_ROUTER = "/business/customer/QUERY_THERMAL_MAP/v1.1";
    private String PV_UPLOAD_ROUTER  = "/scenario/who/ANALYSIS_PERSON/v1.0";


    @Test(dataProvider = "RID")
    public void testStatisticHotmap(String regionId) throws Exception{
        String requestId = "";
        boolean result = true;
        logMine.logCase("testStatisticHotmap, region_id: " + regionId);

        try {
            String jsonDir = "src/main/resources/test-res-repo/pv-post/cloud-hotmap-valid-scenario";
            FileUtil fileUtil = new FileUtil();
            List<File> files = fileUtil.getFiles(jsonDir, ".json");
            for (File file : files) {
                logger.info("test scenario file: " + file.getName());
                String startTime = getHourBegin(-1);
                String endTime = getCurrentHourEnd();
                requestId = UUID.randomUUID().toString();
                HotmapQuery hotmapQuery = new HotmapQuery(regionId, startTime, String.valueOf(endTime), true);
                HotmapInfo hotmapBefore = apiCustomerRequest(HOTMAP_GET_ROUTER, hotmapQuery);
                HotmapInfo hotmapAdd    = getCurrentTestPvInfo(file, hotmapQuery);

                apiSdkPostToCloud(file, PV_UPLOAD_ROUTER, startTime, requestId);
                sleep();

                HotmapInfo hotmapLatest = apiCustomerRequest(HOTMAP_GET_ROUTER, hotmapQuery);
                result = checkTestResult(hotmapBefore, hotmapAdd, hotmapLatest);
                if (!result) {
                    dingdingAlarm("热力图统计测试", "上传的数据和最新获取的数据不同", requestId, "@刘峤");
                    String msg = "request id: " + requestId + "\n热力图统计测试, 上传的数据和最新获取的数据不同";
                    throw new Exception(msg);
                }
            }

            logger.info("test " + files.size() + " scenario-files.");
        } catch (Exception e) {
            logger.error(e.toString());
            if (result) {
                //exception NOT be caused by final result data checking
                dingdingAlarm("热力图统计测试", "正常场景热力图测试\n期望：成功 \n结果：出现Exception", requestId, "@刘峤");
            }
            throw e;
        }

    }

    @Test(dataProvider = "INVALIDRID")
    public void invalidRegionId(String regionID) throws Exception{
        String requestId = "";
        boolean result = true;
        logMine.logCase("invalidRegionId, region id: " + regionID);
        try {
            String jsonDir = "src/main/resources/test-res-repo/pv-post/cloud-hotmap-invalid-scenario";
            String fileName = "pv-hotmap-invalid-region.json";
            File file = new File(jsonDir + "/" + fileName);
            logger.info("file: " + file.getName());

            String startTime = getHourBegin(-1);
            String endTime = getCurrentHourEnd();
            requestId = UUID.randomUUID().toString();

            HotmapQuery hotmapQuery = new HotmapQuery(regionID, startTime, String.valueOf(endTime), true);
            HotmapInfo hotmapBefore = apiCustomerRequest(HOTMAP_GET_ROUTER, hotmapQuery);

            apiSdkPostToCloud(file, regionID, PV_UPLOAD_ROUTER, startTime, requestId);
            sleep();

            HotmapInfo hotmapLatest = apiCustomerRequest(HOTMAP_GET_ROUTER, hotmapQuery);
            result = checkTestResult(hotmapBefore, null, hotmapLatest);
            if (!result) {
                dingdingAlarm("热力图统计测试", "用无效的regionID获取数据", requestId, "@刘峤");
                String msg = "request id: " + requestId + "\n热力图统计测试：间隔1分钟，用相同的无效regionID 两次获取的数据不同";
                throw new Exception(msg);
            }
        } catch (Exception e) {
            logger.error(e.toString());
            if (result) {
                //exception NOT be caused by final result data checking
                dingdingAlarm("热力图统计测试", "用无效的regionID获取数据", requestId, "@刘峤");
            }
            throw e;
        }
    }

    private void sleep() throws Exception{
        //end - current
        long currentTime = System.currentTimeMillis();
        long end = Long.valueOf(getCurrentMinuteEnd());
        long wait = end-currentTime+3000;

        Thread.sleep(wait);
    }

    private String getHourBegin(int index) throws Exception {
        DateTime dateTime = new DateTime();
        int year = dateTime.getYear();
        int month = dateTime.getMonthOfYear();
        int day = dateTime.getDayOfMonth();
        int hour = dateTime.getHourOfDay() + index;
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

    private String getCurrentMinuteEnd() throws Exception {
        DateTime dateTime = new DateTime();
        int year = dateTime.getYear();
        int month = dateTime.getMonthOfYear();
        int day = dateTime.getDayOfMonth();
        int hour = dateTime.getHourOfDay();
        int minute = dateTime.getMinuteOfHour()+1;
        DateTimeUtil dt = new DateTimeUtil();
        String time = year + "/" + month + "/" + day + " " + hour +":"+minute+":00:000";
        return dt.dateToTimestamp(time);
    }

    private HotmapInfo getCurrentTestPvInfo(File file, HotmapQuery hotmapQuery) throws Exception{
        logMine.logStep("get current add hotmap info");
        HotmapInfo hotmapInfo = new HotmapInfo();;
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

                        ConcurrentHashMap<Axis, Integer> axisHm = hotmapInfo.getAxisHm();
                        Axis axis = new Axis(String.valueOf(x), String.valueOf(y));
                        if (axisHm.containsKey(axis)) {
                            axisHm.put(axis, axisHm.get(axis)+1);
                        } else {
                            axisHm.put(axis, 1);
                        }
                    }
                }
            }
            if (count > 0) {
                //found data by filter region id
                hotmapInfo.setStartX(minX);
                hotmapInfo.setStartY(minY);
                hotmapInfo.setWidth(maxX-minX);
                hotmapInfo.setHeight(maxY-minY);
                hotmapInfo.setMaxValue(count);
            } else {
                hotmapInfo = null;
            }

            logger.info("current test json data get done.");
        } catch (Exception e) {
            logger.error(e.toString());
            throw e;
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
        JSONArray mapArray = new JSONObject(response).getJSONObject("data").getJSONArray("thermal_map");
        if (0 == mapArray.length()) {
            return null;
        }
        JSONObject map = mapArray.getJSONObject(0);
        hotmapInfo.setStartX(map.getInt("start_x"));
        hotmapInfo.setGenerateTime(map.getString("generate_time"));
        hotmapInfo.setThermalMapData(map.getString("thermal_map_data"));
        hotmapInfo.setStartY(map.getInt("start_y"));
        hotmapInfo.setWidth(map.getInt("width"));
        hotmapInfo.setTime(map.getString("time"));
        hotmapInfo.setHeight(map.getInt("height"));
        hotmapInfo.setMaxValue(map.getInt("max_value"));
        getAxisHotData(hotmapInfo);

        return hotmapInfo;
    }

    private void getAxisHotData(HotmapInfo hotmapInfo) throws Exception{
        ByteArrayInputStream bi = null;
        GZIPInputStream gi = null;
        ObjectInputStream is = null;
        boolean isException = false;

        try {
            byte[] array = Base64.getDecoder().decode(hotmapInfo.getThermalMapData());

            bi = new ByteArrayInputStream(array);
            gi = new GZIPInputStream(bi);
            is = new ObjectInputStream(gi);

            int[] thermalMapPoint = (int[]) is.readObject();
            for (int index=0; index<thermalMapPoint.length; index++) {
                if (0 == thermalMapPoint[index]) {
                    continue;
                }
                int x = hotmapInfo.getStartX() + index%(hotmapInfo.getWidth()+1);
                int y = hotmapInfo.getStartY() + index/(hotmapInfo.getWidth()+1);
                Axis axis = new Axis(String.valueOf(x), String.valueOf(y));
                if (hotmapInfo.getAxisHm().containsKey(axis)) {
                    hotmapInfo.getAxisHm().put(axis, hotmapInfo.getAxisHm().get(axis)+thermalMapPoint[index]);
                } else {
                    hotmapInfo.getAxisHm().put(axis, thermalMapPoint[index]);
                }
            }
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
            logger.info("post json: " + json);
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

    private void apiSdkPostToCloud(File file, String regionID, String router, String startTime, String requestId) throws Exception {
        try {
            logMine.logStep("post pv info");
            // 传入签名参数
            Credential credential = new Credential("e0709358d368ee13", "ef4e751487888f4a7d5331e8119172a3");
            String json = FileUtils.readFileToString(file,"UTF-8");
            json = json.replaceAll("\n\\s*", "")
                    .replaceAll(INVALID_ID, regionID)
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
            logger.info("post json: " + json);
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
        HotmapInfo expect = null;
        if (null != before && null != add) {
            expect = new HotmapInfo();
            //expect = before + add
            expect.setTime(add.getTime());
            int width  = add.getWidth() > before.getWidth() ? add.getWidth() : before.getWidth();
            int height = add.getHeight() > before.getHeight() ? add.getHeight() : before.getHeight();
            int startX = add.getStartX() > before.getStartX() ? add.getStartX() : before.getStartX();
            int startY = add.getStartY() > before.getStartY() ? add.getStartY() : before.getStartY();
            expect.setWidth(width);
            expect.setHeight(height);
            expect.setStartX(startX);
            expect.setStartY(startY);
            mergHm(expect.getAxisHm(), before.getAxisHm());
            mergHm(expect.getAxisHm(), add.getAxisHm());

        } else if (null != add) {
            expect = add;
        } else if (null != before) {
            expect = before;
        }

        return checkResult(expect, latest);
    }

    private void mergHm(ConcurrentHashMap<Axis,Integer> expect, ConcurrentHashMap<Axis, Integer> existed) {
        for (Map.Entry<Axis, Integer> entry : existed.entrySet()) {
            Axis key = entry.getKey();
            if (expect.containsKey(entry.getKey())) {
                expect.put(key, expect.get(key)+entry.getValue());
            } else {
                expect.put(key, entry.getValue());
            }
        }
    }

    private boolean checkResult(HotmapInfo expect, HotmapInfo result) {
        if (null == expect && null == result) {
            logger.info("upload the no region id data and get no data by the region id");
            return true;
        }

        if (expect.getHeight() == result.getHeight()
                && expect.getWidth() == result.getWidth()
                && expect.getStartX() == result.getStartX()
                && expect.getStartY() == result.getStartY()
        ) {
            logger.info("basic info same");
            ConcurrentHashMap<Axis, Integer> expectAxisHm = expect.getAxisHm();
            ConcurrentHashMap<Axis, Integer> resultAxisHm = result.getAxisHm();

            if (expectAxisHm.size() == resultAxisHm.size()) {
                logger.info("thermal_map_data size same");
                for (Map.Entry<Axis, Integer> entry : expectAxisHm.entrySet()) {
                    Axis key = entry.getKey();
                    int num = entry.getValue();
                    if (resultAxisHm.containsKey(key)) {
                        if (resultAxisHm.get(key) == num) {
                            logger.info("all content of result same as expect");
                            return true;
                        } else {
                            logger.error("result thermal_map_data contains expect data: " + entry.getKey()
                                    + ", expect value: " + num
                                    + ", actual value: " + resultAxisHm.get(key));
                            return false;
                        }
                    } else {
                        logger.error("result thermal_map_data not contains expect data : " + entry.getKey());
                        return false;
                    }
                }
            } else {
                logger.error("thermal_map_data size NOT same");
                return false;
            }
        } else {
            logger.error("basic info NOT same");
            return false;
        }

        return false;
    }
    private void dingdingAlarm(String summary, String detail, String requestId, String atPerson) {
        detail = "请求requestid: " + requestId + " \n" + detail;
        //screenshot do not support local pic, must use pic in web
        String bugPic = "http://i01.lw.aliimg.com/media/lALPBbCc1ZhJGIvNAkzNBLA_1200_588.png";
        String linkUrl = "http://192.168.50.2:8080/view/云端测试/job/pv-cloud-test/Test_20Report/";
        String msg = DingChatbot.getMarkdown(summary, detail, bugPic, linkUrl, atPerson);
        DingChatbot.sendMarkdown(msg);
    }


    @DataProvider(name = "RID")
    public Object[] createValidId() {

        return new String[] {
                "164",
                "166",
                "168"
        };
    }

    @DataProvider(name = "INVALIDRID")
    public Object[] createInvalidId() {

        return new String[] {
                "-1",
                "0",
                "abcdE",
                "2A!34&*a",
                "999999999"
        };
    }

    @BeforeSuite
    public void initial() {
        logger.info("initial");
        MQYun.subscribeTopic();
    }

    @AfterSuite
    public void clean() {
        logger.info("clean");
        MQYun.shutdown();
    }

}
