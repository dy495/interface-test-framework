package com.haisheng.framework.testng.managePlatform;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.util.StatusCode;
import org.testng.annotations.DataProvider;

public class AICameraUtil extends TestCaseCommon {

    /**
     * 单利，确保多个类共用一份类
     * 此部分不变，后面的方法自行更改
     */

    private static volatile AICameraUtil instance = null;
    public JSONArray patrolShopRealV3;

    private AICameraUtil() {
    }

    public static AICameraUtil getInstance() {

        if (null == instance) {
            synchronized (AICameraUtil.class) {
                if (null == instance) {
                    //这里
                    instance = new AICameraUtil();
                }
            }
        }

        return instance;
    }


    /***
     * 方法区，不同产品的测试场景各不相同，自行更改
     */
    public String IpPort = "http://39.106.253.190/";

    /**
     * @description:登录
     * @author: qingqing
     * @time:
     */
    public void login(String userName, String passwd) {
        initHttpConfig();
        String path = "/patrol-login";
        String loginUrl = IpPort + path;
        String json = "{\"type\":0, \"username\":\"" + userName + "\",\"password\":\"" + passwd + "\"}";
        config.url(loginUrl)
                .json(json);
        logger.info("{} json param: {}", path, json);
        long start = System.currentTimeMillis();
        try {
            response = HttpClientUtil.post(config);
            authorization = JSONObject.parseObject(response).getJSONObject("data").getString("token");
            logger.info("authorization:" + authorization);
        } catch (Exception e) {
            appendFailreason(e.toString());
        }
        logger.info("{} time used {} ms", path, System.currentTimeMillis() - start);
        //saveData("登陆");

    }
    //    添加AI摄像头
    public JSONObject addCamera(String subject_id, String name, String serial_number,String manufacturer,String model) throws Exception {
        String url = "/admin/device/aiCamera/add";
        String json =
                "{\n" +
                        "    \"subject_id\":\"" + subject_id + "\",\n" +
                        "    \"name\":\"" + name + "\",\n" +
                        "    \"serial_number\":\"" + serial_number + "\",\n" +
                        "    \"manufacturer\":\"" + manufacturer + "\",\n" +
                        "    \"model\":\"" + model + "\"\n" +
                        "}";


        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }
    //    添加AI摄像头
    public JSONObject deleteCamera(String  device_id) throws Exception {
        String url =  "/admin/device/aiCamera/delete";
        String json =
                "{\n" +
                        "    \"device_id\":\"" + device_id + "\"\n" +
                        "}";


        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }
    //    重启AI摄像头
    public JSONObject restartCamera(String  device_id) throws Exception {
        String url =  "/admin/device/aiCamera/restart";
        String json =
                "{\n" +
                        "    \"device_id\":\"" + device_id + "\"\n" +
                        "}";


        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }
    //    重置AI摄像头
    public JSONObject resetCamera(String  device_id) throws Exception {
        String url = "/admin/device/aiCamera/reset";
        String json =
                "{\n" +
                        "    \"device_id\":\"" + device_id + "\"\n" +
                        "}";


        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }
    //    更换AI摄像头
    public JSONObject changeCamera(String  device_id) throws Exception {
        String url = "/admin/device/aiCamera/reset";
        String json =
                "{\n" +
                        "    \"device_id\":\"" + device_id + "\"\n" +
                        "}";


        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    //    恢复AI摄像头出厂设置
    public JSONObject startAnewCamera(String  device_id) throws Exception {
        String url =  "/admin/device/aiCamera/startAnew";
        String json =
                "{\n" +
                        "    \"device_id\":\"" + device_id + "\"\n" +
                        "}";


        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    //    AI摄像头日志上传
    public JSONObject logUploadCamera(String  device_id,JSONArray ids) throws Exception {
        String url = "/admin/device/aiCamera/logUpload";
        String json =
                "{\n" +
                        "    \"device_id\":\"" + device_id + "\",\n" +
                        "    \"ids\":\"" + ids + "\"\n" +
                        "}";


        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    //    AI摄像头App控制
    public JSONObject appControlCamera(String  device_id,JSONArray ids) throws Exception {
        String url = "/admin/device/aiCamera/appControl";
        String json =
                "{\n" +
                        "    \"device_id\":\"" + device_id + "\",\n" +
                        "    \"ids\":\"" + ids + "\"\n" +
                        "}";


        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    //    当前AI摄像头设备运行的app列表
    public JSONObject appListCamera(String  device_id) throws Exception {
        String url =  "/admin/device/aiCamera/appList";
        String json =
                "{\n" +
                        "    \"device_id\":\"" + device_id + "\"\n" +
                        "}";


        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }
    //    ai摄像头设备生产商列表
    public JSONObject enumCamera() throws Exception {
        String url = IpPort + "/admin/device/aiCamera/manufacturer/enum";
        String json =
                "{}";


        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    //   ai摄像头设备型号列表
    public JSONObject enumModelCamera(String  device_id) throws Exception {
        String url = "/admin/device/aiCamera/model/enum";
        String json =
                "{\n" +
                        "    \"device_id\":\"" + device_id + "\"\n" +
                        "}";


        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

    //  ai摄像头app状态列表
    public JSONObject enumModelCamera() throws Exception {
        String url =  "/admin/device/aiCamera/appStatus/enum";
        String json =
                "{}";


        String res = httpPostWithCheckCode(url, json, IpPort);

        return JSON.parseObject(res).getJSONObject("data");
    }

}
