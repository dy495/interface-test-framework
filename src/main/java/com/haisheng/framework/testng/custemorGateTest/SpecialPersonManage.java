package com.haisheng.framework.testng.custemorGateTest;

import ai.winsense.ApiClient;
import ai.winsense.common.Credential;
import ai.winsense.constant.SdkConstant;
import ai.winsense.model.ApiRequest;
import ai.winsense.model.ApiResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.model.bean.Case;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.LogMine;
import com.haisheng.framework.util.ImageUtil;
import com.haisheng.framework.util.QADbUtil;
import com.haisheng.framework.util.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.UUID;

/**
 * 线下消费者接口测试
 *
 * @author Shine
 */
public class SpecialPersonManage {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private LogMine logMine = new LogMine(logger);

    private static String UID = "uid_7fc78d24";
    private static String APP_ID = "097332a388c2";
    private static String SHOP_ID = "8";
    String AK = "77327ffc83b27f6d";
    String SK = "7624d1e6e190fbc381d0e9e18f03ab81";

    int step = 0;
    private String failReason = "";
    private QADbUtil qaDbUtil = new QADbUtil();
    private int APP_ID_DB = ChecklistDbInfo.DB_APP_ID_CLOUD_SERVICE;
    private int CONFIG_ID = ChecklistDbInfo.DB_SERVICE_ID_CUSTOMER_DATA_SERVICE;
    private String CI_CMD = "curl -X POST http://liaoxiangru:liaoxiangru@192.168.50.2:8080/job/special-person-management/buildWithParameters?case_name=";

    private String vipGroup = "vipGroup";
    private String vipUser = "00000";
    private String queryGrpGrp = "queryGrpGrp";//测试queryGroupTestIsSuccess专用组
    private ApiResponse apiResponse = null;
    private static String fromGrpName = "fromGrp";
    private static String fromUserId = "fromUser";
    private static String toGrpName = "toGrp";
    private static String toUserId = "toUser";
    private static String isCheckSame = "true";

    String ROUTER_REGISTER = "/scenario/gate/SYSTEM_REGISTER_FACE/v1.0";
//    String ROUTER_QUERY_GROUP = "/scenario/gate/SYSTEM_QUERY_GROUP/v1.0";
    String ROUTER_QUERY_USER = "/scenario/gate/SYSTEM_QUERY_USER/v1.0";
    String ROUTER_SEARCH_FACE = "/scenario/gate/SYSTEM_SEARCH_FACE/v1.0";
    String ROUTER_DELETE_USER = "/scenario/gate/SYSTEM_DELETE_USER/v1.0";
    String ROUTER_DELETE_FACE = "/scenario/gate/SYSTEM_DELETE_FACE/v1.0";
    String ROUTER_CHANGE_USER = "/scenario/gate/SYSTEM_CHANGE_USER/v1.0";

    private String[] faceIdArray = {
            "789c568fbc268a4c4e017fd9573247ba",
            "e6a0d55b54ec409737bbeb3ac2c2a590",
            "f9a5bc5800976b35e7e64cdba14f7a23",
            "11c13f7bdaf12d619653c9d08dfc6c03"
    };
    private String[] picPathArr =
            {"src/main/resources/test-res-repo/customer-gateway/1.jpg",
                    "src/main/resources/test-res-repo/customer-gateway/3.jpg",
                    "src/main/resources/test-res-repo/customer-gateway/4.jpg",
                    "src/main/resources/test-res-repo/customer-gateway/5.jpg"};
    private String vipPic = "src/main/resources/test-res-repo/customer-gateway/1.jpg";
    private String[] userIdArr =
            {
                    "user1", "user2", "user3", "user4", "user5"
            };
    //1.jpg是通用的默认图片

    public ApiResponse registerFace(String grpName, String userId, String picPath, int expectCode, Case acase, int step) throws Exception {
        logger.info("------------------------register Face---------------------------------------");
        ImageUtil imageUtil = new ImageUtil();
        String[] resource = new String[]{imageUtil.getImageBinary(picPath)};
        String json =
                "{\"group_name\":\"" + grpName + "\"," +
                        "\"user_id\":\"" + userId + "\"," +
                        "\"is_quality_limit\":\"false\"," +
                        "\"pic_url\":\"@0\"" +
                        "}";
        apiResponse = sendRequest(ROUTER_REGISTER, resource, json);
        sendResAndReqIdToDbApi(apiResponse, acase, step);
        checkCode(apiResponse, ROUTER_REGISTER, expectCode);

        return apiResponse;
    }

    public ApiResponse registerFaceWithShopUser(String grpName, String userId, String picPath, int expectCode, Case acase, int step) throws Exception {
        logger.info("------------------------register Face---------------------------------------");
        ImageUtil imageUtil = new ImageUtil();
        String[] resource = new String[]{imageUtil.getImageBinary(picPath)};
        String json = "{" +
                "\"group_name\":\"" + grpName + "\"," +
                "\"user_id\":\"" + userId + "\"," +
                "\"pic_url\":\"@0\"," +
//                "\"is_quality_limit\":\"true\"," +
                "\"shop_user\":{" +
                "\"134\":[" +
                "{" +
                "\"user_id\":\"00001\"," +
                "\"group_name\":\"" + grpName + "\"" +
                "}" +
                "]" +
                "}" +

                "}";
        apiResponse = sendRequest(ROUTER_REGISTER, resource, json);

        sendResAndReqIdToDbApi(apiResponse, acase, step);

        checkCode(apiResponse, ROUTER_REGISTER, expectCode);

        return apiResponse;
    }

//    public ApiResponse queryGroup(String grpName, int expectCode, Case acase, int step) throws Exception {
//        logger.info("------------------------query group---------------------------------------");
//        logger.info("queryGroup ");
//        String[] resource = new String[]{};
//        String json =
//                "{" +
//                        "\"group_name\":\"" + grpName + "\"" +
//                        "}";
//        apiResponse = sendRequest(ROUTER_QUERY_GROUP, resource, json);
//        checkCode(apiResponse, ROUTER_QUERY_GROUP, expectCode);
//
//        sendResAndReqIdToDbApi(apiResponse, acase, step);
//
//        return apiResponse;
//    }

//    public ApiResponse queryGroupTestHeadPara(String router, String uid, String appid, String version, int expectCode, Case acase, int step) throws Exception {
//        logger.info("------------------------query group test header---------------------------------------");
//        String[] resource = new String[]{};
//        String json =
//                "{" +
//                        "\"group_name\":\"" + vipGroup + "\"" +
//                        "}";
//        apiResponse = sendRequestHeadPara(router, resource, json, uid, appid, version);
//        sendResAndReqIdToDbApi(apiResponse, acase, step);
//        checkCode(apiResponse, ROUTER_QUERY_GROUP, expectCode);
//
//        return apiResponse;
//    }

    public ApiResponse searchFace(String grpName, String picPath, String resultNum, int expectCode, Case acase, int step) throws Exception {
        logger.info("------------------------search face---------------------------------------");
        ImageUtil imageUtil = new ImageUtil();
        String[] resource = new String[]{imageUtil.getImageBinary(picPath)};
        String json =
                "{" +
                        "\"group_name\":\"" + grpName + "\"," +
                        "\"pic_url\":\"@0\"," +
                        "\"result_num\":\"" + resultNum + "\"" +
                        "}";

        apiResponse = sendRequest(ROUTER_SEARCH_FACE, resource, json);
        sendResAndReqIdToDbApi(apiResponse, acase, step);
        checkCode(apiResponse, ROUTER_SEARCH_FACE, expectCode);

        return apiResponse;
    }

    public ApiResponse queryUser(String grpName, String userId, int expectCode, Case acase, int step) throws Exception {
        logger.info("------------------------query user---------------------------------------");
        String[] resource = new String[]{};
        String json =
                "{" +
                        "\"group_name\":\"" + grpName + "\"," +
                        "\"user_id\":\"" + userId + "\"" +
                        "}";
        try {

            apiResponse = sendRequest(ROUTER_QUERY_USER, resource, json);
            sendResAndReqIdToDbApi(apiResponse, acase, step);
            checkCode(apiResponse, ROUTER_QUERY_USER, expectCode);

        } catch (Exception e) {
            throw e;
        }

        return apiResponse;
    }

    public void deleteUser(String grpName, String userId, int expectCode, Case acase, int step) throws Exception {
        logger.info("------------------------delete user---------------------------------------");
        String[] resource = new String[]{};
        String json =
                "{" +
                        "\"group_name\":\"" + grpName + "\"," +
                        "\"user_id\":\"" + userId + "\"" +
                        "}";
        apiResponse = sendRequest(ROUTER_DELETE_USER, resource, json);
        sendResAndReqIdToDbApi(apiResponse, acase, step);
        checkCode(apiResponse, ROUTER_DELETE_USER, expectCode);
    }

    public void deleteFace(String grpName, String userId, String faceId, int expectCode, Case acase, int step) throws Exception {
        logger.info("------------------------delete face---------------------------------------");
        String[] resource = new String[]{};
        String json =
                "{" +
                        "\"group_name\":\"" + grpName + "\"," +
                        "\"user_id\":\"" + userId + "\"," +
                        "\"face_id\":\"" + faceId + "\"" +
                        "}";
        apiResponse = sendRequest(ROUTER_DELETE_FACE, resource, json);
        sendResAndReqIdToDbApi(apiResponse, acase, step);
        checkCode(apiResponse, ROUTER_DELETE_FACE, expectCode);
    }

    public void changeUser(String shopId, String fGrp, String fUser, String toGrp, String toUser, String isCheckSame, int expectCode, Case acase, int step) throws Exception {
        logger.info("------------------------change user---------------------------------------");
        String[] resource = new String[]{};
        String json =
                "{" +
                        "\"shop_id\":\"" + shopId + "\"," +
                        "\"from_group_name\":\"" + fGrp + "\"," +
                        "\"from_user_id\":\"" + fUser + "\"," +
                        "\"to_group_name\":\"" + toGrp + "\"," +
                        "\"to_user_id\":\"" + toUser + "\"," +
                        "\"is_check_same\":\"" + isCheckSame + "\"" +
                        "}";

        apiResponse = sendRequest(ROUTER_CHANGE_USER, resource, json);
        sendResAndReqIdToDbApi(apiResponse, acase, step);
        checkCode(apiResponse, ROUTER_CHANGE_USER, expectCode);
    }

    @Test
    public void registerFaceTestDS() throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        Case aCase = new Case();
        String caseDesc = "验证注册人脸的数据结构";
        failReason = "";
        step = 0;

        try {
            aCase.setRequestData("1、注册人脸；2、查看返回的数据结构" + "\n");
            aCase.setExpect("code==1000，数据结构与文档中的一致");

            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            apiResponse = registerFace(vipGroup, vipUser, vipPic, StatusCode.SUCCESS, aCase, step);

            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            checkRegisterDS(apiResponse, ROUTER_REGISTER);
            aCase.setResult("PASS"); //FAIL, PASS
        } catch (Exception e) {
            e.printStackTrace();
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            throw e;
        } finally {
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);
            qaDbUtil.saveToCaseTable(aCase);
        }
    }

    @Test(dataProvider = "BAD_GRP_NAME", priority = 1)
    public void registerFaceTestBadGrpName(String grpName) throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + "--" + grpName;
        Case aCase = new Case();
        String caseDesc = "注册人脸--验证无效组名是否注册成功。groupName：" + grpName;
        failReason = "";
        step = 0;

        try {
            aCase.setRequestData("用不同的无效组名注册" + "\n");
            aCase.setExpect("code==1001");

            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            apiResponse = registerFace(grpName, vipUser, vipPic, StatusCode.BAD_REQUEST, aCase, step);
            aCase.setResult("PASS"); //FAIL, PASS
        } catch (Exception e) {
            e.printStackTrace();
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            throw e;
        } finally {
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);
            qaDbUtil.saveToCaseTable(aCase);
        }
    }

    @Test(dataProvider = "BAD_USER_ID", priority = 1)
    public void registerFaceTestBadUserId(String userId) throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + "--" + userId;
        Case aCase = new Case();
        String caseDesc = "注册人脸--验证无效的userId。userId：" + userId;
        failReason = "";
        step = 0;

        try {

            aCase.setRequestData("用不同的无效user_id注册" + "\n");
            aCase.setExpect("code==1001");

            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            apiResponse = registerFace(vipGroup, userId, vipPic, StatusCode.BAD_REQUEST, aCase, step);
            aCase.setResult("PASS"); //FAIL, PASS
        } catch (Exception e) {
            e.printStackTrace();
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            throw e;
        } finally {
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);
            qaDbUtil.saveToCaseTable(aCase);
        }
    }

    @Test(priority = 1)
    public void registerFaceTestShopUser() throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        Case aCase = new Case();
        String caseDesc = "注册人脸--验证shop_user参数。";
        failReason = "";
        step = 0;

        try {
            aCase.setRequestData("用带shopUser参数的接送注册人脸" + "\n");
            aCase.setExpect("code==1000");

//            1、注册shopUser中的人物
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            apiResponse = registerFace(vipGroup, "00001", picPathArr[0], StatusCode.SUCCESS, aCase, step);

//            2、注册
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            apiResponse = registerFaceWithShopUser(vipGroup, vipUser, vipPic, StatusCode.SUCCESS, aCase, step);
            aCase.setResult("PASS"); //FAIL, PASS
        } catch (Exception e) {
            e.printStackTrace();
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            throw e;
        } finally {
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);
            qaDbUtil.saveToCaseTable(aCase);
        }
    }

//------------------------------the above are the cases for register face-------------------------------------------------
// -----------------------------here are the cases for query group--------------------------------------------------------

//    @Test(dataProvider = "BAD_UID", priority = 2)
//    public void TestUIDWithoutEmpty(String uid) throws Exception {
//
//        String ciCaseName = new Object() {
//        }
//                .getClass()
//                .getEnclosingMethod()
//                .getName();
//        String caseName = ciCaseName + "--" + uid;
//        Case aCase = new Case();
//        String caseDesc = "测试无效的UID。UID: " + uid;
//        failReason = "";
//        step = 0;
//
//        try {
//            aCase.setRequestData("用无效的UID查询组");
//            aCase.setExpect("code==2001");
//            logger.info("\n\n");
//            logger.info("--------------------------------（" + (++step) + ")------------------------------");
//            apiResponse = queryGroupTestHeadPara(ROUTER_QUERY_GROUP, uid, APP_ID, SdkConstant.API_VERSION, StatusCode.UN_AUTHORIZED, aCase, step);
//            aCase.setResult("PASS"); //FAIL, PASS
//        } catch (Exception e) {
//            e.printStackTrace();
//            failReason += e.getMessage();
//            aCase.setFailReason(failReason);
//            Assert.fail(failReason);
//            throw e;
//        } finally {
//            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);
//            qaDbUtil.saveToCaseTable(aCase);
//        }
//    }

//    @Test(dataProvider = "EMPTY_PARA", priority = 2)
//    public void TestUIDEmpty(String uid) throws Exception {
//
//        String ciCaseName = new Object() {
//        }
//                .getClass()
//                .getEnclosingMethod()
//                .getName();
//        String caseName = ciCaseName + "--" + uid;
//        Case aCase = new Case();
//        String caseDesc = "测试空的UID";
//        failReason = "";
//        step = 0;
//
//        try {
//            aCase.setRequestData("用空的UID查询组");
//            aCase.setExpect("code==1001");
//            logger.info("\n\n");
//            logger.info("--------------------------------（" + (++step) + ")------------------------------");
//            apiResponse = queryGroupTestHeadPara(ROUTER_QUERY_GROUP, uid, APP_ID, SdkConstant.API_VERSION, StatusCode.BAD_REQUEST, aCase, step);
//
//            aCase.setResult("PASS"); //FAIL, PASS
//        } catch (Exception e) {
//            e.printStackTrace();
//            failReason += e.getMessage();
//            aCase.setFailReason(failReason);
//            Assert.fail(failReason);
//            throw e;
//        } finally {
//            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);
//            qaDbUtil.saveToCaseTable(aCase);
//        }
//    }

//    @Test(dataProvider = "BAD_APPID", priority = 2)
//    public void TestAppidWithoutEmpty(String appid) throws Exception {
//
//        String ciCaseName = new Object() {
//        }
//                .getClass()
//                .getEnclosingMethod()
//                .getName();
//        String caseName = ciCaseName + "--" + appid;
//        Case aCase = new Case();
//        String caseDesc = "测试无效的appid";
//        failReason = "";
//        step = 0;
//
//        try {
//            aCase.setRequestData("用无效的APPID查询组");
//            aCase.setExpect("code==2001");
//            logger.info("\n\n");
//            logger.info("--------------------------------（" + (++step) + ")------------------------------");
//            int expectCode = StatusCode.UN_AUTHORIZED;
//            apiResponse = queryGroupTestHeadPara(ROUTER_QUERY_GROUP, UID, appid, SdkConstant.API_VERSION, expectCode, aCase, step);
//            aCase.setResult("PASS"); //FAIL, PASS
//        } catch (Exception e) {
//            e.printStackTrace();
//            failReason += e.getMessage();
//            aCase.setFailReason(failReason);
//            Assert.fail(failReason);
//            throw e;
//        } finally {
//            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);
//            qaDbUtil.saveToCaseTable(aCase);
//        }
//    }

//    @Test(dataProvider = "EMPTY_PARA", priority = 2)
//    public void TestAppidEmpty(String appid) throws Exception {
//
//        String ciCaseName = new Object() {
//        }
//                .getClass()
//                .getEnclosingMethod()
//                .getName();
//        String caseName = ciCaseName + "--" + appid;
//        Case aCase = new Case();
//        String caseDesc = "测试空的APPID";
//        failReason = "";
//        step = 0;
//
//        try {
//            aCase.setRequestData("用空的APPID查询组");
//            aCase.setExpect("code==1001");
//            logger.info("\n\n");
//            logger.info("--------------------------------（" + (++step) + ")------------------------------");
//            int expectCode = StatusCode.BAD_REQUEST;
//            apiResponse = queryGroupTestHeadPara(ROUTER_QUERY_GROUP, UID, appid, SdkConstant.API_VERSION, expectCode, aCase, step);
//
//            aCase.setResult("PASS"); //FAIL, PASS
//        } catch (Exception e) {
//            e.printStackTrace();
//            failReason += e.getMessage();
//            aCase.setFailReason(failReason);
//            Assert.fail(failReason);
//            throw e;
//        } finally {
//            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);
//            qaDbUtil.saveToCaseTable(aCase);
//        }
//    }

//    @Test(dataProvider = "BAD_VERSION")
//    public void queryGroupTestBadVersion(String version) throws Exception {
//
//        String ciCaseName = new Object() {
//        }
//                .getClass()
//                .getEnclosingMethod()
//                .getName();
//        String caseName = ciCaseName + "--" + version;
//        Case aCase = new Case();
//        String caseDesc = "测试无效的version";
//        failReason = "";
//        step = 0;
//
//        try {
//            aCase.setRequestData("用无效的version查询组");
//            aCase.setExpect("code==1001");
//            logger.info("\n\n");
//            logger.info("--------------------------------（" + (++step) + ")------------------------------");
//            int expectCode = StatusCode.BAD_REQUEST;
//            apiResponse = queryGroupTestHeadPara(ROUTER_QUERY_GROUP, UID, APP_ID, version, expectCode, aCase, step);
//
//            aCase.setResult("PASS"); //FAIL, PASS
//        } catch (Exception e) {
//            e.printStackTrace();
//            failReason += e.getMessage();
//            aCase.setFailReason(failReason);
//            Assert.fail(failReason);
//            throw e;
//        } finally {
//            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);
//            qaDbUtil.saveToCaseTable(aCase);
//        }
//    }

//    @Test(dataProvider = "GOOD_VERSION")
//    public void queryGroupTestGoodVersion(String version) throws Exception {
//
//        String ciCaseName = new Object() {
//        }
//                .getClass()
//                .getEnclosingMethod()
//                .getName();
//        String caseName = ciCaseName + "--" + version;
//        Case aCase = new Case();
//        String caseDesc = "测试有效的version";
//        failReason = "";
//        step = 0;
//
//        try {
//            aCase.setRequestData("用有效的version查询组");
//            aCase.setExpect("code==1000");
//            logger.info("\n\n");
//            logger.info("--------------------------------（" + (++step) + ")------------------------------");
//            int expectCode = StatusCode.SUCCESS;
//            apiResponse = queryGroupTestHeadPara(ROUTER_QUERY_GROUP, UID, APP_ID, version, expectCode, aCase, step);
//            aCase.setResult("PASS"); //FAIL, PASS
//        } catch (Exception e) {
//            e.printStackTrace();
//            failReason += e.getMessage();
//            aCase.setFailReason(failReason);
//            Assert.fail(failReason);
//            throw e;
//        } finally {
//            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);
//            qaDbUtil.saveToCaseTable(aCase);
//        }
//    }

    /**
     * @Description: 2.4 测试组名（用的是查询和删除专用的dataProvider）
     * @Param: [grpName]
     * @return: void
     * @Author: Shine
     * @Date: 2019/4/9
     */
//    @Test(dataProvider = "BAD_GRP_NAME", priority = 2)
//    public void queryGroupTestGroupName(String grpName) throws Exception {
//
//        String ciCaseName = new Object() {
//        }
//                .getClass()
//                .getEnclosingMethod()
//                .getName();
//        String caseName = ciCaseName + "--" + grpName;
//        Case aCase = new Case();
//        String caseDesc = "测试无效的group_name";
//        failReason = "";
//        step = 0;
//
//        try {
//            aCase.setRequestData("用无效的group_name查询组");
//            aCase.setExpect("code==1001");
//            logger.info("\n\n");
//            logger.info("--------------------------------（" + (++step) + ")------------------------------");
//            int expectCode = StatusCode.BAD_REQUEST;
//            apiResponse = queryGroup(grpName, expectCode, aCase, step);
//            aCase.setResult("PASS"); //FAIL, PASS
//        } catch (Exception e) {
//            e.printStackTrace();
//            failReason += e.getMessage();
//            aCase.setFailReason(failReason);
//            Assert.fail(failReason);
//            throw e;
//        } finally {
//            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);
//            qaDbUtil.saveToCaseTable(aCase);
//        }
//    }

//    @Test
//    public void QueryGroupWithNewGroup() throws Exception {
//
//        String ciCaseName = new Object() {
//        }
//                .getClass()
//                .getEnclosingMethod()
//                .getName();
//        String caseName = ciCaseName;
//        Case aCase = new Case();
//        String caseDesc = "测试新的组名";
//        failReason = "";
//        step = 0;
//
//        String newGroup = String.valueOf(System.currentTimeMillis());
//
//        try {
//            aCase.setRequestData("用一个新的组名查询组");
//            aCase.setExpect("code==1000");
//
////            -----------------------------------------------(1)---------------------------------------------
//            logger.info("\n\n");
//            logger.info("--------------------------------（" + (++step) + ")------------------------------");
//            registerFace(newGroup, vipUser, vipPic, StatusCode.SUCCESS, aCase, step);
//
////            --------------------------------------------------------(2)---------------------------------------
//            logger.info("\n\n");
//            logger.info("--------------------------------（" + (++step) + ")------------------------------");
//            apiResponse = queryGroup(newGroup, StatusCode.SUCCESS, aCase, step);
//            aCase.setResult("PASS"); //FAIL, PASS
//        } catch (Exception e) {
//            e.printStackTrace();
//            failReason += e.getMessage();
//            aCase.setFailReason(failReason);
//            Assert.fail(failReason);
//            throw e;
//        } finally {
//            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);
//            qaDbUtil.saveToCaseTable(aCase);
//        }
//    }

//    @Test(priority = 2)
//    public void queryGroupTestIsSuccess() throws Exception {
//
//        String ciCaseName = new Object() {
//        }
//                .getClass()
//                .getEnclosingMethod()
//                .getName();
//        String caseName = ciCaseName;
//        Case aCase = new Case();
//        String caseDesc = "验证查询组的功能";
//        failReason = "";
//        step = 0;
//
//        try {
//            aCase.setRequestData("1-5、先注册5个人脸; 6、查询组;7、验证该组中是不是有5个人;8-12、删除人脸" + "\n");
//            aCase.setExpect("code==1000");
//
//            for (int i = 0; i < userIdArr.length; i++) {
//                logger.info("\n\n");
//                logger.info("--------------------------------（" + (++step) + ")------------------------------");
//                registerFace(queryGrpGrp, userIdArr[i], vipPic, StatusCode.SUCCESS, aCase, step);
//            }
//
//            logger.info("\n\n");
//            logger.info("--------------------------------（" + (++step) + ")------------------------------");
//            apiResponse = queryGroup(queryGrpGrp, StatusCode.SUCCESS, aCase, step);
//
//            logger.info("\n\n");
//            logger.info("--------------------------------（" + (++step) + ")------------------------------");
//            int userNum = checkQueryGrpResult(apiResponse, ROUTER_QUERY_GROUP);
//            if (userNum != 5) {
//                String msg = "Query user failed! The number is wrong!" +
//                        "groupName: " + queryGrpGrp;
//                throw new Exception(msg);
//            }
//            aCase.setResult("PASS"); //FAIL, PASS
//        } catch (Exception e) {
//            e.printStackTrace();
//            failReason += e.getMessage();
//            aCase.setFailReason(failReason);
//            Assert.fail(failReason);
//            throw e;
//        } finally {
//            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);
//            qaDbUtil.saveToCaseTable(aCase);
//        }
//    }

//    @Test
//    public void queryGroupTestDS() throws Exception {
//
//        String ciCaseName = new Object() {
//        }
//                .getClass()
//                .getEnclosingMethod()
//                .getName();
//        String caseName = ciCaseName;
//        Case aCase = new Case();
//        String caseDesc = "验证“特定人物库查询”的response结构";
//        failReason = "";
//        step = 0;
//
//        try {
//
//            aCase.setRequestData("1、特定人物库查询;2、查看response结构" + "\n");
//            aCase.setExpect("code==1000,并且返回结构与文档一致");
//
//            logger.info("\n\n");
//            logger.info("--------------------------------（" + (++step) + ")------------------------------");
//            apiResponse = queryGroup(vipGroup, StatusCode.SUCCESS, aCase, step);
//            aCase.setResult("PASS"); //FAIL, PASS
//        } catch (Exception e) {
//            e.printStackTrace();
//            failReason += e.getMessage();
//            aCase.setFailReason(failReason);
//            Assert.fail(failReason);
//            throw e;
//        } finally {
//            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);
//            qaDbUtil.saveToCaseTable(aCase);
//        }
//    }

    //----------------------the above are the cases of query group----------------------------------------------------------
    //----------------------here are the cases of query user----------------------------------------------------------

    @Test(dataProvider = "BAD_GRP_NAME", priority = 3)
    public void queryUserTestGroupName(String grpName) throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + "--" + grpName;
        Case aCase = new Case();
        String caseDesc = "“特定人物查询”--验证无效的组名";
        failReason = "";
        step = 0;

        try {

            aCase.setRequestData("用无效组名查询特殊人物" + "\n");
            aCase.setExpect("code==1001");

            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            apiResponse = queryUser(grpName, vipUser, StatusCode.BAD_REQUEST, aCase, step);
            aCase.setResult("PASS"); //FAIL, PASS
        } catch (Exception e) {
            e.printStackTrace();
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            throw e;
        } finally {
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);
            qaDbUtil.saveToCaseTable(aCase);
        }
    }

    @Test(dataProvider = "BAD_USER_ID", priority = 3)
    public void queryUserTestBadUserId(String badUserId) throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + "--" + badUserId;
        Case aCase = new Case();
        String caseDesc = "“特定人物查询”--验证无效的user_id";
        failReason = "";
        step = 0;


        try {

            aCase.setRequestData("用无效的user_id查询特殊人物" + "\n");
            aCase.setExpect("code==1001");

            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            apiResponse = queryUser(vipGroup, badUserId, StatusCode.BAD_REQUEST, aCase, step);
            aCase.setResult("PASS"); //FAIL, PASS
        } catch (Exception e) {
            e.printStackTrace();
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            throw e;
        } finally {
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);
            qaDbUtil.saveToCaseTable(aCase);
        }
    }

    @Test(priority = 3)
    public void queryUserWithNewGroup() throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        Case aCase = new Case();
        String caseDesc = "“特定人物查询”--验证用新的组名是否能查询成功";
        failReason = "";
        int step = 0;

        String newGroup = String.valueOf(System.currentTimeMillis());

        try {
            aCase.setRequestData("用新组查询特殊人物" + "\n");
            aCase.setExpect("code==1000");
            //1、register face
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            registerFace(newGroup, vipUser, vipPic, StatusCode.SUCCESS, aCase, step);
            //2、query user
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            apiResponse = queryUser(newGroup, vipUser, StatusCode.SUCCESS, aCase, step);
            aCase.setResult("PASS"); //FAIL, PASS
        } catch (Exception e) {
            e.printStackTrace();
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            throw e;
        } finally {
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);
            qaDbUtil.saveToCaseTable(aCase);
        }
    }

    //--------------------the above are the cases of query user--------------------------------------------------------
    //--------------------here are the cases of search face--------------------------------------------------------

    @Test(dataProvider = "BAD_GRP_NAME", priority = 4)
    public void searchFaceTestBadGroup(String grpName) throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + "--" + grpName;
        Case aCase = new Case();
        String caseDesc = "“特殊人脸校验（人脸检索）”--测试无效的组名";
        failReason = "";
        step = 0;

        try {
            aCase.setRequestData("用无效组名查询人脸" + "\n");
            aCase.setExpect("code==1001");
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            apiResponse = searchFace(grpName, vipPic, "1", StatusCode.BAD_REQUEST, aCase, step);
            aCase.setResult("PASS"); //FAIL, PASS
        } catch (Exception e) {
            e.printStackTrace();
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            throw e;
        } finally {
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);
            qaDbUtil.saveToCaseTable(aCase);
        }
    }

    @Test(priority = 4)
    public void SearchFaceWithNewGroup() throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        Case aCase = new Case();
        String caseDesc = "“特殊人脸校验（人脸检索）”--测试用新组是否能查询成功";
        failReason = "";
        step = 0;

        String newGroup = String.valueOf(System.currentTimeMillis());
        String picPath = "src/main/resources/test-res-repo/customer-gateway/NewGroup.jpg";
        String userId = newGroup;

        try {
            aCase.setRequestData("用新的组名查询人脸" + "\n");
            aCase.setExpect("code==1000");
            //1、register face
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            registerFace(userId, newGroup, picPath, StatusCode.SUCCESS, aCase, step);
            //2、search face
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            apiResponse = searchFace(newGroup, picPath, "1", StatusCode.SUCCESS, aCase, step);
            aCase.setResult("PASS"); //FAIL, PASS
        } catch (Exception e) {
            e.printStackTrace();
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            throw e;
        } finally {
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);
            qaDbUtil.saveToCaseTable(aCase);
        }
    }

    @Test(priority = 4)
    public void checkFaceURL() throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        Case aCase = new Case();
        String caseDesc = "验证“特殊人脸校验（人脸检索）”和“特定人物查询”返回的faceUrl是否一致";
        failReason = "";
        int step = 0;

        String grpName = "faceUrlGrp";
        String userId = "faceUrlUser";
        String picPath = "src/main/resources/test-res-repo/customer-gateway/compareUrl.jpg";
        try {
            aCase.setRequestData("1、注册人脸；2、查询特殊人物，获取face_url；3、查询人脸，获取face_url；4、比较两个url；5、删除人脸" + "\n");
            aCase.setExpect("code==1000，并且两个face_url一致");
            //1、register face
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            registerFace(grpName, userId, picPath, StatusCode.SUCCESS, aCase, step);

            //2、extract the faceUrl of query user
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            apiResponse = queryUser(grpName, userId, StatusCode.SUCCESS, aCase, step);

            HashMap<String, String> queryResult = getQueryUserResult(apiResponse);
            String userUrl = queryResult.get("faceUrlFirst");
            logger.info("the faceUrl of query user is：" + userUrl);

            //3、extract the faceUrl of search face
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            apiResponse = searchFace(grpName, picPath, "1", StatusCode.SUCCESS, aCase, step);
            HashMap<String, String> searchFaceResult = getSearchFaceResult(apiResponse);
            String faceUrl = searchFaceResult.get("firstFaceUrl");
            logger.info("the faceUrl of search face is：" + faceUrl);

            //4、compare two urls
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            if (userUrl == null && "".equals(userUrl)) {
                String message = "the faceUrl of query user is empty.";
                throw new Exception(message);
            }
            if (faceUrl == null && "".equals(faceUrl)) {
                String message = "the faceUrl of search face is empty.";
                throw new Exception(message);
            }

            if (!userUrl.equals(faceUrl)) {
                String msg = "two faceUrls are different ！" +
                        "groupName: " + grpName +
                        "userId: " + userId +
                        ". the faceUrl of query user is:" + userUrl +
                        ", the faceUrl of search face:" + faceUrl;
                throw new Exception(msg);
            }

            //5、delete face ,clean up the environment
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            deleteUser(grpName, userId, StatusCode.SUCCESS, aCase, step);
            aCase.setResult("PASS"); //FAIL, PASS
        } catch (Exception e) {
            e.printStackTrace();
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            throw e;
        } finally {
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);
            qaDbUtil.saveToCaseTable(aCase);
        }
    }

    @Test(dataProvider = "GOOD_RESULT_NUM", priority = 4)
    public void searchFaceTestGoodResultNum(int resultNum) throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + "--" + resultNum;
        Case aCase = new Case();
        String caseDesc = "“特殊人脸校验（人脸检索）”--验证有效的result_num";
        failReason = "";
        int step = 0;

        String facePath = "src/main/resources/test-res-repo/customer-gateway/1.jpg";

        try {
            aCase.setRequestData("用有效的result_num查询人脸" + "\n");
            aCase.setExpect("code==1000");
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            apiResponse = searchFace("TestGroup", facePath, String.valueOf(resultNum), StatusCode.SUCCESS, aCase, step);
            aCase.setResult("PASS"); //FAIL, PASS
        } catch (Exception e) {
            e.printStackTrace();
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            throw e;
        } finally {
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);
            qaDbUtil.saveToCaseTable(aCase);
        }
    }

    @Test(dataProvider = "BAD_RESULT_NUM", priority = 4)
    public void searchFaceTestBadResultNum(String badResultNum) throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + "--" + badResultNum;
        Case aCase = new Case();
        String caseDesc = "“特殊人脸校验（人脸检索）”--验证无效的result_num";
        failReason = "";
        int step = 0;

        try {
            aCase.setRequestData("用无效result_num查询人脸" + "\n");
            aCase.setExpect("code==1001");
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            apiResponse = searchFace("TestGroup", vipPic, badResultNum, StatusCode.BAD_REQUEST, aCase, step);
            aCase.setResult("PASS"); //FAIL, PASS
        } catch (Exception e) {
            e.printStackTrace();
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            throw e;
        } finally {
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);
            qaDbUtil.saveToCaseTable(aCase);
        }
    }
    //----------------------the above are the cases of search face-------------------------------------------------
    //----------------------here are the cases of delete user-----------------------------------------------------

    @Test(dataProvider = "BAD_GRP_NAME", priority = 5)
    public void deleteUserTestBadGrp(String grpName) throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + "--" + grpName;
        Case aCase = new Case();
        String caseDesc = "“特殊人物删除”--测试无效的组名";
        failReason = "";
        step = 0;

        try {
            aCase.setRequestData("用无效组名删除特殊人物" + "\n");
            aCase.setExpect("code==1001");
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");

            deleteUser(grpName, vipUser, StatusCode.BAD_REQUEST, aCase, step);
            aCase.setResult("PASS"); //FAIL, PASS
        } catch (Exception e) {
            e.printStackTrace();
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            throw e;
        } finally {
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);
            qaDbUtil.saveToCaseTable(aCase);
        }
    }

    @Test(dataProvider = "BAD_USER_ID", priority = 5)
    public void deleteUserTestBadUserId(String badUserId) throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + "--" + badUserId;
        Case aCase = new Case();
        String caseDesc = "“特殊人物删除”--测试无效的user_id";
        failReason = "";
        int step = 0;

        try {
            aCase.setRequestData("用无效user_id删除特殊人物" + "\n");
            aCase.setExpect("code==1001");
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            deleteUser(vipGroup, badUserId, StatusCode.BAD_REQUEST, aCase, step);
            aCase.setResult("PASS"); //FAIL, PASS
        } catch (Exception e) {
            e.printStackTrace();
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            throw e;
        } finally {
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);
            qaDbUtil.saveToCaseTable(aCase);
        }
    }

    @Test(priority = 5)
    public void deleteUserTestReAdd() throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        Case aCase = new Case();
        String caseDesc = "测试特殊人物被删除以后，是否还能重新添加";
        failReason = "";
        step = 0;

        String msg;
        String userId = String.valueOf(System.currentTimeMillis());

        try {
            aCase.setRequestData("1、注册人脸；2、查询特殊人物；3、删除特殊用户；4、查询特殊人物；5、注册人脸；6、查询特殊人物" + "\n");
            aCase.setExpect("code==1001");
            //1、register
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            registerFace(vipGroup, userId, vipPic, StatusCode.SUCCESS, aCase, step);

            //2、query user
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            apiResponse = queryUser(vipGroup, userId, StatusCode.SUCCESS, aCase, step);
            HashMap beforeDeleteResult = getQueryUserResult(apiResponse);
            int beforeDelete = Integer.parseInt((String) beforeDeleteResult.get("faceNum"));

            //3、delete user
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            deleteUser(vipGroup, userId, StatusCode.SUCCESS, aCase, step);

            //4、query user
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            apiResponse = queryUser(vipGroup, userId, StatusCode.SUCCESS, aCase, step);
            HashMap afterDeleteResult = getQueryUserResult(apiResponse);

            int afterDelete = Integer.parseInt((String) afterDeleteResult.get("faceNum"));
            if (beforeDelete == 1 && afterDelete == 0) {
                msg = "delete user succeeed!";
                logger.info(msg);
            } else {
                msg = "delete user failed!"
                        + "group: " + vipGroup
                        + "userid: " + userId;
                throw new Exception(msg);
            }

            //5、register
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            registerFace(vipGroup, userId, vipPic, StatusCode.SUCCESS, aCase, step);

            //6、query user
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            apiResponse = queryUser(vipGroup, userId, StatusCode.SUCCESS, aCase, step);
            HashMap reAddResult = getQueryUserResult(apiResponse);
            int reAdd = Integer.parseInt((String) reAddResult.get("faceNum"));
            if (reAdd == 1) {
                msg = "register successfully after delete user!";
                logger.info(msg);
            } else {
                msg = "register failed after delete user!"
                        + "group: " + vipGroup
                        + "userid: " + userId;
                throw new Exception(msg);
            }
            aCase.setResult("PASS"); //FAIL, PASS
        } catch (Exception e) {
            e.printStackTrace();
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            throw e;
        } finally {
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);
            qaDbUtil.saveToCaseTable(aCase);
        }
    }

    //--------------------------------above are the cases of delete user-----------------------------------
    //--------------------------------here are the cases of delete face-----------------------------------

    @Test(dataProvider = "BAD_GRP_NAME", priority = 6)
    public void deleteFaceTestBadGrp(String grpName) throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + "--" + grpName;
        Case aCase = new Case();
        String caseDesc = "“特殊人脸删除”--验证无效的组名";
        failReason = "";
        step = 0;

        String faceId = "ee4a2829872770e8e1ee8e1f82e3324a";

        try {

            aCase.setRequestData("用无效的组名删除人脸" + "\n");
            aCase.setExpect("code==1001");

            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            deleteFace(grpName, vipUser, faceId, StatusCode.BAD_REQUEST, aCase, step);
            aCase.setResult("PASS"); //FAIL, PASS
        } catch (Exception e) {
            e.printStackTrace();
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            throw e;
        } finally {
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);
            qaDbUtil.saveToCaseTable(aCase);
        }
    }

    @Test(dataProvider = "BAD_USER_ID", priority = 6)
    public void deleteFaceTestBadUserId(String badUserId) throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName + "--" + badUserId;
        Case aCase = new Case();
        String caseDesc = "“特殊人脸删除”--验证无效的user_id";
        failReason = "";
        int step = 0;

        String faceId = "ee4a2829872770e8e1ee8e1f82e3324a";

        try {

            aCase.setRequestData("用无效user_id删除人脸" + "\n");
            aCase.setExpect("code==1001");
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            deleteFace(vipGroup, badUserId, faceId, StatusCode.BAD_REQUEST, aCase, step);
            aCase.setResult("PASS"); //FAIL, PASS
        } catch (Exception e) {
            e.printStackTrace();
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            throw e;
        } finally {
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);
            qaDbUtil.saveToCaseTable(aCase);
        }
    }

    /**
     * @Description: 6.4 特殊人脸删除，测试多个功能。
     * （注册-查询-删除一张-查询-全部删除-再次删除-注册-查询）
     * 此case可以测试（1）删除一张是否成功，
     * （2）以及删除多张是否成功
     * （3）删除后是否可以重新注册
     * （4）是否可以删除两次
     * @Param: []
     * @return: void
     * @Author: Shine
     * @Date: 2019/4/9
     */
    @Test(priority = 6)
    public void deleteFaceTestReAdd() throws Exception {

        String ciCaseName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        String caseName = ciCaseName;
        Case aCase = new Case();
        String caseDesc = "验证“特殊人脸删除”以后，是否还能重新添加";
        failReason = "";
        step = 0;

        int faceIdArrLen = faceIdArray.length;
        String userId = String.valueOf(System.currentTimeMillis());
        String msg;
        try {
            aCase.setRequestData("1、注册人脸-2、查询人物-3、删除人脸一张-4、查询用户-5、全部删除图片" +
                    "6、查询特殊人物-7、再次删除人脸-8、注册-9、查询特殊人物" + "\n");
            aCase.setExpect("code==1001");
            //1、register five pictures
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            for (int i = 0; i < picPathArr.length; i++) {
                registerFace(vipGroup, userId, picPathArr[i], StatusCode.SUCCESS, aCase, step);
            }
            //2、query user before delete face.
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            apiResponse = queryUser(vipGroup, userId, StatusCode.SUCCESS, aCase, step);
            HashMap beforeDeleteResult = getQueryUserResult(apiResponse);
            String faceIdConcat = (String) beforeDeleteResult.get("faceIdConcat");
            int index;
            for (index = 0; index < faceIdArrLen; index++) {
                if (!faceIdConcat.contains(faceIdArray[index])) {
                    msg = "search face failed!"
                            + "group: " + vipGroup
                            + "userid: " + userId;
                    throw new Exception(msg);
                }
            }

            //3、delete one picture
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            deleteFace(vipGroup, userId, faceIdArray[0], StatusCode.SUCCESS, aCase, step);

            //4、query user
            apiResponse = queryUser(vipGroup, userId, StatusCode.SUCCESS, aCase, step);
            HashMap afterDeleteOneResult = getQueryUserResult(apiResponse);
            String AfterDeleteOnefaceIdConcat = (String) afterDeleteOneResult.get("faceIdConcat");
            if (AfterDeleteOnefaceIdConcat.contains(faceIdArray[0])) {
                msg = "delete one picture failed!"
                        + "group: " + vipGroup
                        + "userid: " + userId
                        + "faceId: " + faceIdArray[0];
                throw new Exception(msg);
            }

            //5、delete all pictures
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            for (int i = 0; i < faceIdArrLen; i++) {
                deleteFace(vipGroup, userId, faceIdArray[i], StatusCode.SUCCESS, aCase, step);
            }

            //6、query user
            apiResponse = queryUser(vipGroup, userId, StatusCode.SUCCESS, aCase, step);
            HashMap afterDeleteAllResult = getQueryUserResult(apiResponse);
            String AfterDeleteAllfaceIdConcat = (String) afterDeleteAllResult.get("faceIdConcat");
            if (!("".equals(AfterDeleteAllfaceIdConcat) || AfterDeleteAllfaceIdConcat != null)) {
                msg = "delete all pictures failed!"
                        + "group: " + vipGroup
                        + "userid: " + userId;
                throw new Exception(msg);
            }
            //7、delete face again
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            for (int i = 0; i < faceIdArrLen; i++) {
                deleteFace(vipGroup, userId, faceIdArray[i], StatusCode.SUCCESS, aCase, step);
            }
            //8、register again
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            for (int i = 0; i < picPathArr.length; i++) {
                registerFace(vipGroup, userId, picPathArr[i], StatusCode.SUCCESS, aCase, step);
            }

            //9、query user
            logger.info("\n\n");
            logger.info("--------------------------------（" + (++step) + ")------------------------------");
            apiResponse = queryUser(vipGroup, userId, StatusCode.SUCCESS, aCase, step);
            HashMap reAddResult = getQueryUserResult(apiResponse);
            String reAddfaceIdConcat = (String) reAddResult.get("faceIdConcat");
            for (index = 0; index < faceIdArrLen; index++) {
                if (!reAddfaceIdConcat.contains(faceIdArray[index])) {
                    msg = "can't register again after delete face!"
                            + "group: " + vipGroup
                            + "userid: " + userId;
                    throw new Exception(msg);
                }
            }
            aCase.setResult("PASS"); //FAIL, PASS
        } catch (Exception e) {
            e.printStackTrace();
            failReason += e.getMessage();
            aCase.setFailReason(failReason);
            Assert.fail(failReason);
            throw e;
        } finally {
            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);
            qaDbUtil.saveToCaseTable(aCase);
        }
    }

//    因为必须用stranger注册成特殊人物，转变一次就不能转变了，没有获取stranger的接口，消费者接口目前只维护，这个功能不常用，所以就暂时搁置

//    @Test
//    public void changeUserTestResult() throws Exception {
//
//        String ciCaseName = new Object() {
//        }
//                .getClass()
//                .getEnclosingMethod()
//                .getName();
//        String caseName = ciCaseName;
//        Case aCase = new Case();
//        String caseDesc = "验证“消费者身份转变”是否成功";
//        failReason = "";
//        int step = 0;
//
//        try {
//            logger.info("\n\n");
//            logger.info("--------------------------------（" + (++step) + ")------------------------------");
//            registerFace(fromGrpName, fromUserId, vipPic, StatusCode.SUCCESS, aCase, step);
//
//            logger.info("\n\n");
//            logger.info("--------------------------------（" + (++step) + ")------------------------------");
//            deleteUser(toGrpName, toUserId, StatusCode.SUCCESS, aCase, step);
//
//            logger.info("\n\n");
//            logger.info("--------------------------------（" + (++step) + ")------------------------------");
//            changeUser(SHOP_ID, fromGrpName, fromUserId, toGrpName, toUserId, isCheckSame, StatusCode.SUCCESS, aCase, step);
//
//            logger.info("\n\n");
//            logger.info("--------------------------------（" + (++step) + ")------------------------------");
//            apiResponse = queryUser(fromGrpName, fromUserId, StatusCode.SUCCESS, aCase, step);
//            getQueryUserResult(apiResponse);
//
//            logger.info("\n\n");
//            logger.info("--------------------------------（" + (++step) + ")------------------------------");
//            apiResponse = queryUser(toGrpName, toUserId, StatusCode.SUCCESS, aCase, step);
//            getQueryUserResult(apiResponse);
//            aCase.setResult("PASS"); //FAIL, PASS
//        } catch (Exception e) {
//            e.printStackTrace();
//            failReason += e.getMessage();
//            aCase.setFailReason(failReason);
//            Assert.fail(failReason);
//            throw e;
//        } finally {
//            setBasicParaToDB(aCase, caseName, caseDesc, ciCaseName);
//            qaDbUtil.saveToCaseTable(aCase);
//        }
//    }
    //-----------------------------the above are the cases of change user-------------------------------------

    private ApiResponse sendRequest(String router, String[] resource, String json) throws Exception {
        logMine.logStep("Send request only！");
        try {
            Credential credential = new Credential(AK, SK);
            String requestId = UUID.randomUUID().toString();
            ApiRequest apiRequest = new ApiRequest.Builder()
                    .uid(UID)
                    .appId(APP_ID)
                    .requestId(requestId)
                    .version(SdkConstant.API_VERSION)
                    .router(router)
                    .dataResource(resource)
                    .dataBizData(JSON.parseObject(json))
                    .build();

            ApiClient apiClient = new ApiClient("http://dev.api.winsenseos.cn/retail/api/data/biz", credential);
            apiResponse = apiClient.doRequest(apiRequest);
            logMine.printImportant(JSON.toJSONString(apiRequest));
            logMine.printImportant(JSON.toJSONString(apiResponse));
        } catch (Exception e) {
            throw e;
        }
        return apiResponse;
    }

    public void checkCode(ApiResponse apiResponse, String router, int expectCode) throws Exception {
        try {
            String requestId = apiResponse.getRequestId();
            if (apiResponse.getCode() != expectCode) {
                String msg = "request id: " + requestId + ", gateway: /retail/api/data/device, router: " + router + ". \nresponse: " + JSON.toJSONString(apiResponse) +
                        "actual code: " + apiResponse.getCode() + " expect code: " + expectCode + ".";
                throw new Exception(msg);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public void checkRegisterDS(ApiResponse apiResponse, String router) throws Exception {
        try {
            String requestId = apiResponse.getRequestId();
            String responseStr = JSON.toJSONString(apiResponse);
            JSONObject resJson = JSON.parseObject(responseStr);

            JSONObject dataJsonObject = resJson.getJSONObject("data");
//            不用判断size，只要有age，is_male，axis就可以了，source是算法调试用的
//            if (dataJsonObject.size() != 4) {
//                String message = "The number of columns that returned in the system is not 4.";
//                message += "request id: " + requestId + ", gateway: /retail/api/data/device, router: " + router + ". \nresponse: " + JSON.toJSONString(apiResponse);
//                throw new Exception(message);
//            }
            String age = dataJsonObject.getString("age");
            String isMale = dataJsonObject.getString("is_male");
            String axis = dataJsonObject.getString("axis");

            if (age == null || isMale == null || axis == null) {
                String message = "The columns that are expected to be returned do not match the columns actually returned in the system.";
                message += "request id: " + requestId + ", gateway: /retail/api/data/device, router: " + router + ". \nresponse: " + JSON.toJSONString(apiResponse);
                throw new Exception(message);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public int checkQueryGrpResult(ApiResponse apiResponse, String router) throws Exception {
        int len = 0;
        try {
            String requestId = apiResponse.getRequestId();
            JSONObject jsonObjectData = (com.alibaba.fastjson.JSONObject) apiResponse.getData();
            com.alibaba.fastjson.JSONArray jsonArrayPerson = jsonObjectData.getJSONArray("person");
            if (jsonArrayPerson != null) {
                len = jsonArrayPerson.size();
            }
            String aa = JSON.toJSONString(apiResponse);
            logMine.printImportant(aa);
            if (!apiResponse.isSuccess()) {
                String msg = "request id: " + requestId + ", gateway: /retail/api/data/biz, router: " + router + "\nresponse: " + JSON.toJSONString(apiResponse);
                throw new Exception(msg);
            }
        } catch (Exception e) {
            throw e;
        }
        return len;
    }

    private ApiResponse sendRequestHeadPara(String router, String[] resource, String json, String uid, String appid, String version) throws Exception {
        logMine.logStep("Test invalid head para!");
        try {
            Credential credential = new Credential(AK, SK);
            String requestId = UUID.randomUUID().toString();
            ApiRequest apiRequest = new ApiRequest.Builder()
                    .uid(uid)
                    .appId(appid)
                    .requestId(requestId)
                    .version(version)
                    .router(router)
                    .dataResource(resource)
                    .dataBizData(JSON.parseObject(json))
                    .build();

            // client 请求
            ApiClient apiClient = new ApiClient("http://dev.api.winsenseos.cn/retail/api/data/biz", credential);
            apiResponse = apiClient.doRequest(apiRequest);
            logMine.printImportant(JSON.toJSONString(apiRequest));
            logMine.printImportant(JSON.toJSONString(apiResponse));
        } catch (Exception e) {
            throw e;
        }
        return apiResponse;
    }

    public HashMap getQueryUserResult(ApiResponse apiResponse) throws Exception {

        String faceUrlFirst = "", faceIdFirst = "", faceUrlConcat = "", faceIdConcat = "";
        //如果不初始化的话，直接put会报错，可怕！
        HashMap<String, String> hm = new HashMap<>();

        String responseStr = JSON.toJSONString(apiResponse);
        JSONObject responseJo = JSON.parseObject(responseStr);
        JSONArray jsonArrayFaces = responseJo.getJSONObject("data").getJSONArray("faces");

        int len = 0;
        //len = jsonArrayFaces.size();(如果jsonArrayFaces是null的话，会报空指针错)
        if (jsonArrayFaces != null) {
            len = jsonArrayFaces.size();
        }
        for (int i = 0; i < len; i++) {
            faceUrlFirst = jsonArrayFaces.getJSONObject(0).getString("face_url");
            faceIdFirst = jsonArrayFaces.getJSONObject(0).getString("face_id");

            String faceUrl = jsonArrayFaces.getJSONObject(i).getString("face_url");
            String faceId = jsonArrayFaces.getJSONObject(i).getString("face_id");

            faceUrlConcat = faceUrlConcat.concat(faceUrl);
            faceIdConcat = faceIdConcat.concat(faceId);
        }
        hm.put("faceNum", String.valueOf(len));
        hm.put("faceIdFirst", faceIdFirst);
        hm.put("faceUrlFirst", faceUrlFirst);
        hm.put("faceIdConcat", faceIdConcat);
        hm.put("faceUrlConcat", faceUrlConcat);

        return hm;
    }

    public HashMap<String, String> getSearchFaceResult(ApiResponse apiResponse) throws Exception {

        HashMap<String, String> hm = new HashMap<>();
        String firstFaceUrl = "";
        String faceUrlConcat = "";

        String responseStr = JSON.toJSONString(apiResponse);
        JSONObject responseJo = JSON.parseObject(responseStr);

        com.alibaba.fastjson.JSONArray jsonArrayFaces = responseJo.getJSONObject("data").getJSONArray("faces");
        int len = jsonArrayFaces.size();
        for (int i = 0; i < len; i++) {
            com.alibaba.fastjson.JSONArray jsonArraySimilar_faces = jsonArrayFaces.getJSONObject(i).getJSONArray("similar_faces");
            for (int j = 0; j < jsonArraySimilar_faces.size(); j++) {
                com.alibaba.fastjson.JSONObject jsonObjectfirstFace = jsonArraySimilar_faces.getJSONObject(0);
                com.alibaba.fastjson.JSONObject jsonObjectFace = jsonArraySimilar_faces.getJSONObject(j);
                firstFaceUrl = jsonObjectfirstFace.getString("face_url");
                String FaceUrl = jsonObjectFace.getString("face_url");
                faceUrlConcat = faceUrlConcat.concat(FaceUrl);
            }
        }
        hm.put("faceNum", String.valueOf(len));
        hm.put("firstFaceUrl", firstFaceUrl);
        hm.put("faceUrlConcat", faceUrlConcat);

        return hm;
    }

    //仅支持a-z,A-Z,0-9,_
    @DataProvider(name = "BAD_GRP_NAME")
    public Object[] createBadGrpName() {
        return new String[]{
                "", " ",
                "嗨[]@-+~！#$^&()={}|;:'\\\"<>.?/·！￥……（）——【】、；：”‘《》。？、,%*"
        };
    }

    //仅支持a-z,A-Z,0-9,_-
    @DataProvider(name = "BAD_USER_ID")
    public Object[] createBadUserid() {

        return new String[]{
                "", " ",
                //英文字符
                "嗨[]@-+~！#$^&()={}|;:'\\\"<>.?/·！￥……（）——【】、；：”‘《》。？、,%*",
        };
    }

    @DataProvider(name = "BAD_UID")
    public Object[] createBadUID() {

        return new String[]{
                //英文字符
                "嗨[]@-+~！#$^&()={}|;:'\\\"<>.?/·！￥……（）——【】、；：”‘《》。？、,%*",
                " uid_e0d1ebec",
                "uid_e0d1ebec "
        };
    }

    @DataProvider(name = "BAD_APPID")
    public Object[] createBadAppid() {

        return new String[]{
                "嗨[]@-+~！#$^&()={}|;:'\\\"<>.?/·！￥……（）——【】、；：”‘《》。？、,%*",
                " a4d4d18741a8",
                "a4d4d18741a8 "
        };
    }

    /**
     * @Description: version建议做成下拉框，减少出错
     * @Param: []
     * @return: java.lang.Object[]
     * @Author: Shine
     * @Date: 2019/4/9
     */
    @DataProvider(name = "BAD_VERSION")
    public Object[] createBadVersion() {

        return new String[]{
                "嗨[]@-+~！#$^&()={}|;:'\\\"<>.?/·！￥……（）——【】、；：”‘《》。？、,%*",
                //特殊数字
                "11", "0", "-1", "0.1",
                "2.2", "-0.1", "-2.2",
                "v1.1.1"
        };
    }

    @DataProvider(name = "GOOD_VERSION")
    public Object[] createGoodVersion() {

        return new String[]{
                "V1", "1.0", "v1.0", ""
        };
    }

    @DataProvider(name = "GOOD_RESULT_NUM")
    public Object[] createGoodResultNum() {
        return new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    }

    @DataProvider(name = "BAD_RESULT_NUM")
    public Object[] createBadResultNum() {
        return new String[]{
                "", " ", "badResultNum",
                //英文字符
                "嗨[]@-+~！#$^&()={}|;:'\\\"<>.?/·！￥……（）——【】、；：”‘《》。？、,%*",
                //特殊数字
                "2001", "0", "-1", "0.1",
                "2.2", "-0.1", "-2.2", "1.0"
        };
    }

    ;

    @DataProvider(name = "CHNG_USER_MISS_PARA")
    public Object[] createChangeUserMissPara() {
        return new String[]{
                "{" +
                        "\"from_group_name\":\"" + fromGrpName + "\"," +
                        "\"from_user_id\":\"" + fromUserId + "\"," +
                        "\"to_group_name\":\"" + toGrpName + "\"," +
                        "\"to_user_id\":\"" + toUserId + "\"," +
                        "\"is_check_same\":\"" + isCheckSame + "\"" +
                        "}",

                "{" +
                        "\"shop_id\":\"" + SHOP_ID + "\"," +
                        "\"from_group_name\":\"" + fromGrpName + "\"," +
                        "\"to_group_name\":\"" + toGrpName + "\"," +
                        "\"to_user_id\":\"" + toUserId + "\"," +
                        "\"is_check_same\":\"" + isCheckSame + "\"" +
                        "}",

                "{" +
                        "\"shop_id\":\"" + SHOP_ID + "\"," +
                        "\"from_group_name\":\"" + fromGrpName + "\"," +
                        "\"from_user_id\":\"" + fromUserId + "\"," +
                        "\"to_group_name\":\"" + toGrpName + "\"," +
                        "\"is_check_same\":\"" + isCheckSame + "\"" +
                        "}"
        };
    }

    ;


    @DataProvider(name = "EMPTY_PARA")
    public Object[] createEmptyPara() {
        return new String[]{
                "  "
        };
    }

    public void setBasicParaToDB(Case aCase, String caseName, String caseDesc, String ciCaseName) {
        aCase.setApplicationId(APP_ID_DB);
        aCase.setConfigId(CONFIG_ID);
        aCase.setCaseName(caseName);
        aCase.setCaseDescription(caseDesc);
        aCase.setCiCmd(CI_CMD + ciCaseName);
        aCase.setQaOwner("廖祥茹");
    }

    public void sendResAndReqIdToDbApi(ApiResponse response, Case acase, int step) {

        if (response != null) {
//            将requestId存入数据库
            String requestId = response.getRequestId();

            String requestDataBefore = acase.getRequestData();
            if (requestDataBefore != null && requestDataBefore.trim().length() > 0) {
                acase.setRequestData(requestDataBefore + "\n" + "(" + step + ") " + requestId + "\n");
            } else {
                acase.setRequestData("(" + step + ") " + requestId + "\n");
            }

//            将response存入数据库
            String responseStr = JSON.toJSONString(response);
            JSONObject responseJo = JSON.parseObject(responseStr);

            String responseBefore = acase.getResponse();
            if (responseBefore != null && responseBefore.trim().length() > 0) {
                acase.setResponse(responseBefore + "\n" + "(" + step + ") " + responseJo + "\n\n");
            } else {
                acase.setResponse(responseJo + "\n\n");
            }
        }
    }


    @BeforeSuite
    public void initial() throws Exception {
        qaDbUtil.openConnection();
    }

    @AfterSuite
    public void clean() {
        qaDbUtil.closeConnection();
    }
}
