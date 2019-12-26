package com.haisheng.framework.testng.managePlatform;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.util.HttpExecutorUtil;
import com.haisheng.framework.util.StatusCode;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.util.HashMap;

public class CopyFromOnline {

    private String APP_ID = "0d28ec728799";
    private String BRAND_ID = "638";

    private org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    private String genAuthURLDaily = "http://39.106.253.190/administrator/login";
    private String authOnline = null;
    private String authDaily = null;
    private HashMap<String, String> headerOnline = new HashMap();
    private HashMap<String, String> headerDaily = new HashMap();

    public SetToDaily setToDaily = new SetToDaily();
    public GetFromOnline getFromOnline = new GetFromOnline();

    private String genAuthURLOnline = "http://39.106.253.135/administrator/login";

    String fromAppId = "";
    String fromSubjectId = "97";

    @Test
    public void onlineTestInDaily() throws Exception {

        String res = getFromOnline.listSubject(fromAppId, fromSubjectId);

        JSONArray shopList = JSON.parseObject(res).getJSONObject("data").getJSONArray("list");

        for (int i = 0; i < shopList.size(); i++) {
            JSONObject single = shopList.getJSONObject(i);

//            取出原shop信息
            String subjectIdOld = single.getString("subject_id");

            int typeId = single.getInteger("type_id");
            String subjectName = single.getString("subject_name");

//            1、新建shop
            String s4 = setToDaily.listSubject(APP_ID, BRAND_ID);
            String subjectIdNew = "";
            subjectIdNew = getSubjectIdByName(s4, subjectName);
            if ("".equals(subjectIdNew)) {
                setToDaily.addSubject(BRAND_ID, subjectName, typeId);
                String listSubject = setToDaily.listSubject(APP_ID, BRAND_ID);

                subjectIdNew = getSubjectIdByName(listSubject, subjectName);
            }

//            配置服务
            String s5 = setToDaily.queryByNodeId(subjectIdNew);
            JSONArray jsonArrayDelete = JSON.parseObject(s5).getJSONObject("data").getJSONArray("list");

            for (int k = 0; k < jsonArrayDelete.size(); k++) {
                JSONObject jsonObject = jsonArrayDelete.getJSONObject(k);
                String id = jsonObject.getString("id");
                setToDaily.deleteConfig(id);
            }

            String s3 = getFromOnline.queryByNodeId(subjectIdOld);

//            删除新建shop时自动添加的服务
            JSONArray jsonArrayAdd = JSON.parseObject(s3).getJSONObject("data").getJSONArray("list");

//            添加新的服务
            for (int j = 0; j < jsonArrayAdd.size(); j++) {
                JSONObject conf = jsonArrayAdd.getJSONObject(j);
                setToDaily.nodeServiceConfig(subjectIdNew, conf.getString("service_id"), conf.getJSONObject("cloud_config"));
            }

//            获取旧设备信息
            String listDevice = getFromOnline.listDevice(subjectIdOld);

            JSONArray devices = JSON.parseObject(listDevice).getJSONObject("data").getJSONArray("list");

            for (int j = 0; j < devices.size(); j++) {
                JSONObject singleDevice = devices.getJSONObject(j);
                String url = "rtsp://admin:winsense2018@192.168.50.153";
                String name = singleDevice.getString("name");
                String deviceType = singleDevice.getString("device_type");
                String cloudSceneType = singleDevice.getString("cloud_scene_type");

//                2、新建设备
                String s = setToDaily.listDevice(subjectIdNew);
                String deviceIdByName = getDeviceIdByName(s, name);
                if ("".equals(deviceIdByName)) {
                    setToDaily.addDevice(name, deviceType, "COMMON", cloudSceneType, url, subjectIdNew);
                }
            }

//            获取平面信息
            String listLayout = getFromOnline.listLayout(subjectIdOld);

            JSONArray layouts = JSON.parseObject(listLayout).getJSONObject("data").getJSONArray("list");

            for (int j = 0; j < layouts.size(); j++) {
                JSONObject singleLayout = layouts.getJSONObject(j);
                String layoutIdOld = singleLayout.getString("layout_id");
                String name = singleLayout.getString("name");
                String description = singleLayout.getString("description");
                String floorId = singleLayout.getString("floor_id");
                String layoutPicOssOld = singleLayout.getString("layout_pic_oss");

//                3、创建平面
                String s = setToDaily.listLayout(subjectIdNew);
                String layoutIdNew = getLayoutIdByName(s, name);
                String layoutPic = "";
                if ("".equals(layoutIdNew)) {
                    String addLayout = setToDaily.addLayout(name, description, subjectIdNew, floorId, StatusCode.SUCCESS);
                    layoutIdNew = getLayoutId(addLayout);
                }

                layoutPic = setToDaily.uploadLayoutPic(layoutPicOssOld);
//                更新平面图
                setToDaily.updateLayout(layoutIdNew, name, description, layoutPic, floorId);

//                获取平面绑定设备
                String layoutDevice = getFromOnline.listLayoutDevice(layoutIdOld);
                JSONArray layoutdeviceList = JSON.parseObject(layoutDevice).getJSONObject("data").getJSONArray("list");
                for (int k = 0; k < layoutdeviceList.size(); k++) {
                    JSONObject singleLayoutDevice = layoutdeviceList.getJSONObject(k);
                    String layoutDeviceName = singleLayoutDevice.getString("name");
                    String layoutDeviceIdOld = singleLayoutDevice.getString("device_id");

//                    获取新建的设备,并绑定
                    String subjectDevices = setToDaily.listDevice(subjectIdNew);

                    String deviceId = getDeviceIdByName(subjectDevices, layoutDeviceName);

                    String s2 = setToDaily.listLayoutDevice(layoutIdNew);
                    if ("".equals(getDeviceIdByName(s2, layoutDeviceName))) {
                        setToDaily.startDevice(deviceId);

                        Thread.sleep(60000);

                        setToDaily.stopDevice(deviceId);

                        setToDaily.addLayoutDevice(layoutIdNew, deviceId);
                    }

                    if (singleLayoutDevice.getBooleanValue("mapping") == true) {
//                    获取映射详情
                        String layoutMapping = getFromOnline.getLayoutMapping(layoutDeviceIdOld, layoutIdOld);
                        JSONObject data = JSON.parseObject(layoutMapping).getJSONObject("data");

                        JSONObject device_mapping = data.getJSONObject("device_mapping");
                        JSONArray layout_matrix = data.getJSONArray("layout_matrix");
                        JSONObject device_location = data.getJSONObject("device_location");
                        JSONObject layout_mapping = data.getJSONObject("layout_mapping");

//                    映射
                        setToDaily.analysisMatrix(deviceId, layoutIdNew, device_mapping, layout_matrix, device_location, layout_mapping);

                        setToDaily.layoutMapping(deviceId, layoutIdNew, device_mapping, layout_matrix, device_location, layout_mapping);
                    }
                }
            }

//            4、区域
            String listRegion = getFromOnline.listRegion(subjectIdOld);

            JSONArray regionList = JSON.parseObject(listRegion).getJSONObject("data").getJSONArray("list");

            for (int k = 0; k < regionList.size(); k++) {
                JSONObject singleRegion = regionList.getJSONObject(k);
                String regionIdOld = singleRegion.getString("region_id");
                String regionType = singleRegion.getString("region_type");
                String regionName = singleRegion.getString("region_name");

                JSONArray region_location = singleRegion.getJSONArray("region_location");

                String layoutIdOld = singleRegion.getString("layout_id");
                String layoutIdNew = "";
                boolean isLayoutRegion = true;
                if (layoutIdOld == null || "".equals(layoutIdOld)) {
                    layoutIdNew = "";
                    isLayoutRegion = false;
                } else {
                    JSONObject layoutInfo = singleRegion.getJSONObject("layout_info");
                    String name = layoutInfo.getString("name");

                    String listLayout1 = setToDaily.listLayout(subjectIdNew);

                    layoutIdNew = getLayoutIdByName(listLayout1, name);
                }

//                4.1 新建区域
                String regionIdNew = "";
                if ("STORE".equals(regionType)) {
                    String listRegion1 = setToDaily.listRegion(subjectIdNew);
                    regionIdNew = getRegionIdNyName(listRegion1, "全店区域" + ":" + subjectName);
                } else if ("DEFAULT".equals(regionType)) {
                    String listRegion1 = setToDaily.listRegion(subjectIdNew);
                    regionIdNew = getRegionIdNyName(listRegion1, "楼层区域" + ":" + subjectName);
                } else {
                    String listRegion1 = setToDaily.listRegion(subjectIdNew);
                    regionIdNew = getRegionIdNyName(listRegion1, regionName);
                    if ("".equals(regionIdNew)) {
                        String addRegion = setToDaily.addRegion(subjectIdNew, regionName, regionType, layoutIdNew, isLayoutRegion);
                        regionIdNew = getRegionId(addRegion);
                    }
                }

//                4.2 更新区域（区域详情）
                setToDaily.updateRegion(regionIdNew, region_location);

//                4.3 区域设备
                String s = getFromOnline.listRegionDevice(regionIdOld);
                JSONArray regionDevices = JSON.parseObject(s).getJSONObject("data").getJSONArray("list");
                for (int n = 0; n < regionDevices.size(); n++) {
                    JSONObject singleDevice = regionDevices.getJSONObject(n);
                    String name = singleDevice.getString("name");

                    String s2 = setToDaily.listRegionDevice(regionIdNew);
                    if ("".equals(getDeviceIdByName(s2, name))) {
//                    查询新建的设备的id
                        String s1 = setToDaily.listDevice(subjectIdNew);
                        String deviceId = getDeviceIdByName(s1, name);

//                    绑定区域设备
                        setToDaily.addRegionDevice(regionIdNew, deviceId);

                    }
                }

//                获取出入口
                String listEntrance = getFromOnline.listEntrance(regionIdOld);

                JSONArray entranceList = JSON.parseObject(listEntrance).getJSONObject("data").getJSONArray("list");
                for (int m = 0; m < entranceList.size(); m++) {
                    JSONObject singleEntrance = entranceList.getJSONObject(m);
                    String entranceIdOld = singleEntrance.getString("entrance_id");
                    String entranceType = singleEntrance.getString("entrance_type");
                    String entranceName = singleEntrance.getString("entrance_name");

                    boolean use_line = singleEntrance.getBooleanValue("use_line");
                    boolean both_dir = singleEntrance.getBooleanValue("both_dir");
                    boolean is_stair = singleEntrance.getBooleanValue("is_stair");

                    String entranceListNew = setToDaily.listEntrance(regionIdNew);

                    String entranceIdNew = getEntranceIdByName(entranceListNew, entranceName);
                    if ("".equals(entranceIdNew)) {
                        String addEntrance = setToDaily.addEntrance(entranceName, entranceType, regionIdNew, use_line, both_dir, is_stair);

                        entranceIdNew = getEntranceId(addEntrance);
                    }

//                    进出口详情
                    String entranceDetail = getFromOnline.getEntrance(entranceIdOld);

                    JSONArray entranceMapLoc = JSON.parseObject(entranceDetail).getJSONObject("data").getJSONArray("entrance_map_location");

//                    在平面图中画进出口的位置
                    setToDaily.updateEntrance(entranceIdNew, entranceMapLoc);

//                    获取进出口绑定的设备列表
                    String s1 = getFromOnline.listEntranceDevice(entranceIdOld);
                    JSONArray jsonArray = JSON.parseObject(s1).getJSONObject("data").getJSONArray("list");
                    for (int l = 0; l < jsonArray.size(); l++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(l);
                        String name = jsonObject.getString("name");
                        JSONObject entrancePoint = jsonObject.getJSONObject("entrance_point");
                        JSONArray entranceLoc = jsonObject.getJSONArray("entrance_location");
                        JSONArray entranceDpLoc = jsonObject.getJSONArray("entrance_dp_location");

                        String s6 = setToDaily.listEntranceDevice(entranceIdNew);
                        String deviceIdEntrance = getDeviceIdByName(s6, name);

                        if ("".equals(deviceIdEntrance)) {
                            String s2 = setToDaily.listDevice(subjectIdNew);
                            deviceIdEntrance = getDeviceIdByName(s2, name);
                            setToDaily.bindEntranceDevice(entranceIdNew, deviceIdEntrance, entrancePoint, entranceLoc, entranceDpLoc);
                        }
                    }
                }
            }
        }
    }

    public void getName(String res) {
        JSONArray jsonArray = JSON.parseObject(res).getJSONObject("data").getJSONArray("list");
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            String scene_name = jsonObject.getString("scene_name");
            System.out.println(scene_name + "，");
        }

        System.out.println("\n\n");

    }

    public String getSubjectIdByName(String res, String subjectName) throws Exception {

        String subjectId = "";
        JSONArray list = JSON.parseObject(res).getJSONObject("data").getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            JSONObject listSingle = list.getJSONObject(i);
            String subjectNameRes = listSingle.getString("subject_name");
            if (subjectName.equals(subjectNameRes)) {
                subjectId = listSingle.getString("subject_id");
                break;
            }
        }

        return subjectId;
    }

    public String getLayoutId(String response) {
        JSONObject data = JSON.parseObject(response).getJSONObject("data");
        String layoutId = data.getString("layout_id");

        return layoutId;
    }


    public String getLayoutIdByName(String response, String name) {
        String layoutId = "";
        JSONArray jsonArray = JSON.parseObject(response).getJSONObject("data").getJSONArray("list");
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            if (name.equals(jsonObject.getString("name"))) {
                layoutId = jsonObject.getString("layout_id");
            }

        }

        return layoutId;
    }

    public String getRegionId(String response) {
        JSONObject data = JSON.parseObject(response).getJSONObject("data");
        String regionId = data.getString("region_id");

        return regionId;
    }

    public String getRegionIdNyName(String response, String name) {
        JSONArray list = JSON.parseObject(response).getJSONObject("data").getJSONArray("list");

        String regionId = "";

        for (int i = 0; i < list.size(); i++) {
            JSONObject singleRegion = list.getJSONObject(i);
            if (name.equals(singleRegion.getString("region_name"))) {
                regionId = singleRegion.getString("region_id");
            }
        }

        return regionId;
    }

    public String getDeviceIdByName(String response, String name) {
        JSONArray list = JSON.parseObject(response).getJSONObject("data").getJSONArray("list");

        String deviceId = "";

        for (int i = 0; i < list.size(); i++) {
            JSONObject jsonObject = list.getJSONObject(i);
            if (name.equals(jsonObject.getString("name"))) {
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

    public String getEntranceIdByName(String response, String name) {

        JSONArray list = JSON.parseObject(response).getJSONObject("data").getJSONArray("list");

        String entranceId = "";

        for (int i = 0; i < list.size(); i++) {
            JSONObject single = list.getJSONObject(i);
            if (name.equals(single.getString("entrance_name"))) {
                entranceId = single.getString("entrance_id");
            }

        }

        return entranceId;
    }

    public JSONObject getConfigByServiceId(String res, String serviceId) {

        JSONArray jsonArray = JSON.parseObject(res).getJSONObject("data").getJSONArray("list");

        JSONObject cloudConfig = null;

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            if (serviceId.equals(jsonObject.getString("service_id"))) {
                cloudConfig = jsonObject.getJSONObject("cloud_config");
            }
        }

        return cloudConfig;
    }

    public String genAuthJson() {
        String json =
                "{\n" +
                        "  \"email\": \"liaoxiangru@winsense.ai\"," +
                        "  \"password\": \"e586aee0d9d9fdb16b9982adb74aeb60\"" +
                        "}";

        return json;
    }

    void genAuthOnline() {

        String json = genAuthJson();

        try {
            String response = postRequest(genAuthURLOnline, json, new HashMap());
            logger.info("\n");
            JSONObject data = JSON.parseObject(response).getJSONObject("data");
            authOnline = data.getString("token");

            headerOnline.put("Authorization", authOnline);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void genAuthDaily() {

        String json = genAuthJson();

        try {
            String response = postRequest(genAuthURLDaily, json, new HashMap());
            logger.info("\n");
            JSONObject data = JSON.parseObject(response).getJSONObject("data");
            authDaily = data.getString("token");

            headerDaily.put("Authorization", authDaily);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String postRequest(String url, String json, HashMap header) throws Exception {
        HttpExecutorUtil executor = new HttpExecutorUtil();
        executor.doPostJsonWithHeaders(url, json, header);
        return executor.getResponse();
    }

    @BeforeSuite
    public void initial() {
        genAuthDaily();
        genAuthOnline();

        getFromOnline.authorization = authOnline;
        setToDaily.authorization = authDaily;

        getFromOnline.header = headerOnline;
        setToDaily.header = headerDaily;
    }
}
