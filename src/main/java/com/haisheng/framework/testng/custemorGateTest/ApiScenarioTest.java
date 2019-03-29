package com.haisheng.framework.testng.custemorGateTest;

import ai.winsense.ApiClient;
import ai.winsense.common.Credential;
import ai.winsense.constant.SdkConstant;
import ai.winsense.model.ApiRequest;
import ai.winsense.model.ApiResponse;
import com.alibaba.fastjson.JSON;
import com.haisheng.framework.testng.CommonDataStructure.LogMine;
import com.haisheng.framework.testng.CommonDataStructure.PvInfo;
import com.haisheng.framework.util.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @Test(dataProvider = "GRP_NAME")
    public void specifiedFaceRegister(String grpName) throws Exception{
        String router = "/scenario/gate/SYSTEM_REGISTER_FACE/v1.0";
        DateTimeUtil dt = new DateTimeUtil();
        String startTime = dt.getHourBegin(0);
        String endTime = dt.getHourBegin(1);
        String[] resource = new String[]{getImageBinary()};
        String json = "{\"group_name\":\"" + grpName +"\"," +
                "\"user_id\":\"uid_e0d1ebec\"," +
                "\"pic_url\":\"@0\"}";
        apiCustomerRequest(router, startTime, endTime, resource, json);
    }

    @Test
    public void specifiedFaceQuery() throws Exception{
        String router = "/scenario/gate/SYSTEM_QUERY_USER/v1.0";
        DateTimeUtil dt = new DateTimeUtil();
        String startTime = dt.getHourBegin(0);
        String endTime = dt.getHourBegin(1);
        String[] resource = new String[]{getImageBinary()};
        String json = "{" +
                "\"group_name\":\"TestGroup\"," +
                "\"user_id\":\"uid_e0d1ebec\"" +
                "}";
        apiCustomerRequest(router, startTime, endTime, resource, json);
    }


    private void apiCustomerRequest(String router, String beginTime, String endTime, String[] resource, String json) throws Exception {
        logMine.logStep("get latest pv info from cloud");
        PvInfo pvInfo = null;
        try {
            Credential credential = new Credential("e0709358d368ee13", "ef4e751487888f4a7d5331e8119172a3");
            // 封装request对象
            String requestId = UUID.randomUUID().toString();
            ApiRequest apiRequest = new ApiRequest.Builder()
                    .uid(UID)
                    .appId(APP_ID)
                    .version(SdkConstant.API_VERSION)
                    .requestId(requestId)
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

    public String getImageBinary(){
        File f = new File("src/main/resources/test-res-repo/customer-gateway/gaoxiaosong.jpg");
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
                "TestGroup1",
                "TestGroup2",
                "TestGroup3"
        };
    }

}
