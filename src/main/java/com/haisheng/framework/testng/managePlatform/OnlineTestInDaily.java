package com.haisheng.framework.testng.managePlatform;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.util.HttpExecutorUtil;
import com.haisheng.framework.util.StatusCode;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;

public class OnlineTestInDaily {

    private DailyManagePlatformUnit dailyManagePlatformUnit = new DailyManagePlatformUnit();
    private org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    private String genAuthURLDaily = "http://39.106.253.190/administrator/login";
    private String authOnline = null;
    private String authDaily = null;
    private HashMap<String, String> headerOnline = new HashMap();
    private HashMap<String, String> headerDaily = new HashMap();

    public SetToDaily setToDaily = new SetToDaily();
    public GetFromOnline getFromOnline = new GetFromOnline();

    private String genAuthURLOnline = "http://39.106.253.135/administrator/login";

//    private String appIdOnline = System.getProperty("APP_ID", "2cf019f4c443");
//    //主体、shop、scope id
//    private String subjectIdOnline = "246";
//    //平面id
//    private String layoutIdOnline = "247";
//    //区域id
//    private String regionIdOnline = "248";
//    //进出口id
//    private String entranceIdOnline = "249";
//    //设备id
//    private String deviceIdOnline = "6588528881599488";

    private String appIdOnline = System.getProperty("APP_ID");
    //主体、shop、scope id
    private String subjectIdOnline = System.getProperty("SHOP_ID");
    //平面id
    private String layoutIdOnline = System.getProperty("LAYOUT_ID");
    //区域id
    private String regionIdOnline = System.getProperty("REGION_ID");
    //进出口id
    private String entranceIdOnline = System.getProperty("ENTRANCE_ID");
    //设备id
    private String deviceIdOnline = System.getProperty("DEVICE_ID");

    private String REMOVE_NODE = System.getProperty("REMOVE_NODE", "true").trim();




    @BeforeClass
    public void initial() {
        genAuthDaily();
        genAuthOnline();

        getFromOnline.authorization = authOnline;
        setToDaily.authorization = authDaily;

        getFromOnline.header = headerOnline;
        setToDaily.header = headerDaily;
    }

    @AfterClass
    public void clean() throws Exception{
        // 释放原有边缘服务器
        if (REMOVE_NODE.toLowerCase().equals("true")) {
            setToDaily.removeNode(dailyManagePlatformUnit.nodeId,
                    dailyManagePlatformUnit.clusterNodeId);
        }
        logger.info("============================");
        logger.info("线下新建shop id: " + dailyManagePlatformUnit.nodeId);
        logger.info("线下新建device id: " + dailyManagePlatformUnit.dailyDeviceId);
        logger.info("============================");
    }


    @Test
    public void onlineTestInDaily() throws Exception {

//        确定唯一shop

        String res = getFromOnline.listSubject(appIdOnline, subjectIdOnline);

        JSONArray shopList = JSON.parseObject(res).getJSONObject("data").getJSONArray("list");

        for (int i = 0; i < shopList.size(); i++) {

            JSONObject single = shopList.getJSONObject(i);

//            取出原shop信息
            String subjectIdOld = single.getString("subject_id");

            int typeId = single.getInteger("type_id");
            String subjectName = single.getString("subject_name");

//            1、新建shop，查看shop是否已存在，不存则新建
            String s4 = setToDaily.listSubject(dailyManagePlatformUnit.appId, dailyManagePlatformUnit.brandId);
            String subjectIdNew = "";
            subjectIdNew = getSubjectIdByName(s4, subjectName);
            if ("".equals(subjectIdNew)) {
                setToDaily.addSubject(dailyManagePlatformUnit.brandId, subjectName, typeId);
                String listSubject = setToDaily.listSubject(dailyManagePlatformUnit.appId, dailyManagePlatformUnit.brandId);

                subjectIdNew = getSubjectIdByName(listSubject, subjectName);
                //更新shop id
                dailyManagePlatformUnit.nodeId = subjectIdNew;
                logger.info("new shop id: " + subjectIdNew);
            } else {
                dailyManagePlatformUnit.nodeId = subjectIdNew;
                logger.info("using already existed shop id: " + subjectIdNew);
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

//            确定唯一device
            String listDevice = getFromOnline.listDevice(deviceIdOnline);
            JSONArray devices = JSON.parseObject(listDevice).getJSONObject("data").getJSONArray("list");

            for (int j = 0; j < devices.size(); j++) {
                JSONObject singleDevice = devices.getJSONObject(j);

                String url = "rtsp://admin:winsense2018@192.168.50.152";
                String name = singleDevice.getString("name");
                String deviceType = singleDevice.getString("device_type");
                String cloudSceneType = singleDevice.getString("cloud_scene_type");

//                2、新建设备，已有同名设备则不新建
                String s = setToDaily.listDeviceBySubjectId(subjectIdNew);
                String deviceIdByName = getDeviceIdByName(s, name);
                if ("".equals(deviceIdByName)) {
                    setToDaily.addDevice(name, deviceType, "COMMON", cloudSceneType, url, subjectIdNew);
                }
            }

//            添加边缘服务器，无边缘服务器则添加
            String nodeList = setToDaily.listNodes(dailyManagePlatformUnit.nodeId);
            JSONArray clusterList = JSON.parseObject(nodeList).getJSONObject("data").getJSONObject("cluster_node_list").getJSONArray("list");
            if (null == clusterList || clusterList.size() == 0) {
                setToDaily.bindNode(dailyManagePlatformUnit.nodeId,
                        dailyManagePlatformUnit.clusterNodeId,
                        dailyManagePlatformUnit.clusterAlias);
            }


//            获取平面信息
            String listLayout = getFromOnline.listLayout(subjectIdOld);

            JSONArray layouts = JSON.parseObject(listLayout).getJSONObject("data").getJSONArray("list");

            for (int j = 0; j < layouts.size(); j++) {
                JSONObject singleLayout = layouts.getJSONObject(j);
                String layoutIdOld = singleLayout.getString("layout_id");

                if (!layoutIdOnline.equals(layoutIdOld)) {
                    continue;
                }

                logger.info("Found online layout " + layoutIdOld + ", getting information");
                String name = singleLayout.getString("name");
                String description = singleLayout.getString("description");
                String floorId = singleLayout.getString("floor_id");
                String layoutPicOssOld = singleLayout.getString("layout_pic_oss");

//                3、创建平面
                String s = setToDaily.listLayout(subjectIdNew);
                String layoutIdNew = getLayoutIdByName(s, name);
                logger.info("starting create new layout " + layoutIdNew);
                String layoutPic = "";
                if ("".equals(layoutIdNew)) {
                    String addLayout = setToDaily.addLayout(name, description, subjectIdNew, floorId, StatusCode.SUCCESS);
                    layoutIdNew = getLayoutId(addLayout);
                    logger.info("done！ create new layout " + layoutIdNew);
                } else {
                    //平面已存在
                    logger.info("layout " + layoutIdNew + " already existed, not create again.");
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

                    if (!deviceIdOnline.equals(layoutDeviceIdOld)) {
                        continue;
                    }

//                    获取新建的设备,并绑定
                    String subjectDevices = setToDaily.listDeviceBySubjectId(subjectIdNew);

                    String deviceId = getDeviceIdByName(subjectDevices, layoutDeviceName);
                    dailyManagePlatformUnit.dailyDeviceId = deviceId;

                    String s2 = setToDaily.listLayoutDevice(layoutIdNew);
                    if ("".equals(getDeviceIdByName(s2, layoutDeviceName))) {
                        setToDaily.startDevice(deviceId);

                        logger.info("sleep 5m, starting device......");
                        Thread.sleep(5*60*1000);

                        setToDaily.stopDevice(deviceId);
                        logger.info("start to bind new device " + deviceId + " to layout " + layoutIdNew);
                        setToDaily.addLayoutDevice(layoutIdNew, deviceId);
                    }

                    if (singleLayoutDevice.getBooleanValue("mapping") == true) {
                        logger.info("getting mapping information");
//                    获取映射详情
                        String layoutMapping = getFromOnline.getLayoutMapping(deviceIdOnline, layoutIdOld);
                        JSONObject data = JSON.parseObject(layoutMapping).getJSONObject("data");

                        JSONObject device_mapping = data.getJSONObject("device_mapping");
                        JSONArray layout_matrix = data.getJSONArray("layout_matrix");
                        JSONObject device_location = data.getJSONObject("device_location");
                        JSONObject layout_mapping = data.getJSONObject("layout_mapping");

//                    映射
                        logger.info("start mapping");
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

                if (!regionIdOnline.equals(regionIdOld)) {
                    continue;
                }

                String regionType = singleRegion.getString("region_type");
                String regionName = singleRegion.getString("region_name");

                JSONArray region_location = singleRegion.getJSONArray("region_location");

                String layoutIdOld = singleRegion.getString("layout_id");
                String layoutIdNew = "";
                String dailyRegionName = "";
                boolean isLayoutRegion = true;
                if (layoutIdOld == null || "".equals(layoutIdOld)) {
                    layoutIdNew = "";
                    isLayoutRegion = false;
                } else {
                    JSONObject layoutInfo = singleRegion.getJSONObject("layout_info");
                    dailyRegionName = layoutInfo.getString("name");

                    String listLayout1 = setToDaily.listLayout(subjectIdNew);

                    layoutIdNew = getLayoutIdByName(listLayout1, dailyRegionName);
                }

//                4.1 新建区域
                logger.info("creating region");
                String regionIdNew = "";
                if ("STORE".equals(regionType)) {
                    String listRegion1 = setToDaily.listRegion(subjectIdNew);
                    regionIdNew = getRegionIdNyName(listRegion1, "全店区域" + ":" + subjectName);
                } else if ("DEFAULT".equals(regionType)) {
                    String listRegion1 = setToDaily.listRegion(subjectIdNew);
                    regionIdNew = getRegionIdNyName(listRegion1, "楼层区域" + ":" + dailyRegionName);
                } else {
                    String listRegion1 = setToDaily.listRegion(subjectIdNew);
                    regionIdNew = getRegionIdNyName(listRegion1, regionName);
                    if ("".equals(regionIdNew)) {
                        String addRegion = setToDaily.addRegion(subjectIdNew, regionName, regionType, layoutIdNew, isLayoutRegion);
                        regionIdNew = getRegionId(addRegion);
                    }
                }

//                4.2 更新区域（区域详情）
                logger.info("updating region");
                setToDaily.updateRegion(regionIdNew, region_location);

//                4.3 区域设备
                String s = getFromOnline.listRegionDevice(regionIdOld);
                JSONArray regionDevices = JSON.parseObject(s).getJSONObject("data").getJSONArray("list");
                for (int n = 0; n < regionDevices.size(); n++) {
                    JSONObject singleDevice = regionDevices.getJSONObject(n);
                    String name = singleDevice.getString("name");
                    if (!deviceIdOnline.equals(singleDevice.getString("name"))) {
                        continue;
                    }

                    String s2 = setToDaily.listRegionDevice(regionIdNew);
                    if ("".equals(getDeviceIdByName(s2, name))) {
//                    查询新建的设备的id
                        String s1 = setToDaily.listDeviceByDeviceId(subjectIdNew);
                        String deviceId = getDeviceIdByName(s1, name);

//                    绑定区域设备
                        logger.info("binding device");
                        setToDaily.addRegionDevice(regionIdNew, deviceId);
                    }
                    break;
                }

//                获取出入口
                String listEntrance = getFromOnline.listEntrance(regionIdOld);

                JSONArray entranceList = JSON.parseObject(listEntrance).getJSONObject("data").getJSONArray("list");
                for (int m = 0; m < entranceList.size(); m++) {
                    JSONObject singleEntrance = entranceList.getJSONObject(m);
                    String entranceIdOld = singleEntrance.getString("entrance_id");

                    if (!entranceIdOnline.equals(entranceIdOld)) {
                        continue;
                    }

                    String entranceType = singleEntrance.getString("entrance_type");
                    String entranceName = singleEntrance.getString("entrance_name");

                    boolean use_line = singleEntrance.getBooleanValue("use_line");
                    boolean both_dir = singleEntrance.getBooleanValue("both_dir");
                    boolean is_stair = singleEntrance.getBooleanValue("is_stair");

                    String entranceListNew = setToDaily.listEntrance(regionIdNew);

                    String entranceIdNew = getEntranceIdByName(entranceListNew, entranceName);
//                    新建出入口
                    logger.info("creating entrance");
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
                        if (!deviceIdOnline.equals(jsonObject.getString("device_id"))) {
                            continue;
                        }

                        String name = jsonObject.getString("name");
                        JSONObject entrancePoint = jsonObject.getJSONObject("entrance_point");
                        JSONArray entranceLoc = jsonObject.getJSONArray("entrance_location");
                        JSONArray entranceDpLoc = jsonObject.getJSONArray("entrance_dp_location");

                        String s6 = setToDaily.listEntranceDevice(entranceIdNew);
                        String deviceIdEntrance = getDeviceIdByName(s6, name);

                        if ("".equals(deviceIdEntrance)) {
                            String s2 = setToDaily.listDeviceBySubjectId(subjectIdNew);
                            deviceIdEntrance = getDeviceIdByName(s2, name);
                            setToDaily.bindEntranceDevice(entranceIdNew, deviceIdEntrance, entrancePoint, entranceLoc, entranceDpLoc);
                        }

                        setToDaily.updateEntranceDevice(entranceIdNew, deviceIdEntrance, entrancePoint, entranceLoc, entranceDpLoc);
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
                        "  \"email\": \"yuhaisheng@winsense.ai\"," +
                        "  \"password\": \"efe03eff1233eea2d1316db23ded613d\"" +
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

}
