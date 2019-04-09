package com.haisheng.framework.testng.custemorGateTest;

import ai.winsense.ApiClient;
import ai.winsense.common.Credential;
import ai.winsense.constant.SdkConstant;
import ai.winsense.model.ApiRequest;
import ai.winsense.model.ApiResponse;
import com.alibaba.fastjson.JSON;
import com.haisheng.framework.testng.CommonDataStructure.LogMine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class Test {
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
    private String vipPic = "src/main/resources/test-res-repo/customer-gateway/1.jpg";
    public void registerFaceWithoutDefault(String grpName, String userId, String picPath) throws Exception{
        String router = "/scenario/gate/SYSTEM_REGISTER_FACE/v1.0";
        String[] resource = new String[]{getImageBinary(picPath)};
        String json = "{\"group_name\":\"" + grpName +"\"," +
                "\"user_id\":\""+userId+"\"," +
                "\"is_quality_limit\":\"true\"," +
                "\"pic_url\":\"@0\"" +
                "}";
        apiCustomerRequest(router,  resource, json);
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


    public String getImageBinary(String picPath){
//        File f = new File("src/main/resources/test-res-repo/customer-gateway/1.jpg");
        File f = new File(picPath);
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
}
