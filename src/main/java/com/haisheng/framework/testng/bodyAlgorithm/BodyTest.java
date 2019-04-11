package com.haisheng.framework.testng.bodyAlgorithm;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
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
import org.testng.annotations.DataProvider;
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
    private String BODY_ID  = "bb5cdd54fe38b1ba00d022a5cc81d132";
    private String KEY_APPKEY  = "app_key";
    private String KEY_GRPNAME = "group_name";
    private String KEY_USERID  = "user_id";
    private String KEY_DBINFO  = "db_info";
    private String KEY_IMAGE   = "image_data";
    private String KEY_SETID   = "set_id";
    private String KEY_BODYID  = "body_id";
    private String KEY_RESULTNUM  = "result_num";



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

    @Test(dataProvider = "MISSING_PARA")
    public void bodyRegisterMissingPara(String para) throws Exception{
        String caseName = "bodyRegisterMissingPara-"+para;
        logMine.logCaseStart(caseName);
        try {
            expect = String.valueOf(StatusCode.BAD_REQUEST);
            Map<String, Object> paras = createRegisterMap(false);
            removeRequestMap(paras, para);
            response = sendRequestOnly(paras);
            verifyResponseByCode(StatusCode.BAD_REQUEST, response);
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
    @Test(dataProvider = "PUNCTUATION", dataProviderClass = com.haisheng.framework.testng.CommonDataStructure.InvalidPara.class)
    public void bodyRegisterSetID(String setID) throws Exception{
        String caseName = "bodyRegisterSetID-"+setID;
        logMine.logCaseStart(caseName);
        try {
            expect = "register body can be get";
            Map<String, Object> paras = createRegisterMap(false);
            modifyRequestMap(paras, KEY_SETID, setID);
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

    //body-delete
    @Test(dataProvider = "PUNCTUATION", dataProviderClass = com.haisheng.framework.testng.CommonDataStructure.InvalidPara.class)
    public void bodyDelete(String setID) throws Exception{
        String caseName = "bodyDelete-";
        logMine.logCaseStart(caseName);
        try {
            expect = "body can be deleted and can NOT be get again";
            Map<String, Object> paras = createDeleteMap(false);
            modifyRequestMap(paras, KEY_SETID, setID);
            response = sendRequest(paras);
            verifySuccessDeleteResponse(paras, response);
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

    @Test(dataProvider = "MISSING_PARA")
    public void bodyDeleteMissingPara(String para) throws Exception{
        String caseName = "bodyDeleteMissingPara-"+para;
        logMine.logCaseStart(caseName);
        try {
            expect = String.valueOf(StatusCode.BAD_REQUEST);
            Map<String, Object> paras = createDeleteMap(false);
            removeRequestMap(paras, para);
            response = sendRequestOnly(paras);
            verifyResponseByCode(StatusCode.BAD_REQUEST, response);
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
    @Test(dataProvider = "PUNCTUATION", dataProviderClass = com.haisheng.framework.testng.CommonDataStructure.InvalidPara.class)
    public void bodyDeleteInvalidGrpName(String grpID) throws Exception{
        String caseName = "bodyDeleteInvalidGrpName-" + grpID;
        logMine.logCaseStart(caseName);
        try {
            expect = String.valueOf(StatusCode.BAD_REQUEST);
            Map<String, Object> paras = createDeleteMap(false);
            modifyRequestMap(paras, KEY_GRPNAME, grpID);
            response = sendRequestOnly(paras);
            verifyResponseByCode(StatusCode.BAD_REQUEST, response);
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
    @Test(dataProvider = "PUNCTUATION", dataProviderClass = com.haisheng.framework.testng.CommonDataStructure.InvalidPara.class)
    public void bodyDeleteInvalidAppkey(String appKey) throws Exception{
        String caseName = "bodyDeleteInvalidAppkey-" + appKey;
        logMine.logCaseStart(caseName);
        try {
            expect = String.valueOf(StatusCode.BAD_REQUEST);
            Map<String, Object> paras = createDeleteMap(false);
            modifyRequestMap(paras, KEY_APPKEY, appKey);
            response = sendRequestOnly(paras);
            verifyResponseByCode(StatusCode.BAD_REQUEST, response);
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
    @Test(dataProvider = "PUNCTUATION", dataProviderClass = com.haisheng.framework.testng.CommonDataStructure.InvalidPara.class)
    public void bodyDeleteInvalidSetID(String setID) throws Exception{
        String caseName = "bodyDeleteInvalidSetID-" + setID;
        logMine.logCaseStart(caseName);
        try {
            expect = String.valueOf(StatusCode.BAD_REQUEST);
            Map<String, Object> paras = createDeleteMap(false);
            modifyRequestMap(paras, KEY_SETID, setID);
            response = sendRequest(paras);
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
    @Test(dataProvider = "PUNCTUATION", dataProviderClass = com.haisheng.framework.testng.CommonDataStructure.InvalidPara.class)
    public void bodyDeleteInvalidBodyID(String bodyID) throws Exception{
        String caseName = "bodyDeleteInvalidBodyID-" + bodyID;
        logMine.logCaseStart(caseName);
        try {
            expect = String.valueOf(StatusCode.BAD_REQUEST);
            Map<String, Object> paras = createDeleteMap(false);
            modifyRequestMap(paras, KEY_BODYID, bodyID);
            response = sendRequest(paras);
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
    public void bodyDeleteGroup() throws Exception{
        String caseName = "bodyDeleteGroup";
        logMine.logCaseStart(caseName);
        try {
            expect = "the delete group can NOT be found";
            Map<String, Object> paras = createDeleteGrpMap();
            response = sendRequest(paras);
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
    @Test(dataProvider = "PUNCTUATION", dataProviderClass = com.haisheng.framework.testng.CommonDataStructure.InvalidPara.class)
    public void bodyDeleteGroupInvalidAppkey(String appKey) throws Exception{
        String caseName = "bodyDeleteGroupInvalidAppkey-"+appKey;
        logMine.logCaseStart(caseName);
        try {
            expect = String.valueOf(StatusCode.BAD_REQUEST);
            Map<String, Object> paras = createDeleteMap(false);
            modifyRequestMap(paras, KEY_APPKEY, appKey);
            response = sendRequestOnly(paras);
            verifyResponseByCode(StatusCode.BAD_REQUEST, response);
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
    @Test(dataProvider = "PUNCTUATION", dataProviderClass = com.haisheng.framework.testng.CommonDataStructure.InvalidPara.class)
    public void bodyDeleteGroupInvalidGrpname(String grpName) throws Exception{
        String caseName = "bodyDeleteGroupInvalidGrpname-"+grpName;
        logMine.logCaseStart(caseName);
        try {
            expect = String.valueOf(StatusCode.BAD_REQUEST);
            Map<String, Object> paras = createDeleteMap(false);
            modifyRequestMap(paras, KEY_GRPNAME, grpName);
            response = sendRequestOnly(paras);
            verifyResponseByCode(StatusCode.BAD_REQUEST, response);
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
    @Test(dataProvider = "PUNCTUATION", dataProviderClass = com.haisheng.framework.testng.CommonDataStructure.InvalidPara.class)
    public void bodySearchInvalidAppkey(String appkey) throws Exception{
        String caseName = "bodySearchInvalidAppkey-"+appkey;
        logMine.logCaseStart(caseName);
        try {
            expect = String.valueOf(StatusCode.NO_GROUP);
            Map<String, Object> paras = createBodySearchMap(true);
            modifyRequestMap(paras, KEY_APPKEY, appkey);
            response = sendRequestOnly(paras);
            verifyResponseByCode(StatusCode.NO_GROUP, response);
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
    @Test(dataProvider = "PUNCTUATION", dataProviderClass = com.haisheng.framework.testng.CommonDataStructure.InvalidPara.class)
    public void bodySearchInvalidGrpname(String grpName) throws Exception{
        String caseName = "bodySearchInvalidGrpname-"+grpName;
        logMine.logCaseStart(caseName);
        try {
            expect = String.valueOf(StatusCode.NO_GROUP);
            Map<String, Object> paras = createBodySearchMap(true);
            modifyRequestMap(paras, KEY_GRPNAME, grpName);
            response = sendRequestOnly(paras);
            verifyResponseByCode(StatusCode.NO_GROUP, response);
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
    @Test(dataProvider = "PUNCTUATION", dataProviderClass = com.haisheng.framework.testng.CommonDataStructure.InvalidPara.class)
    public void bodySearchInvalidImagedata(String data) throws Exception{
        String caseName = "bodySearchInvalidImagedata-"+data;
        logMine.logCaseStart(caseName);
        try {
            expect = String.valueOf(StatusCode.UNKNOWN_ERROR);
            Map<String, Object> paras = createBodySearchMap(false);
            modifyRequestMap(paras, KEY_IMAGE, data);
            response = sendRequestOnly(paras);
            verifyResponseByCode(StatusCode.UNKNOWN_ERROR, response);

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
    @Test(dataProvider = "PUNCTUATION", dataProviderClass = com.haisheng.framework.testng.CommonDataStructure.InvalidPara.class)
    public void bodySearchInexistImagedata(String data) throws Exception{
        String caseName = "bodySearchInexistImagedata-bodyurl-"+data;
        logMine.logCaseStart(caseName);
        try {
            expect = String.valueOf(StatusCode.BAD_REQUEST);
            Map<String, Object> paras = createBodySearchMap(false);
            modifyRequestMap(paras, KEY_IMAGE, getImageData().replace("http://", data));
            response = sendRequestOnly(paras);
            verifyResponseByCode(StatusCode.BAD_REQUEST, response);

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
    @Test(dataProvider = "PUNCTUATION", dataProviderClass = com.haisheng.framework.testng.CommonDataStructure.InvalidPara.class)
    public void bodySearchInvalidDbinfo(String data) throws Exception{
        String caseName = "bodySearchInvalidDbinfo-"+data;
        logMine.logCaseStart(caseName);
        try {
            expect = String.valueOf(StatusCode.UNKNOWN_ERROR);
            Map<String, Object> paras = createBodySearchMap(true);
            modifyRequestMap(paras, KEY_DBINFO, data);
            response = sendRequestOnly(paras);
            verifyResponseByCode(StatusCode.UNKNOWN_ERROR, response);

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
    @Test(dataProvider = "PUNCTUATION", dataProviderClass = com.haisheng.framework.testng.CommonDataStructure.InvalidPara.class)
    public void bodySearchInexistDbinfo(String data) throws Exception{
        String caseName = "bodySearchInexistDbinfo-bodyid-"+data;
        logMine.logCaseStart(caseName);
        try {
            expect = String.valueOf(StatusCode.BAD_REQUEST);
            Map<String, Object> paras = createBodySearchMap(true);
            modifyRequestMap(paras, KEY_IMAGE, getDbinfo().replace(BODY_ID, data));
            response = sendRequestOnly(paras);
            verifyResponseByCode(StatusCode.BAD_REQUEST, response);

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
    @Test(dataProvider = "PUNCTUATION_CHAR", dataProviderClass = com.haisheng.framework.testng.CommonDataStructure.InvalidPara.class)
    public void bodySearchPuncutationResultNum(String resultNum) throws Exception{
        String caseName = "bodySearchPuncutationResultNum-"+resultNum;
        logMine.logCaseStart(caseName);
        try {
            expect = String.valueOf(StatusCode.UNKNOWN_ERROR);
            Map<String, Object> paras = createBodySearchMap(true);
            modifyRequestMap(paras, KEY_RESULTNUM, resultNum);
            response = sendRequestOnly(paras);
            verifyResponseByCode(StatusCode.UNKNOWN_ERROR, response);

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
    //已存在bug，http://192.168.50.3:8081/bug-view-44.html
    //错误返回类型未统一，用例暂时关闭
    @Test(dataProvider = "DIGITAL", dataProviderClass = com.haisheng.framework.testng.CommonDataStructure.InvalidPara.class)
    public void bodySearchDigitalResultNum(String resultNum) throws Exception{
        String caseName = "bodySearchDigitalResultNum-"+resultNum;
        logMine.logCaseStart(caseName);
        try {
            expect = String.valueOf(StatusCode.BAD_REQUEST);
            Map<String, Object> paras = createBodySearchMap(true);
            modifyRequestMap(paras, KEY_RESULTNUM, resultNum);
            response = sendRequestOnly(paras);
            verifyResponseByCode(StatusCode.BAD_REQUEST, response);

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
    public void bodyCompareUser() throws Exception{
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
    public void bodyCompareUserInvalidUser() throws Exception{
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
    public void bodyCompareUserInvalidAppkey() throws Exception{
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
    public void bodyCompareUserInvalidGrpname() throws Exception{
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
    public void bodyCompareUserInvalidResultNum() throws Exception{
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
    public void bodyCompareNoBase64() throws Exception{
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
    public void bodyCompareBase64() throws Exception{
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
    public void bodyCompareBase64AndNoBase64() throws Exception{
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
    public void bodyCompareInvalidAppkey() throws Exception{
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
    public void bodyCompareInvalidPicture() throws Exception{
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
    public void bodyQueryGroup() throws Exception{
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
    public void bodyQueryGroupNeedload() throws Exception{
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
    public void bodyQueryGroupInvalidAppkey() throws Exception{
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
    public void bodyQueryGroupInvalidGrpname() throws Exception{
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


    private void modifyRequestMap(Map<String, Object>map, String key, String value) {
        map.put(key, value);
        setRequestToGlobalPara(map);
    }

    private void removeRequestMap(Map<String, Object>map, String key) {
        map.remove(key);
        setRequestToGlobalPara(map);
    }

    private String getImageData() {
        return "{\"axis\":[1323,175,1575,807],\"body_url\":\"http://retail-huabei2.oss-cn-beijing-internal.aliyuncs.com/dispatcher_daily/uid_e0d1ebec/a4d4d18741a8/3/20190409/e21bf774-5a9f-414b-81ea-9decc05a_0?Expires=1554863192&OSSAccessKeyId=LTAIlYpjA39n18Yr&Signature=OF5xajkTuBSaBwsjyp%2Bk4d1A31g%3D\",\"quality\":0.76550233}";
    }

    private String getDbinfo() {
        return "{\"app_key\":\"who-request\",\"body_id\":\"bb5cdd54fe38b1ba00d022a5cc81d132\",\"group_name\":\"8-2019-04-02\",\"set_id\":\"91c284e6-1e8b-41c0-9a1f-b3af3d26__153\"}";
    }

    private Map<String, Object> createRegisterMap(boolean isImageData) {
        Map<String, Object> paras = new ConcurrentHashMap<>();
        paras.put("request_id", UUID.randomUUID().toString());
        paras.put(KEY_APPKEY, APP_KEY);
        paras.put(KEY_GRPNAME, GRP_NAME);
        paras.put("method", "REGISTER");
        paras.put(KEY_SETID, UUID.randomUUID().toString());
        paras.put(KEY_USERID, USER_ID);
        paras.put("is_quality_limit", "true");
        if (isImageData) {
            paras.put(KEY_IMAGE, getImageData());
        } else {
            paras.put(KEY_DBINFO, getDbinfo());
        }
        setRequestToGlobalPara(paras);

        return paras;
    }

    private Map<String, Object> createDeleteMap(boolean isUserID) {
        Map<String, Object> paras = new ConcurrentHashMap<>();
        paras.put("request_id", UUID.randomUUID().toString());
        paras.put(KEY_APPKEY, APP_KEY);
        paras.put(KEY_GRPNAME, GRP_NAME);
        paras.put("method", "DELETE_BODY");
        paras.put(KEY_SETID, UUID.randomUUID().toString());
        paras.put(KEY_USERID, USER_ID);
        paras.put(KEY_BODYID, BODY_ID);
        if (isUserID) {
            paras.put(KEY_USERID, USER_ID);
        }
        setRequestToGlobalPara(paras);

        return paras;
    }

    private Map<String, Object> createDeleteGrpMap() {
        Map<String, Object> paras = new ConcurrentHashMap<>();
        paras.put("request_id", UUID.randomUUID().toString());
        paras.put(KEY_APPKEY, APP_KEY);
        paras.put(KEY_GRPNAME, GRP_NAME);
        paras.put("method", "DELETE_GROUP");
        setRequestToGlobalPara(paras);

        return paras;
    }

    private Map<String, Object> createBodySearchMap(boolean isDbInfo) {
        Map<String, Object> paras = new ConcurrentHashMap<>();
        paras.put("request_id", UUID.randomUUID().toString());
        paras.put(KEY_APPKEY, APP_KEY);
        paras.put(KEY_GRPNAME, GRP_NAME);
        paras.put("method", "SEARCH_BODY");
        if (isDbInfo) {
            paras.put(KEY_DBINFO, getDbinfo());
        } else {
            paras.put(KEY_IMAGE, getImageData());
        }
        paras.put(KEY_RESULTNUM, 500);
        setRequestToGlobalPara(paras);

        return paras;
    }

    private String sendRequestOnly(Map<String, Object> params) throws Exception {
        HttpExecutorUtil executor = new HttpExecutorUtil();
        executor.doPostJson(URL,params);
        return executor.getResponse();
    }

    private void verifyResponseByCode(int expect, String response) throws Exception{
        int code = JSON.parseObject(response).getInteger("code");
        if (code != expect) {
            throw new Exception("expect code: " + expect + ", actual: " + code);
        }


    }

    private String sendRequest(Map<String, Object> params) throws Exception {
        HttpExecutorUtil executor = new HttpExecutorUtil();
        executor.doPostJson(URL,params);
        verifyResponseSuccessCode(executor.getResponse(), params);
        return executor.getResponse();
    }

    private void verifySuccessDeleteResponse(Map<String, Object> expect, String response) throws Exception{
        JSONObject body = JSON.parseObject(response).getJSONObject("request");

        verifyBodyEqual(body, expect, KEY_APPKEY);
        verifyBodyEqual(body, expect, KEY_BODYID);
        verifyBodyEqual(body, expect, KEY_GRPNAME);
        verifyBodyEqual(body, expect, KEY_SETID);
        verifyBodyIdInexisted(body.getString("bodyId"), expect);
    }

    private void verifySuccessRegisterResponse(Map<String, Object> expect, String response) throws Exception{
        JSONObject body = JSON.parseObject(response).getJSONObject("body");

        verifyBodyEqual(body, expect, KEY_APPKEY);
        verifyBodyEqual(body, expect, KEY_GRPNAME);
        verifyBodyEqual(body, expect, KEY_SETID);
        verifyBodyEqual(body, expect, KEY_USERID);
        expect.put("body_id", body.getString("bodyId"));
        verifyBodyId(body.getString("bodyId"), expect);

    }

    //verify if the bodyID existed by SEARCH_BODY method
    private void verifyBodyId(String bodyID, Map<String, Object> known) throws Exception{
        Map<String, Object> hm = new ConcurrentHashMap<>();
        hm.put("request_id", UUID.randomUUID().toString());
        hm.put(KEY_APPKEY, known.get(KEY_APPKEY));
        hm.put(KEY_GRPNAME, known.get(KEY_GRPNAME));
        hm.put("method", "SEARCH_BODY");
        if (known.containsKey(KEY_DBINFO)) {
            hm.put(KEY_DBINFO, known.get(KEY_DBINFO));
        } else if (known.containsKey(KEY_IMAGE)) {
            hm.put(KEY_IMAGE, known.get(KEY_IMAGE));
        }
        hm.put(KEY_RESULTNUM, 500);
        String response = sendRequest(hm);
        JSONArray bodyArray = JSON.parseObject(response)
                .getJSONArray("bodyL")
                .getJSONObject(0)
                .getJSONArray("resultBody");
        verifyBodyArray(bodyArray, known);
    }
    //verify if the bodyID inexisted by SEARCH_BODY method
    private void verifyBodyIdInexisted(String bodyID, Map<String, Object> known) throws Exception{
        Map<String, Object> hm = new ConcurrentHashMap<>();
        hm.put("request_id", UUID.randomUUID().toString());
        hm.put(KEY_APPKEY, known.get(KEY_APPKEY));
        hm.put(KEY_GRPNAME, known.get(KEY_GRPNAME));
        hm.put("method", "SEARCH_BODY");
        if (known.containsKey(KEY_DBINFO)) {
            hm.put(KEY_DBINFO, known.get(KEY_DBINFO));
        } else if (known.containsKey(KEY_IMAGE)) {
            hm.put(KEY_IMAGE, known.get(KEY_IMAGE));
        }
        hm.put(KEY_RESULTNUM, 500);
        String response = sendRequest(hm);
        JSONArray bodyArray = JSON.parseObject(response)
                .getJSONArray("bodyL")
                .getJSONObject(0)
                .getJSONArray("resultBody");
        verifyBodyInexisted(bodyArray, known);
    }

    private void verifyBodyArray(JSONArray array, Map<String, Object> known) throws Exception{
        boolean isExisted = false;
        for (int i=0; i<array.size(); i++) {
            JSONObject resultBody = array.getJSONObject(i);
            isExisted = isQueryBodyDataExisted(resultBody, known);
            if (isExisted) {
                break;
            }
        }
        if (! isExisted) {
            throw new Exception("no expect body be found in response. expect body: " + JSON.toJSONString(known));
        }
    }

    private void verifyBodyInexisted(JSONArray array, Map<String, Object> known) throws Exception{
        boolean isExisted = false;
        for (int i=0; i<array.size(); i++) {
            JSONObject resultBody = array.getJSONObject(i);
            isExisted = isQueryBodyDataExisted(resultBody, known);
            if (isExisted) {
                break;
            }
        }
        if (isExisted) {
            throw new Exception("expect found no body info, but found it. expect body: " + JSON.toJSONString(known));
        }
    }

    private void verifyQueryBodyData(JSONObject resultBody, Map<String, Object> known) throws Exception {
        verifyBodyEqual(resultBody, known, "body_id");
        verifyBodyEqual(resultBody, known, KEY_GRPNAME);
        verifyBodyEqual(resultBody, known, KEY_SETID);
        verifyBodyEqual(resultBody, known, KEY_USERID);
    }

    private boolean isQueryBodyDataExisted(JSONObject resultBody, Map<String, Object> known){
        boolean result = true;
        result &= isBodyExisted(resultBody, known, "body_id");
        result &= isBodyExisted(resultBody, known, KEY_GRPNAME);
        result &= isBodyExisted(resultBody, known, KEY_SETID);
        result &= isBodyExisted(resultBody, known, KEY_USERID);

        return result;
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

    private boolean isBodyExisted(JSONObject body, Map<String, Object> expectMap, String key) {
        String camelKey = StringUtil.changeUnderLineToLittleCamel(key);
        if (! body.containsKey(camelKey)) {
            logger.error("response body do NOT contains: " + camelKey);
            return false;
        }

        String actual = body.getString(camelKey);
        String expect = (String) expectMap.get(key);
        if (expect.equals(actual)) {
            return true;
        }
        return false;
    }

    private void verifyResponseSuccessCode(String response, Map<String, Object> expect) throws Exception{
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

    @DataProvider(name = "MISSING_PARA")
    public Object[] registerMissing () {
        return new String[]{
                KEY_APPKEY,
                KEY_GRPNAME,
                KEY_USERID,
                KEY_DBINFO,
                KEY_SETID
        };
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
