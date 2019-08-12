package com.haisheng.framework.testng.edgeTest;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.util.FileUtil;
import com.haisheng.framework.util.WinsenseBackendRequst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GetImageConfig {
    String authUrl = "http://39.106.253.190/administrator/login";
    String user = "yuhaisheng@winsense.ai";
    String passwd = "efe03eff1233eea2d1316db23ded613d";

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private WinsenseBackendRequst winsenseBackendRequst = new WinsenseBackendRequst(authUrl, user, passwd);


    private String DEPLOY_ID = System.getProperty("DEPLOY_ID");
    private String YAML_PATH = System.getProperty("YAML_PATH");

    private boolean DEBUG = false;


    @Test
    public void  getImageConfig() throws Exception {

        if (DEBUG) {
            DEPLOY_ID = "27";
            YAML_PATH = "src/main/resources/test-res-repo/get-image-config/docker-compose-template";

        }

        String imgConfigUrl = "http://39.106.253.190/admin/os/deployment/list";
        String json =
                "{" +
                        "\"page\":1," +
                        "\"size\":100" +
                        "}";
        String response = winsenseBackendRequst.sendRequestWithLoginToken(imgConfigUrl, json);

        Map<String, String> envMap = getEnvListByDeployId(response, DEPLOY_ID);

        replaceAndAddEnvPara(envMap, YAML_PATH);

    }

    private ConcurrentHashMap<String, String> getEnvListByDeployId(String response, String id) {
        ConcurrentHashMap<String, String> envMap = new ConcurrentHashMap<>();
        JSONObject jsonObject = JSON.parseObject(response);
        JSONArray list = jsonObject.getJSONObject("data").getJSONArray("list");
        for (int i=0; i<list.size(); i++) {
            JSONObject item = list.getJSONObject(i);
            String deployId = item.getString("deployment_id").trim();

            if (deployId.equals(id)) {
                //get env_param
                //skip EDGE_TEST_MODE
                JSONObject envPara = item.getJSONObject("env_param");
                for (Map.Entry<String, Object> entrySet : envPara.entrySet()) {
                    String key = entrySet.getKey().trim();
                    String value = String.valueOf(entrySet.getValue()).trim();
                    if (
                            key.equals("EDGE_TEST_MODE") ||
                            key.equals("EDGE_DEVICE_URL") ||
                            key.equals("EDGE_DEVICE_ID") ||
                            key.equals("EDGE_UID") ||
                            key.equals("EDGE_INSTANCE_ID") ||
                            key.equals("EDGE_APP_ID") ||
                            key.equals("EDGE_AK") ||
                            key.equals("EDGE_SK")
                    ) {
                        continue;
                    }
                    envMap.put(key, value);
                }
            }
        }

        return envMap;
    }

    private void replaceAndAddEnvPara(Map<String, String> envMap, String yamlFilePath) {
        FileUtil fileUtil = new FileUtil();
        List<String> content = fileUtil.getFileContent(yamlFilePath);
        List<String> edgeContent = new ArrayList<>();
        List<String> latestContent = new ArrayList<>();

        //filter all edge configuration item and replace it to latest
        for (String line : content) {
            String[] items = line.split("=");
            if (null == items || items.length < 2) {
                continue;
            }
            if (! line.contains("- EDGE_")) {
                //exclude non-edge-configuration line
                continue;
            }
            for (String key : envMap.keySet()) {
                if (items[0].contains(key)) {
                    //find same config item, replace its value
                    line = items[0] + "=" + envMap.get(key);
                    //remove already handled config item
                    envMap.remove(key);
                }
            }
            edgeContent.add(line);

        }

        //add new edge configuration item
        for (String key : envMap.keySet()) {
            //add non-existed configuration item
            String line = "    " + "- " + key + "=" + envMap.get(key);
            edgeContent.add(line);
        }

        //merge edge configuration to all file content list
        for (String line : content) {
            if (line.contains("- EDGE_")) {
                continue;
            }

            if (line.contains("    labels:")) {
                //insert latest edge configuration item
                for (String item :  edgeContent) {
                    latestContent.add(item);
                }
            }
            latestContent.add(line);

        }

        content = null;
        edgeContent = null;

        String latestYaml = yamlFilePath.substring(0, yamlFilePath.lastIndexOf("/")+1)+"docker-compose.yaml";
        fileUtil.writeContentToFile(latestYaml, latestContent);

    }
}
