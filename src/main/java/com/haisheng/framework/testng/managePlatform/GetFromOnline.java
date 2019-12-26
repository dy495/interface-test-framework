package com.haisheng.framework.testng.managePlatform;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.util.HttpExecutorUtil;
import com.haisheng.framework.util.StatusCode;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class GetFromOnline {

    //在调用时初始化authorization和header
    String authorization = "";
    HashMap<String, String> header = new HashMap();

    private int pageSize = 10000;

    String URL_prefix = "http://39.106.253.135";

    private org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());


    public String listSubject(String appId, String subjectId) throws Exception {
        String url = URL_prefix + "/admin/data/subject/list";
        String json =
                "{\n";

        if (!"".equals(subjectId)) {
            json += "    \"subject_id\":\"" + subjectId + "\",\n";
        } else if (!"".equals(appId)) {
            json += "    \"app_id\":\"" + appId + "\",\n";
        }

        json += "    \"page\":1,\n" +
                "    \"size\":" + pageSize + "\n" +
                "}";

        String response = postRequest(url, json, header);
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

    public String listDevice(String subjectId) throws Exception {

        String url = URL_prefix + "/admin/data/device/list";

        String json =
                "{\n" +
                        "    \"subject_id\":\"" + subjectId + "\",\n" +
                        "    \"level\":2,\n" +
                        "    \"page\":1,\n" +
                        "    \"size\":" + pageSize + "\n" +
                        "}";

        String response = postRequest(url, json, header);

        checkCode(response, StatusCode.SUCCESS, "");

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
                        "    \"size\":" + pageSize + "\n" +
                        "}";

        String response = postRequest(url, json, header);

        checkCode(response, StatusCode.SUCCESS, "平面列表（分页）");
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
                        "    \"size\":" + pageSize + "\n" +
                        "}";

        String response = postRequest(url, json, header);

        checkCode(response, StatusCode.SUCCESS, "平面所属设备列表");
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

    //    5、区域列表
    public String listRegion(String subjectId) throws Exception {
        String url = URL_prefix + "/admin/data/region/list";

        String json =
                "{\n" +
                        "    \"subject_id\":\"" + subjectId + "\",\n";

        json += "    \"page\":1,\n" +
                "    \"size\":" + pageSize + "\n" +
                "}";

        String response = postRequest(url, json, header);

        checkCode(response, StatusCode.SUCCESS, "区域列表");
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
                        "    \"size\":" + pageSize + "\n" +
                        "}";

        String response = postRequest(url, json, header);

        checkCode(response, StatusCode.SUCCESS, "");
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

    private void checkCode(String response, int expect, String message) throws Exception {
        JSONObject resJo = JSON.parseObject(response);
        int code = resJo.getInteger("code");
        message = resJo.getString("message");

        if (expect != code) {
            throw new Exception(message + " expect code: " + expect + ",actual: " + code);
        }
    }

    private String postRequest(String url, String json, HashMap header) throws Exception {
        HttpExecutorUtil executor = new HttpExecutorUtil();
        executor.doPostJsonWithHeaders(url, json, header);
        return executor.getResponse();
    }

    private String getRequest(String url, HashMap header) throws Exception {
        HttpExecutorUtil executor = new HttpExecutorUtil();
        executor.doGetJsonWithHeaders(url, header);
        return executor.getResponse();
    }
}
