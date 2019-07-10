package com.haisheng.framework.testng.console;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.model.bean.Case;
import com.haisheng.framework.testng.CommonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.util.HttpExecutorUtil;
import com.haisheng.framework.util.QADbUtil;
import com.haisheng.framework.util.StatusCode;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.util.HashMap;

public class Console {

    private org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    private String DeviceUrl = "rtmp://192.168.50.3/live/uv-test";
    private String UID = "uid_04e816df";
    private String APPLICATION_ID = "0d28ec728799";
    private String BRAND_ID = "638";
    private String SHOP_Id = "640";
    private String DEVICE_ID_1 = "6563114751165440";
    private String DEVICE_ID_2 = "6563115959256064";
    private String DEVICE_ID_3 = "6563182824653824";
    private String LAYOUT_ID = "646";
    private String REGION_ID = "648";
    private String ENTRANCE_ID = "662";
    private String LAYOUT_PIC_OSS = "console_operation/dev/console/layout/f9c36670-1675-41bf-addd-c7f82cd160cb/办公室平面图";

    private String entrancTypeGround = "GROUND";
    private String entrancTypeParking = "PARKING";
    private String entrancTypeRegion = "REGION";

    private String deviceTypeFaceCamera = "FACE_CAMERA";
    private String deviceTypeWebCamera = "WEB_CAMERA";

    private String subjectTypeShop = "2";
    private String subjectTypeMarket = "3";
    private String subjectTypeChain = "4";

    private String subjectTypeNameShop = "门店";
    private String subjectTypeNameMarket = "购物中心";
    private String subjectTypeNameChain = "连锁超市";

    private String DingDingUrl = "https://oapi.dingtalk.com/robot/send?access_token=f9b712af64398d3b3234e1657069b9784f7a9360e7afc085211b194841056dca";
    private String Email = "liaoxiangru@winsense.ai";

    private String response;

    private String genAuthURL = "http://dev.sso.winsenseos.com/sso/login";
    private String authorization = null;
    private HashMap<String, String> header = new HashMap();

    private String failReason = "";
    private QADbUtil qaDbUtil = new QADbUtil();
    private int APP_ID = ChecklistDbInfo.DB_APP_ID_OPEN_PLATFORM_SERVICE;
    private int CONFIG_ID = ChecklistDbInfo.DB_SERVICE_ID_CONTROL_CENTER_SERVICE;

    private String CI_CMD = "curl -X POST http://liaoxiangru:liaoxiangru@192.168.50.2:8080/job/console-interface/buildWithParameters?case_name=";

    private String URL_prefix = "http://dev.console.winsenseos.com/consolePlateform/CONSOLE/";

    private String addDeviceServiceId = "3556";
    private String updateDeviceServiceId = "3557";
    private String deleteDeviceServiceId = "3558";
    private String getDeviceServiceId = "3559";
    private String listDeviceServiceId = "3560";
    private String startDeviceServiceId = "3561";
    private String stopDeviceServiceId = "3562";
    private String statusDeviceServiceId = "3563";
    private String batchStartDeviceServiceId = "3564";
    private String batchStopDeviceServiceId = "3565";
    private String batchRemoveDeviceServiceId = "3566";
    private String batchMonitorDeviceServiceId = "3567";
    private String typeListDeviceServiceId = "3568";
    private String getPicDeviceServiceId = "3569";

    private String addEntranceServiceId = "3546";
    private String deleteEntranceServiceId = "3548";
    private String updateEntranceServiceId = "3547";
    private String getEntranceServiceId = "3549";
    private String listEntranceServiceId = "3550";
    private String addEntranceDeviceServiceId = "3551";
    private String updateEntranceDeviceServiceId = "3552";
    private String deleteEntranceDeviceServiceId = "3553";
    private String listEntranceDeviceServiceId = "3554";
    private String bindableEntranceDeviceServiceId = "3555";
    private String listEntranceEnumServiceId = "3577";

    private String addRegionServiceId = "3537";
    private String updateRegionServiceId = "3538";
    private String deleteRegionServiceId = "3539";
    private String getRegionServiceId = "3540";
    private String listRegionServiceId = "3541";
    private String addRegionDeviceServiceId = "3542";
    private String deleteRegionDeviceServiceId = "3543";
    private String listRegionDeviceServiceId = "3544";
    private String bindableRegionDeviceServiceId = "3545";

    private String addLayoutServiceId = "3522";
    private String delLayoutPicServiceId = "3573";
    private String delLayoutServiceId = "3524";
    private String updateLayoutServiceId = "3523";
    private String getLayoutServiceId = "3525";
    private String listLayoutServiceId = "3526";
    private String addLayoutDeviceServiceId = "3527";
    private String batchAddLayoutDeviceServiceId = "3528";
    private String delLayoutDeviceServiceId = "3529";
    private String listLayoutDeviceServiceId = "3530";
    private String bindableLayoutDeviceServiceId = "3531";
    private String addLayoutMappingServiceId = "3532";
    private String updateLayoutMappingServiceId = "3533";
    private String getLayoutMappingServiceId = "3534";
    private String delLayoutMappingServiceId = "3535";
    private String analysisMatrixServiceId = "3536";

    private String addSubjectServiceId = "3514";
    private String delSubjectServiceId = "3516";
    private String updateSubjectServiceId = "3515";
    private String getSubjectServiceId = "3517";
    private String listSubjectServiceId = "3518";
    private String arraySubjectServiceId = "3519";


    private String addBrandServiceId = "3507";
    private String updateBrandServiceId = "3508";
    private String delBrandServiceId = "3509";
    private String getBrandServiceId = "3510";
    private String listBrandServiceId = "3511";

    private String addAppServiceId = "3501";
    private String updateAppServiceId = "3502";
    private String delAppServiceId = "3503";
    private String getAppServiceId = "3504";
    private String listAppServiceId = "3505";
    private String resetAppServiceId = "3506";

//----------------------------------------------------------模块一、设备管理------------------------------------------------------------------

    public String addDevice(String name, String deviceType, String deviceUrl) {
        logger.info("\n");
        logger.info("-------------------------add device!---3556-----------------------");

        String json = genAddDevicePara(name, deviceType, deviceUrl);
        try {
            response = sendRequestWithHeader(addDeviceServiceId, json, header);
        } catch (Exception e) {
            e.printStackTrace();
        }
        {

        }
        return response;
    }

    public String genAddDevicePara(String name, String deviceType, String deviceUrl) {

        String json =
                "{" +
                        "    \"name\":\"" + name + "\"," +
                        "    \"device_type\":\"" + deviceType + "\"," +
                        "    \"config\":{" +
                        "" +
                        "    }," +
                        "    \"url\":\"" + deviceUrl + "\"," +
                        "    \"subject_id\":\"" + SHOP_Id + "\"" +
                        "}";

        return json;
    }

    public String deleteDevice(String deviceId) {
        logger.info("\n");
        logger.info("-------------------------delete device!---3558--------------------");

        String json = genDeleteDevicePara(deviceId);

        try {
            response = sendRequestWithHeader(deleteDeviceServiceId, json, header);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public String genDeleteDevicePara(String deviceId) {

        String json =
                "{\"device_id\":\"" + deviceId + "\"}";

        return json;
    }

    public String updateDevice(String name, String deviceId) {
        logger.info("\n");
        logger.info("-------------------------update device!---3557--------------------");

        String json = genUpdateDevicePara(name, deviceId);

        try {
            response = sendRequestWithHeader(updateDeviceServiceId, json, header);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public String genUpdateDevicePara(String name, String deviceId) {

        String json =
                "{" +
                        "    \"name\":\"" + name + "\"," +
                        "    \"device_type\":\"" + deviceTypeWebCamera + "\"," +
                        "    \"url\":\"" + DeviceUrl + "," +
                        "    \"device_id\":\"" + deviceId + "\"," +
                        "    \"monitor_config\":{" +
                        "        \"open\":true," +
                        "        \"interval\":300," +
                        "        \"time\":[" +
                        "            9," +
                        "            22" +
                        "        ]," +
                        "        \"ding_ding\":[" +
                        "            \"" + DingDingUrl + "\"" +
                        "        ]," +
                        "        \"email\":[" +
                        "            \"" + Email + "\"" +
                        "        ]" +
                        "    }" +
                        "}";

        return json;
    }

    public String getDevice(String deviceId) {
        logger.info("\n");
        logger.info("-------------------------get device!---3559--------------------");
        String json = genGetDevicePara(deviceId);

        try {
            response = sendRequestWithHeader(getDeviceServiceId, json, header);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public String genGetDevicePara(String deviceId) {
        String json =
                "{\"device_id\":\"" + deviceId + "\"}";

        return json;
    }


    public String listDevice() {
        logger.info("\n");
        logger.info("-------------------------list device!---3560--------------------");
        String json = genListDevicePara();

        try {
            response = sendRequestWithHeader(listDeviceServiceId, json, header);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public String genListDevicePara() {
        String json =
                "{" +
                        "    \"page\":1," +
                        "    \"size\":10" +
                        "}";

        return json;
    }

    public String startDevice(String deviceId) {
        logger.info("\n");
        logger.info("-------------------------start device!---3561--------------------");

        String json = genStartDevicePara(deviceId);

        try {
            response = sendRequestWithHeader(startDeviceServiceId, json, header);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public String genStartDevicePara(String deviceId) {

        String json = "{\"device_id\":\"" + deviceId + "\"}";

        return json;
    }


    public String stopDevice(String deviceId) {
        logger.info("\n");
        logger.info("-------------------------stop device!---3562--------------------");

        String json = genStopDevicePara(deviceId);

        try {
            response = sendRequestWithHeader(stopDeviceServiceId, json, header);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public String genStopDevicePara(String deviceId) {

        String json = "{\"device_id\":\"" + deviceId + "\"}";

        return json;
    }


    public String batchStartDevice(String deviceIdArr) {
        logger.info("\n");
        logger.info("-------------------------batch start device!---3564--------------------");

        String json = genBatchStartDevicePara(deviceIdArr);

        try {
            response = sendRequestWithHeader(batchStartDeviceServiceId, json, header);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public String genBatchStartDevicePara(String deviceIdArr) {

        String json =
                "{" +
                        "        \"list\":[" + deviceIdArr +
                        "    ]" +
                        "    }";

        return json;
    }

    public String batchStopDevice(String deviceIdArr) {
        logger.info("\n");
        logger.info("-------------------------batch stop device!---3565--------------------");

        String json = genBatchStopDevicePara(deviceIdArr);

        try {
            response = sendRequestWithHeader(batchStopDeviceServiceId, json, header);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public String genBatchStopDevicePara(String deviceIdArr) {

        String json =
                "{" +
                        "        \"list\":[" + deviceIdArr +
                        "    ]" +
                        "    }";

        return json;
    }

    public String batchRemoveDevice(String deviceIdArr) {
        logger.info("\n");
        logger.info("-------------------------batch remove device!---3566--------------------");

        String json = genBatchRemoveDevicePara(deviceIdArr);

        try {
            response = sendRequestWithHeader(batchRemoveDeviceServiceId, json, header);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public String genBatchRemoveDevicePara(String deviceIdArr) {

        String json =
                "{" +
                        "        \"list\":[" + deviceIdArr +
                        "    ]" +
                        "    }";

        return json;
    }


    public String batchMonitorDevice(String deviceIdArr) {
        logger.info("\n");
        logger.info("-------------------------batch monitor device!---3567--------------------");

        String json = genBatchMonitorDevicePara(deviceIdArr);

        try {
            response = sendRequestWithHeader(batchMonitorDeviceServiceId, json, header);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public String genBatchMonitorDevicePara(String deviceIdArr) {

        String json =
                "{" +
                        "    \"list\":[" + deviceIdArr +
                        "    ]," +
                        "    \"monitor_config\":{" +
                        "        \"open\":true," +
                        "        \"interval\":300," +
                        "        \"time\":[" +
                        "            9," +
                        "            22" +
                        "        ]," +
                        "        \"ding_ding\":[" +
                        "            \"" + DingDingUrl + "\"" +
                        "        ]," +
                        "        \"email\":[" +
                        "            \"" + Email + "\"" +
                        "        ]" +
                        "    }" +
                        "}";

        return json;
    }


    //    -------------------------------------------出入口模块---------------------------------------------
    public String addEntrance(String regionId, String entranceName, String entranceType) {
        logger.info("\n");
        logger.info("-------------------------add entrance!---3546--------------------");

        String json = genAddEntrancePara(regionId, entranceName, entranceType);

        try {
            response = sendRequestWithHeader(addEntranceServiceId, json, header);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public String genAddEntrancePara(String regionId, String entranceName, String entranceType) {

        String json =
                "{" +
                        "    \"entrance_name\":\"" + entranceName + "\"," +
                        "    \"entrance_type\":\"" + entranceType + "\"," +
                        "    \"region_id\":\"" + regionId + "\"" +
                        "}";

        return json;
    }

    public String updateEntrance(String entranceName, String entranceType, String entranceId) {
        logger.info("\n");
        logger.info("-------------------------update entrance!---3547--------------------");

        String json = genUpdateEntrancePara(entranceName, entranceType, entranceId);

        try {
            response = sendRequestWithHeader(updateEntranceServiceId, json, header);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public String genUpdateEntrancePara(String entranceName, String entranceType, String entranceId) {

        String json =
                "{" +
                        "    \"entrance_name\":\"" + entranceName + "\"," +
                        "    \"entrance_type\":\"" + entranceType + "\"," +
                        "    \"entrance_id\":\"" + entranceId + "\"" +
                        "}";

        return json;
    }

    public String deleteEntrance(String entranceId) {
        logger.info("\n");
        logger.info("-------------------------delete entrance!---3548--------------------");

        String json = genDeleteEntrancePara(entranceId);

        try {
            response = sendRequestWithHeader(deleteEntranceServiceId, json, header);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public String genDeleteEntrancePara(String entranceId) {

        String json =
                "{" +
                        "    \"entrance_id\":\"" + entranceId + "\"" +
                        "}";

        return json;
    }

    public String getEntrance(String entranceId) {
        logger.info("\n");
        logger.info("-------------------------get entrance!---3549--------------------");

        String json = genGetEntrancePara(entranceId);

        try {
            response = sendRequestWithHeader(getEntranceServiceId, json, header);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public String genGetEntrancePara(String entranceId) {

        String json =
                "{\"entrance_id\":\"" + entranceId + "\"}";

        return json;
    }

    public String listEntrance(String regionId) {
        logger.info("\n");
        logger.info("-------------------------list entrance!---3550--------------------");

        String json = genListEntrancePara(regionId);

        try {
            response = sendRequestWithHeader(listEntranceServiceId, json, header);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public String genListEntrancePara(String regionId) {

        String json =
                "{" +
                        "    \"region_id\":\"" + regionId + "\"," +
                        "    \"order_type\":\"gmt_create\"," +
                        "    \"page\":1," +
                        "    \"size\":10" +
                        "}";

        return json;
    }

    public String addEntranceDevice(String deviceId, String entranceId, String entranceType) {
        logger.info("\n");
        logger.info("-------------------------add entrance device!---3551--------------------");

        String json = genAddEntranceDevicePara(deviceId, entranceId, entranceType);

        try {
            response = sendRequestWithHeader(addEntranceDeviceServiceId, json, header);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public String genAddEntranceDevicePara(String deviceId, String entranceId, String entranceType) {

        String json =
                "{" +
                        "    \"entrance_location\":[" +
                        "        {" +
                        "            \"x\":0.014," +
                        "            \"y\":0.45368891059027777" +
                        "        }," +
                        "        {" +
                        "            \"x\":0.982," +
                        "            \"y\":0.48213335503472227" +
                        "        }" +
                        "    ]," +
                        "    \"entrance_point\":{" +
                        "        \"x\":0.47400000000000003," +
                        "        \"y\":0.6243555772569443" +
                        "    }," +
                        "    \"device_id\":\"" + deviceId + "\"," +
                        "    \"entrance_id\":\"" + entranceId + "\"" +
                        "}";

        return json;
    }

    public String updateEntranceDevice(String deviceId, String entranceId) {
        logger.info("\n");
        logger.info("-------------------------update entrance device!---3552--------------------");

        String json = genUpdateEntranceDevicePara(deviceId, entranceId);

        try {
            response = sendRequestWithHeader(updateEntranceDeviceServiceId, json, header);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public String genUpdateEntranceDevicePara(String deviceId, String entranceId) {

        String json =
                "{" +
                        "    \"entrance_location\":[" +
                        "        {" +
                        "            \"x\":0.014," +
                        "            \"y\":0.45368891059027777" +
                        "        }," +
                        "        {" +
                        "            \"x\":0.982," +
                        "            \"y\":0.48213335503472227" +
                        "        }" +
                        "    ]," +
                        "    \"entrance_point\":{" +
                        "        \"x\":0.474," +
                        "        \"y\":0.6243555772569443" +
                        "    }," +
                        "    \"device_id\":\"" + deviceId + "\"," +
                        "    \"entrance_id\":\"" + entranceId + "\"" +
                        "}";

        return json;
    }

    public String deleteEntranceDevice(String deviceId, String entranceId) {
        logger.info("\n");
        logger.info("-------------------------delete entrance device!---3553--------------------");

        String json = genDeleteEntranceDevicePara(deviceId, entranceId);

        try {
            response = sendRequestWithHeader(deleteEntranceDeviceServiceId, json, header);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public String genDeleteEntranceDevicePara(String deviceId, String entranceId) {

        String json =
                "{" +
                        "    \"entrance_id\":\"" + entranceId + "\"," +
                        "    \"device_id\":\"" + deviceId + "\"" +
                        "}";

        return json;
    }

    public String listEntranceDevice(String entranceId) {
        logger.info("\n");
        logger.info("-------------------------list entrance device!---3554--------------------");

        String json = genListEntranceDevicePara(entranceId);

        try {
            response = sendRequestWithHeader(listEntranceDeviceServiceId, json, header);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public String genListEntranceDevicePara(String entranceId) {

        String json =
                "{\"entrance_id\":\"" + entranceId + "\"}";

        return json;
    }

    public String bindableEntranceDevice(String entranceId) {
        logger.info("\n");
        logger.info("-------------------------bindable entrance device!---3555--------------------");

        String json = genBindableEntranceDevicePara(entranceId);

        try {
            response = sendRequestWithHeader(bindableEntranceDeviceServiceId, json, header);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public String genBindableEntranceDevicePara(String entranceId) {

        String json =
                "{" +
                        "    \"entrance_id\":\"" + entranceId + "\"," +
                        "    \"page\":1," +
                        "    \"size\":10" +
                        "}";

        return json;
    }

//    --------------------------------------------------------区域模块---------------------------------------------------

    public String addRegion(String regionName, String layoutId) {
        logger.info("\n");
        logger.info("-------------------------add region !---3537--------------------");

        String json = genAddRegionPara(regionName, layoutId);

        try {
            response = sendRequestWithHeader(addRegionServiceId, json, header);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public String genAddRegionPara(String regionName, String layoutId) {

        String json =
                "{" +
                        "    \"region_name\":\"" + regionName + "\"," +
                        "    \"layout_id\":\"" + layoutId + "\"" +
                        "}";

        return json;
    }

    public String updateRegion(String regionName, String regionId) {
        logger.info("\n");
        logger.info("-------------------------update region !---3538--------------------");

        String json = genUpdateRegionPara(regionName, regionId);

        try {
            response = sendRequestWithHeader(updateRegionServiceId, json, header);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public String genUpdateRegionPara(String regionName, String regionId) {

        String json =
                "{" +
                        "    \"region_name\":\"" + regionName + "\"," +
                        "    \"region_id\":\"" + regionId + "\"" +
                        "}";

        return json;
    }

    public String deleteRegion(String regionId) {
        logger.info("\n");
        logger.info("-------------------------delete region !---3539--------------------");

        String json = genDeleteRegionPara(regionId);

        try {
            response = sendRequestWithHeader(deleteRegionServiceId, json, header);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public String genDeleteRegionPara(String regionId) {

        String json =
                "{" +
                        "    \"region_id\":\"" + regionId + "\"" +
                        "}";

        return json;
    }

    public String getRegion(String regionId) {
        logger.info("\n");
        logger.info("-------------------------get region !---3540--------------------");

        String json = genGetRegionPara(regionId);

        try {
            response = sendRequestWithHeader(getRegionServiceId, json, header);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public String genGetRegionPara(String regionId) {

        String json =
                "{" +
                        "    \"region_id\":\"" + regionId + "\"" +
                        "}";

        return json;
    }

    public String listRegion(String layouyId) {
        logger.info("\n");
        logger.info("-------------------------list region !---3541--------------------");

        String json = genListRegionPara(layouyId);

        try {
            response = sendRequestWithHeader(listRegionServiceId, json, header);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public String genListRegionPara(String layouyId) {

        String json =
                "{" +
                        "    \"layout_id\":\"" + layouyId + "\"," +
                        "    \"page\":1," +
                        "    \"size\":10" +
                        "}";

        return json;
    }

    public String addRegionDevice(String regionId, String deviceId) {
        logger.info("\n");
        logger.info("-------------------------add region device!---3542--------------------");

        String json = genAddRegionDevicePara(regionId, deviceId);

        try {
            response = sendRequestWithHeader(addRegionDeviceServiceId, json, header);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public String genAddRegionDevicePara(String regionId, String deviceId) {

        String json =
                "{" +
                        "    \"region_id\":\"" + regionId + "\"," +
                        "    \"device_id\":\"" + deviceId + "\"" +
                        "}";

        return json;
    }

    public String deleteRegionDevice(String regionId, String deviceId) {
        logger.info("\n");
        logger.info("-------------------------delete region device!---3543--------------------");

        String json = genDeleteRegionDevicePara(regionId, deviceId);

        try {
            response = sendRequestWithHeader(deleteRegionDeviceServiceId, json, header);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public String genDeleteRegionDevicePara(String regionId, String deviceId) {

        String json =
                "{" +
                        "    \"region_id\":\"" + regionId + "\"," +
                        "    \"device_id\":\"" + deviceId + "\"" +
                        "}";

        return json;
    }

    public String listRegionDevice(String regionId) {
        logger.info("\n");
        logger.info("-------------------------list region device!---3544--------------------");

        String json = genListRegionDevicePara(regionId);

        try {
            response = sendRequestWithHeader(listRegionDeviceServiceId, json, header);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public String genListRegionDevicePara(String regionId) {

        String json =
                "{" +
                        "    \"region_id\":\"" + regionId + "\"," +
                        "    \"page\":1," +
                        "    \"size\":10" +
                        "}";

        return json;
    }

    public String bindableRegionDevice(String regionId) {
        logger.info("\n");
        logger.info("-------------------------bindable region device!---3545--------------------");

        String json = genBindableRegionDevicePara(regionId);

        try {
            response = sendRequestWithHeader(bindableRegionDeviceServiceId, json, header);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public String genBindableRegionDevicePara(String regionId) {

        String json =
                "{" +
                        "    \"region_id\":\"" + regionId + "\"," +
                        "    \"page\":1," +
                        "    \"size\":10" +
                        "}";

        return json;
    }


    //    ------------------------------------------平面模块--------------------------------------------------------------------------
    public String addLayout(String name, String desc, String subjectId) {
        logger.info("\n");
        logger.info("--------------------------------add layout!---3522--------------------");

        String json = genAddLayoutPara(name, desc, subjectId);

        try {
            response = sendRequestWithHeader(addLayoutServiceId, json, header);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public String genAddLayoutPara(String name, String desc, String subjectId) {

        String json =
                "{" +
                        "    \"name\":\"" + name + "\"," +
                        "    \"description\":\"" + desc + "\"," +
                        "    \"subject_id\":\"" + subjectId + "\"" +
                        "}";

        return json;
    }

    public String delLayoutPic(String layoutId) {
        logger.info("\n");
        logger.info("--------------------------------delete layout picture!---3573--------------------");

        String json = genDelLayoutPicPara(layoutId);

        try {
            response = sendRequestWithHeader(delLayoutPicServiceId, json, header);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public String genDelLayoutPicPara(String layoutId) {

        String json =
                "{" +
                        "    \"layout_id\":\"" + layoutId + "\"" +
                        "}";

        return json;
    }

    public String delLayout(String layoutId) {
        logger.info("\n");
        logger.info("--------------------------------delete layout!---3524--------------------");

        String json = genDelLayoutPara(layoutId);

        try {
            response = sendRequestWithHeader(delLayoutServiceId, json, header);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public String genDelLayoutPara(String layoutId) {

        String json =
                "{" +
                        "    \"layout_id\":\"" + layoutId + "\"" +
                        "}";

        return json;
    }

    public String updateLayout(String layoutId, String name, String desc, String layoutPic) {
        logger.info("\n");
        logger.info("--------------------------------update layout!---3523--------------------");

        String json = genUpdateLayoutPara(layoutId, name, desc, layoutPic);

        try {
            response = sendRequestWithHeader(updateLayoutServiceId, json, header);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public String genUpdateLayoutPara(String layoutId, String name, String desc, String layoutPic) {

        String json =
                "{" +
                        "    \"layout_id\":\"" + layoutId + "\"," +
                        "    \"name\":\"" + name + "\"," +
                        "    \"description\":\"" + desc + "\"," +
                        "    \"layout_pic_oss\":\"" + layoutPic + "\"" +
                        "}";

        return json;
    }

    public String getLayout(String layoutId) {
        logger.info("\n");
        logger.info("--------------------------------get layout!---3525--------------------");

        String json = genGetLayoutPara(layoutId);

        try {
            response = sendRequestWithHeader(getLayoutServiceId, json, header);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public String genGetLayoutPara(String layoutId) {

        String json =
                "{" +
                        "    \"layout_id\":\"" + layoutId + "\"" +
                        "}";

        return json;
    }


    public String listLayout() {
        logger.info("\n");
        logger.info("--------------------------------list layout!---3526--------------------");

        String json = genListLayoutPara();

        try {
            response = sendRequestWithHeader(listLayoutServiceId, json, header);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public String genListLayoutPara() {

        String json =
                "{" +
                        "    \"page\":1," +
                        "    \"size\":10" +
                        "}";

        return json;
    }

    public String addLayoutDevice(String layoutId, String deviceId) {
        logger.info("\n");
        logger.info("--------------------------------add layout device!---3527--------------------");

        String json = genAddLayoutDevicePara(layoutId, deviceId);

        try {
            response = sendRequestWithHeader(addLayoutDeviceServiceId, json, header);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public String genAddLayoutDevicePara(String layoutId, String deviceId) {

        String json =
                "{" +
                        "    \"layout_id\":\"" + layoutId + "\"," +
                        "    \"device_id\":\"" + deviceId + "\"" +
                        "}";

        return json;
    }

    public String getBatchAddLayoutDevice(String layoutId, String deviceId1, String deviceId2) {
        logger.info("\n");
        logger.info("--------------------------------batch add layout device!---3528--------------------");

        String json = genGetBatchAddLayoutDevicePara(layoutId, deviceId1, deviceId2);

        try {
            response = sendRequestWithHeader(batchAddLayoutDeviceServiceId, json, header);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public String genGetBatchAddLayoutDevicePara(String layoutId, String deviceId1, String deviceId2) {

        String json =
                "{" +
                        "    \"list\":[" +
                        "        {" +
                        "            \"layout_id\":\"" + layoutId + "\"," +
                        "            \"device_id\":\"" + deviceId1 + "\"" +
                        "        }," +
                        "        {" +
                        "            \"layout_id\":\"" + layoutId + "\"," +
                        "            \"device_id\":\"" + deviceId2 + "\"" +
                        "        }" +
                        "    ]" +
                        "}";

        return json;
    }

    public String delLayoutDevice(String layoutId, String deviceId) {
        logger.info("\n");
        logger.info("--------------------------------delete layout device!---3529--------------------");

        String json = genDelLayoutDevicePara(layoutId, deviceId);

        try {
            response = sendRequestWithHeader(delLayoutDeviceServiceId, json, header);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public String genDelLayoutDevicePara(String layoutId, String deviceId) {

        String json =
                "{" +
                        "    \"layout_id\":\"" + layoutId + "\"," +
                        "    \"device_id\":\"" + deviceId + "\"" +
                        "}";

        return json;
    }

    public String listLayoutDevice(String layoutId) {
        logger.info("\n");
        logger.info("--------------------------------list layout device!---3530--------------------");

        String json = genListLayoutDevicePara(layoutId);

        try {
            response = sendRequestWithHeader(listLayoutDeviceServiceId, json, header);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public String genListLayoutDevicePara(String layoutId) {

        String json =
                "{" +
                        "    \"page\":\"1\"," +
                        "    \"size\":\"10\"," +
                        "    \"layout_id\":\"" + layoutId + "\"" +
                        "}";

        return json;
    }

    public String bindableLayoutDevice(String layoutId) {
        logger.info("\n");
        logger.info("--------------------------------bindable layout device!---3531--------------------");

        String json = genBindableLayoutDevicePara(layoutId);

        try {
            response = sendRequestWithHeader(bindableLayoutDeviceServiceId, json, header);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public String genBindableLayoutDevicePara(String layoutId) {

        String json =
                "{" +
                        "    \"page\":\"1\"," +
                        "    \"size\":\"10\"," +
                        "    \"layout_id\":\"" + layoutId + "\"" +
                        "}";

        return json;
    }

    public String addLayoutMapping(String layoutId, String deviceId) {
        logger.info("\n");
        logger.info("--------------------------------add layout mapping!---3532--------------------");

        String json = genAddLayoutMappingPara(layoutId, deviceId);

        try {
            response = sendRequestWithHeader(addLayoutMappingServiceId, json, header);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public String genAddLayoutMappingPara(String layoutId, String deviceId) {

        String json =
                "{" +
                        "    \"device_id\":\"" + deviceId + "\"," +
                        "    \"layout_id\":\"" + layoutId + "\"," +
                        "    \"device_location\":{" +
                        "        \"x\":0.19394703079743164," +
                        "        \"y\":0.25996646849154625," +
                        "        \"degree\":0" +
                        "    }," +
                        "    \"layout_mapping\":{" +
                        "        \"a\":{" +
                        "            \"x\":0.521815883256448," +
                        "            \"y\":0.23316579847479585" +
                        "        }," +
                        "        \"b\":{" +
                        "            \"x\":0.19722572701876281," +
                        "            \"y\":0.7299832419135064" +
                        "        }," +
                        "        \"c\":{" +
                        "            \"x\":0.21412358037460347," +
                        "            \"y\":0.4743718286255496" +
                        "        }," +
                        "        \"d\":{" +
                        "            \"x\":0.5419924568859356," +
                        "            \"y\":0.7333333256656002" +
                        "        }" +
                        "    }," +
                        "    \"device_mapping\":{" +
                        "        \"a\":{" +
                        "            \"x\":0.8716666751437717," +
                        "            \"y\":0.16888879846643517" +
                        "        }," +
                        "        \"b\":{" +
                        "            \"x\":0.0894444359673394," +
                        "            \"y\":0.16888879846643517" +
                        "        }," +
                        "        \"c\":{" +
                        "            \"x\":0.2283333248562283," +
                        "            \"y\":0.657777687355324" +
                        "        }," +
                        "        \"d\":{" +
                        "            \"x\":0.780000008477105," +
                        "            \"y\":0.9076542983820408" +
                        "        }" +
                        "    }," +
                        "    \"matrix\":[" +
                        "        -0.793551329530975," +
                        "        0.1834952743744053," +
                        "        0.2081247862560849," +
                        "        -1.0541152146420856," +
                        "        -0.30446713216991056," +
                        "        0.7680217627903003," +
                        "        -2.1986813025209684," +
                        "        0.2911201444834581," +
                        "        0.9999999999999999" +
                        "    ]" +
                        "}";

        return json;
    }

    public String updateLayoutMapping(String layoutId, String deviceId) {
        logger.info("\n");
        logger.info("--------------------------------update layout mapping!---3533--------------------");

        String json = genUpdateLayoutMappingPara(layoutId, deviceId);

        try {
            response = sendRequestWithHeader(updateLayoutMappingServiceId, json, header);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public String genUpdateLayoutMappingPara(String layoutId, String deviceId) {

        String json =
                "{" +
                        "    \"device_id\":\"" + deviceId + "\"," +
                        "    \"layout_id\":\"" + layoutId + "\"," +
                        "    \"device_location\":{" +
                        "        \"x\":0.2509457755359395," +
                        "        \"y\":0.22445561139028475," +
                        "        \"degree\":0" +
                        "    }," +
                        "    \"layout_mapping\":{" +
                        "        \"a\":{" +
                        "            \"x\":0.521815883256448," +
                        "            \"y\":0.23316579847479585" +
                        "        }," +
                        "        \"b\":{" +
                        "            \"x\":0.1972257270187628," +
                        "            \"y\":0.7299832419135064" +
                        "        }," +
                        "        \"c\":{" +
                        "            \"x\":0.21412358037460347," +
                        "            \"y\":0.4743718286255496" +
                        "        }," +
                        "        \"d\":{" +
                        "            \"x\":0.5419924568859356," +
                        "            \"y\":0.7333333256656002" +
                        "        }" +
                        "    }," +
                        "    \"device_mapping\":{" +
                        "        \"a\":{" +
                        "            \"x\":0.8716666751437717," +
                        "            \"y\":0.16888879846643515" +
                        "        }," +
                        "        \"b\":{" +
                        "            \"x\":0.0894444359673394," +
                        "            \"y\":0.16888879846643515" +
                        "        }," +
                        "        \"c\":{" +
                        "            \"x\":0.2283333248562283," +
                        "            \"y\":0.657777687355324" +
                        "        }," +
                        "        \"d\":{" +
                        "            \"x\":0.780000008477105," +
                        "            \"y\":0.9076542983820408" +
                        "        }" +
                        "    }," +
                        "    \"matrix\":[" +
                        "        -0.793551329530975," +
                        "        0.1834952743744053," +
                        "        0.2081247862560849," +
                        "        -1.0541152146420856," +
                        "        -0.30446713216991056," +
                        "        0.7680217627903003," +
                        "        -2.1986813025209684," +
                        "        0.2911201444834581," +
                        "        0.9999999999999999" +
                        "    ]" +
                        "}";

        return json;
    }

    public String getLayoutMapping(String layoutId, String deviceId) {
        logger.info("\n");
        logger.info("--------------------------------get layout mapping!---3534--------------------");

        String json = genGetLayoutMappingPara(layoutId, deviceId);

        try {
            response = sendRequestWithHeader(getLayoutMappingServiceId, json, header);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public String genGetLayoutMappingPara(String layoutId, String deviceId) {

        String json =
                "{" +
                        "    \"device_id\":\"" + deviceId + "\"," +
                        "    \"layout_id\":\"" + layoutId + "\"" +
                        "}";

        return json;
    }

    public String delLayoutMapping(String layoutId, String deviceId) {
        logger.info("\n");
        logger.info("--------------------------------delete layout mapping!---3535--------------------");

        String json = genDelLayoutMappingPara(layoutId, deviceId);

        try {
            response = sendRequestWithHeader(delLayoutMappingServiceId, json, header);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public String genDelLayoutMappingPara(String layoutId, String deviceId) {

        String json =
                "{" +
                        "    \"device_id\":\"" + deviceId + "\"," +
                        "    \"layout_id\":\"" + layoutId + "\"" +
                        "}";

        return json;
    }

    public String analysisMatrix(String layoutId, String deviceId) {
        logger.info("\n");
        logger.info("--------------------------------analysis layout matrix!---3536--------------------");

        String json = genAnalysisMatrixPara(layoutId, deviceId);

        try {
            response = sendRequestWithHeader(analysisMatrixServiceId, json, header);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public String genAnalysisMatrixPara(String layoutId, String deviceId) {

        String json =
                "{" +
                        "    \"device_id\":\"" + deviceId + "\"," +
                        "    \"layout_id\":\"" + layoutId + "\"," +
                        "    \"device_location\":{" +
                        "        \"x\":0.2509457755359395," +
                        "        \"y\":0.22445561139028475," +
                        "        \"degree\":0" +
                        "    }," +
                        "    \"layout_mapping\":{" +
                        "        \"a\":{" +
                        "            \"x\":0.521815883256448," +
                        "            \"y\":0.23316579847479585" +
                        "        }," +
                        "        \"b\":{" +
                        "            \"x\":0.1972257270187628," +
                        "            \"y\":0.7299832419135064" +
                        "        }," +
                        "        \"c\":{" +
                        "            \"x\":0.21412358037460347," +
                        "            \"y\":0.4743718286255496" +
                        "        }," +
                        "        \"d\":{" +
                        "            \"x\":0.5419924568859356," +
                        "            \"y\":0.7333333256656002" +
                        "        }" +
                        "    }," +
                        "    \"device_mapping\":{" +
                        "        \"a\":{" +
                        "            \"x\":0.8716666751437717," +
                        "            \"y\":0.16888879846643515" +
                        "        }," +
                        "        \"b\":{" +
                        "            \"x\":0.0894444359673394," +
                        "            \"y\":0.16888879846643515" +
                        "        }," +
                        "        \"c\":{" +
                        "            \"x\":0.2283333248562283," +
                        "            \"y\":0.657777687355324" +
                        "        }," +
                        "        \"d\":{" +
                        "            \"x\":0.780000008477105," +
                        "            \"y\":0.9076542983820408" +
                        "        }" +
                        "    }" +
                        "}";

        return json;
    }

//    -----------------------------------------------主体模块----------------------------------------------------------------

    public String addSubject(String subjectType, String subjectName, String local, String manager, String phone) {
        logger.info("\n");
        logger.info("--------------------------------add subject!---3514------------------------------------");

        String json = genAddSubjectPara(subjectType, subjectName, local, manager, phone);

        try {
            response = sendRequestWithHeader(addSubjectServiceId, json, header);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }


    public String genAddSubjectPara(String subjectType, String subjectName, String local, String manager, String phone) {

        String json =
                "{\n" +
                        "    \"subject_type\":" + subjectType + ",\n" +
                        "    \"subject_name\":\"" + subjectName + "\",\n" +
                        "    \"region\":{\n" +
                        "        \"country\":\"中国\",\n" +
                        "        \"area\":\"华北区\",\n" +
                        "        \"province\":\"北京市\",\n" +
                        "        \"city\":\"北京市\",\n" +
                        "        \"district\":\"" + "海淀区" + "\"\n" +
                        "    },\n" +
                        "    \"local\":\"" + local + "\",\n" +
                        "    \"manager\":\"" + manager + "\",\n" +
                        "    \"telephone\":\"" + phone + "\",\n" +
                        "    \"brand_id\":\"" + BRAND_ID + "\"\n" +
                        "}";
        return json;
    }

    public String updateSubject(String subjectId, String subjectName, String local, String manager, String phone) {
        logger.info("\n");
        logger.info("--------------------------------update subject!---3515------------------------------------");

        String json = genUpdateSubjectPara(subjectId, subjectName, local, manager, phone);

        try {
            response = sendRequestWithHeader(updateSubjectServiceId, json, header);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public String genUpdateSubjectPara(String subjectId, String subjectName, String local, String manager, String phone) {

        String json =
                "{\n" +
                        "    \"subject_name\":\"" + subjectName + "\",\n" +
                        "    \"region\":{\n" +
                        "        \"country\":\"中国\",\n" +
                        "        \"area\":\"华北区\",\n" +
                        "        \"province\":\"北京市\",\n" +
                        "        \"city\":\"北京市\",\n" +
                        "        \"district\":\"" + "海淀区" + "\"\n" +
                        "    },\n" +
                        "    \"local\":\"" + local + "\",\n" +
                        "    \"manager\":\"" + manager + "\",\n" +
                        "    \"telephone\":\"" + phone + "\",\n" +
                        "    \"subject_id\":\"" + subjectId + "\"\n" +
                        "}";
        return json;
    }

    public String deleteSubject(String subjectId) {
        logger.info("\n");
        logger.info("--------------------------------delete subject!---3516------------------------------------");

        String json = genDeleteSubjectPara(subjectId);

        try {
            response = sendRequestWithHeader(delSubjectServiceId, json, header);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public String genDeleteSubjectPara(String subjectId) {

        String json = "{\"subject_id\":\"" + subjectId + "\"}";

        return json;
    }

    public String getSubject(String subjectId) {
        logger.info("\n");
        logger.info("--------------------------------get subject!---3517------------------------------------");

        String json = genGetSubjectPara(subjectId);

        try {
            response = sendRequestWithHeader(getSubjectServiceId, json, header);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public String genGetSubjectPara(String subjectId) {

        String json = "{\"subject_id\":\"" + subjectId + "\"}";

        return json;
    }

    public String listSubject(String brandId) {
        logger.info("\n");
        logger.info("--------------------------------get subject!---3518------------------------------------");

        String json = genListSubjectPara(brandId);

        try {
            response = sendRequestWithHeader(listSubjectServiceId, json, header);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public String genListSubjectPara(String brandId) {

        String json =
                "{\n" +
                        "    \"brand_id\":\"" + brandId + "\",\n" +
                        "    \"page\":1,\n" +
                        "    \"size\":10\n" +
                        "}";

        return json;
    }


    //    -----------------------------------------------------------品牌模块--------------------------------------------------------
    public String addBrand(String brandName, String manager, String phone, String appId) {
        logger.info("\n");
        logger.info("--------------------------------get brand!---3518------------------------------------");

        String json = genAddBrandPara(brandName, manager, phone, appId);

        try {
            response = sendRequestWithHeader(addBrandServiceId, json, header);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public String genAddBrandPara(String brandName, String manager, String phone, String appId) {

        String json =
                "{\n" +
                        "    \"brand_name\":\"" + brandName + "\",\n" +
                        "    \"manager\":\"" + manager + "\",\n" +
                        "    \"telephone\":\"" + phone + "\",\n" +
                        "    \"app_id\":\"" + appId + "\"\n" +
                        "}";

        return json;
    }

    public String updateBrand(String brandId, String brandName, String manager, String phone) {
        logger.info("\n");
        logger.info("--------------------------------update brand!---3518------------------------------------");

        String json = genUpdateBrandPara(brandId, brandName, manager, phone);

        try {
            response = sendRequestWithHeader(updateBrandServiceId, json, header);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public String genUpdateBrandPara(String brandId, String brandName, String manager, String phone) {

        String json =
                "{\n" +
                        "    \"brand_id\":\"" + brandId + "\",\n" +
                        "    \"brand_name\":\"" + brandName + "\",\n" +
                        "    \"manager\":\"" + manager + "\",\n" +
                        "    \"telephone\":\"" + phone + "\"\n" +
                        "}";

        return json;
    }

    public String deleteBrand(String brandId) {
        logger.info("\n");
        logger.info("--------------------------------delete brand!---3518------------------------------------");

        String json = genDeleteBrandPara(brandId);

        try {
            response = sendRequestWithHeader(delBrandServiceId, json, header);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public String genDeleteBrandPara(String brandId) {

        String json =
                "{\n" +
                        "    \"brand_id\":\"" + brandId + "\"\n" +
                        "}";

        return json;
    }

    public String getBrand(String brandId) {
        logger.info("\n");
        logger.info("--------------------------------get brand!---3518------------------------------------");

        String json = genGetBrandPara(brandId);

        try {
            response = sendRequestWithHeader(getBrandServiceId, json, header);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public String genGetBrandPara(String brandId) {

        String json =
                "{\n" +
                        "    \"brand_id\":\"" + brandId + "\"\n" +
                        "}";

        return json;
    }


    public String listbrand(String appId) {
        logger.info("\n");
        logger.info("--------------------------------list brand!---3518------------------------------------");

        String json = genListbrandPara(appId);

        try {
            response = sendRequestWithHeader(listBrandServiceId, json, header);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public String genListbrandPara(String appId) {

        String json =
                "{\n" +
                        "    \"app_id\":\"" + appId + "\",\n" +
                        "    \"page\":1,\n" +
                        "    \"size\":10\n" +
                        "}";

        return json;
    }


//    -------------------------------------------------应用模块--------------------------------------------------------

    public String addApp(String name) {
        logger.info("\n");
        logger.info("--------------------------------add app!---3518------------------------------------");

        String json = genAddAppPara(name);

        try {
            response = sendRequestWithHeader(addAppServiceId, json, header);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public String genAddAppPara(String name) {

        String json =
                "{\n" +
                        "    \"name\":\"" + name + "\"\n" +
                        "}";

        return json;
    }

    public String updateApp(String appId, String name) {
        logger.info("\n");
        logger.info("--------------------------------add app!---3518------------------------------------");

        String json = genUpdateAppPara(appId, name);

        try {
            response = sendRequestWithHeader(updateAppServiceId, json, header);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public String genUpdateAppPara(String appId, String name) {

        String json =
                "{\n" +
                        "    \"app_id\":\"" + appId + "\",\n" +
                        "    \"name\":\"" + name + "\"\n" +
                        "}";

        return json;
    }

    public String deleteApp(String appId) {
        logger.info("\n");
        logger.info("--------------------------------delete app!---3518------------------------------------");

        String json = genDeleteAppPara(appId);

        try {
            response = sendRequestWithHeader(delAppServiceId, json, header);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public String genDeleteAppPara(String appId) {

        String json =
                "{\n" +
                        "    \"app_id\":\"" + appId + "\"\n" +
                        "}";

        return json;
    }

    public String getApp(String appId) {
        logger.info("\n");
        logger.info("--------------------------------delete app!---3518------------------------------------");

        String json = genGetAppPara(appId);

        try {
            response = sendRequestWithHeader(getAppServiceId, json, header);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public String genGetAppPara(String appId) {

        String json =
                "{\n" +
                        "    \"app_id\":\"" + appId + "\"\n" +
                        "}";

        return json;
    }

    public String listApp() {
        logger.info("\n");
        logger.info("--------------------------------list app!---3518------------------------------------");

        String json = genListAppPara();

        try {
            response = sendRequestWithHeader(listAppServiceId, json, header);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public String genListAppPara() {

        String json =
                "{\n" +
                        "    \"page\":1,\n" +
                        "    \"size\":10\n" +
                        "}";

        return json;
    }

    //----------------------------------------------------------------设备管理模块----------------------------------------------------------------------
//    -----------------------------------------------------1、验证启动、停止设备是否成功--------------------------------------------
//    ---------------------------------1、增加设备-2、设备列表-3、设备启用-4、设备列表-5、设备停止-6、设备列表------------------------------
    @Test
    public void checkStartStop() {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        String deviceName = caseName;

        String caseDesc = "1、验证启动、停止设备是否成功";
        logger.info(caseDesc + "-----------------------------------------------------------------------------------");

        failReason = "";
        Case aCase = new Case();

        JSONObject addDeviceJo;
        JSONObject listDeviceJo;
        JSONObject startDeviceJo;
        JSONObject stopDeviceJo;


        JSONObject addDeviceResJo;
        JSONObject listDeviceResJo1;
        JSONObject startDeviceResJo;
        JSONObject listDeviceResJo2;
        JSONObject stopDeviceResJo;
        JSONObject listDeviceResJo3;

        String addDeviceRes = "";
        String listDeviceRes1 = "";
        String startDeviceRes = "";
        String listDeviceRes2 = "";
        String stopDeviceRes = "";
        String listDeviceRes3 = "";

        String deviceStatus, deviceId = "";
        String deviceType = deviceTypeFaceCamera;
        try {

//            1、增加设备
            addDeviceRes = addDevice(deviceName, deviceType, DeviceUrl);
            checkCode(addDeviceRes, StatusCode.SUCCESS, "新增设备失败！");

//        2、设备列表
            listDeviceRes1 = listDevice();
            checkCode(listDeviceRes1, StatusCode.SUCCESS, "查询设备列表失败！");
//        获取deviceId
            deviceId = getDeviceIdByListDevice(listDeviceRes1, deviceName);

//        3、启动设备
            startDeviceRes = startDevice(deviceId);
            checkCode(startDeviceRes, StatusCode.SUCCESS, "启动设备失败");

            Thread.sleep(120000);
//        4、查询设备列表
            listDeviceRes2 = listDevice();
            deviceStatus = getStatusByListDevice(listDeviceRes2, deviceId);
            Assert.assertEquals(deviceStatus, "RUNNING", "start failed！");
//            DEPLOYMENT_ING

//        5、停止设备
            stopDeviceRes = stopDevice(deviceId);
            checkCode(stopDeviceRes, StatusCode.SUCCESS, "stop failed！");

//        6、查询设备列表
            listDeviceRes3 = listDevice();
            deviceStatus = getStatusByListDevice(listDeviceRes3, deviceId);

            Assert.assertEquals(deviceStatus, "UN_DEPLOYMENT", "查询设备列表失败！");

            aCase.setResult("PASS");
        } catch (Exception e) {
            Assert.assertTrue(false);
        } finally {
            deleteDevice(deviceId);

            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

//            组织入参
            addDeviceJo = JSON.parseObject(genAddDevicePara(deviceName, deviceType, DeviceUrl));
            listDeviceJo = JSON.parseObject(genListDevicePara());
            startDeviceJo = JSON.parseObject(genStartDevicePara(deviceId));
            stopDeviceJo = JSON.parseObject(genStopDevicePara(deviceId));

            aCase.setRequestData(addDeviceJo + "\n\n" +
                    listDeviceJo + "\n\n" +
                    startDeviceJo + "\n\n" +
                    stopDeviceJo);

//            组织response
            addDeviceResJo = JSON.parseObject(addDeviceRes);
            listDeviceResJo1 = JSON.parseObject(listDeviceRes1);
            startDeviceResJo = JSON.parseObject(startDeviceRes);
            listDeviceResJo2 = JSON.parseObject(listDeviceRes2);
            stopDeviceResJo = JSON.parseObject(stopDeviceRes);
            listDeviceResJo3 = JSON.parseObject(listDeviceRes3);

            aCase.setResponse(addDeviceResJo + "\n\n" +
                    listDeviceResJo1 + "\n\n" +
                    startDeviceResJo + "\n\n" +
                    listDeviceResJo2 + "\n\n" +
                    stopDeviceResJo + "\n\n" +
                    listDeviceResJo3);

            qaDbUtil.saveToCaseTable(aCase);

        }
    }

    //    ----------------------------------------------------2、验证批量启动、停止设备是否成功-------------------------------
//    ---------------------------------1、增加设备（三个）-2、设备列表-3、批量启动-4、设备列表-5、批量停止-6、设备列表---------------
    @Test
    public void checkBatchStartStop() {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        String caseDesc = "验证批量启动、停止设备是否成功";
        logger.info(caseDesc + "-----------------------------------------------------------------------------------");

        failReason = "";
        Case aCase = new Case();

        String deviceName_1 = caseName + "-1";
        String deviceName_2 = caseName + "-2";
        String deviceName_3 = caseName + "-3";

        JSONObject addDeviceJo1;
        JSONObject addDeviceJo2;
        JSONObject addDeviceJo3;
        JSONObject listDeviceJo;
        JSONObject batchStartDeviceJo;
        JSONObject batchStopDeviceJo;


        JSONObject addDeviceResJo1;
        JSONObject addDeviceResJo2;
        JSONObject addDeviceResJo3;
        JSONObject listDeviceResJo1;
        JSONObject batchStartDeviceResJo;
        JSONObject listDeviceResJo2;
        JSONObject batchStopDeviceResJo;
        JSONObject listDeviceResJo3;

        String addDeviceRes1 = "";
        String addDeviceRes2 = "";
        String addDeviceRes3 = "";
        String listDeviceRes1 = "";
        String batchStartDeviceRes = "";
        String listDeviceRes2 = "";
        String batchStopDeviceRes = "";
        String listDeviceRes3 = "";

        String deviceStatus_1, deviceStatus_2, deviceStatus_3;
        String deviceId_1 = "", deviceId_2 = "", deviceId_3 = "";
        String deviceType = deviceTypeFaceCamera;
        String deviceIdArr = "";
        try {

//            1、增加设备（三个）
            addDeviceRes1 = addDevice(deviceName_1, deviceType, DeviceUrl);
            checkCode(addDeviceRes1, StatusCode.SUCCESS, "新增设备失败！");
            addDeviceRes2 = addDevice(deviceName_2, deviceType, DeviceUrl);
            checkCode(addDeviceRes2, StatusCode.SUCCESS, "新增设备失败！");
            addDeviceRes3 = addDevice(deviceName_3, deviceType, DeviceUrl);
            checkCode(addDeviceRes3, StatusCode.SUCCESS, "新增设备失败！");

//        2、设备列表
            listDeviceRes1 = listDevice();
            checkCode(listDeviceRes1, StatusCode.SUCCESS, "查询设备列表失败！");

//        获取deviceId
            deviceId_1 = getDeviceIdByListDevice(listDeviceRes1, deviceName_1);
            deviceId_2 = getDeviceIdByListDevice(listDeviceRes1, deviceName_2);
            deviceId_3 = getDeviceIdByListDevice(listDeviceRes1, deviceName_3);

//            3、批量启动
            deviceIdArr = "\"" + deviceId_1 + "\"," + "\"" + deviceId_2 + "\"," + "\"" + deviceId_3 + "\"";
            batchStartDeviceRes = batchStartDevice(deviceIdArr);
            checkCode(batchStartDeviceRes, StatusCode.SUCCESS, "批量启动设备失败");

            Thread.sleep(120000);
//        4、查询设备列表
            listDeviceRes2 = listDevice();
            deviceStatus_1 = getStatusByListDevice(listDeviceRes2, deviceId_1);
            Assert.assertEquals(deviceStatus_1, "RUNNING", "start failed！");
            deviceStatus_2 = getStatusByListDevice(listDeviceRes2, deviceId_2);
            Assert.assertEquals(deviceStatus_2, "RUNNING", "start failed！");
            deviceStatus_3 = getStatusByListDevice(listDeviceRes2, deviceId_3);
            Assert.assertEquals(deviceStatus_3, "RUNNING", "start failed！");

//        5、批量停止设备
            deviceIdArr = "\"" + deviceId_1 + "\"," + "\"" + deviceId_2 + "\"";
            batchStopDeviceRes = batchStopDevice(deviceIdArr);
            checkCode(batchStopDeviceRes, StatusCode.SUCCESS, "stop failed！");

//        6、查询设备列表
            listDeviceRes3 = listDevice();
            deviceStatus_1 = getStatusByListDevice(listDeviceRes3, deviceId_1);
            Assert.assertEquals(deviceStatus_1, "UN_DEPLOYMENT", "查询设备列表失败！");
            deviceStatus_2 = getStatusByListDevice(listDeviceRes3, deviceId_2);
            Assert.assertEquals(deviceStatus_2, "UN_DEPLOYMENT", "查询设备列表失败！");
            deviceStatus_3 = getStatusByListDevice(listDeviceRes3, deviceId_3);
            Assert.assertEquals(deviceStatus_3, "RUNNING", "查询设备列表失败！");

            aCase.setResult("PASS");
        } catch (Exception e) {
            Assert.assertTrue(false);
        } finally {
            deleteDevice(deviceId_1);
            deleteDevice(deviceId_2);
            deleteDevice(deviceId_3);

            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

//            组织入参
            addDeviceJo1 = JSON.parseObject(genAddDevicePara(deviceName_1, deviceType, DeviceUrl));
            addDeviceJo2 = JSON.parseObject(genAddDevicePara(deviceName_2, deviceType, DeviceUrl));
            addDeviceJo3 = JSON.parseObject(genAddDevicePara(deviceName_3, deviceType, DeviceUrl));
            listDeviceJo = JSON.parseObject(genListDevicePara());
            batchStartDeviceJo = JSON.parseObject(genBatchStartDevicePara(deviceIdArr));
            batchStopDeviceJo = JSON.parseObject(genBatchStopDevicePara(deviceIdArr));

            aCase.setRequestData(addDeviceJo1 + "\n\n" +
                    addDeviceJo2 + "\n\n" +
                    addDeviceJo3 + "\n\n" +
                    listDeviceJo + "\n\n" +
                    batchStartDeviceJo + "\n\n" +
                    batchStopDeviceJo);

//            组织response
            addDeviceResJo1 = JSON.parseObject(addDeviceRes1);
            addDeviceResJo2 = JSON.parseObject(addDeviceRes2);
            addDeviceResJo3 = JSON.parseObject(addDeviceRes3);
            listDeviceResJo1 = JSON.parseObject(listDeviceRes1);
            batchStartDeviceResJo = JSON.parseObject(batchStartDeviceRes);
            listDeviceResJo2 = JSON.parseObject(listDeviceRes2);
            batchStopDeviceResJo = JSON.parseObject(batchStopDeviceRes);
            listDeviceResJo3 = JSON.parseObject(listDeviceRes3);

            aCase.setResponse(addDeviceResJo1 + "\n\n" +
                    addDeviceResJo2 + "\n\n" +
                    addDeviceResJo3 + "\n\n" +
                    listDeviceResJo1 + "\n\n" +
                    batchStartDeviceResJo + "\n\n" +
                    listDeviceResJo2 + "\n\n" +
                    batchStopDeviceResJo + "\n\n" +
                    listDeviceResJo3);

            qaDbUtil.saveToCaseTable(aCase);
        }
    }

    //    ----------------------------------------------------3、验证批量删除设备是否成功-------------------------------
//    ------------------1、增加设备（三个）-2、设备列表-3、批量删除设备-4、设备列表---------------------------------
    @Test
    public void checkBatchRemoveDevice() {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;

        String caseDesc = "3、验证批量删除设备是否成功";
        logger.info(caseDesc + "-----------------------------------------------------------------------------------");

        failReason = "";
        Case aCase = new Case();

        JSONObject addDeviceJo1;
        JSONObject addDeviceJo2;
        JSONObject addDeviceJo3;
        JSONObject listDeviceJo;
        JSONObject batchRemoveDeviceJo;

        JSONObject addDeviceResJo1;
        JSONObject addDeviceResJo2;
        JSONObject addDeviceResJo3;
        JSONObject listDeviceResJo1;
        JSONObject batchRemoveDeviceResJo;
        JSONObject listDeviceResJo2;

        String addDeviceRes1 = "";
        String addDeviceRes2 = "";
        String addDeviceRes3 = "";
        String listDeviceRes1 = "";
        String batchRemoveDeviceRes = "";
        String listDeviceRes2 = "";

        String deviceName_1 = caseName + "-1";
        String deviceName_2 = caseName + "-2";
        String deviceName_3 = caseName + "-3";

        String deviceArr = "";
        String deviceId_1 = "", deviceId_2 = "", deviceId_3 = "";
        String deviceType = deviceTypeFaceCamera;
        try {
//            1、增加设备（三个）
            addDeviceRes1 = addDevice(deviceName_1, deviceType, DeviceUrl);
            checkCode(addDeviceRes1, StatusCode.SUCCESS, "新增设备失败！");
            addDeviceRes2 = addDevice(deviceName_2, deviceType, DeviceUrl);
            checkCode(addDeviceRes2, StatusCode.SUCCESS, "新增设备失败！");
            addDeviceRes3 = addDevice(deviceName_3, deviceType, DeviceUrl);
            checkCode(addDeviceRes3, StatusCode.SUCCESS, "新增设备失败！");

//        2、设备列表
            listDeviceRes1 = listDevice();
            checkCode(listDeviceRes1, StatusCode.SUCCESS, "查询设备列表失败！");
//        获取deviceId
            deviceId_1 = getDeviceIdByListDevice(listDeviceRes1, deviceName_1);
            deviceId_2 = getDeviceIdByListDevice(listDeviceRes1, deviceName_2);
            deviceId_3 = getDeviceIdByListDevice(listDeviceRes1, deviceName_3);

//        3、批量删除设备
            deviceArr = "\"" + deviceId_1 + "\"," + "\"" + deviceId_2 + "\"";
            batchRemoveDeviceRes = batchRemoveDevice(deviceArr);
            checkCode(batchRemoveDeviceRes, StatusCode.SUCCESS, "stop failed！");

//        4、查询设备列表
            listDeviceRes2 = listDevice();
            Assert.assertEquals(checkBatchRemoveByListDevice(listDeviceRes2, deviceId_1), false, "批量删除设备失败！");
            Assert.assertEquals(checkBatchRemoveByListDevice(listDeviceRes2, deviceId_2), false, "批量删除设备失败！");
            Assert.assertEquals(checkBatchRemoveByListDevice(listDeviceRes2, deviceId_3), true, "批量删除设备失败！");

            aCase.setResult("PASS");
        } catch (Exception e) {
            Assert.assertTrue(false);
        } finally {
            deleteDevice(deviceId_1);
            deleteDevice(deviceId_2);
            deleteDevice(deviceId_3);

            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

//            组织参数
            addDeviceJo1 = JSON.parseObject(genAddDevicePara(deviceName_1, deviceType, DeviceUrl));
            addDeviceJo2 = JSON.parseObject(genAddDevicePara(deviceName_2, deviceType, DeviceUrl));
            addDeviceJo3 = JSON.parseObject(genAddDevicePara(deviceName_3, deviceType, DeviceUrl));
            listDeviceJo = JSON.parseObject(genListDevicePara());
            batchRemoveDeviceJo = JSON.parseObject(genBatchRemoveDevicePara(deviceArr));

            aCase.setRequestData(addDeviceJo1 + "\n\n" +
                    addDeviceJo2 + "\n\n" +
                    addDeviceJo3 + "\n\n" +
                    listDeviceJo + "\n\n" +
                    batchRemoveDeviceJo);

//            组织response
            addDeviceResJo1 = JSON.parseObject(addDeviceRes1);
            addDeviceResJo2 = JSON.parseObject(addDeviceRes2);
            addDeviceResJo3 = JSON.parseObject(addDeviceRes3);
            listDeviceResJo1 = JSON.parseObject(listDeviceRes1);
            batchRemoveDeviceResJo = JSON.parseObject(batchRemoveDeviceRes);
            listDeviceResJo2 = JSON.parseObject(listDeviceRes2);

            aCase.setResponse(addDeviceResJo1 + "\n\n" +
                    addDeviceResJo2 + "\n\n" +
                    addDeviceResJo3 + "\n\n" +
                    listDeviceResJo1 + "\n\n" +
                    batchRemoveDeviceResJo + "\n\n" +
                    listDeviceResJo2);

            qaDbUtil.saveToCaseTable(aCase);
        }
    }

    //    ----------------------------------------------------4、验证批量设置告警是否成功-------------------------------
//    ---------------------------------------1、增加设备（三个）-2、设备列表-3、批量告警-4、设备详情----------------------------
    @Test
    public void checkBatchMonitorDevice() {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;

        String caseDesc = "1、验证启动、停止设备是否成功";
        logger.info(caseDesc + "-----------------------------------------------------------------------------------");

        failReason = "";
        Case aCase = new Case();

        JSONObject addDeviceJo1;
        JSONObject addDeviceJo2;
        JSONObject addDeviceJo3;
        JSONObject listDeviceJo;
        JSONObject batchMonitorDeviceJo;
        JSONObject getDeviceJo1;
        JSONObject getDeviceJo2;
        JSONObject getDeviceJo3;


        JSONObject addDeviceResJo1;
        JSONObject addDeviceResJo2;
        JSONObject addDeviceResJo3;
        JSONObject listDeviceResJo;
        JSONObject batchMonitorDeviceResJo;
        JSONObject getDeviceResJo1;
        JSONObject getDeviceResJo2;
        JSONObject getDeviceResJo3;

        String addDeviceRes1 = "";
        String addDeviceRes2 = "";
        String addDeviceRes3 = "";
        String listDeviceRes = "";
        String batchMonitorDeviceRes = "";
        String getDeviceRes1 = "";
        String getDeviceRes2 = "";
        String getDeviceRes3 = "";

        String deviceName_1 = caseName + "-1";
        String deviceName_2 = caseName + "-2";
        String deviceName_3 = caseName + "-3";

        String deviceArr = "";
        String deviceId_1 = "", deviceId_2 = "", deviceId_3 = "";
        String deviceType = deviceTypeFaceCamera;
        try {
//            1、增加设备（三个）
            addDeviceRes1 = addDevice(deviceName_1, deviceType, DeviceUrl);
            checkCode(addDeviceRes1, StatusCode.SUCCESS, "新增设备失败！");
            addDeviceRes2 = addDevice(deviceName_2, deviceType, DeviceUrl);
            checkCode(addDeviceRes2, StatusCode.SUCCESS, "新增设备失败！");
            addDeviceRes3 = addDevice(deviceName_3, deviceType, DeviceUrl);
            checkCode(addDeviceRes3, StatusCode.SUCCESS, "新增设备失败！");

//            2、设备列表
            listDeviceRes = listDevice();
            checkCode(listDeviceRes, StatusCode.SUCCESS, "查询设备列表失败！");

//        获取deviceId
            deviceId_1 = getDeviceIdByListDevice(response, deviceName_1);
            deviceId_2 = getDeviceIdByListDevice(response, deviceName_2);
            deviceId_3 = getDeviceIdByListDevice(response, deviceName_3);

//        3、批量设置告警
            deviceArr = "\"" + deviceId_1 + "\",\"" + deviceId_2 + "\",\"" + deviceId_3 + "\"";
            batchMonitorDeviceRes = batchMonitorDevice(deviceArr);
            checkCode(batchMonitorDeviceRes, StatusCode.SUCCESS, "monitor failed！");

//        4、设备详情
            getDeviceRes1 = getDevice(deviceId_1);
            checkCode(listDeviceRes, StatusCode.SUCCESS, "");
            checkBatchMonitorByGetDevice(getDeviceRes1);

            getDeviceRes2 = getDevice(deviceId_2);
            checkCode(listDeviceRes, StatusCode.SUCCESS, "");
            checkBatchMonitorByGetDevice(getDeviceRes2);

            getDeviceRes3 = getDevice(deviceId_3);
            checkCode(listDeviceRes, StatusCode.SUCCESS, "");
            checkBatchMonitorByGetDevice(getDeviceRes3);

            aCase.setResult("PASS");

        } catch (Exception e) {
            Assert.assertTrue(false);
        } finally {
            deleteDevice(deviceId_1);
            deleteDevice(deviceId_2);
            deleteDevice(deviceId_3);

            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);
//            组织参数
            addDeviceJo1 = JSON.parseObject(genAddDevicePara(deviceName_1, deviceType, DeviceUrl));
            addDeviceJo2 = JSON.parseObject(genAddDevicePara(deviceName_2, deviceType, DeviceUrl));
            addDeviceJo3 = JSON.parseObject(genAddDevicePara(deviceName_3, deviceType, DeviceUrl));
            listDeviceJo = JSON.parseObject(genListDevicePara());
            batchMonitorDeviceJo = JSON.parseObject(genBatchRemoveDevicePara(deviceArr));
            getDeviceJo1 = JSON.parseObject(genGetDevicePara(deviceId_1));
            getDeviceJo2 = JSON.parseObject(genGetDevicePara(deviceId_2));
            getDeviceJo3 = JSON.parseObject(genGetDevicePara(deviceId_3));

            aCase.setRequestData(addDeviceJo1 + "\n\n" +
                    addDeviceJo2 + "\n\n" +
                    addDeviceJo3 + "\n\n" +
                    listDeviceJo + "\n\n" +
                    batchMonitorDeviceJo + "\n\n" +
                    getDeviceJo1 + "\n\n" +
                    getDeviceJo2 + "\n\n" +
                    getDeviceJo3
            );

//            组织response
            addDeviceResJo1 = JSON.parseObject(addDeviceRes1);
            addDeviceResJo2 = JSON.parseObject(addDeviceRes2);
            addDeviceResJo3 = JSON.parseObject(addDeviceRes3);
            listDeviceResJo = JSON.parseObject(listDeviceRes);
            batchMonitorDeviceResJo = JSON.parseObject(batchMonitorDeviceRes);
            getDeviceResJo1 = JSON.parseObject(getDeviceRes1);
            getDeviceResJo2 = JSON.parseObject(getDeviceRes2);
            getDeviceResJo3 = JSON.parseObject(getDeviceRes3);

            aCase.setResponse(addDeviceResJo1 + "\n\n" +
                    addDeviceResJo2 + "\n\n" +
                    addDeviceResJo3 + "\n\n" +
                    listDeviceResJo + "\n\n" +
                    batchMonitorDeviceResJo + "\n\n" +
                    getDeviceResJo1 + "\n\n" +
                    getDeviceResJo2 + "\n\n" +
                    getDeviceResJo3);

            qaDbUtil.saveToCaseTable(aCase);
        }
    }

    //    ---------------------------------------------------出入口模块---------------------------------------------------
//    ------------------------------------------------1、验证新增编辑出入口--------------------------------------------------------
//   --------------------------------1、新增出入口-2、出入口列表-3、编辑出入口-4、出入口详情-5、出入口列表-6、删除出入口-7、出入口列表-----------------------
    @Test
    public void checkUpdateEntrance() {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;

        String caseDesc = "1、验证启动、停止设备是否成功";
        logger.info(caseDesc + "-----------------------------------------------------------------------------------");

        failReason = "";
        Case aCase = new Case();

        JSONObject addEntranceJo;
        JSONObject listEntranceJo;
        JSONObject updateEntranceJo;
        JSONObject getEntranceJo;
        JSONObject deleteEntranceJo;

        JSONObject addEntranceResJo;
        JSONObject listEntranceResJo1;
        JSONObject updateEntranceResJo;
        JSONObject getEntranceResJo1;
        JSONObject listEntranceResJo2;
        JSONObject deleteEntranceResJo;
        JSONObject listEntranceResJo3;

        String addEntranceRes = "";
        String listEntranceRes1 = "";
        String updateEntranceRes = "";
        String getEntranceRes1 = "";
        String listEntranceRes2 = "";
        String deleteEntranceRes = "";
        String listEntranceRes3 = "";

        String entranceNameOld = caseName + "-old";
        String entranceNameNew = caseName + "-new";

        String entranceTypeOld = entrancTypeGround;
        String entranceTypeNew = entrancTypeParking;
        String entranceId = "";
        try {
//            1、新增出入口
            addEntranceRes = addEntrance(REGION_ID, entranceNameOld, entranceTypeOld);
            checkCode(addEntranceRes, StatusCode.SUCCESS, "新增出入口失败！");

//            2、出入口列表
            listEntranceRes1 = listEntrance(REGION_ID);
            checkCode(listEntranceRes1, StatusCode.SUCCESS, "出入口列表查询失败！");
            entranceId = getEntranceIdByList(listEntranceRes1, entranceNameOld);

//            3、编辑出入口
            updateEntranceRes = updateEntrance(entranceNameNew, entranceTypeNew, entranceId);
            checkCode(updateEntranceRes, StatusCode.SUCCESS, "");

//            4、出入口详情
            getEntranceRes1 = getEntrance(entranceId);
            checkCode(getEntranceRes1, StatusCode.SUCCESS, "");
            checkUpdateByGetEntrance(getEntranceRes1, entranceId, entranceNameNew, entranceTypeNew);

//            5、出入口列表
            listEntranceRes2 = listEntrance(REGION_ID);
            checkCode(listEntranceRes2, StatusCode.SUCCESS, "");
            checkUpdateByListEntrance(listEntranceRes2, entranceId, entranceNameNew, entranceTypeNew, true);

//            6、删除出入口
            deleteEntranceRes = deleteEntrance(entranceId);
            checkCode(deleteEntranceRes, StatusCode.SUCCESS, "");

//            7、出入口列表
            listEntranceRes3 = listEntrance(REGION_ID);
            checkCode(listEntranceRes3, StatusCode.SUCCESS, "");
            checkUpdateByListEntrance(listEntranceRes3, entranceId, entranceNameNew, entranceTypeNew, false);

            aCase.setResult("PASS");

        } catch (Exception e) {
            Assert.assertTrue(false);
        } finally {
            deleteEntrance(entranceId);

            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

//            组织参数
            addEntranceJo = JSON.parseObject(genAddEntrancePara(REGION_ID, entranceNameOld, entranceTypeOld));
            listEntranceJo = JSON.parseObject(genListEntrancePara(REGION_ID));
            updateEntranceJo = JSON.parseObject(genUpdateEntrancePara(entranceNameNew, entranceTypeNew, entranceId));
            getEntranceJo = JSON.parseObject(genGetEntrancePara(entranceId));
            deleteEntranceJo = JSON.parseObject(genDeleteEntrancePara(entranceId));

            aCase.setRequestData(addEntranceJo + "\n\n" +
                    listEntranceJo + "\n\n" +
                    updateEntranceJo + "\n\n" +
                    getEntranceJo + "\n\n" +
                    deleteEntranceJo
            );

//            组织response
            addEntranceResJo = JSON.parseObject(addEntranceRes);
            listEntranceResJo1 = JSON.parseObject(listEntranceRes1);
            updateEntranceResJo = JSON.parseObject(updateEntranceRes);
            getEntranceResJo1 = JSON.parseObject(getEntranceRes1);
            listEntranceResJo2 = JSON.parseObject(listEntranceRes2);
            deleteEntranceResJo = JSON.parseObject(deleteEntranceRes);
            listEntranceResJo3 = JSON.parseObject(listEntranceRes3);

            aCase.setResponse(addEntranceResJo + "\n\n" +
                    listEntranceResJo1 + "\n\n" +
                    updateEntranceResJo + "\n\n" +
                    getEntranceResJo1 + "\n\n" +
                    listEntranceResJo2 + "\n\n" +
                    deleteEntranceResJo + "\n\n" +
                    listEntranceResJo3);

            qaDbUtil.saveToCaseTable(aCase);
        }
    }

    //-----------------------------------------------------------出入口设备编辑----------------------------------------------------------------
//-----------------------------------------------1、绑定设备-2、编辑设备-3、出入口设备绑定------------------------------
    @Test
    public void checkUpdateEntranceDevice() {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;

        String caseDesc = "1、验证启动、停止设备是否成功";
        logger.info(caseDesc + "-----------------------------------------------------------------------------------");

        failReason = "";
        Case aCase = new Case();

        JSONObject addEntranceDeviceJo;
        JSONObject updateEntranceDeviceJo;
        JSONObject deleteEntranceDeviceJo;

        JSONObject addEntranceDeviceResJo;
        JSONObject updateEntranceDeviceResJo;
        JSONObject deleteEntranceDeviceResJo;

        String addEntranceDeviceRes = "";
        String updateEntranceDeviceRes = "";
        String deleteEntranceDeviceRes = "";


        String entranceId = ENTRANCE_ID;
        String deviceId = DEVICE_ID_1;
        String entranceType = entrancTypeGround;
        try {
//        1、绑定设备
            addEntranceDeviceRes = addEntranceDevice(deviceId, entranceId, entranceType);
            checkCode(addEntranceDeviceRes, StatusCode.SUCCESS, "");

//            2、编辑设备
//            由于编辑设备只涉及到entranceId，deviceId和出入口坐标，并且实际操作过程中，没有出现坐标不对的错误，所以仅对code做验证
            updateEntranceDeviceRes = updateEntranceDevice(deviceId, entranceId);
            checkCode(updateEntranceDeviceRes, StatusCode.SUCCESS, "");

//        3、出入口设备解绑
            deleteEntranceDeviceRes = deleteEntranceDevice(deviceId, entranceId);
            checkCode(deleteEntranceDeviceRes, StatusCode.SUCCESS, "");

            aCase.setResult("PASS");
        } catch (Exception e) {
            Assert.assertTrue(false);
        } finally {

            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);
//            组织入参
            addEntranceDeviceJo = JSON.parseObject(genAddEntranceDevicePara(deviceId, entranceId, entranceType));
            updateEntranceDeviceJo = JSON.parseObject(genUpdateEntranceDevicePara(deviceId, entranceId));
            deleteEntranceDeviceJo = JSON.parseObject(genDeleteEntranceDevicePara(deviceId, entranceId));

            aCase.setRequestData(addEntranceDeviceJo + "\n\n" +
                    updateEntranceDeviceJo + "\n\n" +
                    deleteEntranceDeviceJo);

//            组织response
            addEntranceDeviceResJo = JSON.parseObject(addEntranceDeviceRes);
            updateEntranceDeviceResJo = JSON.parseObject(updateEntranceDeviceRes);
            deleteEntranceDeviceResJo = JSON.parseObject(deleteEntranceDeviceRes);

            aCase.setResponse(addEntranceDeviceResJo + "\n\n" +
                    updateEntranceDeviceResJo + "\n\n" +
                    deleteEntranceDeviceResJo);

            qaDbUtil.saveToCaseTable(aCase);
        }
    }

    //    -----------------------出入口所属设备列表/可绑定设备列表；绑定/解绑定-----------------------------------
//    ----------------1、出入口可绑定设备列表-2、绑定设备-3、出入口所属设备列表---------------------------------------------
//    ----------------4、出入口设备解绑-5、出入口可绑定设备列表-6、出入口所属设备列表----------------------------------
    @Test
    public void checkBindEntranceDevice() {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;

        String caseDesc = "1、验证启动、停止设备是否成功";
        logger.info(caseDesc + "-----------------------------------------------------------------------------------");

        failReason = "";
        Case aCase = new Case();

        JSONObject bindableEntranceDeviceJo;
        JSONObject addEntranceDeviceJo;
        JSONObject listEntranceDeviceJo;
        JSONObject deleteEntranceDeviceJo;

        JSONObject bindableEntranceDeviceResJo1;
        JSONObject addEntranceDeviceResJo;
        JSONObject listEntranceDeviceResJo1;
        JSONObject deleteEntranceDeviceResJo;
        JSONObject bindableEntranceDeviceResJo2;
        JSONObject listEntranceDeviceResJo2;

        String bindableEntranceDeviceRes1 = "";
        String addEntranceDeviceRes = "";
        String listEntranceDeviceRes1 = "";
        String deleteEntranceDeviceRes = "";
        String bindableEntranceDeviceRes2 = "";
        String listEntranceDeviceRes2 = "";

        String entranceId = ENTRANCE_ID;
        String deviceId = DEVICE_ID_1;
        String entranceType = entrancTypeGround;
        try {

//        1、出入口可绑定设备列表
            bindableEntranceDeviceRes1 = bindableEntranceDevice(entranceId);
            checkBindableDevice(bindableEntranceDeviceRes1, deviceId);

//        2、绑定设备
            addEntranceDeviceRes = addEntranceDevice(deviceId, entranceId, entranceType);
            checkCode(addEntranceDeviceRes, StatusCode.SUCCESS, "");

//        3、出入口所属设备列表
            listEntranceDeviceRes1 = listEntranceDevice(entranceId);
            checkCode(listEntranceDeviceRes1, StatusCode.SUCCESS, "");
            checkListDevice(listEntranceDeviceRes1, deviceId);

//        4、出入口设备解绑
            deleteEntranceDeviceRes = deleteEntranceDevice(deviceId, entranceId);
            checkCode(deleteEntranceDeviceRes, StatusCode.SUCCESS, "");

//        5、出入口可绑定设备列表
            bindableEntranceDeviceRes2 = bindableEntranceDevice(entranceId);
            checkBindableDevice(bindableEntranceDeviceRes2, deviceId);

//        6、出入口所属设备列表
            listEntranceDeviceRes2 = listEntranceDevice(entranceId);
            checkCode(listEntranceDeviceRes2, StatusCode.SUCCESS, "");
            checkListDeviceNull(listEntranceDeviceRes2);

            aCase.setResult("PASS");

        } catch (Exception e) {
            Assert.assertTrue(false);
        } finally {

            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

//            组织入参
            bindableEntranceDeviceJo = JSON.parseObject(genBindableEntranceDevicePara(entranceId));
            addEntranceDeviceJo = JSON.parseObject(genAddEntranceDevicePara(deviceId, entranceId, entranceType));
            listEntranceDeviceJo = JSON.parseObject(genListEntranceDevicePara(entranceId));
            deleteEntranceDeviceJo = JSON.parseObject(genDeleteEntranceDevicePara(deviceId, entranceId));

            aCase.setRequestData(bindableEntranceDeviceJo + "\n\n" +
                    addEntranceDeviceJo + "\n\n" +
                    listEntranceDeviceJo + "\n\n" +
                    deleteEntranceDeviceJo);

//            组织response

            bindableEntranceDeviceResJo1 = JSON.parseObject(bindableEntranceDeviceRes1);
            addEntranceDeviceResJo = JSON.parseObject(addEntranceDeviceRes);
            listEntranceDeviceResJo1 = JSON.parseObject(listEntranceDeviceRes1);
            deleteEntranceDeviceResJo = JSON.parseObject(deleteEntranceDeviceRes);
            bindableEntranceDeviceResJo2 = JSON.parseObject(bindableEntranceDeviceRes2);
            listEntranceDeviceResJo2 = JSON.parseObject(listEntranceDeviceRes2);

            aCase.setResponse(bindableEntranceDeviceResJo1 + "\n\n" +
                    addEntranceDeviceResJo + "\n\n" +
                    listEntranceDeviceResJo1 + "\n\n" +
                    deleteEntranceDeviceResJo + "\n\n" +
                    bindableEntranceDeviceResJo2 + "\n\n" +
                    listEntranceDeviceResJo2);
            qaDbUtil.saveToCaseTable(aCase);
        }
    }

//-----------------------------------------------区域模块----------------------------------------

    //---------------------------------------1、测试编辑区域是否成功-------------------------------------------
//    --------------------1、新增区域-2、区域列表-3、编辑区域-4、区域详情--------------------------------------------
    @Test
    public void checkUpdateRegion() {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;

        String caseDesc = "1、测试编辑区域是否成功";
        logger.info(caseDesc + "-----------------------------------------------------------------------------------");

        failReason = "";
        Case aCase = new Case();

        JSONObject addRegionJo;
        JSONObject listRegionJo;
        JSONObject updateRegionJo;
        JSONObject getRegionJo;

        JSONObject addRegionResJo;
        JSONObject listRegionResJo;
        JSONObject updateRegionResJo;
        JSONObject getRegionResJo;

        String addRegionRes = "";
        String listRegionRes = "";
        String updateRegionRes = "";
        String getRegionRes = "";


        String regionNameOld = caseName + "-old";
        String regionNameNew = caseName + "-new";
        String regionId = "";
        String layoutId = LAYOUT_ID;
        try {
//            1、新增区域
            addRegionRes = addRegion(regionNameOld, layoutId);
            checkCode(addRegionRes, StatusCode.SUCCESS, "");

//            2、区域列表
            listRegionRes = listRegion(layoutId);
            checkCode(listRegionRes, StatusCode.SUCCESS, "");
            regionId = getRegionIdByList(listRegionRes, regionNameOld);

//            3、编辑区域
            updateRegionRes = updateRegion(regionNameNew, regionId);
            checkCode(updateRegionRes, StatusCode.SUCCESS, "");

//            4、区域详情
            getRegionRes = getRegion(regionId);
            checkCode(getRegionRes, StatusCode.SUCCESS, "");
            checkUpdateBygetRegion(getRegionRes, regionId, regionNameNew);

            aCase.setResult("PASS");

        } catch (Exception e) {
            Assert.assertTrue(false);
        } finally {
            deleteRegion(regionId);

            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

            //            组织入参
            addRegionJo = JSON.parseObject(genAddRegionDevicePara(regionNameOld, layoutId));
            listRegionJo = JSON.parseObject(genListRegionPara(layoutId));
            updateRegionJo = JSON.parseObject(genUpdateRegionPara(regionNameNew, regionId));
            getRegionJo = JSON.parseObject(genGetRegionPara(regionId));

            aCase.setRequestData(addRegionJo + "\n\n" +
                    listRegionJo + "\n\n" +
                    updateRegionJo + "\n\n" +
                    getRegionJo);

//            组织response
            addRegionResJo = JSON.parseObject(addRegionRes);
            listRegionResJo = JSON.parseObject(listRegionRes);
            updateRegionResJo = JSON.parseObject(updateRegionRes);
            getRegionResJo = JSON.parseObject(getRegionRes);

            aCase.setResponse(addRegionResJo + "\n\n" +
                    listRegionResJo + "\n\n" +
                    updateRegionResJo + "\n\n" +
                    getRegionResJo);

            qaDbUtil.saveToCaseTable(aCase);
        }
    }

    //---------------------------------------测试新增区域设备是否成功-------------------------------------------
//    ---------------1、新建平面-2、平面列表-3、新增区域-4、区域列表-5、新增设备-6、设备列表-------------------------------
//    -----------------7、新增平面设备-8、新增区域设备-9、区域设备列表-10、区域可绑定设备列表--------------------------------
//    -----------------11、区域设备删除-12、区域设备列表-13、区域可绑定设备列表--------------------------------------------
    @Test
    public void checkAddRegionDevice() {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;

        String caseDesc = "1、测试新增区域设备是否成功";
        logger.info(caseDesc + "-----------------------------------------------------------------------------------");


        JSONObject addLayoutJo;
        JSONObject listLayoutJo;
        JSONObject addRegionJo;
        JSONObject listRegionJo;
        JSONObject addDeviceJo;
        JSONObject listDeviceJo;
        JSONObject addLayoutDeviceJo;
        JSONObject addRegionDeviceJo;
        JSONObject listRegionDeviceJo;
        JSONObject bindableRegionDeviceJo1;
        JSONObject deleteRegionDeviceJo;

        JSONObject addLayoutResJo;
        JSONObject listLayoutResJo;
        JSONObject addRegionResJo;
        JSONObject listRegionResJo;
        JSONObject addDeviceResJo;
        JSONObject listDeviceResJo;
        JSONObject addLayoutDeviceResJo;
        JSONObject addRegionDeviceResJo;
        JSONObject listRegionDeviceResJo1;
        JSONObject bindableRegionDeviceResJo1;
        JSONObject deleteRegionDeviceResJo;
        JSONObject listRegionDeviceResJo2;
        JSONObject bindableRegionDeviceResJo2;

        String addLayoutRes = "";
        String listLayoutRes = "";
        String addRegionRes = "";
        String listRegionRes = "";
        String addDeviceRes = "";
        String listDeviceRes = "";
        String addLayoutDeviceRes = "";
        String addRegionDeviceRes = "";
        String listRegionDeviceRes1 = "";
        String bindableRegionDeviceRes1 = "";
        String deleteRegionDeviceRes = "";
        String listRegionDeviceRes2 = "";
        String bindableRegionDeviceRes2 = "";

        failReason = "";
        Case aCase = new Case();

        String layoutName = caseName;
        String regionName = caseName;
        String deviceName = caseName;
        String layoutDesc = "-测试新增区域设备是否成功";
        String deviceUrl = DeviceUrl;
        String deviceType = deviceTypeFaceCamera;
        String regionId = "", deviceId = "", layoutId = "";
        try {
//            1、新建平面
            addLayoutRes = addLayout(layoutName, layoutDesc, SHOP_Id);
            checkCode(addLayoutRes, StatusCode.SUCCESS, "");

//            2、平面列表
            listLayoutRes = listLayout();
            checkCode(listLayoutRes, StatusCode.SUCCESS, "");
            layoutId = getLayoutIdBylist(listLayoutRes, layoutName);

//            3、新增区域
            addRegionRes = addRegion(regionName, layoutId);
            checkCode(addRegionRes, StatusCode.SUCCESS, "");

//            4、区域列表
            listRegionRes = listRegion(layoutId);
            checkCode(listRegionRes, StatusCode.SUCCESS, "");
            regionId = getRegionIdByList(listRegionRes, regionName);

//            5、新增设备
            addDeviceRes = addDevice(deviceName, deviceType, deviceUrl);
            checkCode(addDeviceRes, StatusCode.SUCCESS, "");

//            6、设备列表
            listDeviceRes = listDevice();
            checkCode(listDeviceRes, StatusCode.SUCCESS, "");
            deviceId = getDeviceIdByListDevice(listDeviceRes, deviceName);

//            7、新增平面设备
            addLayoutDeviceRes = addLayoutDevice(layoutId, deviceId);
            checkCode(addLayoutDeviceRes, StatusCode.SUCCESS, "");

//            8、新增区域设备
            addRegionDeviceRes = addRegionDevice(regionId, deviceId);
            checkCode(addRegionDeviceRes, StatusCode.SUCCESS, "");

//            9、区域设备列表
            listRegionDeviceRes1 = listRegionDevice(regionId);
            checkCode(listRegionDeviceRes1, StatusCode.SUCCESS, "");
            checkAddRegionDeviceByList(listRegionDeviceRes1, deviceId, deviceName, deviceType);

//            10、区域可绑定设备列表
            bindableRegionDeviceRes1 = bindableRegionDevice(regionId);
            checkCode(bindableRegionDeviceRes1, StatusCode.SUCCESS, "");
            checkBindableRegionDeviceByListNUll(bindableRegionDeviceRes1);

//            11、区域设备删除
            deleteRegionDeviceRes = deleteRegionDevice(regionId, deviceId);
            checkCode(deleteRegionDeviceRes, StatusCode.SUCCESS, "");

//            12、区域设备列表
            listRegionDeviceRes2 = listRegionDevice(regionId);
            checkCode(listRegionDeviceRes2, StatusCode.SUCCESS, "");
            checkregionDeviceByListNUll(listRegionDeviceRes2);

//            13、区域可绑定设备列表
            bindableRegionDeviceRes2 = bindableRegionDevice(regionId);
            checkCode(bindableRegionDeviceRes2, StatusCode.SUCCESS, "");
            checkBindableRegionDeviceByList(bindableRegionDeviceRes2, deviceId, deviceName, deviceType);

            aCase.setResult("PASS");

        } catch (Exception e) {
            Assert.assertTrue(false);
        } finally {
            deleteDevice(deviceId);
            deleteRegion(regionId);
            delLayout(layoutId);

            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);
//            组织入参
            addLayoutJo = JSON.parseObject(genAddLayoutPara(layoutName, layoutDesc, SHOP_Id));
            listLayoutJo = JSON.parseObject(genListLayoutPara());
            addRegionJo = JSON.parseObject(genAddRegionPara(regionName, layoutId));
            listRegionJo = JSON.parseObject(genListRegionPara(layoutId));
            addDeviceJo = JSON.parseObject(genAddDevicePara(deviceName, deviceType, deviceUrl));
            listDeviceJo = JSON.parseObject(genListDevicePara());
            addLayoutDeviceJo = JSON.parseObject(genAddLayoutDevicePara(layoutId, deviceId));
            addRegionDeviceJo = JSON.parseObject(genAddRegionDevicePara(regionId, deviceId));
            listRegionDeviceJo = JSON.parseObject(genListRegionDevicePara(regionId));
            bindableRegionDeviceJo1 = JSON.parseObject(genBindableRegionDevicePara(regionId));
            deleteRegionDeviceJo = JSON.parseObject(genDeleteRegionDevicePara(regionId, deviceId));

            aCase.setRequestData(addLayoutJo + "\n\n" +
                    listLayoutJo + "\n\n" +
                    addRegionJo + "\n\n" +
                    listRegionJo + "\n\n" +
                    addRegionJo + "\n\n" +
                    addDeviceJo + "\n\n" +
                    listDeviceJo + "\n\n" +
                    addLayoutDeviceJo + "\n\n" +
                    addRegionDeviceJo + "\n\n" +
                    listRegionDeviceJo + "\n\n" +
                    bindableRegionDeviceJo1 + "\n\n" +
                    deleteRegionDeviceJo);

//            组织response
            addLayoutResJo = JSON.parseObject(addLayoutDeviceRes);
            listLayoutResJo = JSON.parseObject(listLayoutRes);
            addRegionResJo = JSON.parseObject(addRegionRes);
            listRegionResJo = JSON.parseObject(listRegionRes);
            addDeviceResJo = JSON.parseObject(addDeviceRes);
            listDeviceResJo = JSON.parseObject(listDeviceRes);
            addLayoutDeviceResJo = JSON.parseObject(addLayoutDeviceRes);
            addRegionDeviceResJo = JSON.parseObject(addRegionDeviceRes);
            listRegionDeviceResJo1 = JSON.parseObject(listRegionDeviceRes1);
            bindableRegionDeviceResJo1 = JSON.parseObject(bindableRegionDeviceRes1);
            deleteRegionDeviceResJo = JSON.parseObject(deleteRegionDeviceRes);
            listRegionDeviceResJo2 = JSON.parseObject(listRegionDeviceRes2);
            bindableRegionDeviceResJo2 = JSON.parseObject(bindableRegionDeviceRes2);

            aCase.setResponse(addLayoutResJo + "\n\n" +
                    listLayoutResJo + "\n\n" +
                    addRegionResJo + "\n\n" +
                    listRegionResJo + "\n\n" +
                    addDeviceResJo + "\n\n" +
                    listDeviceResJo + "\n\n" +
                    addLayoutDeviceResJo + "\n\n" +
                    addRegionDeviceResJo + "\n\n" +
                    listRegionDeviceResJo1 + "\n\n" +
                    bindableRegionDeviceResJo1 + "\n\n" +
                    deleteRegionDeviceResJo + "\n\n" +
                    listRegionDeviceResJo2 + "\n\n" +
                    bindableRegionDeviceResJo2);

            qaDbUtil.saveToCaseTable(aCase);
        }
    }

//-----------------------------------------------------测试平面图片相关接口---------------------------------------
//    ----------------1、新建平面-2、平面列表-3、平面编辑-4、平面列表-5、平面详情-6、平面图片删除-7、平面列表-8、平面详情---------------

    @Test
    public void checkLayoutPic() {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;

        String caseDesc = "2、测试平面图片相关接口";
        logger.info(caseDesc + "-----------------------------------------------------------------------------------");

        failReason = "";
        Case aCase = new Case();

        JSONObject addLayoutJo;
        JSONObject listLayoutJo;
        JSONObject updateLayoutJo;
        JSONObject getLayoutJo;
        JSONObject delLayoutPicJo;

        JSONObject addLayoutResJo;
        JSONObject listLayoutResJo1;
        JSONObject updateLayoutResJo;
        JSONObject listLayoutResJo2;
        JSONObject getLayoutResJo1;
        JSONObject delLayoutPicResJo;
        JSONObject listLayoutResJo3;
        JSONObject getLayoutResJo2;

        String addLayoutRes = "";
        String listLayoutRes1 = "";
        String updateLayoutRes = "";
        String listLayoutRes2 = "";
        String getLayoutRes1 = "";
        String delLayoutPicRes = "";
        String listLayoutRes3 = "";
        String getLayoutRes2 = "";

        String layoutNameOld = caseName + "-old";
        String layoutNameNew = caseName + "-new";
        String layoutDescOld = "-测试新增区域设备是否成功（原来）";
        String layoutDescNew = "-测试新增区域设备是否成功（新）";
        String layoutId = "";
        try {
//            1、新建平面
            addLayoutRes = addLayout(layoutNameOld, layoutDescOld, SHOP_Id);
            checkCode(addLayoutRes, StatusCode.SUCCESS, "");

//            2、平面列表
            listLayoutRes1 = listLayout();
            checkCode(listLayoutRes1, StatusCode.SUCCESS, "");
            layoutId = getLayoutIdBylist(listLayoutRes1, layoutNameOld);

//            3、平面编辑
            updateLayoutRes = updateLayout(layoutId, layoutNameNew, layoutDescNew, LAYOUT_PIC_OSS);
            checkCode(updateLayoutRes, StatusCode.SUCCESS, "");

//            4、平面列表
            listLayoutRes2 = listLayout();
            checkCode(listLayoutRes2, StatusCode.SUCCESS, "");
            checkUpdateByLayoutList(listLayoutRes2, layoutId, layoutNameNew, layoutDescNew, true);

//            5、平面详情
            getLayoutRes1 = getLayout(layoutId);
            checkCode(getLayoutRes1, StatusCode.SUCCESS, "");
            checkUpdateBygetLayout(getLayoutRes1, layoutId, layoutNameNew, layoutDescNew, true);

//            6、平面图片删除
            delLayoutPicRes = delLayoutPic(layoutId);
            checkCode(delLayoutPicRes, StatusCode.SUCCESS, "");

//            7、平面列表
            listLayoutRes3 = listLayout();
            checkCode(listLayoutRes3, StatusCode.SUCCESS, "");
            checkUpdateByLayoutList(listLayoutRes3, layoutId, layoutNameNew, layoutDescNew, false);

//            8、平面详情
            getLayoutRes2 = getLayout(layoutId);
            checkCode(getLayoutRes2, StatusCode.SUCCESS, "");
            checkUpdateBygetLayout(getLayoutRes2, layoutId, layoutNameNew, layoutDescNew, false);

            aCase.setResult("PASS");

        } catch (Exception e) {
            Assert.assertTrue(false);
        } finally {
            delLayout(layoutId);

            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

//            组织入参
            addLayoutJo = JSON.parseObject(genAddLayoutPara(layoutNameOld, layoutDescOld, SHOP_Id));
            listLayoutJo = JSON.parseObject(genListLayoutPara());
            updateLayoutJo = JSON.parseObject(genUpdateLayoutPara(layoutId, layoutNameNew, layoutDescNew, LAYOUT_PIC_OSS));
            getLayoutJo = JSON.parseObject(genGetLayoutPara(layoutId));
            delLayoutPicJo = JSON.parseObject(genDelLayoutPicPara(layoutId));

            aCase.setRequestData(addLayoutJo + "\n\n" +
                    listLayoutJo + "\n\n" +
                    updateLayoutJo + "\n\n" +
                    getLayoutJo + "\n\n" +
                    delLayoutPicJo);

//            组织response
            addLayoutResJo = JSON.parseObject(addLayoutRes);
            listLayoutResJo1 = JSON.parseObject(listLayoutRes1);
            updateLayoutResJo = JSON.parseObject(updateLayoutRes);
            listLayoutResJo2 = JSON.parseObject(listLayoutRes2);
            getLayoutResJo1 = JSON.parseObject(getLayoutRes1);
            delLayoutPicResJo = JSON.parseObject(delLayoutPicRes);
            listLayoutResJo3 = JSON.parseObject(listLayoutRes3);
            getLayoutResJo2 = JSON.parseObject(getLayoutRes2);

            aCase.setResponse(addLayoutResJo + "\n\n" +
                    listLayoutResJo1 + "\n\n" +
                    updateLayoutResJo + "\n\n" +
                    listLayoutResJo2 + "\n\n" +
                    getLayoutResJo1 + "\n\n" +
                    delLayoutPicResJo + "\n\n" +
                    listLayoutResJo3 + "\n\n" +
                    getLayoutResJo2);

            qaDbUtil.saveToCaseTable(aCase);
        }
    }

//    ----------------------------------------测试平面设备----------------------------------------------------
//    -------1、新建平面-2、平面列表-3、新增设备-4、设备列表-5、平面设备新增-6、平面设备批量新增---------------------
//    ---------7、平面所属设备列表-8、平面可绑定设备列表-9、平面设备删除-10、平面所属设备列表-11、平面可绑定设备列表--------

    @Test
    public void checkLayoutDevice() {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;

        String caseDesc = "3、测试平面设备";
        logger.info(caseDesc + "-----------------------------------------------------------------------------------");

        failReason = "";
        Case aCase = new Case();

        JSONObject addLayoutJo;
        JSONObject listLayoutJo;
        JSONObject addDeviceJo1;
        JSONObject addDeviceJo2;
        JSONObject addDeviceJo3;
        JSONObject addDeviceJo4;
        JSONObject addDeviceJo5;
        JSONObject listDeviceJo;
        JSONObject addLayoutDeviceJo;
        JSONObject getBatchAddLayoutDeviceJo;
        JSONObject listLayoutDeviceJo1;
        JSONObject bindableLayoutDeviceJo1;
        JSONObject delLayoutDeviceJo1;
        JSONObject delLayoutDeviceJo2;
        JSONObject delLayoutDeviceJo3;

        JSONObject addLayoutResJo;
        JSONObject listLayoutResJo;
        JSONObject addDeviceResJo1;
        JSONObject addDeviceResJo2;
        JSONObject addDeviceResJo3;
        JSONObject addDeviceResJo4;
        JSONObject addDeviceResJo5;
        JSONObject listDeviceResJo;
        JSONObject addLayoutDeviceResJo;
        JSONObject getBatchAddLayoutDeviceResJo;
        JSONObject listLayoutDeviceResJo1;
        JSONObject bindableLayoutDeviceResJo1;
        JSONObject delLayoutDeviceResJo1;
        JSONObject delLayoutDeviceResJo2;
        JSONObject delLayoutDeviceResJo3;
        JSONObject listLayoutDeviceResJo2;
        JSONObject bindableLayoutDeviceResJo2;

        String addLayoutRes = "";
        String listLayoutRes = "";
        String addDeviceRes1 = "";
        String addDeviceRes2 = "";
        String addDeviceRes3 = "";
        String addDeviceRes4 = "";
        String addDeviceRes5 = "";
        String listDeviceRes = "";
        String addLayoutDeviceRes = "";
        String getBatchAddLayoutDeviceRes = "";
        String listLayoutDeviceRes1 = "";
        String bindableLayoutDeviceRes1 = "";
        String delLayoutDeviceRes1 = "";
        String delLayoutDeviceRes2 = "";
        String delLayoutDeviceRes3 = "";
        String listLayoutDeviceRes2 = "";
        String bindableLayoutDeviceRes2 = "";

        String layoutName = caseName;
        String deviceName_1 = caseName + "-1";
        String deviceName_2 = caseName + "-2";
        String deviceName_3 = caseName + "-3";
        String deviceName_4 = caseName + "-4";
        String deviceName_5 = caseName + "-5";
        String deviceId_1 = "", deviceId_2 = "", deviceId_3 = "", deviceId_4 = "", deviceId_5 = "";

        String layoutDesc = "-测试平面区域设备是否成功";
        String layoutId = "";
        try {
//            1、新建平面
            addLayoutRes = addLayout(layoutName, layoutDesc, SHOP_Id);
            checkCode(addLayoutRes, StatusCode.SUCCESS, "");

//            2、平面列表
            listLayoutRes = listLayout();
            checkCode(listLayoutRes, StatusCode.SUCCESS, "");
            layoutId = getLayoutIdBylist(listLayoutRes, layoutName);

//            3、新增设备
            addDeviceRes1 = addDevice(deviceName_1, deviceTypeFaceCamera, DeviceUrl);
            checkCode(addDeviceRes1, StatusCode.SUCCESS, "");
            addDeviceRes2 = addDevice(deviceName_2, deviceTypeFaceCamera, DeviceUrl);
            checkCode(addDeviceRes2, StatusCode.SUCCESS, "");
            addDeviceRes3 = addDevice(deviceName_3, deviceTypeFaceCamera, DeviceUrl);
            checkCode(addDeviceRes3, StatusCode.SUCCESS, "");
            addDeviceRes4 = addDevice(deviceName_4, deviceTypeFaceCamera, DeviceUrl);
            checkCode(addDeviceRes4, StatusCode.SUCCESS, "");
            addDeviceRes5 = addDevice(deviceName_5, deviceTypeFaceCamera, DeviceUrl);
            checkCode(addDeviceRes5, StatusCode.SUCCESS, "");

//            4、设备列表
            listDeviceRes = listDevice();
            checkCode(listDeviceRes, StatusCode.SUCCESS, "");
            deviceId_1 = getDeviceIdByListDevice(listDeviceRes, deviceName_1);
            deviceId_2 = getDeviceIdByListDevice(listDeviceRes, deviceName_2);
            deviceId_3 = getDeviceIdByListDevice(listDeviceRes, deviceName_3);
            deviceId_4 = getDeviceIdByListDevice(listDeviceRes, deviceName_4);
            deviceId_5 = getDeviceIdByListDevice(listDeviceRes, deviceName_5);

//            5、平面设备新增
            addLayoutDeviceRes = addLayoutDevice(layoutId, deviceId_1);
            checkCode(addLayoutDeviceRes, StatusCode.SUCCESS, "");

//            6、平面设备批量新增
            getBatchAddLayoutDeviceRes = getBatchAddLayoutDevice(layoutId, deviceId_2, deviceId_3);
            checkCode(getBatchAddLayoutDeviceRes, StatusCode.SUCCESS, "");

//            7、平面所属设备列表
            listLayoutDeviceRes1 = listLayoutDevice(layoutId);
            checkCode(listLayoutDeviceRes1, StatusCode.SUCCESS, "");
            checkAddLayoutDeviceByList(listLayoutDeviceRes1, deviceId_1, true);
            checkAddLayoutDeviceByList(listLayoutDeviceRes1, deviceId_2, true);
            checkAddLayoutDeviceByList(listLayoutDeviceRes1, deviceId_3, true);
            checkAddLayoutDeviceByList(listLayoutDeviceRes1, deviceId_4, false);
            checkAddLayoutDeviceByList(listLayoutDeviceRes1, deviceId_5, false);

//            8、平面可绑定设备列表
            bindableLayoutDeviceRes1 = bindableLayoutDevice(layoutId);
            checkCode(bindableLayoutDeviceRes1, StatusCode.SUCCESS, "");
            checkAddLayoutDeviceByList(bindableLayoutDeviceRes1, deviceId_1, false);
            checkAddLayoutDeviceByList(bindableLayoutDeviceRes1, deviceId_2, false);
            checkAddLayoutDeviceByList(bindableLayoutDeviceRes1, deviceId_3, false);
            checkAddLayoutDeviceByList(bindableLayoutDeviceRes1, deviceId_4, true);
            checkAddLayoutDeviceByList(bindableLayoutDeviceRes1, deviceId_5, true);

//            9、平面设备删除
            delLayoutDeviceRes1 = delLayoutDevice(layoutId, deviceId_1);
            checkCode(delLayoutDeviceRes1, StatusCode.SUCCESS, "");
            delLayoutDeviceRes2 = delLayoutDevice(layoutId, deviceId_2);
            checkCode(delLayoutDeviceRes2, StatusCode.SUCCESS, "");
            delLayoutDeviceRes3 = delLayoutDevice(layoutId, deviceId_3);
            checkCode(delLayoutDeviceRes3, StatusCode.SUCCESS, "");

//            10、平面所属设备列表
            listLayoutDeviceRes2 = listLayoutDevice(layoutId);
            checkCode(listLayoutDeviceRes2, StatusCode.SUCCESS, "");
            checkAddLayoutDeviceByList(listLayoutDeviceRes2, deviceId_5, false);
            checkAddLayoutDeviceByList(listLayoutDeviceRes2, deviceId_4, false);
            checkAddLayoutDeviceByList(listLayoutDeviceRes2, deviceId_3, false);
            checkAddLayoutDeviceByList(listLayoutDeviceRes2, deviceId_2, false);
            checkAddLayoutDeviceByList(listLayoutDeviceRes2, deviceId_1, false);

//            11、平面可绑定设备列表
            bindableLayoutDeviceRes2 = bindableLayoutDevice(layoutId);
            checkCode(bindableLayoutDeviceRes2, StatusCode.SUCCESS, "");
            checkAddLayoutDeviceByList(bindableLayoutDeviceRes2, deviceId_5, true);
            checkAddLayoutDeviceByList(bindableLayoutDeviceRes2, deviceId_4, true);
            checkAddLayoutDeviceByList(bindableLayoutDeviceRes2, deviceId_3, true);
            checkAddLayoutDeviceByList(bindableLayoutDeviceRes2, deviceId_2, true);
            checkAddLayoutDeviceByList(bindableLayoutDeviceRes2, deviceId_1, true);

            aCase.setResult("PASS");

        } catch (Exception e) {
            Assert.assertTrue(false);
        } finally {
            delLayout(layoutId);
            deleteDevice(deviceId_1);
            deleteDevice(deviceId_2);
            deleteDevice(deviceId_3);
            deleteDevice(deviceId_4);
            deleteDevice(deviceId_5);

            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

//            组织入参
            addLayoutJo = JSON.parseObject(genAddLayoutPara(layoutName, layoutDesc, SHOP_Id));
            listLayoutJo = JSON.parseObject(genListLayoutPara());
            addDeviceJo1 = JSON.parseObject(genAddDevicePara(deviceName_1, deviceTypeFaceCamera, DeviceUrl));
            addDeviceJo2 = JSON.parseObject(genAddDevicePara(deviceName_2, deviceTypeFaceCamera, DeviceUrl));
            addDeviceJo3 = JSON.parseObject(genAddDevicePara(deviceName_3, deviceTypeFaceCamera, DeviceUrl));
            addDeviceJo4 = JSON.parseObject(genAddDevicePara(deviceName_4, deviceTypeFaceCamera, DeviceUrl));
            addDeviceJo5 = JSON.parseObject(genAddDevicePara(deviceName_5, deviceTypeFaceCamera, DeviceUrl));
            listDeviceJo = JSON.parseObject(genListDevicePara());
            addLayoutDeviceJo = JSON.parseObject(genAddLayoutDevicePara(layoutId, deviceId_1));
            getBatchAddLayoutDeviceJo = JSON.parseObject(genGetBatchAddLayoutDevicePara(layoutId, deviceId_2, deviceId_3));
            listLayoutDeviceJo1 = JSON.parseObject(genListLayoutDevicePara(layoutId));
            bindableLayoutDeviceJo1 = JSON.parseObject(genBindableLayoutDevicePara(layoutId));
            delLayoutDeviceJo1 = JSON.parseObject(genDelLayoutDevicePara(layoutId, deviceId_1));
            delLayoutDeviceJo2 = JSON.parseObject(genDelLayoutDevicePara(layoutId, deviceId_1));
            delLayoutDeviceJo3 = JSON.parseObject(genDelLayoutDevicePara(layoutId, deviceId_1));

            aCase.setRequestData(addLayoutJo + "\n\n" +
                    listDeviceJo + "\n\n" +
                    listLayoutJo + "\n\n" +
                    addDeviceJo1 + "\n\n" +
                    addDeviceJo2 + "\n\n" +
                    addDeviceJo3 + "\n\n" +
                    addDeviceJo4 + "\n\n" +
                    addDeviceJo5 + "\n\n" +
                    listDeviceJo + "\n\n" +
                    addLayoutDeviceJo + "\n\n" +
                    getBatchAddLayoutDeviceJo + "\n\n" +
                    listLayoutDeviceJo1 + "\n\n" +
                    bindableLayoutDeviceJo1 + "\n\n" +
                    delLayoutDeviceJo1 + "\n\n" +
                    delLayoutDeviceJo2 + "\n\n" +
                    delLayoutDeviceJo3);

//            组织response
            addLayoutResJo = JSON.parseObject(addLayoutRes);
            listLayoutResJo = JSON.parseObject(listLayoutRes);
            addDeviceResJo1 = JSON.parseObject(addDeviceRes1);
            addDeviceResJo2 = JSON.parseObject(addDeviceRes2);
            addDeviceResJo3 = JSON.parseObject(addDeviceRes3);
            addDeviceResJo4 = JSON.parseObject(addDeviceRes4);
            addDeviceResJo5 = JSON.parseObject(addDeviceRes5);
            listDeviceResJo = JSON.parseObject(listDeviceRes);
            addLayoutDeviceResJo = JSON.parseObject(addLayoutDeviceRes);
            getBatchAddLayoutDeviceResJo = JSON.parseObject(getBatchAddLayoutDeviceRes);
            listLayoutDeviceResJo1 = JSON.parseObject(listLayoutDeviceRes1);
            bindableLayoutDeviceResJo1 = JSON.parseObject(bindableLayoutDeviceRes1);
            delLayoutDeviceResJo1 = JSON.parseObject(delLayoutDeviceRes1);
            delLayoutDeviceResJo2 = JSON.parseObject(delLayoutDeviceRes2);
            delLayoutDeviceResJo3 = JSON.parseObject(delLayoutDeviceRes3);
            listLayoutDeviceResJo2 = JSON.parseObject(listLayoutDeviceRes2);
            bindableLayoutDeviceResJo2 = JSON.parseObject(bindableLayoutDeviceRes2);

            aCase.setResponse(addLayoutResJo + "\n\n" +
                    listLayoutResJo + "\n\n" +
                    addDeviceResJo1 + "\n\n" +
                    addDeviceResJo2 + "\n\n" +
                    addDeviceResJo3 + "\n\n" +
                    addDeviceResJo4 + "\n\n" +
                    addDeviceResJo5 + "\n\n" +
                    listDeviceResJo + "\n\n" +
                    addLayoutDeviceResJo + "\n\n" +
                    getBatchAddLayoutDeviceResJo + "\n\n" +
                    listLayoutDeviceResJo1 + "\n\n" +
                    bindableLayoutDeviceResJo1 + "\n\n" +
                    delLayoutDeviceResJo1 + "\n\n" +
                    delLayoutDeviceResJo2 + "\n\n" +
                    delLayoutDeviceResJo3 + "\n\n" +
                    listLayoutDeviceResJo2 + "\n\n" +
                    bindableLayoutDeviceResJo2);

            qaDbUtil.saveToCaseTable(aCase);
        }
    }

    //    --------------------------------------------测试平面映射新增/编辑/删除------------------------------------------
//-----------1. 新建平面-2.平面列表（取id）-3.新建设备（在同一shop）-4.设备列表-5.平面编辑（新增平面图）-6.平面设备新增----
// ----------7.映射详情（is_mapping=false）-8.平面映射新增-9.平面映射矩阵解析（不知道是干啥的）-10.平面映射编辑（3533）------
//  --------11.平面映射矩阵解析-12.映射详情（is_mapping=true）-13.平面所属设备列表（mapping = true）-------------------
//  --------14.平面映射删除（mapping字段为null）-15.映射详情（is_mapping=false）-16.平面所属设备列表（mapping = false）----------
    @Test
    public void checkLayoutMapping() {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;

        String caseDesc = "测试平面映射新增/编辑/删除";
        logger.info(caseDesc + "-----------------------------------------------------------------------------------");

        failReason = "";
        Case aCase = new Case();


        JSONObject addLayoutJo;
        JSONObject listLayoutJo;
        JSONObject addDeviceJo;
        JSONObject listDeviceJo;
        JSONObject updateLayoutJo;
        JSONObject addLayoutDeviceJo;
        JSONObject getLayoutMappingJo1;
        JSONObject addLayoutMappingJo;
        JSONObject analysisMatrixJo1;
        JSONObject updateLayoutMappingJo;
        JSONObject listLayoutDeviceJo1;
        JSONObject delLayoutMappingJo;


        JSONObject addLayoutResJo;
        JSONObject listLayoutResJo;
        JSONObject addDeviceResJo;
        JSONObject listDeviceResJo;
        JSONObject updateLayoutResJo;
        JSONObject addLayoutDeviceResJo;
        JSONObject getLayoutMappingResJo1;
        JSONObject addLayoutMappingResJo;
        JSONObject analysisMatrixResJo1;
        JSONObject updateLayoutMappingResJo;
        JSONObject analysisMatrixResJo2;
        JSONObject getLayoutMappingResJo2;
        JSONObject listLayoutDeviceResJo1;
        JSONObject delLayoutMappingResJo;
        JSONObject getLayoutMappingResJo3;
        JSONObject listLayoutDeviceResJo2;

        String addLayoutRes = "";
        String listLayoutRes = "";
        String addDeviceRes = "";
        String listDeviceRes = "";
        String updateLayoutRes = "";
        String addLayoutDeviceRes = "";
        String getLayoutMappingRes1 = "";
        String addLayoutMappingRes = "";
        String analysisMatrixRes1 = "";
        String updateLayoutMappingRes = "";
        String analysisMatrixRes2 = "";
        String getLayoutMappingRes2 = "";
        String listLayoutDeviceRes1 = "";
        String delLayoutMappingRes = "";
        String getLayoutMappingRes3 = "";
        String listLayoutDeviceRes2 = "";

        String layoutName = caseName;
        String deviceName = caseName;

        String layoutDesc = "测试平面映射";
        String layoutId = "", deviceId = "";
        try {
//            1、新建平面
            addLayoutRes = addLayout(layoutName, layoutDesc, SHOP_Id);
            checkCode(addLayoutRes, StatusCode.SUCCESS, "");

//            2、平面列表
            listLayoutRes = listLayout();
            checkCode(listLayoutRes, StatusCode.SUCCESS, "");
            layoutId = getLayoutIdBylist(listLayoutRes, layoutName);

//            3、新增设备
            addDeviceRes = addDevice(deviceName, deviceTypeFaceCamera, DeviceUrl);
            checkCode(addDeviceRes, StatusCode.SUCCESS, "");

//            4、设备列表
            listDeviceRes = listDevice();
            checkCode(listDeviceRes, StatusCode.SUCCESS, "");
            deviceId = getDeviceIdByListDevice(listDeviceRes, deviceName);

//            5、平面编辑
            updateLayoutRes = updateLayout(layoutId, layoutName, layoutDesc, LAYOUT_PIC_OSS);
            checkCode(updateLayoutRes, StatusCode.SUCCESS, "");

//            6、平面设备新增
            addLayoutDeviceRes = addLayoutDevice(layoutId, deviceId);
            checkCode(addLayoutDeviceRes, StatusCode.SUCCESS, "");

//            7、映射详情
            getLayoutMappingRes1 = getLayoutMapping(layoutId, deviceId);
            checkCode(getLayoutMappingRes1, StatusCode.SUCCESS, "");
            checkIsMappingByGetMapping(getLayoutMappingRes1, false);

//            8、平面映射新增
            addLayoutMappingRes = addLayoutMapping(layoutId, deviceId);
            checkCode(addLayoutMappingRes, StatusCode.SUCCESS, "");

//            9、平面映射矩阵解析
            analysisMatrixRes1 = analysisMatrix(layoutId, deviceId);
            checkCode(analysisMatrixRes1, StatusCode.SUCCESS, "");

//            10、平面映射编辑
            updateLayoutMappingRes = updateLayoutMapping(layoutId, deviceId);
            checkCode(updateLayoutMappingRes, StatusCode.SUCCESS, "");

//            11、平面映射矩阵解析
            analysisMatrixRes2 = analysisMatrix(layoutId, deviceId);
            checkCode(analysisMatrixRes2, StatusCode.SUCCESS, "");

//            12、映射详情
            getLayoutMappingRes2 = getLayoutMapping(layoutId, deviceId);
            checkCode(getLayoutMappingRes2, StatusCode.SUCCESS, "");
            checkIsMappingByGetMapping(getLayoutMappingRes2, true);

//            13、平面所属设备列表
            listLayoutDeviceRes1 = listLayoutDevice(layoutId);
            checkCode(listLayoutDeviceRes1, StatusCode.SUCCESS, "");
            checkIsMappingByLayoutDeviceList(listLayoutDeviceRes1, deviceId, true);

//            14、平面映射删除
            delLayoutMappingRes = delLayoutMapping(layoutId, deviceId);
            checkCode(delLayoutMappingRes, StatusCode.SUCCESS, "");

//            15、映射详情
            getLayoutMappingRes3 = getLayoutMapping(layoutId, deviceId);
            checkCode(getLayoutMappingRes3, StatusCode.SUCCESS, "");
            checkIsMappingByGetMapping(getLayoutMappingRes3, false);

//            16、平面所属设备列表
            listLayoutDeviceRes2 = listLayoutDevice(layoutId);
            checkCode(listLayoutDeviceRes2, StatusCode.SUCCESS, "");
            checkIsMappingByLayoutDeviceList(listLayoutDeviceRes2, deviceId, false);

            aCase.setResult("PASS");

        } catch (Exception e) {
            Assert.assertTrue(false);
        } finally {
            delLayout(layoutId);
            deleteDevice(deviceId);

            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);

//            组织入参

            addLayoutJo = JSON.parseObject(genAddLayoutPara(layoutName, layoutDesc, SHOP_Id));
            listLayoutJo = JSON.parseObject(genListLayoutPara());
            addDeviceJo = JSON.parseObject(genAddDevicePara(deviceName, deviceTypeFaceCamera, DeviceUrl));
            listDeviceJo = JSON.parseObject(genListDevicePara());
            updateLayoutJo = JSON.parseObject(genUpdateLayoutPara(layoutId, layoutName, layoutDesc, LAYOUT_PIC_OSS));
            addLayoutDeviceJo = JSON.parseObject(genGetLayoutMappingPara(layoutId, deviceId));
            getLayoutMappingJo1 = JSON.parseObject(genGetLayoutMappingPara(layoutId, deviceId));
            addLayoutMappingJo = JSON.parseObject(genAddLayoutMappingPara(layoutId, deviceId));
            analysisMatrixJo1 = JSON.parseObject(genAnalysisMatrixPara(layoutId, deviceId));
            updateLayoutMappingJo = JSON.parseObject(genUpdateLayoutMappingPara(layoutId, deviceId));
            listLayoutDeviceJo1 = JSON.parseObject(genListLayoutDevicePara(layoutId));
            delLayoutMappingJo = JSON.parseObject(genDelLayoutMappingPara(layoutId, deviceId));

            aCase.setRequestData(addLayoutJo + "\n\n" +
                    listLayoutJo + "\n\n" +
                    addDeviceJo + "\n\n" +
                    listDeviceJo + "\n\n" +
                    updateLayoutJo + "\n\n" +
                    addLayoutDeviceJo + "\n\n" +
                    getLayoutMappingJo1 + "\n\n" +
                    addLayoutMappingJo + "\n\n" +
                    analysisMatrixJo1 + "\n\n" +
                    updateLayoutMappingJo + "\n\n" +
                    listLayoutDeviceJo1 + "\n\n" +
                    delLayoutMappingJo);

//            组织response

            addLayoutResJo = JSON.parseObject(addLayoutRes);
            listLayoutResJo = JSON.parseObject(listLayoutRes);
            addDeviceResJo = JSON.parseObject(addDeviceRes);
            listDeviceResJo = JSON.parseObject(listDeviceRes);
            updateLayoutResJo = JSON.parseObject(updateLayoutRes);
            addLayoutDeviceResJo = JSON.parseObject(addLayoutDeviceRes);
            getLayoutMappingResJo1 = JSON.parseObject(getLayoutMappingRes1);
            addLayoutMappingResJo = JSON.parseObject(addLayoutMappingRes);
            analysisMatrixResJo1 = JSON.parseObject(analysisMatrixRes1);
            updateLayoutMappingResJo = JSON.parseObject(updateLayoutMappingRes);
            analysisMatrixResJo2 = JSON.parseObject(analysisMatrixRes2);
            getLayoutMappingResJo2 = JSON.parseObject(getLayoutMappingRes2);
            listLayoutDeviceResJo1 = JSON.parseObject(listLayoutDeviceRes1);
            delLayoutMappingResJo = JSON.parseObject(delLayoutMappingRes);
            getLayoutMappingResJo3 = JSON.parseObject(getLayoutMappingRes3);
            listLayoutDeviceResJo2 = JSON.parseObject(listLayoutDeviceRes2);

            aCase.setResponse(addLayoutResJo + "\n\n" +
                    listLayoutResJo + "\n\n" +
                    addDeviceResJo + "\n\n" +
                    listDeviceResJo + "\n\n" +
                    updateLayoutResJo + "\n\n" +
                    addLayoutDeviceResJo + "\n\n" +
                    getLayoutMappingResJo1 + "\n\n" +
                    addLayoutMappingResJo + "\n\n" +
                    analysisMatrixResJo1 + "\n\n" +
                    updateLayoutMappingResJo + "\n\n" +
                    analysisMatrixResJo2 + "\n\n" +
                    getLayoutMappingResJo2 + "\n\n" +
                    listLayoutDeviceResJo1 + "\n\n" +
                    delLayoutMappingResJo + "\n\n" +
                    getLayoutMappingResJo3 + "\n\n" +
                    listLayoutDeviceResJo2);

            qaDbUtil.saveToCaseTable(aCase);
        }
    }

    //--------------------------------------------------验证主体新增/更新/删除--------------------------------------------------
//    1、
    @Test
    public void checkSubject() {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;

        String caseDesc = "验证主体新增/更新/删除";
        logger.info(caseDesc + "-----------------------------------------------------------------------------------");

        failReason = "";
        Case aCase = new Case();


        JSONObject addSubjectJo;
        JSONObject listSubjectJo;
        JSONObject getSubjectJo;
        JSONObject updateSubjectJo;
        JSONObject deleteSubjectJo;

        JSONObject addSubjectResJo;
        JSONObject listSubjectResJo1;
        JSONObject getSubjectResJo1;
        JSONObject updateSubjectResJo;
        JSONObject listSubjectResJo2;
        JSONObject getSubjectResJo2;
        JSONObject deleteSubjectResJo;
        JSONObject listSubjectResJo3;

        String addSubjectRes = "";
        String listSubjectRes1 = "";
        String getSubjectRes1 = "";
        String updateSubjectRes = "";
        String listSubjectRes2 = "";
        String getSubjectRes2 = "";
        String deleteSubjectRes = "";
        String listSubjectRes3 = "";

        String subjectType = subjectTypeMarket;
        String subjectTypeName = subjectTypeNameMarket;
        String subjectNameOLd = caseName + "-old";
        String localOld = "中关村soho-A区";
        String managerOld = "sophie" + "-old";
        String phoneOld = "15165153865";

        String subjectNameNew = caseName + "-new";
        String localNew = "中关村soho-B区";
        String managerNew = "sophie" + "-new";
        String phoneNew = "17610248107";

        String subjectId = "", subjectIdTemp = "";

        try {
//            1、新增主体
            addSubjectRes = addSubject(subjectType, subjectNameOLd, localOld, managerOld, phoneOld);
            checkCode(addSubjectRes, StatusCode.SUCCESS, "");

            String addSubjectTempRes = addSubject(subjectType, subjectNameOLd + "-temp", localOld, managerOld, phoneOld);
            checkCode(addSubjectTempRes, StatusCode.SUCCESS, "");

//            2、主体列表
            listSubjectRes1 = listSubject(BRAND_ID);
            checkCode(listSubjectRes1, StatusCode.SUCCESS, "");
            subjectId = getSubjectIdByList(listSubjectRes1, subjectNameOLd);
            subjectIdTemp = getSubjectIdByList(listSubjectRes1, subjectNameOLd + "-temp");

//            3、主体详情
            getSubjectRes1 = getSubject(subjectId);
            checkCode(getSubjectRes1, StatusCode.SUCCESS, "");
            checkGetSubject(getSubjectRes1, subjectTypeName, subjectNameOLd, localOld, managerOld, phoneOld);

//            4、更新主体
            updateSubjectRes = updateSubject(subjectId, subjectNameNew, localNew, managerNew, phoneNew);
            checkCode(updateSubjectRes, StatusCode.SUCCESS, "");

//            5、主体列表
            listSubjectRes2 = listSubject(BRAND_ID);
            checkCode(listSubjectRes2, StatusCode.SUCCESS, "");
            checkListSubject(listSubjectRes2, subjectId, subjectNameNew, localNew, managerNew, phoneNew, true);

//            6、主体详情
            getSubjectRes2 = getSubject(subjectId);
            checkCode(getSubjectRes2, StatusCode.SUCCESS, "");
            checkGetSubject(getSubjectRes2, subjectTypeName, subjectNameNew, localNew, managerNew, phoneNew);

//            7、删除主体
            deleteSubjectRes = deleteSubject(subjectId);
            checkCode(deleteSubjectRes, StatusCode.SUCCESS, "");

//            8、主体列表
            listSubjectRes3 = listSubject(BRAND_ID);
            checkCode(listSubjectRes3, StatusCode.SUCCESS, "");
            checkCode(listSubjectRes3, StatusCode.SUCCESS, "");
            checkListSubject(listSubjectRes3, subjectId, subjectNameNew, localNew, managerNew, phoneNew, false);

            aCase.setResult("PASS");

        } catch (Exception e) {
            Assert.assertTrue(false);
        } finally {
            deleteSubject(subjectId);
            deleteSubject(subjectIdTemp);

            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);
//            组织入参
            addSubjectJo = JSON.parseObject(genAddSubjectPara(subjectType, subjectNameOLd, localOld, managerOld, phoneOld));
            listSubjectJo = JSON.parseObject(genListSubjectPara(BRAND_ID));
            getSubjectJo = JSON.parseObject(genGetSubjectPara(subjectId));
            updateSubjectJo = JSON.parseObject(genUpdateSubjectPara(subjectId, subjectNameNew, localNew, managerNew, phoneNew));
            deleteSubjectJo = JSON.parseObject(genDeleteSubjectPara(subjectId));

            aCase.setRequestData(addSubjectJo + "\n\n" +
                    listSubjectJo + "\n\n" +
                    getSubjectJo + "\n\n" +
                    updateSubjectJo + "\n\n" +
                    deleteSubjectJo);

//            组织response
            addSubjectResJo = JSON.parseObject(addSubjectRes);
            listSubjectResJo1 = JSON.parseObject(listSubjectRes1);
            getSubjectResJo1 = JSON.parseObject(getSubjectRes1);
            updateSubjectResJo = JSON.parseObject(updateSubjectRes);
            listSubjectResJo2 = JSON.parseObject(listSubjectRes2);
            getSubjectResJo2 = JSON.parseObject(getSubjectRes2);
            deleteSubjectResJo = JSON.parseObject(deleteSubjectRes);
            listSubjectResJo3 = JSON.parseObject(listSubjectRes3);

            aCase.setResponse(addSubjectResJo + "\n\n" +
                    listSubjectResJo1 + "\n\n" +
                    getSubjectResJo1 + "\n\n" +
                    updateSubjectResJo + "\n\n" +
                    listSubjectResJo2 + "\n\n" +
                    getSubjectResJo2 + "\n\n" +
                    deleteSubjectResJo + "\n\n" +
                    listSubjectResJo3);
            qaDbUtil.saveToCaseTable(aCase);
        }
    }


    //-------------------------------------------------验证新增、更新、删除品牌----------------------------------------------------
    @Test
    public void checkBrand() {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;

        String caseDesc = "验证新增、更新、删除品牌";
        logger.info(caseDesc + "-----------------------------------------------------------------------------------");

        failReason = "";
        Case aCase = new Case();


        JSONObject addBrandJo;
        JSONObject listbrandJo;
        JSONObject getBrandJo;
        JSONObject updateBrandJo;
        JSONObject deleteBrandJo;

        JSONObject addBrandResJo;
        JSONObject listbrandResJo1;
        JSONObject getBrandResJo1;
        JSONObject updateBrandResJo;
        JSONObject getBrandResJo2;
        JSONObject listbrandResJo2;
        JSONObject deleteBrandResJo;
        JSONObject listbrandResJo3;

        String addBrandRes = "";
        String listbrandRes1 = "";
        String getBrandRes1 = "";
        String updateBrandRes = "";
        String getBrandRes2 = "";
        String listbrandRes2 = "";
        String deleteBrandRes = "";
        String listbrandRes3 = "";

        String brandNameOld = caseName + "-old";
        String managerOld = "old";
        String phoneOld = "15165153865";

        String brandNameNew = caseName + "-new";
        String managerNew = "new";
        String phoneNew = "17610248107";

        String brandId = "";

        try {
//        1、新增品牌
            addBrandRes = addBrand(brandNameOld, managerOld, phoneOld, APPLICATION_ID);
            checkCode(addBrandRes, StatusCode.SUCCESS, "");

//        2、品牌列表
            listbrandRes1 = listbrand(APPLICATION_ID);
            checkCode(listbrandRes1, StatusCode.SUCCESS, "");
            brandId = getBrandIdByList(listbrandRes1, brandNameOld);

//        3、品牌详情
            getBrandRes1 = getBrand(brandId);
            checkCode(getBrandRes1, StatusCode.SUCCESS, "");
            checkGetBrand(getBrandRes1, brandNameOld, managerOld, phoneOld);

//        4、更新品牌
            updateBrandRes = updateBrand(brandId, brandNameNew, managerNew, phoneNew);
            checkCode(updateBrandRes, StatusCode.SUCCESS, "");

//        5、品牌详情
            getBrandRes2 = getBrand(brandId);
            checkCode(getBrandRes2, StatusCode.SUCCESS, "");
            checkGetBrand(getBrandRes2, brandNameNew, managerNew, phoneNew);

//        6、品牌列表
            listbrandRes2 = listbrand(APPLICATION_ID);
            checkCode(listbrandRes2, StatusCode.SUCCESS, "");
            checkBrandList(listbrandRes2, brandId, brandNameNew, managerNew, phoneNew, true);

//        7、删除品牌
            deleteBrandRes = deleteBrand(brandId);
            checkCode(deleteBrandRes, StatusCode.SUCCESS, "");

//        8、品牌列表
            listbrandRes3 = listbrand(APPLICATION_ID);
            checkCode(listbrandRes3, StatusCode.SUCCESS, "");
            checkBrandList(listbrandRes3, brandId, brandNameNew, managerNew, phoneNew, false);

            aCase.setResult("PASS");

        } catch (Exception e) {
            Assert.assertTrue(false);
        } finally {
            deleteBrand(brandId);

            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);
//            组织入参
            addBrandJo = JSON.parseObject(genAddBrandPara(brandNameOld, managerOld, phoneOld, APPLICATION_ID));
            listbrandJo = JSON.parseObject(genListbrandPara(APPLICATION_ID));
            getBrandJo = JSON.parseObject(genGetBrandPara(brandId));
            updateBrandJo = JSON.parseObject(genUpdateBrandPara(brandId, brandNameNew, managerNew, phoneNew));
            deleteBrandJo = JSON.parseObject(genDeleteBrandPara(brandId));

            aCase.setRequestData(addBrandJo + "\n\n" +
                    listbrandJo + "\n\n" +
                    getBrandJo + "\n\n" +
                    updateBrandJo + "\n\n" +
                    deleteBrandJo);

//            组织response
            addBrandResJo = JSON.parseObject(addBrandRes);
            listbrandResJo1 = JSON.parseObject(listbrandRes1);
            getBrandResJo1 = JSON.parseObject(getBrandRes1);
            updateBrandResJo = JSON.parseObject(updateBrandRes);
            getBrandResJo2 = JSON.parseObject(getBrandRes2);
            listbrandResJo2 = JSON.parseObject(listbrandRes2);
            deleteBrandResJo = JSON.parseObject(deleteBrandRes);
            listbrandResJo3 = JSON.parseObject(listbrandRes3);

            aCase.setResponse(addBrandResJo + "\n\n" +
                    listbrandResJo1 + "\n\n" +
                    getBrandResJo1 + "\n\n" +
                    updateBrandResJo + "\n\n" +
                    getBrandResJo2 + "\n\n" +
                    listbrandResJo2 + "\n\n" +
                    deleteBrandResJo + "\n\n" +
                    listbrandResJo3);
            qaDbUtil.saveToCaseTable(aCase);
        }
    }

    //-------------------------------------------------验证新增、更新、删除应用----------------------------------------------------
    @Test
    public void checkApp() {
        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;

        String caseDesc = "验证新增、更新、删除应用";
        logger.info(caseDesc + "-----------------------------------------------------------------------------------");

        failReason = "";
        Case aCase = new Case();

        JSONObject addAppJo;
        JSONObject listAppJo;
        JSONObject getAppJo;
        JSONObject updateAppJo;
        JSONObject deleteAppJo;

        JSONObject addAppResJo;
        JSONObject listAppResJo1;
        JSONObject getAppResJo1;
        JSONObject updateAppResJo;
        JSONObject getAppResJo2;
        JSONObject listAppJo2;
        JSONObject deleteAppResJo;
        JSONObject listAppResJo3;

        String addAppRes = "";
        String listAppRes1 = "";
        String getAppRes1 = "";
        String updateAppRes = "";
        String getAppRes2 = "";
        String listApp2 = "";
        String deleteAppRes = "";
        String listAppRes3 = "";

        String appNameOld = caseName + "-old";
        String appNameNew = caseName + "-new";

        String appId = "";

        try {
//        1、新增应用
            addAppRes = addApp(appNameOld);
            checkCode(addAppRes, StatusCode.SUCCESS, "");

//        2、应用列表
            listAppRes1 = listApp();
            checkCode(listAppRes1, StatusCode.SUCCESS, "");
            appId = getAppIdByList(listAppRes1, appNameOld);

//        3、应用详情
            getAppRes1 = getApp(appId);
            checkCode(getAppRes1, StatusCode.SUCCESS, "");
            checkGetApp(getAppRes1, appNameOld);

//        4、更新品牌
            updateAppRes = updateApp(appId, appNameNew);
            checkCode(updateAppRes, StatusCode.SUCCESS, "");

//        5、应用详情
            getAppRes2 = getApp(appId);
            checkCode(getAppRes2, StatusCode.SUCCESS, "");
            checkGetApp(getAppRes2, appNameNew);

//        6、应用列表
            listApp2 = listApp();
            checkCode(listApp2, StatusCode.SUCCESS, "");
            checkListApp(listApp2, appId, appNameNew, true);

//        7、删除应用
            deleteAppRes = deleteApp(appId);
            checkCode(deleteAppRes, StatusCode.SUCCESS, "");

//        8、应用列表
            listAppRes3 = listApp();
            checkCode(listAppRes3, StatusCode.SUCCESS, "");
            checkListApp(listAppRes3, appId, appNameNew, false);

            aCase.setResult("PASS");

        } catch (Exception e) {
            Assert.assertTrue(false);
        } finally {
            deleteBrand(appId);

            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);
//            组织入参
            addAppJo = JSON.parseObject(genAddAppPara(appNameOld));
            listAppJo = JSON.parseObject(genListAppPara());
            getAppJo = JSON.parseObject(genGetAppPara(appId));
            updateAppJo = JSON.parseObject(genUpdateAppPara(appId, appNameNew));
            deleteAppJo = JSON.parseObject(genDeleteAppPara(appId));

            aCase.setRequestData(addAppJo + "\n\n" +
                    listAppJo + "\n\n" +
                    getAppJo + "\n\n" +
                    updateAppJo + "\n\n" +
                    deleteAppJo);

//            组织response
            addAppResJo = JSON.parseObject(addAppRes);
            listAppResJo1 = JSON.parseObject(listAppRes1);
            getAppResJo1 = JSON.parseObject(getAppRes1);
            updateAppResJo = JSON.parseObject(updateAppRes);
            getAppResJo2 = JSON.parseObject(getAppRes2);
            listAppJo2 = JSON.parseObject(listApp2);
            deleteAppResJo = JSON.parseObject(deleteAppRes);
            listAppResJo3 = JSON.parseObject(listAppRes3);

            aCase.setResponse(addAppResJo + "\n\n" +
                    listAppResJo1 + "\n\n" +
                    getAppResJo1 + "\n\n" +
                    updateAppResJo + "\n\n" +
                    getAppResJo2 + "\n\n" +
                    listAppJo2 + "\n\n" +
                    deleteAppResJo + "\n\n" +
                    listAppResJo3);

            qaDbUtil.saveToCaseTable(aCase);
        }
    }

//    -----------------------------------------------------普通方法--------------------------------------------------------------------

    public String getDeviceIdByListDevice(String response, String deviceName) {
        String deviceId = "";
        JSONArray list = JSON.parseObject(response).getJSONObject("data").getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            JSONObject listSingle = list.getJSONObject(i);
            String nameRes = listSingle.getString("name");
            if (deviceName.equals(nameRes)) {
                deviceId = listSingle.getString("device_id");
                break;
            }
        }
        return deviceId;
    }

    public String getStatusByListDevice(String response, String deviceId) {
        String deviceStatus = "";
        JSONArray list = JSON.parseObject(response).getJSONObject("data").getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            JSONObject listSingle = list.getJSONObject(i);
            String deviceIdRes = listSingle.getString("device_id");
            if (deviceId.equals(deviceIdRes)) {
                deviceStatus = listSingle.getString("device_status");
                break;
            }
        }
        return deviceStatus;
    }

    public void checkDeleteByListDevice(String response, String message) {
        JSONObject list = JSON.parseObject(response).getJSONObject("data");
        Assert.assertEquals(list, null, message);
    }

    public boolean checkBatchRemoveByListDevice(String response, String deviceId) {
        boolean isExist = false;
        JSONArray list = JSON.parseObject(response).getJSONObject("data").getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            JSONObject listSingle = list.getJSONObject(i);
            String deviceIdRes = listSingle.getString("device_id");
            if (deviceId.equals(deviceIdRes)) {
                isExist = true;
                break;
            }
        }
        return isExist;
    }

    public void checkAddDeviceByGetDevice(String response, String deviceId, String deviceName, String deviceType) {
        JSONObject data = JSON.parseObject(response).getJSONObject("data");

        String deviceIdRes = data.getString("device_id");
        Assert.assertEquals(deviceId, deviceIdRes, "设备详情查询失败---没有该deviceId！");

        String deviceNameRes = data.getString("name");
        Assert.assertEquals(deviceName, deviceNameRes, "设备详情查询失败---没有该deviceName！");

        String deviceTypeRes = data.getString("device_type");
        Assert.assertEquals(deviceType, deviceTypeRes, "设备详情查询失败---没有该deviceType！");

    }

    public void checkBatchMonitorByGetDevice(String response) {
        String message = "";
        JSONObject data = JSON.parseObject(response).getJSONObject("data");
        JSONObject monitorConfig = data.getJSONObject("monitor_config");

        int interVal = monitorConfig.getInteger("interval");
        Assert.assertEquals(interVal, 300, message);

        String time = monitorConfig.getJSONArray("time").toJSONString();
        Assert.assertEquals(time, "[\"" + 9 + "\"," + "\"" + 22 + "\"]", message);

        String open = monitorConfig.getString("open");
        Assert.assertEquals(open, "true", message);

        JSONArray emailArr = monitorConfig.getJSONArray("email");
        String email = emailArr.getString(0);
        Assert.assertEquals(email, Email, message);

        JSONArray dingdingArr = monitorConfig.getJSONArray("ding_ding");
        String dingdingUrl = dingdingArr.getString(0);
        Assert.assertEquals(dingdingUrl, DingDingUrl, message);

    }

    public String getEntranceIdByList(String response, String entranceName) {
        String entranceId = "";
        JSONObject data = JSON.parseObject(response).getJSONObject("data");
        JSONArray list = data.getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            JSONObject listSingle = list.getJSONObject(i);
            String entranceNameRes = listSingle.getString("entrance_name");
            if (entranceName.equals(entranceNameRes)) {
                entranceId = listSingle.getString("entrance_id");
            }
        }
        return entranceId;
    }

    public void checkUpdateByGetEntrance(String response, String entranceId, String entranceName, String entranceType) {
        JSONObject data = JSON.parseObject(response).getJSONObject("data");

        String entranceIdRes = data.getString("entrance_id");
        Assert.assertEquals(entranceIdRes, entranceId, "entranceId is wrong!");

        String entranceTypeRes = data.getString("entrance_type");
        Assert.assertEquals(entranceTypeRes, entranceType, "entranceType is wrong!");

        String entranceNameRes = data.getString("entrance_name");
        Assert.assertEquals(entranceNameRes, entranceName, "entranceName is wrong!");
    }

    public void checkUpdateByListEntrance(String response, String entranceId, String entranceName, String entranceType, boolean isExist) {
        boolean isExistRes = false;
        JSONObject data = JSON.parseObject(response).getJSONObject("data");
        JSONArray list = data.getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            JSONObject listSingle = list.getJSONObject(i);
            String entranceIdRes = listSingle.getString("entrance_id");
            if (entranceId.equals(entranceIdRes)) {
                isExistRes = true;

                String entranceNameRes = listSingle.getString("entrance_name");
                Assert.assertEquals(entranceNameRes, entranceName, "");

                String entranceTypeRes = listSingle.getString("entrance_type");
                Assert.assertEquals(entranceTypeRes, entranceType, "");

            }
        }

        Assert.assertEquals(isExistRes, isExist, "存在与否错误");
    }

    public void checkBindableDevice(String response, String deviceId) {
        boolean isExist = false;
        JSONObject data = JSON.parseObject(response).getJSONObject("data");
        JSONArray list = data.getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            JSONObject listSingle = list.getJSONObject(i);
            String deviceIdRes = listSingle.getString("device_id");
            if (deviceId.equals(deviceIdRes)) {
                isExist = true;
            }
        }

        Assert.assertEquals(isExist, true, "Bindable list failed！");

    }

    public void checkListDevice(String response, String deviceId) {
        boolean isExist = false;
        JSONObject data = JSON.parseObject(response).getJSONObject("data");
        JSONArray list = data.getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            JSONObject listSingle = list.getJSONObject(i);
            String deviceIdRes = listSingle.getString("device_id");
            if (deviceId.equals(deviceIdRes)) {
                isExist = true;
            }
        }

        Assert.assertEquals(isExist, true, "binded list failed！");

    }

    public void checkListDeviceNull(String response) {
        JSONObject data = JSON.parseObject(response).getJSONObject("data");
        JSONArray list = data.getJSONArray("list");

        Assert.assertEquals(list.size(), 0, "binded list failed！");

    }

    public String getRegionIdByList(String response, String regionName) {
        String regionId = "";
        JSONObject data = JSON.parseObject(response).getJSONObject("data");
        JSONArray list = data.getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            JSONObject listSingle = list.getJSONObject(i);
            String regionNameRes = listSingle.getString("region_name");
            if (regionNameRes.equals(regionName)) {
                regionId = listSingle.getString("region_id");
            }
        }
        return regionId;
    }

    public void checkUpdateBygetRegion(String response, String regionId, String regionName) {
        JSONObject data = JSON.parseObject(response).getJSONObject("data");

        String regionIdRes = data.getString("region_id");
        Assert.assertEquals(regionIdRes, regionId, "区域详情查询失败---regionId！");

        String regionNameRes = data.getString("region_name");
        Assert.assertEquals(regionNameRes, regionName, "设备详情查询失败---regionName！");
    }


    public void checkAddRegionDeviceByList(String response, String deviceId, String deviceName, String deviceType) {
        boolean isExist = false;
        JSONObject data = JSON.parseObject(response).getJSONObject("data");
        JSONArray list = data.getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            JSONObject listSingle = list.getJSONObject(i);
            String deviceIdRes = listSingle.getString("device_id");
            if (deviceId.equals(deviceIdRes)) {
                isExist = true;

                String deviceNameRes = listSingle.getString("name");
                Assert.assertEquals(deviceNameRes, deviceName, "");

                String deviceTypeRes = listSingle.getString("device_type");
                Assert.assertEquals(deviceTypeRes, deviceType, "");

            }
        }

        Assert.assertEquals(isExist, true, "");
    }

    public void checkregionDeviceByListNUll(String response) {
        JSONObject data = JSON.parseObject(response).getJSONObject("data");
        JSONArray list = data.getJSONArray("list");
        Assert.assertEquals(list.size(), 0, "");
    }

    public void checkBindableRegionDeviceByList(String response, String deviceId, String deviceName, String deviceType) {
        boolean isExist = false;
        JSONObject data = JSON.parseObject(response).getJSONObject("data");
        JSONArray list = data.getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            JSONObject listSingle = list.getJSONObject(i);
            String deviceIdRes = listSingle.getString("device_id");
            if (deviceId.equals(deviceIdRes)) {
                isExist = true;

                String deviceNameRes = listSingle.getString("name");
                Assert.assertEquals(deviceNameRes, deviceName, "");

                String deviceTypeRes = listSingle.getString("device_type");
                Assert.assertEquals(deviceTypeRes, deviceType, "");

            }
        }

        Assert.assertEquals(isExist, true, "");
    }

    public void checkBindableRegionDeviceByListNUll(String response) {
        JSONObject data = JSON.parseObject(response).getJSONObject("data");
        JSONArray list = data.getJSONArray("list");
        Assert.assertEquals(list.size(), 0, "");
    }

    public String getLayoutIdBylist(String response, String layoutName) {
        String layoutId = "";
        JSONObject data = JSON.parseObject(response).getJSONObject("data");
        JSONArray list = data.getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            JSONObject listSingle = list.getJSONObject(i);
            String layoutNameRes = listSingle.getString("name");
            if (layoutName.equals(layoutNameRes)) {
                layoutId = listSingle.getString("layout_id");
            }
        }
        return layoutId;
    }

    public void checkUpdateByLayoutList(String response, String layoutId, String layoutName, String desc, boolean withPic) {
        boolean isExist = false;
        JSONObject data = JSON.parseObject(response).getJSONObject("data");
        JSONArray list = data.getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            JSONObject listSingle = list.getJSONObject(i);
            String layoutIdRes = listSingle.getString("layout_id");
            if (layoutId.equals(layoutIdRes)) {
                isExist = true;

                String layoutNameRes = listSingle.getString("name");
                Assert.assertEquals(layoutNameRes, layoutName, "");

                String descRes = listSingle.getString("description");
                Assert.assertEquals(descRes, desc, "");

                boolean withPicRes = listSingle.getBooleanValue("with_pic");
                Assert.assertEquals(withPicRes, withPic, "");

            }
        }

        Assert.assertEquals(isExist, true, "");
    }

    public void checkUpdateBygetLayout(String response, String layoutId, String layoutName, String desc, boolean withPic) {
        JSONObject data = JSON.parseObject(response).getJSONObject("data");

        String layoutIdRes = data.getString("layout_id");
        Assert.assertEquals(layoutIdRes, layoutId, "区域详情查询失败---layoutId！");

        String layoutNameRes = data.getString("name");
        Assert.assertEquals(layoutNameRes, layoutName, "设备详情查询失败---layoutName！");

        String descRes = data.getString("description");
        Assert.assertEquals(descRes, desc, "");

        boolean withPicRes = data.getBooleanValue("with_pic");
        Assert.assertEquals(withPicRes, withPic, "");
    }

    public void checkAddLayoutDeviceByList(String response, String deviceId, boolean isExist) {
        boolean isExistRes = false;
        JSONObject data = JSON.parseObject(response).getJSONObject("data");
        JSONArray list = data.getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            JSONObject listSingle = list.getJSONObject(i);
            String deviceIdRes = listSingle.getString("device_id");
            if (deviceId.equals(deviceIdRes)) {
                isExistRes = true;
            }
        }

        Assert.assertEquals(isExistRes, isExist, "");
    }

    public void checkIsMappingByGetMapping(String response, boolean isMapping) {
        JSONObject data = JSON.parseObject(response).getJSONObject("data");
        boolean isMappingRes = data.getBooleanValue("is_mapping");
        Assert.assertEquals(isMappingRes, isMapping, "");
    }

    public void checkIsMappingByLayoutDeviceList(String response, String deviceId, boolean mapping) {
        boolean isExistRes = false;
        boolean mappingRes = false;
        JSONObject data = JSON.parseObject(response).getJSONObject("data");
        JSONArray list = data.getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            JSONObject listSingle = list.getJSONObject(i);
            String deviceIdRes = listSingle.getString("device_id");
            if (deviceId.equals(deviceIdRes)) {
                isExistRes = true;
                mappingRes = listSingle.getBooleanValue("mapping");
            }
        }
        Assert.assertEquals(isExistRes, true, "不存在");
        Assert.assertEquals(mappingRes, mapping, "映射状态错误！");
    }

    public String getSubjectIdByList(String response, String subjectName) {
        String subjectId = "";
        boolean isExistRes = false;
        JSONObject data = JSON.parseObject(response).getJSONObject("data");
        JSONArray list = data.getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            JSONObject listSingle = list.getJSONObject(i);
            String subjectNameRes = listSingle.getString("subject_name");
            if (subjectName.equals(subjectNameRes)) {
                isExistRes = true;
                subjectId = listSingle.getString("subject_id");
            }
        }

        Assert.assertEquals(isExistRes, true, "存在与否错误");
        return subjectId;
    }

    public void checkGetSubject(String response, String subjectTypeName, String subjectName, String local, String manager, String phone) {
        JSONObject data = JSON.parseObject(response).getJSONObject("data");
        String sujectNameRes = data.getString("subject_name");
        Assert.assertEquals(sujectNameRes, subjectName, "sujectNameRes is wrong！");

        String subjectTypeNameRes = data.getString("type_name");
        Assert.assertEquals(subjectTypeNameRes, subjectTypeName, "subjectType is wrong！");

        String localRes = data.getString("local");
        Assert.assertEquals(localRes, local, "local is wrong！");

        String managerRes = data.getString("manager");
        Assert.assertEquals(managerRes, manager, "manager is wrong！");

        String phoneRes = data.getString("telephone");
        Assert.assertEquals(phoneRes, phone, "phone is wrong！");
    }

    public void checkListSubject(String response, String subjectId, String subjectName,
                                 String local, String manager, String phone, boolean isExist) {
        boolean isExistRes = false;
        JSONObject data = JSON.parseObject(response).getJSONObject("data");
        JSONArray list = data.getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            JSONObject listSingle = list.getJSONObject(i);
            String subjectIdRes = listSingle.getString("subject_id");
            if (subjectId.equals(subjectIdRes)) {
                isExistRes = true;
                String sujectNameRes = listSingle.getString("subject_name");
                Assert.assertEquals(sujectNameRes, subjectName, "sujectNameRes is wrong！");

                String localRes = listSingle.getString("local");
                Assert.assertEquals(localRes, local, "local is wrong！");

                String managerRes = listSingle.getString("manager");
                Assert.assertEquals(managerRes, manager, "manager is wrong！");

                String phoneRes = listSingle.getString("telephone");
                Assert.assertEquals(phoneRes, phone, "phone is wrong！");
            }
        }

        Assert.assertEquals(isExistRes, isExist, "存在与否错误");
    }

    public String getAppIdByList(String response, String appName) {
        String appId = "";
        boolean isExist = false;
        JSONObject data = JSON.parseObject(response).getJSONObject("data");
        JSONArray list = data.getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            JSONObject listSingle = list.getJSONObject(i);
            String appNameRes = listSingle.getString("name");
            if (appName.equals(appNameRes)) {
                isExist = true;
                appId = listSingle.getString("app_id");
            }
        }

        Assert.assertEquals(isExist, true, "app不存在");
        return appId;
    }

    public String getBrandIdByList(String response, String brandName) {
        String brandId = "";
        boolean isExist = false;
        JSONObject data = JSON.parseObject(response).getJSONObject("data");
        JSONArray list = data.getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            JSONObject listSingle = list.getJSONObject(i);
            String brandNameRes = listSingle.getString("brand_name");
            if (brandName.equals(brandNameRes)) {
                isExist = true;
                brandId = listSingle.getString("brand_id");
            }
        }

        Assert.assertEquals(isExist, true, "不存在该brandId");
        return brandId;
    }

    public void checkBrandList(String response, String brandId, String brandName, String manager, String phone, boolean isExist) {
        boolean isExistRes = false;
        JSONObject data = JSON.parseObject(response).getJSONObject("data");
        JSONArray list = data.getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            JSONObject listSingle = list.getJSONObject(i);
            String brandIdRes = listSingle.getString("brand_id");
            if (brandId.equals(brandIdRes)) {
                isExistRes = true;

                String brandNameRes = listSingle.getString("brand_name");
                Assert.assertEquals(brandNameRes, brandName, "brandName is wrong!");

                String managerRes = listSingle.getString("manager");
                Assert.assertEquals(managerRes, manager, "manager is wrong!");

                String phoneRes = listSingle.getString("telephone");
                Assert.assertEquals(phoneRes, phone, "phone is wrong");
            }
        }

        Assert.assertEquals(isExistRes, isExist, "brandId 存在与否错误！");
    }

    public void checkGetBrand(String response, String brandName, String manager, String phone) {
        JSONObject data = JSON.parseObject(response).getJSONObject("data");
        String brandNameRes = data.getString("brand_name");
        Assert.assertEquals(brandNameRes, brandName, "brandName is wrong！");

        String managerRes = data.getString("manager");
        Assert.assertEquals(managerRes, manager, "manager is wrong！");

        String phoneRes = data.getString("telephone");
        Assert.assertEquals(phoneRes, phone, "phone is wrong！");
    }

    public void checkGetApp(String response, String appName) {
        JSONObject data = JSON.parseObject(response).getJSONObject("data");
        String appNameRes = data.getString("name");
        Assert.assertEquals(appNameRes, appName, "appName is wrong！");
    }

    public void checkListApp(String response, String appId, String appName, boolean isExist) {
        boolean isExistRes = false;
        JSONObject data = JSON.parseObject(response).getJSONObject("data");
        JSONArray list = data.getJSONArray("list");
        for (int i = 0; i < list.size(); i++) {
            JSONObject listSingle = list.getJSONObject(i);
            String appIdRes = listSingle.getString("app_id");
            if (appId.equals(appIdRes)) {
                isExistRes = true;

                String appNameRes = listSingle.getString("name");
                Assert.assertEquals(appNameRes, appName, "appName is wrong!");
            }
        }

        Assert.assertEquals(isExistRes, isExist, "brandId 存在与否错误！");
    }

    private void checkCode(String response, int expect, String message) {
        int code = JSON.parseObject(response).getInteger("code");
        Assert.assertEquals(code, expect, message);
    }

    private String sendRequestWithHeader(String serviceId, String json, HashMap header) throws Exception {
        HttpExecutorUtil executor = new HttpExecutorUtil();
        executor.doPostJsonWithHeaders(URL_prefix + serviceId, json, header);
        return executor.getResponse();
    }

    private String sendRequestWithUrl(String url, String json, HashMap header) throws Exception {
        HttpExecutorUtil executor = new HttpExecutorUtil();
        executor.doPostJsonWithHeaders(url, json, header);
        return executor.getResponse();
    }

    private String sendRequestWithUrlMultipart(String url, JSONObject json, HashMap header) throws Exception {
        HttpExecutorUtil executor = new HttpExecutorUtil();
//        executor.doPostJsonWithHeadersMultipart(url, json, header);
        return executor.getResponse();
    }

    public void setBasicParaToDB(Case aCase, String caseName, String caseDesc, String ciCaseName) {
        aCase.setApplicationId(APP_ID);
        aCase.setConfigId(CONFIG_ID);
        aCase.setCaseName(caseName);
        aCase.setCaseDescription(caseDesc);
        aCase.setCiCmd(CI_CMD + ciCaseName);
        aCase.setQaOwner("廖祥茹");
        aCase.setExpect("code==1000");
    }

    private void genAuth() {
        logger.info("\n");
        logger.info("-------------------------gen authrization!-----------------------");
        String json =
                "{" +
                        "  \"email\": \"1400893423@qq.com\"," +
                        "  \"password\": \"2a2d705f837ad4b895b3d50965a6e1f7\"" +
                        "}";
        try {
            response = sendRequestWithUrl(genAuthURL, json, header);
            JSONObject data = JSON.parseObject(response).getJSONObject("data");
            authorization = data.getString("token");

            header.put("Authorization", authorization);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @BeforeSuite
    public void initial() throws Exception {
        genAuth();
        qaDbUtil.openConnection();
    }

    @AfterSuite
    public void clean() {
        qaDbUtil.closeConnection();
    }
}
