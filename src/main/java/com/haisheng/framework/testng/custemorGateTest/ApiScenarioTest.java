package com.haisheng.framework.testng.custemorGateTest;

import ai.winsense.ApiClient;
import ai.winsense.common.Credential;
import ai.winsense.constant.SdkConstant;
import ai.winsense.model.ApiRequest;
import ai.winsense.model.ApiResponse;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.io.JsonStringEncoder;
import com.haisheng.framework.testng.CommonDataStructure.LogMine;
import com.haisheng.framework.testng.CommonDataStructure.PvInfo;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.StatusCode;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.UUID;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.File;

public class ApiScenarioTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private LogMine logMine = new LogMine(logger);
    private String UID            = "uid_e0d1ebec";
    private String APP_ID         = "a4d4d18741a8";
    private String SHOP_ID        = "134";
    private String RE_ID          = "144";
    private String DEVICE_ID      = "6254834559910912";
    private BASE64Encoder encoder = new sun.misc.BASE64Encoder();
    private BASE64Decoder decoder = new sun.misc.BASE64Decoder();
    private String vipGroup = "vipGroup";
    private String vipUser = "00000";

    @Test (dataProvider = "CASE_USER_ID")
    public void registerFace(String userId) throws Exception{
        String router = "/scenario/gate/SYSTEM_REGISTER_FACE/v1.0";
        String[] resource = new String[]{getImageBinary()};
        String json = "{\"group_name\":\"" + vipGroup +"\"," +
                "\"user_id\":\""+userId+"\"," +
                "\"is_quality_limit\":\"true\"," +
                "\"pic_url\":\"@0\"" +
                "}";
        apiCustomerRequest(router,  resource, json);
    }

    public void registerFaceSingle(String userId) throws Exception{
        String router = "/scenario/gate/SYSTEM_REGISTER_FACE/v1.0";
        String[] resource = new String[]{getImageBinary()};
        String json = "{\"group_name\":\"" + vipGroup +"\"," +
                "\"user_id\":\""+userId+"\"," +
                "\"is_quality_limit\":\"true\"," +
                "\"pic_url\":\"@0\"" +
                "}";
        apiCustomerRequest(router,  resource, json);
    }

    @Test(dataProvider = "CASE_USER_ID")
    public void queryUserNormal(String userId) throws Exception{
        String router = "/scenario/gate/SYSTEM_QUERY_USER/v1.0";
        String[] resource = new String[]{};
        String json = "{" +
                "\"group_name\":\""+vipGroup+"\"," +
                "\"user_id\":\""+userId+"\"" +
                "}";
        apiCustomerRequest(router, resource, json);
    }


    public int queryUserWithResult(String userId) throws Exception{
        String router = "/scenario/gate/SYSTEM_QUERY_USER/v1.0";
        String[] resource = new String[]{};
        String json = "{" +
                "\"group_name\":\""+vipGroup+"\"," +
                "\"user_id\":\""+userId+"\"" +
                "}";
        return apiCustomerRequestWithResult(router, resource, json);
    }

    public void deleteUserWithUserId(String userId) throws Exception{
        String router = "/scenario/gate/SYSTEM_DELETE_USER/v1.0";
        String[] resource = new String[]{};
        String json = "{" +
                "\"group_name\":\""+vipGroup+"\"," +
                "\"user_id\":\""+userId+"\"" +
                "}";
        apiCustomerRequest(router, resource, json);
    }

    @Test(dataProvider = "CASE_UID")
    public void TestUID(String UIDCase) throws Exception {
        String router = "/scenario/gate/SYSTEM_QUERY_GROUP/v1.0";
        String[] resource = new String[]{};
        String json = "{" +
                "\"group_name\":\""+vipGroup+"\"" +
                "}";
        apiCustomerRequestTestUID(router, resource, json, UIDCase);
    }

    @Test(dataProvider = "CASE_APPID")
    public void TestAppid(String AppidCase) throws Exception {
        String router = "/scenario/gate/SYSTEM_QUERY_GROUP/v1.0";
        String[] resource = new String[]{};
        String json = "{" +
                "\"group_name\":\""+vipGroup+"\"" +
                "}";
        apiCustomerRequestTestAppid(router, resource, json, AppidCase);
    }

    @Test(dataProvider = "GRP_NAME")
    public void queryGroupTestGroupName(String grpName) throws Exception {
        String router = "/scenario/gate/SYSTEM_QUERY_GROUP/v1.0";
        String[] resource = new String[]{};
        String json = "{" +
                "\"group_name\":\""+grpName+"\"" +
                "}";
        apiCustomerRequestTestGrpName(router, resource, json);
    }

    @Test (dataProvider = "CASE_BAD_USER_ID")
    public void queryUserTestBadUserId(String badUserId) throws Exception{
        String router = "/scenario/gate/SYSTEM_QUERY_USER/v1.0";
        String[] resource = new String[]{};
        String json = "{" +
                "\"group_name\":\""+vipGroup+"\"," +
                "\"user_id\":\""+badUserId+"\"" +
                "}";
        apiCustomerRequestTestUserId(router, resource, json);
    }

    @Test(dataProvider = "GRP_NAME")
    public void queryUserTestGroupName(String grpName) throws Exception {
        String router = "/scenario/gate/SYSTEM_QUERY_USER/v1.0";
        String[] resource = new String[]{};
        String json = "{" +
                "\"group_name\":\""+grpName+"\"," +
                "\"user_id\":\""+vipUser+"\"" +
                "}";
        apiCustomerRequestTestGrpName(router, resource, json);
    }

    @Test (dataProvider = "CASE_BAD_USER_ID")
    public void deleteUserTestBadUserId(String badUserId) throws Exception{
        String router = "/scenario/gate/SYSTEM_DELETE_USER/v1.0";
        String[] resource = new String[]{};
        String json = "{" +
                "\"group_name\":\""+vipGroup+"\"," +
                "\"user_id\":\""+badUserId+"\"" +
                "}";
        apiCustomerRequestTestUserId(router, resource, json);
    }

    @Test(dataProvider = "GRP_NAME")
    public void deleteUserTestGroupName(String grpName) throws Exception {
        String router = "/scenario/gate/SYSTEM_DELETE_USER/v1.0";
        String[] resource = new String[]{};
        String json = "{" +
                "\"group_name\":\""+grpName+"\"," +
                "\"user_id\":\""+vipUser+"\"" +
                "}";
        apiCustomerRequestTestGrpName(router, resource, json);
    }


    @Test
    public void deleteUserTestTestIsSuccess() throws Exception{
        String msg = "";
        String userId = "04021400";
        registerFaceSingle(userId);
        int beforeDelete = queryUserWithResult(userId);
        deleteUserWithUserId(userId);
        int afterDelete = queryUserWithResult(userId);
        if(beforeDelete>0&&afterDelete==0){
            msg = "delete successfully!";
            logger.info(msg);
        }else{
            msg = "invalid delete!";
            throw new Exception(msg);
        }

    }

    @Test
    public void deleteUserTestTestReAdd() throws Exception{
        String msg = "";
        String userId = "04021400";
        deleteUserWithUserId(userId);
        int beforeDelete = queryUserWithResult(userId);
        registerFaceSingle(userId);
        int afterDelete = queryUserWithResult(userId);
        if(beforeDelete==0&&afterDelete>0){
            msg = "ReAdd successfully!";
            logger.info(msg);
        }else{
            msg = "Cannot ReAdd!";
            throw new Exception(msg);
        }

    }


    private void apiCustomerRequest(String router, String[] resource, String json) throws Exception {
        logMine.logStep("Test normal");
        try {
            Credential credential = new Credential("e0709358d368ee13", "ef4e751487888f4a7d5331e8119172a3");
            // 封装request对象
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

            // client 请求
            ApiClient apiClient = new ApiClient("http://dev.api.winsenseos.com/retail/api/data/biz", credential);
            ApiResponse apiResponse = apiClient.doRequest(apiRequest);
            logMine.printImportant(JSON.toJSONString(apiResponse));
            if(! apiResponse.isSuccess()) {
                String msg = "request id: " + requestId + ", gateway: /retail/api/data/biz, router: " + router + "\nresponse: " + JSON.toJSONString(apiResponse);
                throw new Exception(msg);
            }
        } catch (Exception e) {
            throw e;
        }

    }

    private void apiCustomerRequestTestUID(String router, String[] resource, String json,String UIDCase) throws Exception {
        logMine.logStep("Test invalid UID!");
        try {
            Credential credential = new Credential("e0709358d368ee13", "ef4e751487888f4a7d5331e8119172a3");
            // 封装request对象
            String requestId = UUID.randomUUID().toString();
            ApiRequest apiRequest = new ApiRequest.Builder()
                    .uid(UIDCase)
                    .appId(APP_ID)
                    .requestId(requestId)
                    .version(SdkConstant.API_VERSION)
                    .router(router)
                    .dataResource(resource)
                    .dataBizData(JSON.parseObject(json))
                    .build();

            // client 请求
            ApiClient apiClient = new ApiClient("http://dev.api.winsenseos.com/retail/api/data/biz", credential);
            ApiResponse apiResponse = apiClient.doRequest(apiRequest);
            logMine.printImportant(JSON.toJSONString(apiResponse));

            if (apiResponse.getCode() != StatusCode.UN_AUTHORIZED) {
                String msg = "request id: " + requestId + ", gateway: /retail/api/data/device, router: " + router + ". \nresponse: " + JSON.toJSONString(apiResponse)+
                        "系统返回的状态码 "+ apiResponse.getCode()+ " 与期待返回的状态码 " + StatusCode.UN_AUTHORIZED +" 不符！";
                throw new Exception(msg);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    private void apiCustomerRequestTestAppid(String router, String[] resource, String json, String appidCase) throws Exception {
        logMine.logStep("Test invalid appid");
        try {
            Credential credential = new Credential("e0709358d368ee13", "ef4e751487888f4a7d5331e8119172a3");
            // 封装request对象
            String requestId = UUID.randomUUID().toString();
            ApiRequest apiRequest = new ApiRequest.Builder()
                    .uid(UID)
                    .appId(appidCase)
                    .requestId(requestId)
                    .version(SdkConstant.API_VERSION)
                    .router(router)
                    .dataResource(resource)
                    .dataBizData(JSON.parseObject(json))
                    .build();

            // client 请求
            ApiClient apiClient = new ApiClient("http://dev.api.winsenseos.com/retail/api/data/biz", credential);
            ApiResponse apiResponse = apiClient.doRequest(apiRequest);
            logMine.printImportant(JSON.toJSONString(apiResponse));
            if (apiResponse.getCode() != StatusCode.UN_AUTHORIZED) {
                String msg = "request id: " + requestId + ", gateway: /retail/api/data/device, router: " + router + ". \nresponse: " + JSON.toJSONString(apiResponse)+
                        "系统返回的状态码 "+ apiResponse.getCode()+ " 与期待返回的状态码 " + StatusCode.UN_AUTHORIZED +" 不符！";
                throw new Exception(msg);
            }
        } catch (Exception e) {
            throw e;
        }

    }

    private void apiCustomerRequestTestGrpName(String router, String[] resource, String json) throws Exception {
        logMine.logStep("Test invalid groupName！");
        try {
            Credential credential = new Credential("e0709358d368ee13", "ef4e751487888f4a7d5331e8119172a3");
            // 封装request对象
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

            // client 请求
            ApiClient apiClient = new ApiClient("http://dev.api.winsenseos.com/retail/api/data/biz", credential);
            ApiResponse apiResponse = apiClient.doRequest(apiRequest);
            logMine.printImportant(JSON.toJSONString(apiResponse));
            if (apiResponse.getCode() != StatusCode.BAD_REQUEST) {
                String msg = "request id: " + requestId + ", gateway: /retail/api/data/device, router: " + router + ". \nresponse: " + JSON.toJSONString(apiResponse)+
                        "系统返回的状态码 "+ apiResponse.getCode()+ " 与期待返回的状态码 " + StatusCode.BAD_REQUEST +" 不符！";
                throw new Exception(msg);
            }
        } catch (Exception e) {
            throw e;
        }

    }

    private void apiCustomerRequestTestUserId(String router, String[] resource, String json) throws Exception {
        logMine.logStep("Test invalid userId！");
        try {
            Credential credential = new Credential("e0709358d368ee13", "ef4e751487888f4a7d5331e8119172a3");
            // 封装request对象
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

            // client 请求
            ApiClient apiClient = new ApiClient("http://dev.api.winsenseos.com/retail/api/data/biz", credential);
            ApiResponse apiResponse = apiClient.doRequest(apiRequest);
            logMine.printImportant(JSON.toJSONString(apiResponse));
            if (apiResponse.getCode() != StatusCode.SUCCESS) {
                String msg = "request id: " + requestId + ", gateway: /retail/api/data/device, router: " + router + ". \nresponse: " + JSON.toJSONString(apiResponse)+
                        "系统返回的状态码 "+ apiResponse.getCode()+ " 与期待返回的状态码 " + StatusCode.SUCCESS +" 不符！";
                throw new Exception(msg);
            }
        } catch (Exception e) {
            throw e;
        }

    }

    private int apiCustomerRequestWithResult(String router, String[] resource, String json) throws Exception {
        logMine.logStep("Test query user result!");
//        String userId = null;
        int faceNum = 0;
        try {
            Credential credential = new Credential("e0709358d368ee13", "ef4e751487888f4a7d5331e8119172a3");
            // 封装request对象
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

            // client 请求
            ApiClient apiClient = new ApiClient("http://dev.api.winsenseos.com/retail/api/data/biz", credential);
            ApiResponse apiResponse = apiClient.doRequest(apiRequest);
            com.alibaba.fastjson.JSONObject jstr = (com.alibaba.fastjson.JSONObject) apiResponse.getData();
//            String userId = jstr.getString("user_id");
            com.alibaba.fastjson.JSONArray jsonArray = jstr.getJSONArray("faces");
            faceNum = jsonArray.size();
            logMine.printImportant(JSON.toJSONString(apiResponse));
            if(! apiResponse.isSuccess()) {
                String msg = "request id: " + requestId + ", gateway: /retail/api/data/biz, router: " + router + "\nresponse: " + JSON.toJSONString(apiResponse);
                throw new Exception(msg);
            }
        } catch (Exception e) {
            throw e;
        }
        return faceNum;
    }

    public String getImageBinary(){
        File f = new File("src/main/resources/test-res-repo/customer-gateway/1.jpg");
        BufferedImage bi;
        try {
            bi = ImageIO.read(f);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bi, "jpg", baos);
            byte[] bytes = baos.toByteArray();

            return encoder.encodeBuffer(bytes).trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @DataProvider(name = "GRP_NAME")
    public Object[] createGrpName() {
        return new String[] {
                // "^&*$￥||\\*",  1001
//                "%$^hu",   1001
                " TestGroup",
                "TestGroup ",
                "！@#￥%……",
                "Test Group",
                "12345",
                "123qwe",
                "%$^hu",
                "嗨"
        };
    }

    @DataProvider(name = "CASE_UID")
    public Object[] createInvalidUID() {

        return new String[] {
                "0.0",
                "0",
                "你好",
                "abcd",
                "^&*$￥||\\*",
                " uid_e0d1ebec",
                "uid_e0d1ebec "
        };
    }

    @DataProvider(name = "CASE_APPID")
    public Object[] createInvalidAppid() {

        return new String[] {
                "-1",
                "0",
                "0.0",
                "hello",
                "2A!34'\\*a",
                " a4d4d18741a8",
                "a4d4d18741a8 "
        };
    }

    @DataProvider(name = "CASE_REGION_ID")
    public Object[] createInvalidRegion_id() {

        return new String[] {
                "-1",
                "0",
                "0.0",
                "hello",
                "",
                "2A!34'\\*a",
                " 144",
                "144 "
        };
    }

    @DataProvider(name = "CASE_USER_ID")
    public Object[] createUserid() {

        return new String[] {
                "00000",
                "00001",
                "00002",
                "00003",
                "00004",
                "00005",
        };
    }

    @DataProvider(name = "CASE_BAD_USER_ID")
    public Object[] createBadUserid() {

        return new String[] {
                "00004",
                "你好",
                "hello！",
                "-100",
                "#^%$#￥",
                "00004 ",
                " 00005"
        };
    }
}
