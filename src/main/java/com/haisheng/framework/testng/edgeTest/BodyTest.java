package com.haisheng.framework.testng.edgeTest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.haisheng.framework.dao.ICaseDao;
import com.haisheng.framework.model.bean.Case;
import com.haisheng.framework.testng.CommonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.CommonDataStructure.LogMine;
import com.haisheng.framework.util.DingChatbot;
import com.haisheng.framework.util.HttpExecutorUtil;
import com.haisheng.framework.util.StatusCode;
import com.haisheng.framework.util.StringUtil;
import org.apache.commons.io.FileUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BodyTest {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private LogMine logMine       = new LogMine(logger);
    private SqlSession sqlSession = null;
    private ICaseDao caseDao      = null;
    private String request        = "";
    private String response       = "";
    private String expect         = null;
    private boolean IS_SUCCESS    = false;
    private String HOST     = "39.105.227.173"; //daily env
    private String URL      = "http://"+HOST+"/body/api";
    private String APP_KEY  = "TEST-APP-KEY";
    private String GRP_NAME = "TEST-GRP-NAME";
    private String USER_ID  = "TEST-USER-ID";



    //body-register
    @Test
    public void bodyRegisterByImageData() throws Exception{
        String caseName = "bodyRegisterByImageData";
        logMine.logCaseStart(caseName);
        try {
            expect = "register body can be get";
            Map<String, Object> paras = createRegisterMap(true);
            response = sendRequest(paras);
            verifySuccessRegisterResponse(paras, response);
            IS_SUCCESS = true;
            logMine.logCaseEnd(IS_SUCCESS, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            logMine.logCaseEnd(IS_SUCCESS, caseName);
            throw e;
        } finally {

            saveCaseToDb(caseName, request, response, expect, IS_SUCCESS);
        }

    }
    @Test
    public void bodyRegisterByDbinfo() throws Exception{
        String caseName = "bodyRegisterByDbinfo";
        logMine.logCaseStart(caseName);
        try {
            expect = "register body can be get";
            Map<String, Object> paras = createRegisterMap(false);
            response = sendRequest(paras);
            verifySuccessRegisterResponse(paras, response);
            IS_SUCCESS = true;
            logMine.logCaseEnd(IS_SUCCESS, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            logMine.logCaseEnd(IS_SUCCESS, caseName);
            throw e;
        } finally {

            saveCaseToDb(caseName, request, response, expect, IS_SUCCESS);
        }
    }

    //body-register
    @Test
    public void bodyRegisterInvalidSetID(String setID) {
        String caseName = "bodyRegisterInvalidSetID-"+setID;
        logMine.logCaseStart(caseName);
        try {
            expect = "register failure";

            IS_SUCCESS = true;
            logMine.logCaseEnd(IS_SUCCESS, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            logMine.logCaseEnd(IS_SUCCESS, caseName);
            throw e;
        } finally {

            saveCaseToDb(caseName, request, response, expect, IS_SUCCESS);
        }

    }

    //body-delete
    @Test
    public void bodyDelete() {
        String caseName = "bodyDelete";
        logMine.logCaseStart(caseName);
        try {
            expect = "the delete body can NOT be found";

            IS_SUCCESS = true;
            logMine.logCaseEnd(IS_SUCCESS, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            logMine.logCaseEnd(IS_SUCCESS, caseName);
            throw e;
        } finally {

            saveCaseToDb(caseName, request, response, expect, IS_SUCCESS);
        }

    }

    //body-delete
    @Test
    public void bodyDeleteInvalidGrpName() {
        String caseName = "bodyDeleteInvalidGrpName";
        logMine.logCaseStart(caseName);
        try {
            expect = "the delete body can NOT be found";

            IS_SUCCESS = true;
            logMine.logCaseEnd(IS_SUCCESS, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            logMine.logCaseEnd(IS_SUCCESS, caseName);
            throw e;
        } finally {

            saveCaseToDb(caseName, request, response, expect, IS_SUCCESS);
        }

    }
    //body-delete
    @Test
    public void bodyDeleteInvalidSetID() {
        String caseName = "bodyDeleteInvalidSetID";
        logMine.logCaseStart(caseName);
        try {
            expect = "the delete body can NOT be found";

            IS_SUCCESS = true;
            logMine.logCaseEnd(IS_SUCCESS, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            logMine.logCaseEnd(IS_SUCCESS, caseName);
            throw e;
        } finally {

            saveCaseToDb(caseName, request, response, expect, IS_SUCCESS);
        }

    }
    //body-delete
    @Test
    public void bodyDeleteInvalidBodyID() {
        String caseName = "bodyDeleteInvalidSetID";
        logMine.logCaseStart(caseName);
        try {
            expect = "the delete body can NOT be found";

            IS_SUCCESS = true;
            logMine.logCaseEnd(IS_SUCCESS, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            logMine.logCaseEnd(IS_SUCCESS, caseName);
            throw e;
        } finally {

            saveCaseToDb(caseName, request, response, expect, IS_SUCCESS);
        }

    }
    //body-delete
    @Test
    public void bodyDeleteInvalidUserID() {
        String caseName = "bodyDeleteInvalidUserID";
        logMine.logCaseStart(caseName);
        try {
            expect = "the delete body can NOT be found";

            IS_SUCCESS = true;
            logMine.logCaseEnd(IS_SUCCESS, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            logMine.logCaseEnd(IS_SUCCESS, caseName);
            throw e;
        } finally {

            saveCaseToDb(caseName, request, response, expect, IS_SUCCESS);
        }

    }

    //body-grp-delete
    @Test
    public void bodyDeleteGroup() {
        String caseName = "bodyDeleteGroup";
        logMine.logCaseStart(caseName);
        try {
            expect = "the delete body can NOT be found";

            IS_SUCCESS = true;
            logMine.logCaseEnd(IS_SUCCESS, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            logMine.logCaseEnd(IS_SUCCESS, caseName);
            throw e;
        } finally {

            saveCaseToDb(caseName, request, response, expect, IS_SUCCESS);
        }

    }

    //body-grp-delete
    @Test
    public void bodyDeleteGroupInvalidAppkey() {
        String caseName = "bodyDeleteGroupInvalidAppkey";
        logMine.logCaseStart(caseName);
        try {
            expect = "the delete body can NOT be found";

            IS_SUCCESS = true;
            logMine.logCaseEnd(IS_SUCCESS, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            logMine.logCaseEnd(IS_SUCCESS, caseName);
            throw e;
        } finally {

            saveCaseToDb(caseName, request, response, expect, IS_SUCCESS);
        }

    }
    //body-grp-delete
    @Test
    public void bodyDeleteGroupInvalidGrpName() {
        String caseName = "bodyDeleteGroupInvalidGrpName";
        logMine.logCaseStart(caseName);
        try {
            expect = "the delete body can NOT be found";

            IS_SUCCESS = true;
            logMine.logCaseEnd(IS_SUCCESS, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            logMine.logCaseEnd(IS_SUCCESS, caseName);
            throw e;
        } finally {

            saveCaseToDb(caseName, request, response, expect, IS_SUCCESS);
        }

    }

    //body-search
    @Test
    public void bodySearchInvalidAppkey() {
        String caseName = "bodySearchInvalidAppkey";
        logMine.logCaseStart(caseName);
        try {
            expect = "the delete body can NOT be found";

            IS_SUCCESS = true;
            logMine.logCaseEnd(IS_SUCCESS, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            logMine.logCaseEnd(IS_SUCCESS, caseName);
            throw e;
        } finally {

            saveCaseToDb(caseName, request, response, expect, IS_SUCCESS);
        }

    }
    //body-search
    @Test
    public void bodySearchInvalidGrpname() {
        String caseName = "bodySearchInvalidGrpname";
        logMine.logCaseStart(caseName);
        try {
            expect = "the delete body can NOT be found";

            IS_SUCCESS = true;
            logMine.logCaseEnd(IS_SUCCESS, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            logMine.logCaseEnd(IS_SUCCESS, caseName);
            throw e;
        } finally {

            saveCaseToDb(caseName, request, response, expect, IS_SUCCESS);
        }

    }
    //body-search
    @Test
    public void bodySearchInvalidImagedata() {
        String caseName = "bodySearchInvalidImagedata";
        logMine.logCaseStart(caseName);
        try {
            expect = "the delete body can NOT be found";

            IS_SUCCESS = true;
            logMine.logCaseEnd(IS_SUCCESS, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            logMine.logCaseEnd(IS_SUCCESS, caseName);
            throw e;
        } finally {

            saveCaseToDb(caseName, request, response, expect, IS_SUCCESS);
        }

    }
    //body-search
    @Test
    public void bodySearchInvalidDbinfo() {
        String caseName = "bodySearchInvalidDbinfo";
        logMine.logCaseStart(caseName);
        try {
            expect = "the delete body can NOT be found";

            IS_SUCCESS = true;
            logMine.logCaseEnd(IS_SUCCESS, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            logMine.logCaseEnd(IS_SUCCESS, caseName);
            throw e;
        } finally {

            saveCaseToDb(caseName, request, response, expect, IS_SUCCESS);
        }

    }
    //body-search
    @Test
    public void bodySearchInvalidDbinfoAndImagedata() {
        String caseName = "bodySearchInvalidDbinfoAndImagedata";
        logMine.logCaseStart(caseName);
        try {
            expect = "the delete body can NOT be found";

            IS_SUCCESS = true;
            logMine.logCaseEnd(IS_SUCCESS, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            logMine.logCaseEnd(IS_SUCCESS, caseName);
            throw e;
        } finally {

            saveCaseToDb(caseName, request, response, expect, IS_SUCCESS);
        }

    }
    //body-search
    @Test
    public void bodySearchInvalidResultNum() {
        String caseName = "bodySearchInvalidResultNum";
        logMine.logCaseStart(caseName);
        try {
            expect = "the delete body can NOT be found";

            IS_SUCCESS = true;
            logMine.logCaseEnd(IS_SUCCESS, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            logMine.logCaseEnd(IS_SUCCESS, caseName);
            throw e;
        } finally {

            saveCaseToDb(caseName, request, response, expect, IS_SUCCESS);
        }

    }

    //body-compare-user
    @Test
    public void bodyCompareUser() {
        String caseName = "bodyCompareUser";
        logMine.logCaseStart(caseName);
        try {
            expect = "the delete body can NOT be found";

            IS_SUCCESS = true;
            logMine.logCaseEnd(IS_SUCCESS, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            logMine.logCaseEnd(IS_SUCCESS, caseName);
            throw e;
        } finally {

            saveCaseToDb(caseName, request, response, expect, IS_SUCCESS);
        }

    }

    //body-compare-user
    @Test
    public void bodyCompareUserInvalidUser() {
        String caseName = "bodyCompareUserInvalidUser";
        logMine.logCaseStart(caseName);
        try {
            expect = "the delete body can NOT be found";

            IS_SUCCESS = true;
            logMine.logCaseEnd(IS_SUCCESS, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            logMine.logCaseEnd(IS_SUCCESS, caseName);
            throw e;
        } finally {

            saveCaseToDb(caseName, request, response, expect, IS_SUCCESS);
        }

    }
    //body-compare-user
    @Test
    public void bodyCompareUserInvalidAppkey() {
        String caseName = "bodyCompareUserInvalidAppkey";
        logMine.logCaseStart(caseName);
        try {
            expect = "the delete body can NOT be found";

            IS_SUCCESS = true;
            logMine.logCaseEnd(IS_SUCCESS, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            logMine.logCaseEnd(IS_SUCCESS, caseName);
            throw e;
        } finally {

            saveCaseToDb(caseName, request, response, expect, IS_SUCCESS);
        }

    }
    //body-compare-user
    @Test
    public void bodyCompareUserInvalidGrpname() {
        String caseName = "bodyCompareUserInvalidGrpname";
        logMine.logCaseStart(caseName);
        try {
            expect = "the delete body can NOT be found";

            IS_SUCCESS = true;
            logMine.logCaseEnd(IS_SUCCESS, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            logMine.logCaseEnd(IS_SUCCESS, caseName);
            throw e;
        } finally {

            saveCaseToDb(caseName, request, response, expect, IS_SUCCESS);
        }

    }
    //body-compare-user
    @Test
    public void bodyCompareUserInvalidResultNum() {
        String caseName = "bodyCompareUserInvalidResultNum";
        logMine.logCaseStart(caseName);
        try {
            expect = "the delete body can NOT be found";

            IS_SUCCESS = true;
            logMine.logCaseEnd(IS_SUCCESS, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            logMine.logCaseEnd(IS_SUCCESS, caseName);
            throw e;
        } finally {

            saveCaseToDb(caseName, request, response, expect, IS_SUCCESS);
        }

    }

    //body-compare
    @Test
    public void bodyCompareNoBase64() {
        String caseName = "bodyCompareNoBase64";
        logMine.logCaseStart(caseName);
        try {
            expect = "the delete body can NOT be found";

            IS_SUCCESS = true;
            logMine.logCaseEnd(IS_SUCCESS, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            logMine.logCaseEnd(IS_SUCCESS, caseName);
            throw e;
        } finally {

            saveCaseToDb(caseName, request, response, expect, IS_SUCCESS);
        }

    }
    //body-compare
    @Test
    public void bodyCompareBase64() {
        String caseName = "bodyCompareBase64";
        logMine.logCaseStart(caseName);
        try {
            expect = "the delete body can NOT be found";

            IS_SUCCESS = true;
            logMine.logCaseEnd(IS_SUCCESS, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            logMine.logCaseEnd(IS_SUCCESS, caseName);
            throw e;
        } finally {

            saveCaseToDb(caseName, request, response, expect, IS_SUCCESS);
        }

    }
    //body-compare
    @Test
    public void bodyCompareBase64AndNoBase64() {
        String caseName = "bodyCompareBase64AndNoBase64";
        logMine.logCaseStart(caseName);
        try {
            expect = "the delete body can NOT be found";

            IS_SUCCESS = true;
            logMine.logCaseEnd(IS_SUCCESS, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            logMine.logCaseEnd(IS_SUCCESS, caseName);
            throw e;
        } finally {

            saveCaseToDb(caseName, request, response, expect, IS_SUCCESS);
        }

    }

    //body-compare
    @Test
    public void bodyCompareInvalidAppkey() {
        String caseName = "bodyCompareInvalidAppkey";
        logMine.logCaseStart(caseName);
        try {
            expect = "the delete body can NOT be found";

            IS_SUCCESS = true;
            logMine.logCaseEnd(IS_SUCCESS, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            logMine.logCaseEnd(IS_SUCCESS, caseName);
            throw e;
        } finally {

            saveCaseToDb(caseName, request, response, expect, IS_SUCCESS);
        }

    }
    //body-compare
    @Test
    public void bodyCompareInvalidPicture() {
        String caseName = "bodyCompareInvalidPicture";
        logMine.logCaseStart(caseName);
        try {
            expect = "the delete body can NOT be found";

            IS_SUCCESS = true;
            logMine.logCaseEnd(IS_SUCCESS, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            logMine.logCaseEnd(IS_SUCCESS, caseName);
            throw e;
        } finally {

            saveCaseToDb(caseName, request, response, expect, IS_SUCCESS);
        }

    }

    //body-query-group
    @Test
    public void bodyQueryGroup() {
        String caseName = "bodyQueryGroup";
        logMine.logCaseStart(caseName);
        try {
            expect = "the delete body can NOT be found";

            IS_SUCCESS = true;
            logMine.logCaseEnd(IS_SUCCESS, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            logMine.logCaseEnd(IS_SUCCESS, caseName);
            throw e;
        } finally {

            saveCaseToDb(caseName, request, response, expect, IS_SUCCESS);
        }

    }
    //body-query-group
    @Test
    public void bodyQueryGroupNeedload() {
        String caseName = "bodyQueryGroupNeedload";
        logMine.logCaseStart(caseName);
        try {
            expect = "the delete body can NOT be found";

            IS_SUCCESS = true;
            logMine.logCaseEnd(IS_SUCCESS, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            logMine.logCaseEnd(IS_SUCCESS, caseName);
            throw e;
        } finally {

            saveCaseToDb(caseName, request, response, expect, IS_SUCCESS);
        }

    }

    //body-query-group
    @Test
    public void bodyQueryGroupInvalidAppkey() {
        String caseName = "bodyQueryGroupInvalidAppkey";
        logMine.logCaseStart(caseName);
        try {
            expect = "the delete body can NOT be found";

            IS_SUCCESS = true;
            logMine.logCaseEnd(IS_SUCCESS, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            logMine.logCaseEnd(IS_SUCCESS, caseName);
            throw e;
        } finally {

            saveCaseToDb(caseName, request, response, expect, IS_SUCCESS);
        }

    }
    //body-query-group
    @Test
    public void bodyQueryGroupInvalidGrpname() {
        String caseName = "bodyQueryGroupInvalidGrpname";
        logMine.logCaseStart(caseName);
        try {
            expect = "the delete body can NOT be found";

            IS_SUCCESS = true;
            logMine.logCaseEnd(IS_SUCCESS, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            logMine.logCaseEnd(IS_SUCCESS, caseName);
            throw e;
        } finally {

            saveCaseToDb(caseName, request, response, expect, IS_SUCCESS);
        }

    }


    private void setRequestToGlobalPara(Object obj) {
        request = JSON.toJSONString(obj);
    }

    private Map<String, Object> createRegisterMap(boolean isImageData) {
        Map<String, Object> paras = new ConcurrentHashMap<>();
        paras.put("request_id", UUID.randomUUID().toString());
        paras.put("app_key", APP_KEY);
        paras.put("group_name", GRP_NAME);
        paras.put("method", "REGISTER");
        paras.put("set_id", UUID.randomUUID().toString());
        paras.put("user_id", USER_ID);
        paras.put("is_quality_limit", "true");
        if (isImageData) {
            paras.put("image_data", "{\"axis\":[1323,175,1575,807],\"body_url\":\"http://retail-huabei2.oss-cn-beijing-internal.aliyuncs.com/dispatcher_daily/uid_e0d1ebec/a4d4d18741a8/3/20190409/e21bf774-5a9f-414b-81ea-9decc05a_0?Expires=1554863192&OSSAccessKeyId=LTAIlYpjA39n18Yr&Signature=OF5xajkTuBSaBwsjyp%2Bk4d1A31g%3D\",\"quality\":0.76550233}");
        } else {
            paras.put("db_info", "{\"app_key\":\"who-request\",\"body_id\":\"bb5cdd54fe38b1ba00d022a5cc81d132\",\"group_name\":\"8-2019-04-02\",\"set_id\":\"91c284e6-1e8b-41c0-9a1f-b3af3d26__153\"}");
        }
        setRequestToGlobalPara(paras);

        return paras;
    }

    private String sendRequest(Map<String, Object> params) throws Exception {
        HttpExecutorUtil executor = new HttpExecutorUtil();
        executor.doPostJson(URL,params);
        verifyResponseSuccessCode(executor.getResponse(), params);
        return executor.getResponse();
    }

    private void verifySuccessRegisterResponse(Map<String, Object> expect, String response) throws Exception{
        JSONObject body = JSON.parseObject(response).getJSONObject("body");

        verifyBodyEqual(body, expect, "app_key");
        verifyBodyEqual(body, expect, "group_name");
        verifyBodyEqual(body, expect, "set_id");
        verifyBodyEqual(body, expect, "user_id");
        expect.put("body_id", body.getString("bodyId"));
        verifyBodyId(body.getString("bodyId"), expect);

    }

    //verify if the bodyID existed by SEARCH_BODY method
    private void verifyBodyId(String bodyID, Map<String, Object> known) throws Exception{
        Map<String, Object> hm = new ConcurrentHashMap<>();
        hm.put("request_id", UUID.randomUUID().toString());
        hm.put("app_key", known.get("app_key"));
        hm.put("group_name", known.get("group_name"));
        hm.put("method", "SEARCH_BODY");
        if (known.containsKey("db_info")) {
            hm.put("db_info", known.get("db_info"));
        } else if (known.containsKey("image_data")) {
            hm.put("image_data", known.get("image_data"));
        }
        hm.put("result_num", 1);
        String response = sendRequest(hm);
        JSONObject resultBody = JSON.parseObject(response)
                .getJSONArray("bodyL")
                .getJSONObject(0)
                .getJSONArray("resultBody")
                .getJSONObject(0);
        verifyQueryBodyData(resultBody, known);
    }

    private void verifyQueryBodyData(JSONObject resultBody, Map<String, Object> known) throws Exception {
        verifyBodyEqual(resultBody, known, "body_id");
        verifyBodyEqual(resultBody, known, "group_name");
        verifyBodyEqual(resultBody, known, "set_id");
        verifyBodyEqual(resultBody, known, "user_id");
    }


    private void verifyBodyEqual(JSONObject body, Map<String, Object> expectMap, String key) throws Exception{
        String camelKey = StringUtil.changeUnderLineToLittleCamel(key);
        if (! body.containsKey(camelKey)) {
            throw new Exception("response body do NOT contains: " + camelKey);
        }

        String actual = body.getString(camelKey);
        String expect = (String) expectMap.get(key);
        if (! expect.equals(actual)) {
            throw new Exception("expect " + key + ": " + expect + ", actual: " + actual);
        }
    }
    private void verifyResponseSuccessCode(String response, Map<String, Object> expect) throws Exception{
        logger.info(response);
        JSONObject resJson = JSON.parseObject(response);
        int code = resJson.getInteger("code");
        if (code != StatusCode.SUCCESS) {
            throw new Exception("response expect 1000, actual: " + code);
        }

        String requestId = resJson.getString("requestId");
        if (! requestId.equals(expect.get("request_id"))) {
            throw new Exception("response expect requestid: " + expect.get("request_id") + ", actual: " + requestId);
        }
    }


    private void createPressTestData() throws Exception {

        ArrayList<String> userId = new ArrayList();
        String dir = "/Users/yuhaisheng/jason/document/work/bodyPerfomanceData/ ";

        for (int i=1; i<=10; i++) {
            for (int j = 0; j < 100000; j++) {
                String id = UUID.randomUUID().toString();
                userId.add(id);
            }
            String filename = dir + i + "0w-userid.csv";
            FileUtils.writeLines(new File(filename ), userId, true);
        }


    }

    private void saveCaseToDb(String caseName, String request, String response, String expect, boolean isSuccess) {

        Case checklist = new Case();
        int appId = ChecklistDbInfo.DB_APP_ID_CLOUD_SERVICE;
        int configId = ChecklistDbInfo.DB_SERVICE_ID_BODY_SERVICE;
        List<Integer> listId = caseDao.queryCaseByName(appId,
                configId,
                caseName);
        int id = -1;
        if (listId.size() > 0) {
            checklist.setId(listId.get(0));
        }
        checklist.setApplicationId(appId);
        checklist.setConfigId(configId);
        checklist.setCaseName(caseName);
        checklist.setEditTime(new Timestamp(System.currentTimeMillis()));
        checklist.setQaOwner("于海生");
        checklist.setRequestData(request);
        checklist.setResponse(response);
        checklist.setExpect(expect);
        if (isSuccess) {
            checklist.setResult("PASS");
        } else {
            checklist.setResult("FAIL");
        }
        caseDao.insert(checklist);
        sqlSession.commit();

    }

    private void dingdingAlarm(String summary, String detail, String requestId, String atPerson) {
        detail = "请求requestid: " + requestId + " \n" + detail;
        //screenshot do not support local pic, must use pic in web
        String bugPic = "http://i01.lw.aliimg.com/media/lALPBbCc1ZhJGIvNAkzNBLA_1200_588.png";
        String linkUrl = "http://192.168.50.2:8080/view/云端测试/job/pv-cloud-test/Test_20Report/";
        String msg = DingChatbot.getMarkdown(summary, detail, bugPic, linkUrl, atPerson);
        DingChatbot.sendMarkdown(msg);
    }

    @BeforeSuite
    public void initial() {
        logger.debug("initial");
        SqlSessionFactory sessionFactory = null;
        String resource = "configuration.xml";
        try {
            sessionFactory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsReader(
                    resource));
            sqlSession = sessionFactory.openSession();
            caseDao = sqlSession.getMapper(ICaseDao.class);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @AfterSuite
    public void clean() {
        logger.info("clean");
        sqlSession.close();
        if (IS_SUCCESS) {
            dingdingAlarm("人体算法回归测试失败", "请点击下面详细链接查看log", "", "@刘峤 @蔡思明");
        }
    }
}
