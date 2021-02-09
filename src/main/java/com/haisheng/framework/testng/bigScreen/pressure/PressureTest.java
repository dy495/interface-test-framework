package com.haisheng.framework.testng.bigScreen.pressure;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.arronlong.httpclientutil.builder.HCB;
import com.arronlong.httpclientutil.common.HttpConfig;
import com.arronlong.httpclientutil.common.HttpHeader;
import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.xundianDaily.hqq.fucPackage.StoreFuncPackage;
import com.haisheng.framework.testng.bigScreen.xundianDaily.StoreScenarioUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.CommonUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.testng.annotations.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author : huangqingqing
 * @date :  2020/08/04
 */
public class PressureTest extends TestCaseCommon implements TestCaseStd {
    Util util = Util.getInstance();

    /**
     * @description: initial test class level config, such as appid/uid/ak/dinghook/push_rd_name
     */
    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before classs initial");
        CommonConfig commonConfig = new CommonConfig();
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_MENDIAN_DAILY_SERVICE;
        commonConfig.checklistQaOwner = "青青";
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "xundian-daily-test");
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, "门店 日常");
        commonConfig.dingHook = DingWebhook.DAILY_STORE_MANAGEMENT_PLATFORM_GRP;
        commonConfig.pushRd = new String[]{"15898182672", "18513118484", "18810332354", "15084928847"};
        commonConfig.shopId = getXundianShop(); //要改！！！
//        commonConfig.shopId = "43072"; //要改！！！
        beforeClassInit(commonConfig);
       // logger.debug("store " + md);
      //  md.login("yuexiu@test.com", "f5b3e737510f31b88eb2d4b5d0cd2fb4");
        //util.login();

    }

    @AfterClass
    @Override
    public void clean() {
        afterClassClean();
    }

    /**
     * @description: get a fresh case ds to save case result, such as result/response
     */
    @BeforeMethod
    @Override
    public void createFreshCase(Method method) {
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
    }
    private Map<String, String> buildHttpHeaders(String accessToken) {
        Map<String, String> headers = new HashMap<>();
        if(StringUtils.isNotEmpty(accessToken)) {
            headers.put("Authorization", accessToken);
        }
        logger.info("header:{}", headers);
        return headers;
    }
    /**
     *
     * ====================宇视截屏======================
     * */
     @Test
    public void getTaskDetail() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack=false;
        try {
            JSONObject data = util.getToken();
            String url = "https://ezcloud.uniview.com/openapi/device/capture/get";
            String accessToken = data.getString("accessToken");
            JSONObject req = new JSONObject();
            req.put("deviceSerial", "210235C3C1F206000013");
            req.put("channelNo", "5");
            logger.info("request json:{}", req);
            String response = Util.postJsonHeader(buildHttpHeaders(accessToken), url, JSON.toJSONString(req));
            System.out.println(response);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("宇视截屏");
        }
    }


}