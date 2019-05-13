package com.haisheng.framework.testng.custemorGateTest;

import ai.winsense.ApiClient;
import ai.winsense.common.Credential;
import ai.winsense.constant.SdkConstant;
import ai.winsense.exception.SdkClientException;
import ai.winsense.model.ApiRequest;
import ai.winsense.model.ApiResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.testng.CommonDataStructure.LogMine;
import com.haisheng.framework.util.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.UUID;

/**
 * 线下消费者接口测试
 * @author Shine
 */
public class CharacterSyn {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private LogMine logMine = new LogMine(logger);
    private static String UID = "uid_7fc78d24";
    private static String APP_ID = "097332a388c2";
    private static String SHOP_ID = "8";
    //2019-05-07 09:22:30
    private static String START_TIME = "1557192150000";
    //2019-05-07 11:22:30
    private static String END_TIME = "1557199350000";
    private static String REGION_ID = "126";

    private static String CUSTOMER_ID = "4eb4c30b-0cb8-4374-bff0-1812f1e9df64";
    private static String GROUP_NAME = "55";

    private ApiResponse apiResponse  = null;


    @Test
    public void queryCustomerHistoryNormal() throws Exception{
        String router = "/business/customer/QUERY_CUSTOMER_HISTORY/v1.1";
        String[] resource = new String[]{};
        String json = "{\"shop_id\":\"" + SHOP_ID +"\"," +
                "\"start_time\":\""+START_TIME+"\"," +
                "\"end_time\":\""+END_TIME+"\"," +
                "\"region_id\":\""+REGION_ID+"\"" +
                "}";
        try {
            int expectCode = StatusCode.SUCCESS;
            apiResponse = sendRequest(router, resource, json);
            checkCode(apiResponse,router,expectCode);
        } catch (Exception e) {
            //throw exception to case running job, then user can get details of failure
            throw e;
        }
    }


    @Test
    public void queryCustomerHistoryCheckDS() throws Exception {
        String router = "/business/customer/QUERY_CUSTOMER_HISTORY/v1.1";
        String[] resource = new String[]{};
        String json = "{\"shop_id\":\"" + SHOP_ID + "\"," +
                "\"start_time\":\"" + START_TIME + "\"," +
                "\"end_time\":\"" + END_TIME + "\"," +
                "\"region_id\":\"" + REGION_ID + "\"" +
                "}";
        try {
            int expectCode = StatusCode.SUCCESS;
            apiResponse = sendRequest(router, resource, json);
            checkCode(apiResponse,router,expectCode);
            checkQueryCustomerHistoryDS(apiResponse,router);
        } catch (Exception e) {
            //throw exception to case running job, then user can get details of failure
            throw e;
        }
    }

    @Test(dataProvider = "BAD_SHOP_ID", priority = 1)
    public void queryCustomerHistoryTestBadShopId(String shopId) throws Exception {
        String router = "/business/customer/QUERY_CUSTOMER_HISTORY/v1.1";
        String[] resource = new String[]{};
        String json = "{\"shop_id\":\"" + shopId + "\"," +
                "\"start_time\":\"" + START_TIME + "\"," +
                "\"end_time\":\"" + END_TIME + "\"," +
                "\"region_id\":\"" + REGION_ID + "\"" +
                "}";
        try {
            int expectCode = StatusCode.BAD_REQUEST;
            apiResponse = sendRequest(router, resource, json);
            checkCode(apiResponse,router,expectCode);
        } catch (Exception e) {
            //throw exception to case running job, then user can get details of failure
            throw e;
        }
    }

    @Test(dataProvider = "GOOD_SHOP_ID", priority = 1)
    public void queryCustomerHistoryTestGoodShopId(String shopId) throws Exception {
        String router = "/business/customer/QUERY_CUSTOMER_HISTORY/v1.1";
        String[] resource = new String[]{};
        String json = "{\"shop_id\":\"" + shopId + "\"," +
                "\"start_time\":\"" + START_TIME + "\"," +
                "\"end_time\":\"" + END_TIME + "\"," +
                "\"region_id\":\"" + REGION_ID + "\"" +
                "}";
        try {
            int expectCode = StatusCode.BAD_REQUEST;
            apiResponse = sendRequest(router, resource, json);
            checkCode(apiResponse,router,expectCode);
        } catch (Exception e) {
            //throw exception to case running job, then user can get details of failure
            throw e;
        }
    }

    @Test(dataProvider = "BAD_START_TIME", priority = 1)
    public void queryCustomerHistoryTestBadStartTime(String startTime) throws Exception {
        String router = "/business/customer/QUERY_CUSTOMER_HISTORY/v1.1";
        String[] resource = new String[]{};
        String json = "{\"shop_id\":\"" + SHOP_ID + "\"," +
                "\"start_time\":\"" + startTime + "\"," +
                "\"end_time\":\"" + END_TIME + "\"," +
                "\"region_id\":\"" + REGION_ID + "\"" +
                "}";
        try {
            int expectCode = StatusCode.BAD_REQUEST;
            apiResponse = sendRequest(router, resource, json);
            checkCode(apiResponse,router,expectCode);
        } catch (Exception e) {
            //throw exception to case running job, then user can get details of failure
            throw e;
        }
    }

    @Test(dataProvider = "BAD_END_TIME", priority = 1)
    public void queryCustomerHistoryTestBadEndTime(String endTime) throws Exception {
        String router = "/business/customer/QUERY_CUSTOMER_HISTORY/v1.1";
        String[] resource = new String[]{};
        String json = "{\"shop_id\":\"" + SHOP_ID + "\"," +
                "\"start_time\":\"" + START_TIME + "\"," +
                "\"end_time\":\"" + endTime + "\"," +
                "\"region_id\":\"" + REGION_ID + "\"" +
                "}";
        try {
            int expectCode = StatusCode.BAD_REQUEST;
            apiResponse = sendRequest(router, resource, json);
            checkCode(apiResponse,router,expectCode);
        } catch (Exception e) {
            //throw exception to case running job, then user can get details of failure
            throw e;
        }
    }

    @Test(dataProvider = "BAD_REGION_ID", priority = 1)
    public void queryCustomerHistoryTestBadRegionId(String regionId) throws Exception {
        String router = "/business/customer/QUERY_CUSTOMER_HISTORY/v1.1";
        String[] resource = new String[]{};
        String json = "{\"shop_id\":\"" + SHOP_ID + "\"," +
                "\"start_time\":\"" + START_TIME + "\"," +
                "\"end_time\":\"" + END_TIME + "\"," +
                "\"region_id\":\"" + regionId + "\"" +
                "}";
        try {
            int expectCode = StatusCode.BAD_REQUEST;
            apiResponse = sendRequest(router, resource, json);
            checkCode(apiResponse,router,expectCode);
        } catch (Exception e) {
            //throw exception to case running job, then user can get details of failure
            throw e;
        }
    }

    @Test
    public void queryCustomerHistoryStartMTEnd() throws Exception {
        String router = "/business/customer/QUERY_CUSTOMER_HISTORY/v1.1";
        String[] resource = new String[]{};
        String json = "{\"shop_id\":\"" + SHOP_ID + "\"," +
                "\"start_time\":\"" + END_TIME + "\"," +
                "\"end_time\":\"" + START_TIME + "\"," +
                "\"region_id\":\"" + REGION_ID + "\"" +
                "}";
        try {
            int expectCode = StatusCode.BAD_REQUEST;
            apiResponse = sendRequest(router, resource, json);
            checkCode(apiResponse,router,expectCode);
        } catch (Exception e) {
            //throw exception to case running job, then user can get details of failure
            throw e;
        }
    }

    @Test
    public void queryCustomerHistoryMT3h() throws Exception {
        String router = "/business/customer/QUERY_CUSTOMER_HISTORY/v1.1";
        String[] resource = new String[]{};
        //2019-05-08 10:22:05
        //2019-05-08 13:22:06
        String startTime = "1557282125000";
        String endTime = "1557292926000";
        String json = "{\"shop_id\":\"" + SHOP_ID + "\"," +
                "\"start_time\":\"" + startTime + "\"," +
                "\"end_time\":\"" + endTime + "\"," +
                "\"region_id\":\"" + REGION_ID + "\"" +
                "}";
        try {
            int expectCode = StatusCode.BAD_REQUEST;
            apiResponse = sendRequest(router, resource, json);
            checkCode(apiResponse,router,expectCode);
        } catch (Exception e) {
            //throw exception to case running job, then user can get details of failure
            throw e;
        }
    }

    @Test
    public void queryCustomerHistoryIntervalZero() throws Exception {
        String router = "/business/customer/QUERY_CUSTOMER_HISTORY/v1.1";
        String[] resource = new String[]{};
        //2019-05-08 10:22:05
        String startTime = "1557282125000";
        String json = "{\"shop_id\":\"" + SHOP_ID + "\"," +
                "\"start_time\":\"" + startTime + "\"," +
                "\"end_time\":\"" + startTime + "\"," +
                "\"region_id\":\"" + REGION_ID + "\"" +
                "}";
        try {
            int expectCode = StatusCode.BAD_REQUEST;
            apiResponse = sendRequest(router, resource, json);
            checkCode(apiResponse,router,expectCode);
        } catch (Exception e) {
            //throw exception to case running job, then user can get details of failure
            throw e;
        }
    }

    @Test
    public void queryCustomerHistoryequals3h() throws Exception {
        String router = "/business/customer/QUERY_CUSTOMER_HISTORY/v1.1";
        String[] resource = new String[]{};
        //2019-05-08 10:22:05
        //2019-05-08 13:22:05
        String startTime = "1557282125000";
        String endTime = "1557292925000";
        String json = "{\"shop_id\":\"" + SHOP_ID + "\"," +
                "\"start_time\":\"" + startTime + "\"," +
                "\"end_time\":\"" + endTime + "\"," +
                "\"region_id\":\"" + REGION_ID + "\"" +
                "}";
        try {
            int expectCode = StatusCode.SUCCESS;
apiResponse = sendRequest(router, resource, json);
checkCode(apiResponse,router,expectCode);
        } catch (Exception e) {
            //throw exception to case running job, then user can get details of failure
            throw e;
        }
    }

    @Test(dataProvider = "HISTORY_MISS_PARA", priority = 1)
    public void queryCustomerHistoryMissPara(String missPara) throws Exception {
        String router = "/business/customer/QUERY_CUSTOMER_HISTORY/v1.1";
        String[] resource = new String[]{};
        String json = missPara;
        try {
            int expectCode = StatusCode.BAD_REQUEST;
            apiResponse = sendRequest(router, resource, json);
            checkCode(apiResponse,router,expectCode);
        } catch (Exception e) {
            //throw exception to case running job, then user can get details of failure
            throw e;
        }
    }

    @Test
    public void querySingleCustomerNormal() throws Exception {
        String router = "/business/customer/QUERY_SINGLE_CUSTOMER/v1.1";
        String[] resource = new String[]{};
        String json =
                "{" +
                        "\"shop_id\":\"" + SHOP_ID + "\"," +
                        "\"customer_id\":\"" + CUSTOMER_ID + "\"," +
                        "\"group_name\":\"" + GROUP_NAME + "\"," +
                        "\"start_time\":\"" + START_TIME + "\"," +
                        "\"end_time\":\"" + END_TIME + "\"," +
                        "}";
        try {
            int expectCode = StatusCode.SUCCESS;
apiResponse = sendRequest(router, resource, json);
checkCode(apiResponse,router,expectCode);
        } catch (Exception e) {
            //throw exception to case running job, then user can get details of failure
            throw e;
        }
    }

    @Test
    public void querySingleCustomerTestDS() throws Exception {
        String router = "/business/customer/QUERY_SINGLE_CUSTOMER/v1.1";
        String[] resource = new String[]{};
        //2019-05-8 12:13:30
        String startTime = "1557288810000";
        //2019-05-8 12:13:31
        String endTime = "1557288811000";
        String json =
                "{" +
                        "\"shop_id\":\"" + SHOP_ID + "\"," +
                        "\"customer_id\":\"" + CUSTOMER_ID + "\"," +
                        "\"group_name\":\"" + GROUP_NAME + "\"," +
                        "\"start_time\":\"" + startTime + "\"," +
                        "\"end_time\":\"" + endTime + "\"," +
                        "}";
        try {
            int expectCode = StatusCode.SUCCESS;
            apiResponse = sendRequest(router, resource, json);
            checkCode(apiResponse,router,expectCode);
            checkQuerySingleCustomerDS(apiResponse,router);
        } catch (Exception e) {
            //throw exception to case running job, then user can get details of failure
            throw e;
        }
    }

    @Test
    public void querySingleCustomerTestStartMTEnd() throws Exception {
        String router = "/business/customer/QUERY_SINGLE_CUSTOMER/v1.1";
        String[] resource = new String[]{};
        String json =
                "{" +
                        "\"shop_id\":\"" + SHOP_ID + "\"," +
                        "\"customer_id\":\"" + CUSTOMER_ID + "\"," +
                        "\"group_name\":\"" + GROUP_NAME + "\"," +
                        "\"start_time\":\"" + END_TIME + "\"," +
                        "\"end_time\":\"" + START_TIME + "\"," +
                        "}";
        try {
            int expectCode = StatusCode.BAD_REQUEST;
            apiResponse = sendRequest(router, resource, json);
            checkCode(apiResponse,router,expectCode);
        } catch (Exception e) {
            //throw exception to case running job, then user can get details of failure
            throw e;
        }
    }

    @Test
    public void querySingleCustomerTestStartEqualsEnd() throws Exception {
        String router = "/business/customer/QUERY_SINGLE_CUSTOMER/v1.1";
        String[] resource = new String[]{};
        String json =
                "{" +
                        "\"shop_id\":\"" + SHOP_ID + "\"," +
                        "\"customer_id\":\"" + CUSTOMER_ID + "\"," +
                        "\"group_name\":\"" + GROUP_NAME + "\"," +
                        "\"start_time\":\"" + START_TIME + "\"," +
                        "\"end_time\":\"" + START_TIME + "\"," +
                        "}";
        try {
            int expectCode = StatusCode.BAD_REQUEST;
            apiResponse = sendRequest(router, resource, json);
            checkCode(apiResponse,router,expectCode);
        } catch (Exception e) {
            //throw exception to case running job, then user can get details of failure
            throw e;
        }
    }

    @Test
    public void querySingleCustomerTestIs3Days() throws Exception {
        String router = "/business/customer/QUERY_SINGLE_CUSTOMER/v1.1";
        String[] resource = new String[]{};
        //start_time = 2019-05-05 15:11:07    end_time = 2019-05-08 15:11:07
        String startTime = "1557040267000";
        String endTime = "1557299467000";
        String json =
                "{" +
                        "\"shop_id\":\"" + SHOP_ID + "\"," +
                        "\"customer_id\":\"" + CUSTOMER_ID + "\"," +
                        "\"group_name\":\"" + GROUP_NAME + "\"," +
                        "\"start_time\":\"" + startTime + "\"," +
                        "\"end_time\":\"" + endTime + "\"," +
                        "}";
        try {
            int expectCode = StatusCode.SUCCESS;
            apiResponse = sendRequest(router, resource, json);
            checkCode(apiResponse,router,expectCode);
        } catch (Exception e) {
            //throw exception to case running job, then user can get details of failure
            throw e;
        }
    }

    @Test
    public void querySingleCustomerTestMT3Days() throws Exception {
        String router = "/business/customer/QUERY_SINGLE_CUSTOMER/v1.1";
        String[] resource = new String[]{};
        //start_time = 2019-05-05 15:11:07    end_time = 2019-05-08 15:11:08
        String startTime = "1557040267000";
        String endTime = "1557299468000";
        String json =
                "{" +
                        "\"shop_id\":\"" + SHOP_ID + "\"," +
                        "\"customer_id\":\"" + CUSTOMER_ID + "\"," +
                        "\"group_name\":\"" + GROUP_NAME + "\"," +
                        "\"start_time\":\"" + startTime + "\"," +
                        "\"end_time\":\"" + endTime + "\"," +
                        "}";
        try {
            int expectCode = StatusCode.BAD_REQUEST;
            apiResponse = sendRequest(router, resource, json);
            checkCode(apiResponse,router,expectCode);
        } catch (Exception e) {
            //throw exception to case running job, then user can get details of failure
            throw e;
        }
    }

    @Test(dataProvider = "BAD_SHOP_ID", priority = 1)
    public void querySingleCustomerTestBadShopId(String shopId) throws Exception {
        String router = "/business/customer/QUERY_SINGLE_CUSTOMER/v1.1";
        String[] resource = new String[]{};
        String json =
                "{" +
                        "\"shop_id\":\"" + shopId + "\"," +
                        "\"customer_id\":\"" + CUSTOMER_ID + "\"," +
                        "\"group_name\":\"" + GROUP_NAME + "\"," +
                        "\"start_time\":\"" + START_TIME + "\"," +
                        "\"end_time\":\"" + END_TIME + "\"," +
                        "}";
        try {
            int expectCode = StatusCode.BAD_REQUEST;
            apiResponse = sendRequest(router, resource, json);
            checkCode(apiResponse,router,expectCode);
        } catch (Exception e) {
            //throw exception to case running job, then user can get details of failure
            throw e;
        }
    }

    @Test(dataProvider = "BAD_CUSTOMER_ID", priority = 1)
    public void querySingleCustomerTestBadCustomerId(String customer_id) throws Exception {
        String router = "/business/customer/QUERY_SINGLE_CUSTOMER/v1.1";
        String[] resource = new String[]{};
        String json =
                "{" +
                        "\"shop_id\":\"" + SHOP_ID + "\"," +
                        "\"customer_id\":\"" + customer_id + "\"," +
                        "\"group_name\":\"" + GROUP_NAME + "\"," +
                        "\"start_time\":\"" + START_TIME + "\"," +
                        "\"end_time\":\"" + END_TIME + "\"," +
                        "}";
        try {
            int expectCode = StatusCode.BAD_REQUEST;
            apiResponse = sendRequest(router, resource, json);
            checkCode(apiResponse,router,expectCode);
        } catch (Exception e) {
            //throw exception to case running job, then user can get details of failure
            throw e;
        }
    }

    @Test(dataProvider = "BAD_START_TIME", priority = 1)
    public void querySingleCustomerTestBadStartTime(String startTime) throws Exception {
        String router = "/business/customer/QUERY_SINGLE_CUSTOMER/v1.1";
        String[] resource = new String[]{};
        String json =
                "{" +
                        "\"shop_id\":\"" + SHOP_ID + "\"," +
                        "\"customer_id\":\"" + CUSTOMER_ID + "\"," +
                        "\"group_name\":\"" + GROUP_NAME + "\"," +
                        "\"start_time\":\"" + startTime + "\"," +
                        "\"end_time\":\"" + END_TIME + "\"," +
                        "}";
        try {
            int expectCode = StatusCode.BAD_REQUEST;
            apiResponse = sendRequest(router, resource, json);
            checkCode(apiResponse,router,expectCode);
        } catch (Exception e) {
            //throw exception to case running job, then user can get details of failure
            throw e;
        }
    }

    @Test(dataProvider = "BAD_END_TIME", priority = 1)
    public void querySingleCustomerTestBadEndTime(String endTime) throws Exception {
        String router = "/business/customer/QUERY_SINGLE_CUSTOMER/v1.1";
        String[] resource = new String[]{};
        String json =
                "{" +
                        "\"shop_id\":\"" + SHOP_ID + "\"," +
                        "\"customer_id\":\"" + CUSTOMER_ID + "\"," +
                        "\"group_name\":\"" + GROUP_NAME + "\"," +
                        "\"start_time\":\"" + START_TIME + "\"," +
                        "\"end_time\":\"" + endTime + "\"," +
                        "}";
        try {
            int expectCode = StatusCode.BAD_REQUEST;
            apiResponse = sendRequest(router, resource, json);
            checkCode(apiResponse,router,expectCode);
        } catch (Exception e) {
            //throw exception to case running job, then user can get details of failure
            throw e;
        }
    }

    @Test(dataProvider = "SINGLE_MISS_PARA", priority = 1)
    public void querySingleCustomerMissPara(String missPara) throws Exception {
        String router = "/business/customer/QUERY_SINGLE_CUSTOMER/v1.1";
        String[] resource = new String[]{};
        String json = missPara;
        try {
            int expectCode = StatusCode.BAD_REQUEST;
            apiResponse = sendRequest(router, resource, json);
            checkCode(apiResponse,router,expectCode);
        } catch (Exception e) {
            //throw exception to case running job, then user can get details of failure
            throw e;
        }
    }

    @Test()
    public void queryHisThermalMapNormal() throws Exception {
        String router = "/business/customer/QUERY_HISTORY_THERMAL_MAP/v1.1";
        String[] resource = new String[]{};
        String json =
                "{" +
                        "\"shop_id\":\"" + SHOP_ID + "\"," +
                        "\"region_id\":\"" + REGION_ID + "\"," +
                        "\"start_time\":\"" + START_TIME + "\"," +
                        "\"end_time\":\"" + END_TIME + "\"," +
                        "}";
        try {
            int expectCode = StatusCode.SUCCESS;
apiResponse = sendRequest(router, resource, json);
checkCode(apiResponse,router,expectCode);
        } catch (Exception e) {
            //throw exception to case running job, then user can get details of failure
            throw e;
        }
    }

    @Test(dataProvider = "HIS_THERMAL_MISS_PARA", priority = 1)
    public void queryHisThermalMapMissPara(String missPara) throws Exception {
        String router = "/business/customer/QUERY_HISTORY_THERMAL_MAP/v1.1";
        String[] resource = new String[]{};
        String json = missPara;
        try {
            int expectCode = StatusCode.BAD_REQUEST;
            apiResponse = sendRequest(router, resource, json);
            checkCode(apiResponse,router,expectCode);
        } catch (Exception e) {
            //throw exception to case running job, then user can get details of failure
            throw e;
        }
    }

    @Test()
    public void queryStatisticsNormal() throws Exception {
        String router = "/business/customer/QUERY_CUSTOMER_STATISTICS/v1.1";
        String[] resource = new String[]{};
        String json =
                "{" +
                        "\"shop_id\":\"" + SHOP_ID + "\"," +
                        "\"region_id\":\"" + REGION_ID + "\"," +
                        "\"start_time\":\"" + START_TIME + "\"," +
                        "\"end_time\":\"" + END_TIME + "\"," +
                        "}";
        try {
            int expectCode = StatusCode.SUCCESS;
apiResponse = sendRequest(router, resource, json);
checkCode(apiResponse,router,expectCode);
        } catch (Exception e) {
            //throw exception to case running job, then user can get details of failure
            throw e;
        }
    }

    @Test(dataProvider = "STATISTICS_MISS_PARA", priority = 1)
    public void queryStatisticsMissPara(String missPara) throws Exception {
        String router = "/business/customer/QUERY_CUSTOMER_STATISTICS/v1.1";
        String[] resource = new String[]{};
        String json = missPara;
        try {
            int expectCode = StatusCode.BAD_REQUEST;
            apiResponse = sendRequest(router, resource, json);
            checkCode(apiResponse,router,expectCode);
        } catch (Exception e) {
            //throw exception to case running job, then user can get details of failure
            throw e;
        }
    }

    @Test()
    public void queryThermalMapNormal() throws Exception {
        String router = "/business/customer/QUERY_THERMAL_MAP/v1.1";
        String[] resource = new String[]{};
        String json =
                "{" +
                        "\"shop_id\":\"" + SHOP_ID + "\"," +
                        "\"region_id\":\"" + REGION_ID + "\"," +
                        "\"start_time\":\"" + START_TIME + "\"," +
                        "\"end_time\":\"" + END_TIME + "\"," +
                        "}";
        try {
            int expectCode = StatusCode.SUCCESS;
apiResponse = sendRequest(router, resource, json);
checkCode(apiResponse,router,expectCode);
        } catch (Exception e) {
            //throw exception to case running job, then user can get details of failure
            throw e;
        }
    }

    @Test(dataProvider = "HIS_THERMAL_MISS_PARA", priority = 1)
    public void queryThermalMapMissPara(String missPara) throws Exception {
        String router = "/business/customer/QUERY_THERMAL_MAP/v1.1";
        String[] resource = new String[]{};
        String json = missPara;
        try {
            int expectCode = StatusCode.BAD_REQUEST;
            apiResponse = sendRequest(router, resource, json);
            checkCode(apiResponse,router,expectCode);
        } catch (Exception e) {
            //throw exception to case running job, then user can get details of failure
            throw e;
        }
    }

    @Test()
    public void queryCurrentStatisticsNormal() throws Exception {
        String router = "/business/customer/QUERY_CURRENT_CUSTOMER_STATISTICS/v1.1";
        String[] resource = new String[]{};
        String json =
                "{" +
                        "\"shop_id\":\"" + SHOP_ID + "\"," +
                        "\"region_id\":\"" + REGION_ID + "\"" +
                        "}";
        try {
            int expectCode = StatusCode.SUCCESS;
apiResponse = sendRequest(router, resource, json);
checkCode(apiResponse,router,expectCode);
        } catch (Exception e) {
            //throw exception to case running job, then user can get details of failure
            throw e;
        }
    }

    @Test(dataProvider = "CURRENT_STATISTICS_MISS_PARA", priority = 1)
    public void queryCurrentStatisticsMissPara(String missPara) throws Exception {
        String router = "/business/customer/QUERY_CURRENT_CUSTOMER_STATISTICS/v1.1";
        String[] resource = new String[]{};
        String json = missPara;
        try {
            int expectCode = StatusCode.BAD_REQUEST;
            apiResponse = sendRequest(router, resource, json);
            checkCode(apiResponse,router,expectCode);
        } catch (Exception e) {
            //throw exception to case running job, then user can get details of failure
            throw e;
        }
    }

    @Test()
    public void queryHistoryNormal() throws Exception {
        String router = "/business/customer/QUERY_CUSTOMER_HISTORY/v1.1";
        String[] resource = new String[]{};
        String json =
                "{" +
                        "\"shop_id\":\"" + SHOP_ID + "\"," +
                        "\"start_time\":\"" + START_TIME + "\"," +
                        "\"end_time\":\"" + END_TIME + "\"," +
                        "\"region_id\":\"" + REGION_ID + "\"" +
                        "}";
        try {
            int expectCode = StatusCode.SUCCESS;
apiResponse = sendRequest(router, resource, json);
checkCode(apiResponse,router,expectCode);
        } catch (Exception e) {
            //throw exception to case running job, then user can get details of failure
            throw e;
        }
    }

    @Test(dataProvider = "HISTORY_STATISTICS_MISS_PARA", priority = 1)
    public void queryHistoryMissPara(String missPara) throws Exception {
        String router = "/business/customer/QUERY_CUSTOMER_HISTORY/v1.1";
        String[] resource = new String[]{};
        String json = missPara;
        try {
            int expectCode = StatusCode.BAD_REQUEST;
            apiResponse = sendRequest(router, resource, json);
            checkCode(apiResponse,router,expectCode);
        } catch (Exception e) {
            //throw exception to case running job, then user can get details of failure
            throw e;
        }
    }

    //----------------------------------------------------------------------------------------------------------------------------------//

    private ApiResponse sendRequest(String router, String[] resource, String json) throws Exception {
        logMine.logStep("send Request！");
        try {
            Credential credential = new Credential("77327ffc83b27f6d", "7624d1e6e190fbc381d0e9e18f03ab81");
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
            apiResponse = apiClient.doRequest(apiRequest);
            logMine.printImportant("apiClient" + JSON.toJSONString(apiClient));
            logMine.printImportant("apiRequest" + JSON.toJSONString(apiRequest));
            logMine.printImportant("apiResponse" + JSON.toJSONString(apiResponse));
        } catch (SdkClientException e) {
            String msg = e.getMessage();
            throw new Exception(msg);
        } catch (Exception e) {
            throw e;
        }
        return apiResponse;
    }

    public void checkCode(ApiResponse apiResponse,String router,int expectCode) throws Exception{
        try {
            String requestId = apiResponse.getRequestId();
            if (apiResponse.getCode() != expectCode) {
                String msg = "request id: " + requestId + ", gateway: /retail/api/data/device, router: " + router + ". \nresponse: " + JSON.toJSONString(apiResponse) +
                        "actual code: " + apiResponse.getCode() + " expect code: " + expectCode+ ".";
                throw new Exception(msg);
            }
        }catch(Exception e){
            throw e;
        }
    }

    public void checkCodeArr(ApiResponse apiResponse,String router,int[] expectCodeArr) throws Exception{
        String msg = "";
        try {
            String requestId = apiResponse.getRequestId();
            String expectCode = "";
            for(int i = 0; i< expectCodeArr.length;i++){
                expectCode = expectCodeArr[i] + "  ";
            }
            if (!expectCode.contains(String.valueOf(apiResponse.getCode()))) {
                msg = "request id: " + requestId + ", gateway: /retail/api/data/device, router: " + router + ". \nresponse: " + JSON.toJSONString(apiResponse) +"/n"+
                        "actualCode：" + apiResponse.getCode() + " expectCode: " + expectCode + ".";
                throw new Exception(msg);
            }
        }catch(Exception e){
            throw e;
        }
    }

    public void checkQueryCustomerHistoryDS(ApiResponse apiResponse,String router) throws Exception{
        try {
            String requestId = apiResponse.getRequestId();
            String responseStr = JSON.toJSONString(apiResponse);
            JSONObject resJson = JSON.parseObject(responseStr);

            JSONObject dataJsonObject = resJson.getJSONObject("data");
            JSONArray  personJsonArray = dataJsonObject.getJSONArray("person");
            JSONObject firstPerson = personJsonArray.getJSONObject(0);
            if(firstPerson.size()!=6){
                String message = "The number of columns that are expected to be returned is not equals to that actually returned in the system.";
                message += "request id: " + requestId + ", gateway: /retail/api/data/device, router: " + router + ". \nresponse: " + JSON.toJSONString(apiResponse);
                throw new Exception(message);
            }
            String customerId = firstPerson.getString("customer_id");
            String customerType = firstPerson.getString("customer_type");
            String groupName = firstPerson.getString("group_name");
            String age = firstPerson.getString("age");
            String isMale = firstPerson.getString("is_male");
            String faceUrl = firstPerson.getString("face_url");

            if(customerId==null||customerType==null||groupName==null||age==null||isMale==null||faceUrl==null){
                String message = "The columns that are expected to be returned do not match the columns actually returned in the system.";
                message += "request id: " + requestId + ", gateway: /retail/api/data/device, router: " + router + ". \nresponse: " + JSON.toJSONString(apiResponse);
                throw new Exception(message);
            }
        }catch(Exception e){
            throw e;
        }
    }

    public void checkQuerySingleCustomerDS(ApiResponse apiResponse,String router) throws Exception{
        try {
            String requestId = apiResponse.getRequestId();
            String responseStr = JSON.toJSONString(apiResponse);
            JSONObject resJson = JSON.parseObject(responseStr);

            JSONObject dataJsonObject = resJson.getJSONObject("data");

            String isMerge = dataJsonObject.getString("is_merge");
            String mergeFrom = dataJsonObject.getString("merge_from");
            String mergeTo = dataJsonObject.getString("merge_to");
            if("true".equals(isMerge)){
                if(mergeFrom==null||mergeTo==null){
                    String message = "is_merge is true,however,merge_from or merge_to is null.";
                    message += "request id: " + requestId + ", gateway: /retail/api/data/device, router: " + router + ". \nresponse: " + JSON.toJSONString(apiResponse);
                    throw new Exception(message);
                }
            }else if("false".equals(isMerge)){
                if(mergeFrom!=null||mergeTo!=null){
                    String message = "is_merge is false,however,merge_from or merge_to is not null.";
                    message += "request id: " + requestId + ", gateway: /retail/api/data/device, router: " + router + ". \nresponse: " + JSON.toJSONString(apiResponse);
                    throw new Exception(message);
                }
            }

            JSONObject personJSONObject = dataJsonObject.getJSONObject("person");
            if(personJSONObject.size()!=5){
                String message = "The size of person that returned in the system is not 5";
                message += "request id: " + requestId + ", gateway: /retail/api/data/device, router: " + router + ". \nresponse: " + JSON.toJSONString(apiResponse);
                throw new Exception(message);
            }
            String customerId = personJSONObject.getString("customer_id");
            String customerType = personJSONObject.getString("customer_type");
            String groupName = personJSONObject.getString("group_name");
            JSONObject customerProperty = personJSONObject.getJSONObject("customer_property");
            JSONArray customerPosition = personJSONObject.getJSONArray("customer_position");

            if("STRANGER".equals(customerType)){
                if(groupName!=null){
                    String message = "A STRANGER with group_name!";
                    message += "request id: " + requestId + ", gateway: /retail/api/data/device, router: " + router + ". \nresponse: " + JSON.toJSONString(apiResponse);
                    throw new Exception(message);
                }
            }else if("SPECIAL".equals(customerType)){
                if(groupName==null){
                    String message = "A SPECIAL without group_name!";
                    message += "request id: " + requestId + ", gateway: /retail/api/data/device, router: " + router + ". \nresponse: " + JSON.toJSONString(apiResponse);
                    throw new Exception(message);
                }
            }

            if(customerId==null||customerType==null||customerProperty==null||customerPosition==null){
                String message = "expected person's columns do not match that actually returned in the system.";
                message += "request id: " + requestId + ", gateway: /retail/api/data/device, router: " + router + ". \nresponse: " + JSON.toJSONString(apiResponse);
                throw new Exception(message);
            }

            if(customerProperty.size()!=4){
                String message = "The number of columns of customer_property that actually returned in the system is not 4.";
                message += "request id: " + requestId + ", gateway: /retail/api/data/device, router: " + router + ". \nresponse: " + JSON.toJSONString(apiResponse);
                throw new Exception(message);
            }
            String age = customerProperty.getString("age");
            String isMale = customerProperty.getString("is_male");
            String faceUrl = customerProperty.getString("face_url");
            String gender = customerProperty.getString("gender");
            if(age==null||isMale==null||faceUrl==null||gender==null){
                String message = "expected customer_property's columns do not match the columns actually returned in the system.";
                message += "request id: " + requestId + ", gateway: /retail/api/data/device, router: " + router + ". \nresponse: " + JSON.toJSONString(apiResponse);
                throw new Exception(message);
            }

            JSONObject firstPositionArr = customerPosition.getJSONObject(0);
            if(firstPositionArr.size()!=4){
                String message = "The size of customer_position that returned in the system is not 4.";
                message += "request id: " + requestId + ", gateway: /retail/api/data/device, router: " + router + ". \nresponse: " + JSON.toJSONString(apiResponse);
                throw new Exception(message);
            }
            String firstEnterTime = firstPositionArr.getString("first_enter_time");
            String lastLeaveTime = firstPositionArr.getString("last_leave_time");
            JSONArray regionArr = firstPositionArr.getJSONArray("region");
            JSONArray deviceArr = firstPositionArr.getJSONArray("device");
            if(firstEnterTime==null||lastLeaveTime==null||regionArr==null||deviceArr==null){
                String message = "expected customer_position's columns do not match that actually returned in the system.";
                message += "request id: " + requestId + ", gateway: /retail/api/data/device, router: " + router + ". \nresponse: " + JSON.toJSONString(apiResponse);
                throw new Exception(message);
            }

            JSONObject regionJsonObject = regionArr.getJSONObject(0);
            if(regionJsonObject.size()<5){
                String message = "The size of customer_position that returned in the system is less than 5.";
                message += "request id: " + requestId + ", gateway: /retail/api/data/device, router: " + router + ". \nresponse: " + JSON.toJSONString(apiResponse);
                throw new Exception(message);
            }

            String regionStartTime = regionJsonObject.getString("start_time");
            String regionEndTime = regionJsonObject.getString("end_time");
            String regionId = regionJsonObject.getString("region_id");
            String regionType = regionJsonObject.getString("region_type");
            JSONArray traceJsonArray = regionJsonObject.getJSONArray("trace");
            if(regionStartTime==null||regionEndTime==null||regionId==null||regionType==null||traceJsonArray==null){
                String message = "expected region's columns do not match that actually returned in the system.";
                message += "request id: " + requestId + ", gateway: /retail/api/data/device, router: " + router + ". \nresponse: " + JSON.toJSONString(apiResponse);
                throw new Exception(message);
            }

            JSONObject traceJsonObject = traceJsonArray.getJSONObject(0);
            if(traceJsonObject.size()!=3){
                String message = "The size of trace that returned in the system is not 3.";
                message += "request id: " + requestId + ", gateway: /retail/api/data/device, router: " + router + ". \nresponse: " + JSON.toJSONString(apiResponse);
                throw new Exception(message);
            }
            String mapId = traceJsonObject.getString("map_id");
            String traceTime = traceJsonObject.getString("time");
            String point = traceJsonObject.getString("point");
            if(mapId==null||traceTime==null||point==null){
                String message = "expected trace's columns do not match that actually returned in the system.";
                message += "request id: " + requestId + ", gateway: /retail/api/data/device, router: " + router + ". \nresponse: " + JSON.toJSONString(apiResponse);
                throw new Exception(message);
            }

            JSONObject firstDevice = deviceArr.getJSONObject(0);
            if(firstDevice.size()!=3){
                String message = "The size of device that returned in the system is not 3.";
                message += "request id: " + requestId + ", gateway: /retail/api/data/device, router: " + router + ". \nresponse: " + JSON.toJSONString(apiResponse);
                throw new Exception(message);
            }

            String deviceId = firstDevice.getString("device_id");
            String deviceIdStartTime = firstDevice.getString("start_time");
            String deviceIdEndTime = firstDevice.getString("end_time");
            if(deviceId==null||deviceIdStartTime==null||deviceIdEndTime==null){
                String message = "expected device's columns do not match that actually returned in the system.";
                message += "request id: " + requestId + ", gateway: /retail/api/data/device, router: " + router + ". \nresponse: " + JSON.toJSONString(apiResponse);
                throw new Exception(message);
            }
        }catch(Exception e){
            throw e;
        }
    }

    @DataProvider(name = "BAD_SHOP_ID")
    public static Object[] badShopId() {

        return new String[]{
                "[]@-+~！#$^&()={}|;:'\\\"<>.?/·！￥……（）——【】、；：”‘《》。？、,%*",
                "嗨", "", " ", "normal",
                //英文字符
                "[", "]", "@", "+",
                "~", "！", "#", "$", "^",
                "&", "(", ")", "=", "{",
                "}", "|", ";", ":", "'",
                "\\\"", "<", ">", ".", "?",
                "/",
                //中文字符
                "·", "！", "￥", "……", "（",
                "）", "——", "【", "】", "、",
                "；", "：", "”", "‘", "《",
                "》", "。", "？", "、",
                ",", "%", "*",

                //小数
                "0.1","0.0"
        };
    }

    @DataProvider(name = "GOOD_SHOP_ID")
    public static Object[] goodShopId() {

        return new String[]{
                "82938", "0","9"
        };
    }

    @DataProvider(name = "BAD_CUSTOMER_ID")
    public static Object[] badCustomerId() {

        return new String[]{
                "嗨", "", " ",
                //英文字符
                "[", "]", "@", "+",
                "~", "！", "#", "$", "^",
                "&", "(", ")", "=", "{",
                "}", "|", ";", ":", "'",
                "\\\"", "<", ">", ".", "?",
                "/",
                //中文字符
                "·", "！", "￥", "……", "（",
                "）", "——", "【", "】", "、",
                "；", "：", "”", "‘", "《",
                "》", "。", "？", "、",
                ",", "%", "*"
        };
    }

    @DataProvider(name = "BAD_START_TIME")
    public static Object[] badStartTime() {

        return new String[]{
                "嗨", "", " ", "normal", "1234567890",
                //英文字符
                "[", "]", "@", "+",
                "~", "！", "#", "$", "^",
                "&", "(", ")", "=", "{",
                "}", "|", ";", ":", "'",
                "\\\"", "<", ">", ".", "?",
                "/",
                //中文字符
                "·", "！", "￥", "……", "（",
                "）", "——", "【", "】", "、",
                "；", "：", "”", "‘", "《",
                "》", "。", "？", "、",
                ",", "%", "*"
        };
    }

    @DataProvider(name = "BAD_END_TIME")
    public static Object[] badEndTime() {

        return new String[]{
                "嗨", "", " ", "normal", "1234567890",
                //英文字符
                "[", "]", "@", "+",
                "~", "！", "#", "$", "^",
                "&", "(", ")", "=", "{",
                "}", "|", ";", ":", "'",
                "\\\"", "<", ">", ".", "?",
                "/",
                //中文字符
                "·", "！", "￥", "……", "（",
                "）", "——", "【", "】", "、",
                "；", "：", "”", "‘", "《",
                "》", "。", "？", "、",
                ",", "%", "*"
        };
    }

    @DataProvider(name = "BAD_REGION_ID")
    public static Object[] badRegionId() {

        return new String[]{
                "嗨", "narmal",
                //英文字符
                "[", "]", "@", "+",
                "~", "！", "#", "$", "^",
                "&", "(", ")", "=", "{",
                "}", "|", ";", ":", "'",
                "\\\"", "<", ">", ".", "?",
                "/",
                //中文字符
                "·", "！", "￥", "……", "（",
                "）", "——", "【", "】", "、",
                "；", "：", "”", "‘", "《",
                "》", "。", "？", "、",
                ",", "%", "*"
        };
    }

    @DataProvider(name = "HISTORY_MISS_PARA")
    public static Object[] badHistoryMissPara() {

        return new String[]{
                "{" +
                        "\"start_time\":\"" + START_TIME + "\"," +
                        "\"end_time\":\"" + END_TIME + "\"," +
                        "\"region_id\":\"" + REGION_ID + "\"" +
                        "}",
                "{" +
                        "\"shop_id\":\"" + SHOP_ID + "\"," +
                        "\"end_time\":\"" + END_TIME + "\"," +
                        "\"region_id\":\"" + REGION_ID + "\"" +
                        "}",
                "{" +
                        "\"shop_id\":\"" + SHOP_ID + "\"," +
                        "\"start_time\":\"" + START_TIME + "\"," +
                        "\"region_id\":\"" + REGION_ID + "\"" +
                        "}"
        };
    }

    @DataProvider(name = "SINGLE_MISS_PARA")
    public static Object[] badSingleMissPara() {

        return new String[]{
                "{" +
                        "\"customer_id\":\"" + CUSTOMER_ID + "\"," +
                        "\"group_name\":\"" + GROUP_NAME + "\"," +
                        "\"start_time\":\"" + START_TIME + "\"," +
                        "\"end_time\":\"" + END_TIME + "\"" +
                        "}",
                "{" +
                        "\"shop_id\":\"" + SHOP_ID + "\"," +
                        "\"group_name\":\"" + GROUP_NAME + "\"," +
                        "\"start_time\":\"" + START_TIME + "\"," +
                        "\"end_time\":\"" + END_TIME + "\"" +
                        "}",
                "{" +
                        "\"shop_id\":\"" + SHOP_ID + "\"," +
                        "\"customer_id\":\"" + CUSTOMER_ID + "\"," +
                        "\"group_name\":\"" + GROUP_NAME + "\"," +
                        "\"end_time\":\"" + END_TIME + "\"" +
                        "}",
                "{" +
                        "\"shop_id\":\"" + SHOP_ID + "\"," +
                        "\"customer_id\":\"" + CUSTOMER_ID + "\"," +
                        "\"group_name\":\"" + GROUP_NAME + "\"," +
                        "\"start_time\":\"" + START_TIME + "\"" +
                        "}"
        };
    }

    @DataProvider(name = "HIS_THERMAL_MISS_PARA")
    public static Object[] badHisThermalMissPara() {

        return new String[]{
                "{" +
                        "\"region_id\":\"" + REGION_ID + "\"," +
                        "\"start_time\":\"" + START_TIME + "\"," +
                        "\"end_time\":\"" + END_TIME + "\"" +
                        "}",
                "{" +
                        "\"shop_id\":\"" + SHOP_ID + "\"," +
                        "\"start_time\":\"" + START_TIME + "\"," +
                        "\"end_time\":\"" + END_TIME + "\"" +
                        "}",
                "{" +
                        "\"shop_id\":\"" + SHOP_ID + "\"," +
                        "\"region_id\":\"" + REGION_ID + "\"," +
                        "\"end_time\":\"" + END_TIME + "\"" +
                        "}",
                "{" +
                        "\"shop_id\":\"" + SHOP_ID + "\"," +
                        "\"region_id\":\"" + REGION_ID + "\"," +
                        "\"start_time\":\"" + START_TIME + "\"" +
                        "}",
        };
    }

    @DataProvider(name = "STATISTICS_MISS_PARA")
    public static Object[] badStatisticsMissPara() {

        return new String[]{
                "{" +
                        "\"region_id\":\"" + REGION_ID + "\"," +
                        "\"start_time\":\"" + START_TIME + "\"," +
                        "\"end_time\":\"" + END_TIME + "\"" +
                        "}",
                "{" +
                        "\"shop_id\":\"" + SHOP_ID + "\"," +
                        "\"region_id\":\"" + REGION_ID + "\"," +
                        "\"end_time\":\"" + END_TIME + "\"" +
                        "}",
                "{" +
                        "\"shop_id\":\"" + SHOP_ID + "\"," +
                        "\"region_id\":\"" + REGION_ID + "\"," +
                        "\"start_time\":\"" + START_TIME + "\"" +
                        "}",
        };
    }

    @DataProvider(name = "CURRENT_STATISTICS_MISS_PARA")
    public static Object[] badCurrentStatisticsMissPara() {

        return new String[]{
                "{" +
                        "\"region_id\":\"" + REGION_ID + "\"" +
                        "}"
        };
    }

    @DataProvider(name = "HISTORY_STATISTICS_MISS_PARA")
    public static Object[] badHistoryStatisticsMissPara() {

        return new String[]{
                "{" +
                        "\"start_time\":\"" + START_TIME + "\"," +
                        "\"end_time\":\"" + END_TIME + "\"" +
                        "}",
                "{" +
                        "\"shop_id\":\"" + SHOP_ID + "\"," +
                        "\"end_time\":\"" + END_TIME + "\"" +
                        "}",
                "{" +
                        "\"shop_id\":\"" + SHOP_ID + "\"," +
                        "\"start_time\":\"" + START_TIME + "\"" +
                        "}",
        };
    }
}
