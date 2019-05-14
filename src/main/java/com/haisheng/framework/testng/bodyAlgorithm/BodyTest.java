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
    private boolean IS_SUCCESS    = true;
    private String HOST     = "39.105.227.173"; //daily env
    private String URL      = "http://"+HOST+"/body/api";

    private String APP_KEY          = "TEST-APP-KEY";
    private String GRP_NAME         = "TEST-GRP-NAME";
    private String USER_ID          = "TEST-USER-ID";
    private String BODY_ID          = "bb5cdd54fe38b1ba00d022a5cc81d132";
    private String USER_COMPARE_ID1 = "zhangfan1";
    private String USER_COMPARE_ID2 = "zhangfan2";
    private String GRP_COMPARE_MERGE     = "compare-user-grp-not-merge";
    private String GRP_COMPARE_NOT_MERGE = "compare-user-grp-merge";
    private String PIC_A            = "";
    private String PIC_B            = "";
    private String BODY_A_ID        = "ae46a2bb2faf6dc19b3c546f8e189251";
    private String BODY_B_ID        = "254952f104df4793b60ed05232de226a";
    private String MSG_MISSING      = "null";
    private String MSG_ERROR        = "error";
    private String MSG_INVALID      = "missing";

    private String KEY_APPKEY  = "app_key";
    private String KEY_GRPNAME = "group_name";
    private String KEY_USERID  = "user_id";
    private String KEY_DBINFO  = "db_info";
    private String KEY_IMAGE   = "image_data";
    private String KEY_SETID   = "set_id";
    private String KEY_BODYID  = "body_id";
    private String KEY_RESULTNUM  = "result_num";
    private String KEY_NEEDLOAD   = "need_load";
    private String KEY_USERA      = "user_a";
    private String KEY_USERB      = "user_b";
    private String KEY_PICTUREA   = "picture_a";
    private String KEY_PICTUREB   = "picture_b";



    //body-register
    //鉴于图片有过期时间，此用例暂时关闭
    //@Test(priority = 0)
    public void bodyRegisterByImageData() throws Exception{
        String caseName = "bodyRegisterByImageData";
        logMine.logCaseStart(caseName);
        boolean isSuccess = true;
        try {
            expect = "register body can be get";
            Map<String, Object> paras = createRegisterMap(true);
            response = sendRequest(paras);
            verifySuccessRegisterResponse(paras, response);

            logMine.logCaseEnd(true, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            isSuccess = false;
            logMine.logCaseEnd(isSuccess, caseName);
            throw e;
        } finally {

            saveCaseToDb(caseName, request, response, expect, isSuccess);
        }

    }

    /**
     * @Description:  用db_info注册，之后查询，并对查询结果的resultBody域检查，看
     * 是否包含setid，userid，grpName，bodyId.
     * @Param: []
     * @return: void
     * @Author: Shine
     * @Date: 2019/4/17
     */
    @Test(priority = 0)
    public void bodyRegisterByDbinfo() throws Exception{
        String caseName = "bodyRegisterByDbinfo";
        logMine.logCaseStart(caseName);
        boolean isSuccess = true;
        try {
            expect = "register body can be get";
//          1、组织入参
//          必：user_id 非：db_info/image_data,set_id,is_quality_limit
            Map<String, Object> paras = createRegisterMap(false);
//          2、执行(里面还校验了状态码)
            response = sendRequest(paras);
//          3、验证结果的准确性(用body域（就是data域）的信息校验的)
            verifySuccessRegisterResponse(paras, response);

            logMine.logCaseEnd(true, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            isSuccess = false;
            logMine.logCaseEnd(isSuccess, caseName);
            throw e;
        } finally {
            saveCaseToDb(caseName, request, response, expect, isSuccess);
        }
    }

    /**
     * @Description: 缺失参数时期待返回1001
     * @Param: [para]
     * @return: void
     * @Author: Shine
     * @Date: 2019/4/17
     */
    @Test(dataProvider = "MISSING_PARA", priority = 0)
    public void bodyRegisterMissingPara(String para) throws Exception{
        String caseName = "bodyRegisterMissingPara-"+para;
        logMine.logCaseStart(caseName);
        boolean isSuccess = true;
        try {
            expect = String.valueOf(StatusCode.BAD_REQUEST);
//          1、组织正常的参数
            Map<String, Object> paras = createRegisterMap(false);
//          2、将某个必填参数删除？？？？？？？？？？？
            removeRequestMap(paras, para);
//          3、发送请求（只执行请求，sendRequest请求并验证状态码是否为1000）
            response = sendRequestOnly(paras);
//          4、验证请求返回的状态码
            verifyResponseByCode(StatusCode.BAD_REQUEST, response);
//          5、验证请求返回的信息（MSG_MISSING是null，验证过message中不是null）
            verifyResponseByMsg(MSG_MISSING, response);

            logMine.logCaseEnd(true, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            isSuccess = false;
            logMine.logCaseEnd(isSuccess, caseName);
            throw e;
        } finally {
            saveCaseToDb(caseName, request, response, expect, isSuccess);
        }
    }

    //body-register
    @Test(  dataProvider = "PUNCTUATION",
            dataProviderClass = com.haisheng.framework.testng.CommonDataStructure.InvalidPara.class,
            priority = 0 )
    public void bodyRegisterSetIdPuntuation(String setID) throws Exception{
        String caseName = "bodyRegisterSetIdPuntuation-"+setID;
        logMine.logCaseStart(caseName);
        boolean isSuccess = true;
        try {
            expect = "register body can be get";
//          1、组织正常的参数
            Map<String, Object> paras = createRegisterMap(false);
//          2、换掉要测试的参数
            modifyRequestMap(paras, KEY_SETID, setID);
//          3、发送请求
            response = sendRequest(paras);
//          4、验证注册返回的结果
            verifySuccessRegisterResponse(paras, response);

            logMine.logCaseEnd(true, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            isSuccess = false;
            logMine.logCaseEnd(isSuccess, caseName);
            throw e;
        } finally {

            saveCaseToDb(caseName, request, response, expect, isSuccess);
        }

    }

    /**
     * @Description: 测试“删除人体”setid为特殊字符的情况。删-查
     * @Param: [setID]
     * @return: void
     * @Author: Shine
     * @Date: 2019/4/17
     */
    @Test(  dataProvider = "PUNCTUATION",
            dataProviderClass = com.haisheng.framework.testng.CommonDataStructure.InvalidPara.class,
            priority = 1 )
    public void bodyDeleteSetIdPunctuation(String setID) throws Exception{
        String caseName = "bodyDeleteSetIdPunctuation-" + setID;
        logMine.logCaseStart(caseName);
        boolean isSuccess = true;
        try {
            expect = "body can be deleted and can NOT be get again";
            Map<String, Object> paras = createDeleteMap(false);
            modifyRequestMap(paras, KEY_SETID, setID);
            response = sendRequest(paras);
            paras.put(KEY_DBINFO, getDbinfo());
            verifySuccessDeleteResponse(paras, response);

            logMine.logCaseEnd(true, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            isSuccess = false;
            logMine.logCaseEnd(isSuccess, caseName);
            throw e;
        } finally {

            saveCaseToDb(caseName, request, response, expect, isSuccess);
        }

    }

    /**
     * @Description: “删除人体”缺失必填参数，期待1001
     * @Param: [para]
     * @return: void
     * @Author: Shine
     * @Date: 2019/4/17
     */
    @Test(dataProvider = "MISSING_PARA_DELETE_BODY", priority = 1)
    public void bodyDeleteMissingPara(String para) throws Exception{
        String caseName = "bodyDeleteMissingPara-"+para;
        logMine.logCaseStart(caseName);
        boolean isSuccess = true;
        try {
            expect = String.valueOf(StatusCode.BAD_REQUEST);
            Map<String, Object> paras = createDeleteMap(false);
            removeRequestMap(paras, para);
            response = sendRequestOnly(paras);
            verifyResponseByCode(StatusCode.BAD_REQUEST, response);
            verifyResponseByMsg(MSG_MISSING, response);

            logMine.logCaseEnd(true, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            isSuccess = false;
            logMine.logCaseEnd(isSuccess, caseName);
            throw e;
        } finally {
            saveCaseToDb(caseName, request, response, expect, isSuccess);
        }
    }

    /**
     * @Description: “删除人体”测试不存在的GrpName，期待1001
     * @Param: [grpID]
     * @return: void
     * @Author: Shine
     * @Date: q
     */
    @Test(  dataProvider = "PUNCTUATION",
            dataProviderClass = com.haisheng.framework.testng.CommonDataStructure.InvalidPara.class,
            priority = 2 )
    public void bodyDeleteInexistGrpName(String grpID) throws Exception{
        String caseName = "bodyDeleteInexistGrpName-" + grpID;
        logMine.logCaseStart(caseName);
        boolean isSuccess = true;
        try {
            expect = String.valueOf(StatusCode.BAD_REQUEST);
            Map<String, Object> paras = createDeleteMap(false);
            modifyRequestMap(paras, KEY_GRPNAME, grpID);
            response = sendRequestOnly(paras);
            verifyResponseByCode(StatusCode.BAD_REQUEST, response);
            verifyResponseByMsg(MSG_INVALID, response);

            logMine.logCaseEnd(true, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            isSuccess = false;
            logMine.logCaseEnd(isSuccess, caseName);
            throw e;
        } finally {

            saveCaseToDb(caseName, request, response, expect, isSuccess);
        }

    }
    /**
     * @Description: “删除人体”测试不存在的appkey，期待1001
     * @Param: [appKey]
     * @return: void
     * @Author: Shine
     * @Date: 2019/4/17
     */
    @Test(  dataProvider = "PUNCTUATION",
            dataProviderClass = com.haisheng.framework.testng.CommonDataStructure.InvalidPara.class,
            priority = 2 )
    public void bodyDeleteInexistAppkey(String appKey) throws Exception{
        String caseName = "bodyDeleteInexistAppkey-" + appKey;
        logMine.logCaseStart(caseName);
        boolean isSuccess = true;
        try {
            expect = String.valueOf(StatusCode.BAD_REQUEST);
            Map<String, Object> paras = createDeleteMap(false);
            modifyRequestMap(paras, KEY_APPKEY, appKey);
            response = sendRequestOnly(paras);
            verifyResponseByCode(StatusCode.BAD_REQUEST, response);
            verifyResponseByMsg(MSG_INVALID, response);

            logMine.logCaseEnd(true, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            isSuccess = false;
            logMine.logCaseEnd(isSuccess, caseName);
            throw e;
        } finally {

            saveCaseToDb(caseName, request, response, expect, isSuccess);
        }
    }

    /**
     * @Description: “人脸删除”测试不存在的setid，期待3011
     * @Param: [setID]
     * @return: void
     * @Author: Shine
     * @Date: 2019/4/17
     */
    @Test(  dataProvider = "PUNCTUATION",
            dataProviderClass = com.haisheng.framework.testng.CommonDataStructure.InvalidPara.class,
            priority = 2 )
    public void bodyDeleteInexistSetID(String setID) throws Exception{
        String caseName = "bodyDeleteInexistSetID-" + setID;
        logMine.logCaseStart(caseName);
        boolean isSuccess = true;
        try {
            expect = String.valueOf(StatusCode.BODY_UNQUALIFIED);
            Map<String, Object> paras = createDeleteMap(false);
            modifyRequestMap(paras, KEY_SETID, setID);
            response = sendRequestOnly(paras);
            verifyResponseByCode(StatusCode.BODY_UNQUALIFIED, response);

            logMine.logCaseEnd(true, caseName);
        } catch (Exception e) {
            IS_SUCCESS = false;
            isSuccess = false;
            logMine.logCaseEnd(isSuccess, caseName);
            throw e;
        } finally {
            saveCaseToDb(caseName, request, response, expect, isSuccess);
        }

    }

    /**
     * @Description: “人脸删除”测试不存在的bodyid，期待3011
     * @Param: [bodyID]
     * @return: void
     * @Author: Shine
     * @Date: 2019/4/17
     */
    @Test(  dataProvider = "PUNCTUATION",
            dataProviderClass = com.haisheng.framework.testng.CommonDataStructure.InvalidPara.class,
            priority = 2 )
    public void bodyDeleteInexistBodyID(String bodyID) throws Exception{
        String caseName = "bodyDeleteInexistBodyID-" + bodyID;
        logMine.logCaseStart(caseName);
        boolean isSuccess = true;
        try {
            expect = String.valueOf(StatusCode.BODY_UNQUALIFIED);
            Map<String, Object> paras = createDeleteMap(false);
            modifyRequestMap(paras, KEY_BODYID, bodyID);
            response = sendRequestOnly(paras);
            verifyResponseByCode(StatusCode.BODY_UNQUALIFIED, response);

            logMine.logCaseEnd(true, caseName);
        } catch (Exception e) {
            IS_SUCCESS = false;
            isSuccess = false;
            logMine.logCaseEnd(isSuccess, caseName);
            throw e;
        } finally {

            saveCaseToDb(caseName, request, response, expect, isSuccess);
        }

    }

    @Test(priority = 3)
    public void bodyQueryGroup() throws Exception{
        String caseName = "bodyQueryGroup";
        logMine.logCaseStart(caseName);
        boolean isSuccess = true;
        try {
            expect = "query group info same as existed";
            Map<String, Object> paras = createQueryGrpMap(false);
            response = sendRequest(paras);
            verifySuccessQueryResponse(paras, response);

            logMine.logCaseEnd(true, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            isSuccess = false;
            logMine.logCaseEnd(isSuccess, caseName);
            throw e;
        } finally {

            saveCaseToDb(caseName, request, response, expect, isSuccess);
        }

    }

    @Test(priority = 3)
    public void bodyQueryGroupNeedload() throws Exception{
        String caseName = "bodyQueryGroupNeedload";
        logMine.logCaseStart(caseName);
        boolean isSuccess = true;
        try {
            expect = "query group info same as existed";
            Map<String, Object> paras = createQueryGrpMap(true);
            response = sendRequest(paras);
            verifySuccessQueryResponse(paras, response);

            logMine.logCaseEnd(true, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            isSuccess = false;
            logMine.logCaseEnd(isSuccess, caseName);
            throw e;
        } finally {

            saveCaseToDb(caseName, request, response, expect, isSuccess);
        }

    }
    /**
     * @Description: “查询组”测试不存在的appkey，期待1009或3007
     * @Param: [appkey]
     * @return: void
     * @Author: Shine
     * @Date: 2019/4/17
     */
    @Test(  dataProvider = "PUNCTUATION",
            dataProviderClass = com.haisheng.framework.testng.CommonDataStructure.InvalidPara.class,
            priority = 3 )
    public void bodyQueryGroupInexistAppkey(String appkey) throws Exception{
        String caseName = "bodyQueryGroupInexistAppkey-"+appkey;
        logMine.logCaseStart(caseName);
        boolean isSuccess = true;
        try {
            expect = StatusCode.UNKNOWN_ERROR + " " + StatusCode.NO_GROUP;
            Map<String, Object> paras = createQueryGrpMap(true);
            modifyRequestMap(paras, KEY_APPKEY, appkey);
            response = sendRequestOnly(paras);
            int[] expect = {StatusCode.UNKNOWN_ERROR, StatusCode.NO_GROUP};
            verifyResponseByCodes(expect, response);

            logMine.logCaseEnd(true, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            isSuccess = false;
            logMine.logCaseEnd(isSuccess, caseName);
            throw e;
        } finally {

            saveCaseToDb(caseName, request, response, expect, isSuccess);
        }

    }

    /**
     * @Description: “查询组”测试不存在的GrpName，期待1009或3007
     * @Param: [grpName]
     * @return: void
     * @Author: Shine
     * @Date: 2019/4/17
     */
    @Test(  dataProvider = "PUNCTUATION",
            dataProviderClass = com.haisheng.framework.testng.CommonDataStructure.InvalidPara.class,
            priority = 3 )
    public void bodyQueryGroupInexistGrpname(String grpName) throws Exception{
        String caseName = "bodyQueryGroupInexistGrpname-"+grpName;
        logMine.logCaseStart(caseName);
        boolean isSuccess = true;
        try {
            expect = StatusCode.UNKNOWN_ERROR + " || " + StatusCode.NO_GROUP;
            Map<String, Object> paras = createQueryGrpMap(true);
            modifyRequestMap(paras, KEY_GRPNAME, grpName);
            response = sendRequestOnly(paras);
            int[] expect = {StatusCode.UNKNOWN_ERROR, StatusCode.NO_GROUP};
            verifyResponseByCodes(expect, response);

            logMine.logCaseEnd(true, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            isSuccess = false;
            logMine.logCaseEnd(isSuccess, caseName);
            throw e;
        } finally {

            saveCaseToDb(caseName, request, response, expect, isSuccess);
        }

    }

    /**
     * @Description: 查询组测试缺失必填参数，期待1001
     * @Param: [para]
     * @return: void
     * @Author: Shine
     * @Date: 2019/4/17
     */
    @Test(dataProvider = "MISSING_PARA_QUERY_GRP", priority = 3)
    public void bodyGroupQueryMissingPara(String para) throws Exception{
        String caseName = "bodyGroupQueryMissingPara-"+para;
        logMine.logCaseStart(caseName);
        boolean isSuccess = true;
        try {
            expect = String.valueOf(StatusCode.BAD_REQUEST);
            Map<String, Object> paras = createQueryGrpMap(true);
            removeRequestMap(paras, para);
            response = sendRequestOnly(paras);
            verifyResponseByCode(StatusCode.BAD_REQUEST, response);
            verifyResponseByMsg(MSG_MISSING, response);

            logMine.logCaseEnd(true, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            isSuccess = false;
            logMine.logCaseEnd(isSuccess, caseName);
            throw e;
        } finally {
            saveCaseToDb(caseName, request, response, expect, isSuccess);
        }
    }

    /**
     * @Description: “删除组”返回的状态码只能是1001或3005
     * @Param: []
     * @return: void
     * @Author: Shine
     * @Date: 2019/4/17
     */
    @Test (priority = 4)
    public void bodyDeleteGroup() throws Exception{
        String caseName = "bodyDeleteGroup";
        logMine.logCaseStart(caseName);
        boolean isSuccess = true;
        try {
            expect = "the delete group->query the group, expect query group code: "
                    + StatusCode.BAD_REQUEST + " || "
                    + StatusCode.GROUP_LOCK;
            Map<String, Object> paras = createDeleteGrpMap();
            response = sendRequestOnly(paras);
            int[] expect = {StatusCode.BAD_REQUEST, StatusCode.GROUP_LOCK};
            verifyResponseByCodes(expect, response);
//            paras = createQueryGrpMap(true);
//            response = sendRequestOnly(paras);
//            int[] expectQuery = {StatusCode.UNKNOWN_ERROR, StatusCode.NO_GROUP};
//            verifyResponseByCodes(expectQuery, response);

            logMine.logCaseEnd(true, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            isSuccess = false;
            logMine.logCaseEnd(isSuccess, caseName);
            throw e;
        } finally {
            saveCaseToDb(caseName, request, response, expect, isSuccess);
        }

    }

    /**
     * @Description: “删除组”测试不存在的appkey，期待1001
     * @Param: [appKey]
     * @return: void
     * @Author: Shine
     * @Date: 2019/4/17
     */
    @Test(  dataProvider = "PUNCTUATION",
            dataProviderClass = com.haisheng.framework.testng.CommonDataStructure.InvalidPara.class,
            priority = 3)
    public void bodyDeleteGroupInexistAppkey(String appKey) throws Exception{
        String caseName = "bodyDeleteGroupInexistAppkey-"+appKey;
        logMine.logCaseStart(caseName);
        boolean isSuccess = true;
        try {
            expect = String.valueOf(StatusCode.BAD_REQUEST);
            Map<String, Object> paras = createDeleteGrpMap();
            modifyRequestMap(paras, KEY_APPKEY, appKey);
            response = sendRequestOnly(paras);
            verifyResponseByCode(StatusCode.BAD_REQUEST, response);
            verifyResponseByMsg(MSG_INVALID, response);

            logMine.logCaseEnd(true, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            isSuccess = false;
            logMine.logCaseEnd(isSuccess, caseName);
            throw e;
        } finally {

            saveCaseToDb(caseName, request, response, expect, isSuccess);
        }

    }

    /**
     * @Description: “删除组”测试不存在的GrpName，期待1001
     * @Param: [appKey]
     * @return: void
     * @Author: Shine
     * @Date: 2019/4/17
     */
    @Test(  dataProvider = "PUNCTUATION",
            dataProviderClass = com.haisheng.framework.testng.CommonDataStructure.InvalidPara.class,
            priority = 3)
    public void bodyDeleteGroupInexistGrpname(String grpName) throws Exception{
        String caseName = "bodyDeleteGroupInexistGrpname-"+grpName;
        logMine.logCaseStart(caseName);
        boolean isSuccess = true;
        try {
            expect = String.valueOf(StatusCode.BAD_REQUEST);
            Map<String, Object> paras = createDeleteGrpMap();
            modifyRequestMap(paras, KEY_GRPNAME, grpName);
            response = sendRequestOnly(paras);
            verifyResponseByCode(StatusCode.BAD_REQUEST, response);
            verifyResponseByMsg(MSG_INVALID, response);

            logMine.logCaseEnd(true, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            isSuccess = false;
            logMine.logCaseEnd(isSuccess, caseName);
            throw e;
        } finally {

            saveCaseToDb(caseName, request, response, expect, isSuccess);
        }

    }

    /**
     * @Description: “删除组”测试缺失必填参数，期待1001
     * @Param: [appKey]
     * @return: void
     * @Author: Shine
     * @Date: 2019/4/17
     */
    @Test(dataProvider = "MISSING_PARA_DELETE_GRP", priority = 3)
    public void bodyDeleteGrpMissingPara(String para) throws Exception{
        String caseName = "bodyDeleteGrpMissingPara-"+para;
        logMine.logCaseStart(caseName);
        boolean isSuccess = true;
        try {
            expect = String.valueOf(StatusCode.BAD_REQUEST);
            Map<String, Object> paras = createDeleteGrpMap();
            removeRequestMap(paras, para);
            response = sendRequestOnly(paras);
            verifyResponseByCode(StatusCode.BAD_REQUEST, response);
            verifyResponseByMsg(MSG_MISSING, response);

            logMine.logCaseEnd(true, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            isSuccess = false;
            logMine.logCaseEnd(isSuccess, caseName);
            throw e;
        } finally {
            saveCaseToDb(caseName, request, response, expect, isSuccess);
        }
    }

    @Test(priority = 5)
    public void deleteGrpReReg() throws Exception {
        //为了确保删除成功，故等待5秒
        Thread.sleep(5*1000);
        String caseName = "deleteGrpRegisterAgain";
        logMine.logCaseStart(caseName);
        boolean isSuccess = true;
        try {
            expect = "delete grp -> register group -> query body successfully";
            Map<String, Object> paras = createRegisterMap(false);
            response = sendRequest(paras);
            verifySuccessRegisterResponse(paras, response);

            logMine.logCaseEnd(true, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            isSuccess = false;
            logMine.logCaseEnd(isSuccess, caseName);
            throw e;
        } finally {
            saveCaseToDb(caseName, request, response, expect, isSuccess);
        }
    }

    /**
     * @Description: “查询人体”测试缺失必填参数，期待1001
     * @Param: [para]
     * @return: void
     * @Author: Shine
     * @Date: 2019/4/17
     */
    @Test(dataProvider = "MISSING_PARA_SEARCH_BODY", priority = 0)
    public void bodySearchMissingPara(String para) throws Exception{
        String caseName = "bodySearchMissingPara-"+para;
        logMine.logCaseStart(caseName);
        boolean isSuccess = true;
        try {
            expect = String.valueOf(StatusCode.BAD_REQUEST);
            Map<String, Object> paras = createBodySearchMap(true);
            removeRequestMap(paras, para);
            response = sendRequestOnly(paras);
            verifyResponseByCode(StatusCode.BAD_REQUEST, response);
            verifyResponseByMsg(MSG_MISSING, response);

            logMine.logCaseEnd(true, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            isSuccess = false;
            logMine.logCaseEnd(isSuccess, caseName);
            throw e;
        } finally {
            saveCaseToDb(caseName, request, response, expect, isSuccess);
        }
    }

    /**
     * @Description: “查询人体”测试不存在的appkey，期待3007
     * @Param: [para]
     * @return: void
     * @Author: Shine
     * @Date: 2019/4/17
     */
    @Test(  dataProvider = "PUNCTUATION",
            dataProviderClass = com.haisheng.framework.testng.CommonDataStructure.InvalidPara.class,
            priority = 6)
    public void bodySearchInexistAppkey(String appkey) throws Exception{
        String caseName = "bodySearchInexistAppkey-"+appkey;
        logMine.logCaseStart(caseName);
        boolean isSuccess = true;
        try {
            expect = String.valueOf(StatusCode.NO_GROUP);
            Map<String, Object> paras = createBodySearchMap(true);
            modifyRequestMap(paras, KEY_APPKEY, appkey);
            response = sendRequestOnly(paras);
            verifyResponseByCode(StatusCode.NO_GROUP, response);

            logMine.logCaseEnd(true, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            isSuccess = false;
            logMine.logCaseEnd(isSuccess, caseName);
            throw e;
        } finally {

            saveCaseToDb(caseName, request, response, expect, isSuccess);
        }

    }

    /**
     * @Description: “查询人体”测试不存在的grpName，期待3007
     * @Param: [para]
     * @return: void
     * @Author: Shine
     * @Date: 2019/4/17
     */
    @Test(  dataProvider = "PUNCTUATION",
            dataProviderClass = com.haisheng.framework.testng.CommonDataStructure.InvalidPara.class,
            priority = 6)
    public void bodySearchInvalidGrpname(String grpName) throws Exception{
        String caseName = "bodySearchInvalidGrpname-"+grpName;
        logMine.logCaseStart(caseName);
        boolean isSuccess = true;
        try {
            expect = String.valueOf(StatusCode.NO_GROUP);
            Map<String, Object> paras = createBodySearchMap(true);
            modifyRequestMap(paras, KEY_GRPNAME, grpName);
            response = sendRequestOnly(paras);
            verifyResponseByCode(StatusCode.NO_GROUP, response);

            logMine.logCaseEnd(true, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            isSuccess = false;
            logMine.logCaseEnd(isSuccess, caseName);
            throw e;
        } finally {
            saveCaseToDb(caseName, request, response, expect, isSuccess);
        }

    }

    /**
     * @Description: “查询人体”测试无效的imageData，期待1009，同bodySearchInexistImagedata比较
     * @Param: [para]
     * @return: void
     * @Author: Shine
     * @Date: 2019/4/17
     */
    @Test(  dataProvider = "PUNCTUATION",
            dataProviderClass = com.haisheng.framework.testng.CommonDataStructure.InvalidPara.class,
            priority = 6)
    public void bodySearchInvalidImagedata(String data) throws Exception{
        String caseName = "bodySearchInvalidImagedata-"+data;
        logMine.logCaseStart(caseName);
        boolean isSuccess = true;
        try {
            expect = String.valueOf(StatusCode.UNKNOWN_ERROR);
            Map<String, Object> paras = createBodySearchMap(false);
            modifyRequestMap(paras, KEY_IMAGE, data);
            response = sendRequestOnly(paras);
            verifyResponseByCode(StatusCode.UNKNOWN_ERROR, response);

            logMine.logCaseEnd(true, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            isSuccess = false;
            logMine.logCaseEnd(isSuccess, caseName);
            throw e;
        } finally {

            saveCaseToDb(caseName, request, response, expect, isSuccess);
        }

    }

    @Test(  dataProvider = "PUNCTUATION",
            dataProviderClass = com.haisheng.framework.testng.CommonDataStructure.InvalidPara.class,
            priority = 6)
    public void bodySearchInexistImagedata(String data) throws Exception{
        String caseName = "bodySearchInexistImagedata-bodyurl-"+data;
        logMine.logCaseStart(caseName);
        boolean isSuccess = true;
        try {
            expect = String.valueOf(StatusCode.UNKNOWN_ERROR);
            Map<String, Object> paras = createBodySearchMap(false);
            modifyRequestMap(paras, KEY_IMAGE, getImageData().replace("http://", data));
            response = sendRequestOnly(paras);
            verifyResponseByCode(StatusCode.UNKNOWN_ERROR, response);

            logMine.logCaseEnd(true, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            isSuccess = false;
            logMine.logCaseEnd(isSuccess, caseName);
            throw e;
        } finally {

            saveCaseToDb(caseName, request, response, expect, isSuccess);
        }

    }
    //body-search
    @Test(  dataProvider = "PUNCTUATION",
            dataProviderClass = com.haisheng.framework.testng.CommonDataStructure.InvalidPara.class,
            priority = 6)
    public void bodySearchInvalidDbinfo(String data) throws Exception{
        String caseName = "bodySearchInvalidDbinfo-"+data;
        logMine.logCaseStart(caseName);
        boolean isSuccess = true;
        try {
            expect = StatusCode.UNKNOWN_ERROR + " || " + StatusCode.BAD_REQUEST;
            Map<String, Object> paras = createBodySearchMap(true);
            modifyRequestMap(paras, KEY_DBINFO, data);
            response = sendRequestOnly(paras);
            int[] expect = {StatusCode.UNKNOWN_ERROR, StatusCode.BAD_REQUEST};
            verifyResponseByCodes(expect, response);

            logMine.logCaseEnd(true, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            isSuccess = false;
            logMine.logCaseEnd(isSuccess, caseName);
            throw e;
        } finally {

            saveCaseToDb(caseName, request, response, expect, isSuccess);
        }

    }
    //body-search
    @Test(  dataProvider = "PUNCTUATION",
            dataProviderClass = com.haisheng.framework.testng.CommonDataStructure.InvalidPara.class,
            priority = 6)
    public void bodySearchInexistDbinfo(String data) throws Exception{
        String caseName = "bodySearchInexistDbinfo-bodyid-"+data;
        logMine.logCaseStart(caseName);
        boolean isSuccess = true;
        try {
            expect = String.valueOf(StatusCode.UNKNOWN_ERROR);
            Map<String, Object> paras = createBodySearchMap(true);
            modifyRequestMap(paras, KEY_IMAGE, getDbinfo().replace(BODY_ID, data));
            response = sendRequestOnly(paras);
            verifyResponseByCode(StatusCode.UNKNOWN_ERROR, response);

            logMine.logCaseEnd(true, caseName);
        } catch (Exception e) {
            IS_SUCCESS = false;
            isSuccess = false;
            logMine.logCaseEnd(isSuccess, caseName);
            throw e;
        } finally {

            saveCaseToDb(caseName, request, response, expect, isSuccess);
        }

    }

    @Test(  dataProvider = "PUNCTUATION_CHAR",
            dataProviderClass = com.haisheng.framework.testng.CommonDataStructure.InvalidPara.class,
            priority = 6 )
    public void bodySearchPuncutationResultNum(String resultNum) throws Exception{
        String caseName = "bodySearchPuncutationResultNum-"+resultNum;
        logMine.logCaseStart(caseName);
        boolean isSuccess = true;
        try {
            expect = String.valueOf(StatusCode.UNKNOWN_ERROR);
            Map<String, Object> paras = createBodySearchMap(true);
            modifyRequestMap(paras, KEY_RESULTNUM, resultNum);
            response = sendRequestOnly(paras);
            verifyResponseByCode(StatusCode.UNKNOWN_ERROR, response);

            logMine.logCaseEnd(true, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            isSuccess = false;
            logMine.logCaseEnd(isSuccess, caseName);
            throw e;
        } finally {

            saveCaseToDb(caseName, request, response, expect, isSuccess);
        }

    }

    @Test(  dataProvider = "DIGITAL",
            dataProviderClass = com.haisheng.framework.testng.CommonDataStructure.InvalidPara.class,
            priority = 6 )
    public void bodySearchDigitalResultNum(String resultNum) throws Exception{
        String caseName = "bodySearchDigitalResultNum-"+resultNum;
        logMine.logCaseStart(caseName);
        boolean isSuccess = true;
        try {
            expect = String.valueOf(StatusCode.BAD_REQUEST);
            Map<String, Object> paras = createBodySearchMap(true);
            modifyRequestMap(paras, KEY_RESULTNUM, resultNum);
            response = sendRequestOnly(paras);
            verifyResponseByCode(StatusCode.BAD_REQUEST, response);

            logMine.logCaseEnd(true, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            isSuccess = false;
            logMine.logCaseEnd(isSuccess, caseName);
            throw e;
        } finally {

            saveCaseToDb(caseName, request, response, expect, isSuccess);
        }

    }

    @Test(  dataProvider = "BOOLEAN",
            dataProviderClass = com.haisheng.framework.testng.CommonDataStructure.InvalidPara.class,
            priority = 7)
    public void bodyCompareUserCanMerge(boolean isResultNumAdd) throws Exception{
        String caseName = "bodyCompareUserCanMerge-resultnum-add-"+isResultNumAdd;
        logMine.logCaseStart(caseName);
        boolean isSuccess = true;
        try {
            expect = "the user can be merged";
            //prepare user
            prepareUsersCanMerge();
            Map<String, Object> paras = createCompareUserMap(isResultNumAdd);
            response = sendRequest(paras);
            verifySuccessCompareUser(response, "true", "2", "2");

            logMine.logCaseEnd(true, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            isSuccess = false;
            logMine.logCaseEnd(isSuccess, caseName);
            throw e;
        } finally {
            restoreDefaultValue();
            saveCaseToDb(caseName, request, response, expect, isSuccess);
        }
    }

    @Test(  dataProvider = "BOOLEAN",
            dataProviderClass = com.haisheng.framework.testng.CommonDataStructure.InvalidPara.class,
            priority = 7)
    public void bodyCompareUserCanNotMerge(boolean isResultNumAdd) throws Exception{
        String caseName = "bodyCompareUserCanMerge-resultnum-add-"+isResultNumAdd;
        logMine.logCaseStart(caseName);
        boolean isSuccess = true;
        try {
            expect = "the user can not be merged";
            prepareUsersCanNotMerge();
            Map<String, Object> paras = createCompareUserMap(isResultNumAdd);
            response = sendRequest(paras);
            verifySuccessCompareUser(response, "false", "2", "2");

            logMine.logCaseEnd(true, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            isSuccess = false;
            logMine.logCaseEnd(isSuccess, caseName);
            throw e;
        } finally {
            restoreDefaultValue();
            saveCaseToDb(caseName, request, response, expect, isSuccess);
        }

    }

    @Test(  dataProvider = "PUNCTUATION",
            dataProviderClass = com.haisheng.framework.testng.CommonDataStructure.InvalidPara.class,
            priority = 7)
    public void bodyCompareUserInexistAppkey(String appKey) throws Exception{
        String caseName = "bodyCompareUserInexistAppkey-"+appKey;
        logMine.logCaseStart(caseName);
        boolean isSuccess = true;
        try {
            expect = String.valueOf(StatusCode.NO_GROUP);
            Map<String, Object> paras = createCompareUserMap(false);
            modifyRequestMap(paras, KEY_APPKEY, appKey);
            response = sendRequestOnly(paras);
            verifyResponseByCode(StatusCode.NO_GROUP, response);

            logMine.logCaseEnd(true, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            isSuccess = false;
            logMine.logCaseEnd(isSuccess, caseName);
            throw e;
        } finally {

            saveCaseToDb(caseName, request, response, expect, isSuccess);
        }

    }
    //body-compare-user
    @Test(  dataProvider = "PUNCTUATION",
            dataProviderClass = com.haisheng.framework.testng.CommonDataStructure.InvalidPara.class,
            priority = 7)
    public void bodyCompareUserInexistGrpname(String grpName) throws Exception{
        String caseName = "bodyCompareUserInexistGrpname-"+grpName;
        logMine.logCaseStart(caseName);
        boolean isSuccess = true;
        try {
            expect = String.valueOf(StatusCode.NO_GROUP);
            Map<String, Object> paras = createCompareUserMap(false);
            modifyRequestMap(paras, KEY_GRPNAME, grpName);
            response = sendRequestOnly(paras);
            verifyResponseByCode(StatusCode.NO_GROUP, response);

            logMine.logCaseEnd(true, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            isSuccess = false;
            logMine.logCaseEnd(isSuccess, caseName);
            throw e;
        } finally {

            saveCaseToDb(caseName, request, response, expect, isSuccess);
        }

    }

    //body-compare-user
    //bug http://192.168.50.3:8081/bug-view-65.html
    @Test(  dataProvider = "PUNCTUATION",
            dataProviderClass = com.haisheng.framework.testng.CommonDataStructure.InvalidPara.class,
            priority = 7 )
    public void bodyCompareUserByPunctuationUser(String user) throws Exception{
        String caseName = "bodyCompareUserByPunctuationUser-"+user;
        logMine.logCaseStart(caseName);
        boolean isSuccess = true;
        try {
            expect = "the inexisted user can not be merged";
            prepareUsersCanNotMerge();
            Map<String, Object> paras = createCompareUserMap(true);
            modifyRequestMap(paras, KEY_USERA, user);
            modifyRequestMap(paras, KEY_USERB, user);
            response = sendRequest(paras);
            verifySuccessCompareUser(response, "false", "0", "0");

            logMine.logCaseEnd(true, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            isSuccess = false;
            logMine.logCaseEnd(isSuccess, caseName);
            throw e;
        } finally {

            saveCaseToDb(caseName, request, response, expect, isSuccess);
        }
    }

    //body-compare-user
    //bug: http://192.168.50.3:8081/bug-view-64.html
    @Test(  dataProvider = "DIGITAL",
            dataProviderClass = com.haisheng.framework.testng.CommonDataStructure.InvalidPara.class,
            priority = 7 )
    public void bodyCompareUserDigitalResultNum(String resultNum) throws Exception{
        String caseName = "bodyCompareUserDigitalResultNum-"+resultNum;
        logMine.logCaseStart(caseName);
        boolean isSuccess = true;
        try {
            expect = String.valueOf(StatusCode.BAD_REQUEST);
            Map<String, Object> paras = createCompareUserMap(true);
            modifyRequestMap(paras, KEY_RESULTNUM, resultNum);
            response = sendRequestOnly(paras);
            verifyResponseByCode(StatusCode.BAD_REQUEST, response);

            logMine.logCaseEnd(true, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            isSuccess = false;
            logMine.logCaseEnd(isSuccess, caseName);
            throw e;
        } finally {

            saveCaseToDb(caseName, request, response, expect, isSuccess);
        }

    }

    @Test(dataProvider = "MISSING_PARA_COMPARE_USER", priority = 9)
    public void bodyCompareUserMissingPara(String para) throws Exception{
        String caseName = "bodyCompareUserMissingPara-"+para;
        logMine.logCaseStart(caseName);
        boolean isSuccess = true;
        try {
            expect = String.valueOf(StatusCode.BAD_REQUEST);
            Map<String, Object> paras = createCompareUserMap(true);
            removeRequestMap(paras, para);
            response = sendRequestOnly(paras);
            verifyResponseByCode(StatusCode.BAD_REQUEST, response);
            verifyResponseByMsg(MSG_MISSING, response);

            logMine.logCaseEnd(true, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            isSuccess = false;
            logMine.logCaseEnd(isSuccess, caseName);
            throw e;
        } finally {
            saveCaseToDb(caseName, request, response, expect, isSuccess);
        }
    }

    //body-compare
    // @Test(priority = 8)
    public void bodyCompareNoBase64() throws Exception{
        String caseName = "bodyCompareNoBase64";
        logMine.logCaseStart(caseName);
        boolean isSuccess = true;
        try {
            expect = "1000 and similar == 1";
            PIC_A = getPicB();
            PIC_B = PIC_A;
            Map<String, Object> paras = createCompareBodyMap();
            response = sendRequest(paras);
            paras.put("similarity", "1");
            paras.put(KEY_BODYID, BODY_B_ID);
            verifySuccessCompareBodySame(response, paras);

            logMine.logCaseEnd(true, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            isSuccess = false;
            logMine.logCaseEnd(isSuccess, caseName);
            throw e;
        } finally {
            restoreDefaultValue();
            saveCaseToDb(caseName, request, response, expect, isSuccess);
        }

    }
    //body-compare
    //bug: http://192.168.50.3:8081/bug-view-69.html
    @Test(priority = 8)
    public void bodyCompareBase64() throws Exception{
        String caseName = "bodyCompareBase64";
        logMine.logCaseStart(caseName);
        boolean isSuccess = true;
        try {
            expect = "1000 and similar == 1";
            PIC_A = getBase64PicA();
            PIC_B = PIC_A;
            Map<String, Object> paras = createCompareBodyMap();
            response = sendRequest(paras);
            paras.put("similarity", "1.0");
            paras.put(KEY_BODYID, BODY_A_ID);
            verifySuccessCompareBodySame(response, paras);

            logMine.logCaseEnd(true, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            isSuccess = false;
            logMine.logCaseEnd(isSuccess, caseName);
            throw e;
        } finally {
            restoreDefaultValue();
            saveCaseToDb(caseName, request, response, expect, isSuccess);
        }

    }
    //body-compare
    //  @Test(priority = 8)
    public void bodyCompareBase64AndNoBase64() throws Exception{
        String caseName = "bodyCompareBase64AndNoBase64";
        logMine.logCaseStart(caseName);
        boolean isSuccess = true;
        try {
            expect = "1000 and similar == 1";
            PIC_A = getBase64PicA();
            PIC_B = getPicB();
            Map<String, Object> paras = createCompareBodyMap();
            response = sendRequest(paras);
            verifySuccessCompareBodyNotSame(response);

            logMine.logCaseEnd(true, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            isSuccess = false;
            logMine.logCaseEnd(isSuccess, caseName);
            throw e;
        } finally {
            restoreDefaultValue();
            saveCaseToDb(caseName, request, response, expect, isSuccess);
        }

    }

    //body-compare
    @Test(  dataProvider = "BLANK",
            dataProviderClass = com.haisheng.framework.testng.CommonDataStructure.InvalidPara.class,
            priority = 9 )
    public void bodyCompareInvalidAppkey(String appkey) throws Exception{
        String caseName = "bodyCompareInvalidAppkey-"+appkey;
        logMine.logCaseStart(caseName);
        boolean isSuccess = true;
        try {
            expect = StatusCode.BAD_REQUEST + " || " + StatusCode.UNKNOWN_ERROR;
            Map<String, Object> paras = createCompareBodyMap();
            modifyRequestMap(paras, KEY_APPKEY, appkey);
            response = sendRequestOnly(paras);
            int[] expect = {StatusCode.BAD_REQUEST, StatusCode.UNKNOWN_ERROR};
            verifyResponseByCodes(expect, response);

            logMine.logCaseEnd(true, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            isSuccess = false;
            logMine.logCaseEnd(isSuccess, caseName);
            throw e;
        } finally {

            saveCaseToDb(caseName, request, response, expect, isSuccess);
        }

    }

    //body-compare
    //bug: http://192.168.50.3:8081/bug-view-65.html
    @Test(  dataProvider = "PUNCTUATION",
            dataProviderClass = com.haisheng.framework.testng.CommonDataStructure.InvalidPara.class,
            priority = 9 )
    public void bodyCompareByAppkeyPunctuation(String appkey) throws Exception{
        String caseName = "bodyCompareByAppkeyPunctuation-"+appkey;
        logMine.logCaseStart(caseName);
        boolean isSuccess = true;
        try {
            expect = String.valueOf(StatusCode.SUCCESS);
            PIC_B = getBase64PicA();
            PIC_A = PIC_B;
            Map<String, Object> paras = createCompareBodyMap();
            modifyRequestMap(paras, KEY_APPKEY, appkey);
            response = sendRequestOnly(paras);
            verifyResponseByCode(StatusCode.SUCCESS, response);

            logMine.logCaseEnd(true, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            isSuccess = false;
            logMine.logCaseEnd(isSuccess, caseName);
            throw e;
        } finally {

            saveCaseToDb(caseName, request, response, expect, isSuccess);
        }

    }

    //body-compare
    @Test(  dataProvider = "BLANK",
            dataProviderClass = com.haisheng.framework.testng.CommonDataStructure.InvalidPara.class,
            priority = 9 )
    public void bodyCompareBlankPicture(String pic) throws Exception{
        String caseName = "bodyCompareBlankPicture-"+pic;
        logMine.logCaseStart(caseName);
        boolean isSuccess = true;
        try {
            expect = String.valueOf(StatusCode.BAD_REQUEST);
            PIC_A = getPicB();
            PIC_B = PIC_A;
            Map<String, Object> paras = createCompareBodyMap();
            modifyRequestMap(paras, KEY_PICTUREA, pic);
            modifyRequestMap(paras, KEY_PICTUREB, pic);
            response = sendRequestOnly(paras);
            verifyResponseByCode(StatusCode.BAD_REQUEST, response);

            logMine.logCaseEnd(true, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            isSuccess = false;
            logMine.logCaseEnd(isSuccess, caseName);
            throw e;
        } finally {

            saveCaseToDb(caseName, request, response, expect, isSuccess);
        }

    }

    //body-compare
    @Test(  dataProvider = "PUNCTUATION",
            dataProviderClass = com.haisheng.framework.testng.CommonDataStructure.InvalidPara.class,
            priority = 9 )
    public void bodyCompareInexistedPicture(String pic) throws Exception{
        String caseName = "bodyCompareInexistedPicture-"+pic;
        logMine.logCaseStart(caseName);
        boolean isSuccess = true;
        try {
            expect = String.valueOf(StatusCode.BAD_REQUEST);
            PIC_A = getPicB();
            PIC_B = PIC_A;
            Map<String, Object> paras = createCompareBodyMap();
            modifyRequestMap(paras, KEY_PICTUREA, pic);
            modifyRequestMap(paras, KEY_PICTUREB, pic);
            response = sendRequestOnly(paras);
            verifyResponseByCode(StatusCode.BAD_REQUEST, response);

            logMine.logCaseEnd(true, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            isSuccess = false;
            logMine.logCaseEnd(isSuccess, caseName);
            throw e;
        } finally {

            saveCaseToDb(caseName, request, response, expect, isSuccess);
        }

    }

    @Test(dataProvider = "MISSING_PARA_COMPARE_BODY", priority = 9)
    public void bodyCompareMissingPara(String para) throws Exception{
        String caseName = "bodyCompareMissingPara-"+para;
        logMine.logCaseStart(caseName);
        boolean isSuccess = true;
        try {
            expect = String.valueOf(StatusCode.BAD_REQUEST);
            Map<String, Object> paras = createCompareBodyMap();
            removeRequestMap(paras, para);
            response = sendRequestOnly(paras);
            verifyResponseByCode(StatusCode.BAD_REQUEST, response);
            String[] expect = {MSG_ERROR, MSG_MISSING};
            verifyResponseByMsgs(expect, response);

            logMine.logCaseEnd(true, caseName);

        } catch (Exception e) {
            IS_SUCCESS = false;
            isSuccess = false;
            logMine.logCaseEnd(isSuccess, caseName);
            throw e;
        } finally {
            saveCaseToDb(caseName, request, response, expect, isSuccess);
        }
    }


    private void setRequestToGlobalPara(Object obj) {
        request = JSON.toJSONString(obj);
    }


    private void modifyRequestMap(Map<String, Object>map, String key, String value) {
        map.put(key, value);
        setRequestToGlobalPara(map);
    }

    private void removeRequestMap(Map<String, Object>paras, String key) {
        paras.remove(key);
        setRequestToGlobalPara(paras);
    }

    private String getImageData() {
        return "{\"axis\":[1323,175,1575,807],\"body_url\":\"http://retail-huabei2.oss-cn-beijing-internal.aliyuncs.com/dispatcher_daily/uid_e0d1ebec/a4d4d18741a8/3/20190409/e21bf774-5a9f-414b-81ea-9decc05a_0?Expires=1554863192&OSSAccessKeyId=LTAIlYpjA39n18Yr&Signature=OF5xajkTuBSaBwsjyp%2Bk4d1A31g%3D\",\"quality\":0.76550233}";
    }

    private String getDbinfo() {
        return "{\"app_key\":\"who-request\",\"body_id\":\"bb5cdd54fe38b1ba00d022a5cc81d132\",\"group_name\":\"8-2019-04-02\",\"set_id\":\"91c284e6-1e8b-41c0-9a1f-b3af3d26__153\"}";
    }

    private String getDiffDbinfo() {
        return "{\"app_key\":\"who-request\",\"body_id\":\"15439132957b101c7f167a3909346e9c\",\"group_name\":\"8-2019-04-11\",\"set_id\":\"396c5787-c4aa-46df-9cae-0d667e79__153\"}";
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
        //将Map型的paras转成String型的，并赋给request，用于finally存到数据库。
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
    /**
     * @Description: 组织SearchBody的参数
     * @Param: [known] 即paras（入参，而非这个方法中的paras变量）+bodyId
     * @return: java.util.Map<java.lang.String,java.lang.Object>
     * @Author: Shine
     * @Date: 2019/4/16
     */
    private Map<String, Object> createBodySearchMapByKnown(Map<String, Object> known) {
        Map<String, Object> paras = new ConcurrentHashMap<>();
        paras.put("request_id", UUID.randomUUID().toString());
        paras.put(KEY_APPKEY, known.get(KEY_APPKEY));
        paras.put(KEY_GRPNAME, known.get(KEY_GRPNAME));
        paras.put("method", "SEARCH_BODY");
        if (known.containsKey(KEY_DBINFO)) {
            paras.put(KEY_DBINFO, known.get(KEY_DBINFO));
        } else if (known.containsKey(KEY_IMAGE)) {
            paras.put(KEY_IMAGE, known.get(KEY_IMAGE));
        } else {
            paras.put(KEY_DBINFO, known.get(KEY_DBINFO));
        }
        paras.put(KEY_RESULTNUM, 500);

        return paras;
    }

    private Map<String, Object> createQueryGrpMap(boolean isLoadOTS) {
        Map<String, Object> paras = new ConcurrentHashMap<>();
        paras.put("request_id", UUID.randomUUID().toString());
        paras.put(KEY_APPKEY, APP_KEY);
        paras.put("method", "QUERY_GROUP");
        paras.put(KEY_GRPNAME, GRP_NAME);
        if (isLoadOTS) {
            paras.put(KEY_NEEDLOAD, true);
        }
        setRequestToGlobalPara(paras);

        return paras;
    }

    private Map<String, Object> createCompareUserMap(boolean isResultNum) {
        Map<String, Object> paras = new ConcurrentHashMap<>();
        paras.put("request_id", UUID.randomUUID().toString());
        paras.put(KEY_APPKEY, APP_KEY);
        paras.put(KEY_GRPNAME, GRP_NAME);
        paras.put("method", "COMPARE_USER");
        paras.put(KEY_USERA, USER_COMPARE_ID1);
        paras.put(KEY_USERB, USER_COMPARE_ID2);
        if (isResultNum) {
            paras.put(KEY_RESULTNUM, 500);
        }
        setRequestToGlobalPara(paras);

        return paras;
    }

    private Map<String, Object> createCompareBodyMap() {
        Map<String, Object> paras = new ConcurrentHashMap<>();
        paras.put("request_id", UUID.randomUUID().toString());
        paras.put(KEY_APPKEY, APP_KEY);
        paras.put("method", "COMPARE");
        paras.put(KEY_PICTUREA, PIC_A);
        paras.put(KEY_PICTUREB, PIC_B);

        setRequestToGlobalPara(paras);

        return paras;
    }

    private String toStringMethod(int[] arr)
    {
        // 自定义一个字符缓冲区，
        StringBuilder sb = new StringBuilder();
        sb.append("[ ");
        //遍历int数组，并将int数组中的元素转换成字符串储存到字符缓冲区中去
        for(int i=0;i<arr.length;i++)
        {
            if(i!=arr.length-1)
                sb.append(arr[i]+" ,");
            else
                sb.append(arr[i]+" ]");
        }
        return sb.toString();
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
    private void verifyResponseByCodes(int[] expect, String response) throws Exception{
        int code = JSON.parseObject(response).getInteger("code");
        boolean isSuccess = false;
        for (int item : expect) {
            if (item == code) {
                isSuccess = true;
            }
        }
        if (! isSuccess) {
            throw new Exception("expect code: " + toStringMethod(expect) + ", actual: " + code);
        }
    }

    private void verifyResponseByMsg(String expect, String response) throws Exception{
        String msg = JSON.parseObject(response).getString("message");
        if (! msg.contains(expect)) {
            throw new Exception("expect msg contain: " + expect + ", actual: " + msg);
        }
    }
    private void verifyResponseByMsgs(String[] expect, String response) throws Exception{
        String msg = JSON.parseObject(response).getString("message");
        boolean isSuccess = false;
        for (String item : expect) {
            if (msg.contains(item)) {
                isSuccess = true;
            }
        }
        if (! isSuccess) {
            throw new Exception("expect msg: " + expect.toString() + ", actual: " + msg);
        }

    }

    private String sendRequest(Map<String, Object> params) throws Exception {
        HttpExecutorUtil executor = new HttpExecutorUtil();
//      真正执行请求的
        executor.doPostJson(URL,params);
//      验证状态码。params作为入参，只是为了取得requestID，与response中的requestID比较
        verifyResponseSuccessCode(executor.getResponse(), params);
//      getResponse()方法只是返回response，执行是executor.doPostJson(URL,params);
        return executor.getResponse();
    }

    private void verifySuccessDeleteResponse(Map<String, Object> expect, String response) throws Exception{
        JSONObject body = JSON.parseObject(response).getJSONObject("request");

        verifyBodyEqual(body, expect, KEY_APPKEY);
        verifyBodyEqual(body, expect, KEY_BODYID);
        verifyBodyEqual(body, expect, KEY_GRPNAME);
        verifyBodyEqual(body, expect, KEY_SETID);
        verifyBodyIdInexisted(expect);
    }

    /**
     * @Description: 验证body域中是否有这些变量，并且变量值是否一样
     * @Param: [paras, response]
     * @return: void
     * @Author: Shine
     * @Date: 2019/4/16
     */
    private void verifySuccessRegisterResponse(Map<String, Object> paras, String response) throws Exception{
        JSONObject body = JSON.parseObject(response).getJSONObject("body");
//      1、验证“注册人体”返回的信息中的body域中是否有这些变量，并且变量值是否一样
        verifyBodyEqual(body, paras, KEY_APPKEY);
        verifyBodyEqual(body, paras, KEY_GRPNAME);
        verifyBodyEqual(body, paras, KEY_SETID);
        verifyBodyEqual(body, paras, KEY_USERID);
//      检查body是否存在时用的是检查返回的该body的几个字段（包括bodyId）是否存在，故这里传
        paras.put(KEY_BODYID, body.getString("bodyId"));
//      2、通过“查询人体”验证返回的具体信息是否正确
        verifyBodyId(paras);

    }
    /**
     * @Description: 通过SEARCH_BODY方法验证查询返回的body域信息
     * @Param: [known]即paras+bodyId
     * @return: void
     * @Author: Shine
     * @Date: 2019/4/16
     */
    private void verifyBodyId(Map<String, Object> known) throws Exception{
//      1、组织“查询人体”的参数
        Map<String, Object> hm = createBodySearchMapByKnown(known);
//      2、发送请求
        String response = sendRequest(hm);
//      3、获取resultBody域(bodyL域即是data域，resultBody是个jsonArrary，是bodyL里面的)
        JSONArray bodyArray = JSON.parseObject(response)
                .getJSONArray("bodyL")
                .getJSONObject(0)
                .getJSONArray("resultBody");
//      4、验证resultBody中的某个body的信息
        verifyResultBodyArray(bodyArray, known);
    }

    /**
     * @Description:  通过查询验证是否删除成功
     * @Param: [known]
     * @return: void
     * @Author: Shine
     * @Date: 2019/4/17
     */
    private void verifyBodyIdInexisted(Map<String, Object> known) throws Exception{
        Map<String, Object> hm = createBodySearchMapByKnown(known);
        String response = sendRequest(hm);
        JSONArray bodyArray = JSON.parseObject(response)
                .getJSONArray("bodyL")
                .getJSONObject(0)
                .getJSONArray("resultBody");
        verifyBodyInexisted(bodyArray, known);
    }
    /**
     * @Description:验证resultBody中的某个body的信息
     * @Param: [array, known] arrry是response中的resultBody域，known是paras+bodyId
     * @return: void
     * @Author: Shine
     * @Date: 2019/4/16
     */
    private void verifyResultBodyArray(JSONArray array, Map<String, Object> known) throws Exception{
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

    /**
     * @Description: 期待返回结果中没有body info
     *            ps:同verifyResultBodyArray方法（期待有body info）的区别仅在于最后一句
     * @Param: [array, known]
     * @return: void
     * @Author: Shine
     * @Date:
     */
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

    private void verifySuccessQueryResponse(Map<String, Object> expect, String response) throws Exception{
        JSONObject group = JSON.parseObject(response).getJSONArray("groups").getJSONObject(0);

        verifyBodyEqual(group, expect, KEY_APPKEY);
        verifyBodyEqual(group, expect, KEY_GRPNAME);

    }

    /** 检查返回的resultBody（array）中的某个body的信息是否存在
     * @Param: [resultBody, known]
     * @return: boolean
     * @Author: Shine
     * @Date: 2019/4/17
     */
    private boolean isQueryBodyDataExisted(JSONObject resultBody, Map<String, Object> known){
        boolean result = true;
//      检查某个body的某个字段是否存在
        result &= isBodyExisted(resultBody, known, KEY_BODYID);
        result &= isBodyExisted(resultBody, known, KEY_GRPNAME);
        result &= isBodyExisted(resultBody, known, KEY_SETID);
        result &= isBodyExisted(resultBody, known, KEY_USERID);

        return result;
    }
    /**
     * @Description:  验证body域中是否有这些变量，并且变量值是否一样
     * @Param: [body, expectMap, key]
     * @return: void
     * @Author: Shine
     * @Date: 2019/4/16
     */
    private void verifyBodyEqual(JSONObject body, Map<String, Object> expectMap, String key) throws Exception{
//      这个只是将一个变量变形，参数是key，如app_key-->appKey
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

    private void verifyJsonEqual(JSONObject body, Map<String, Object> expectMap, String key) throws Exception{
        String expect = (String) expectMap.get(key);
        String actual = body.getString(key);
        if (! expect.equals(actual)) {
            throw new Exception("expect " + key + ": " + expect + ", actual: " + actual);
        }
    }

    /**
     * @Description:  检查某个返回的body中的某个字段是否存在
     * @Param: [body, expectMap, key]
     * @return: boolean
     * @Author: Shine
     * @Date: 2019/4/17
     */
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

    private void verifySuccessCompareUser(String response, String shouldMerge, String distNum, String srcNum) throws Exception{
        if (! JSON.parseObject(response).containsKey("data")) {
            throw new Exception("successfully compare user, code is 1000, response but do NOT contains [data] filed");
        }
        JSONObject resJson = JSON.parseObject(response).getJSONObject("data");

        if (! resJson.containsKey("graphMerge")) {
            throw new Exception("successfully compare user, code is 1000, response but do NOT contains [graphMerge] filed");
        }
        JSONObject graphMerge = resJson.getJSONObject("graphMerge");
        Map<String, Object> hm = getExpectCompareUserResult(shouldMerge, distNum, srcNum);

        verifyJsonEqual(resJson, hm, "distNum");
        verifyJsonEqual(resJson, hm, "srcNum");
        verifyJsonEqual(graphMerge, hm, "graphMerge");
    }

    private void verifySuccessCompareBodySame(String response, Map<String, Object> known) throws Exception{

        JSONObject resJson = JSON.parseObject(response).getJSONObject("relation");
        JSONObject bodyA = resJson.getJSONObject("bodyA");
        JSONObject bodyB = resJson.getJSONObject("bodyB");

        verifyBodyEqual(resJson, known, "similarity");
        verifyBodyEqual(bodyA, known, KEY_BODYID);
        verifyBodyEqual(bodyB, known, KEY_BODYID);
    }

    private void verifySuccessCompareBodyNotSame(String response) throws Exception{
        JSONObject resJson = JSON.parseObject(response).getJSONObject("relation");
        if (! resJson.containsKey("similarity")) {
            throw new Exception("response do NOT contains key: similarity");
        }
        String bodyA = resJson.getJSONObject("bodyA").getString(KEY_BODYID);
        String bodyB = resJson.getJSONObject("bodyB").getString(KEY_BODYID);

        if (! bodyA.equals(BODY_A_ID)) {
            throw new Exception("expect get bodyA id: " + BODY_A_ID + ", response acutal return bodyA id: " + bodyA);
        }
        if (! bodyA.equals(BODY_B_ID)) {
            throw new Exception("expect get bodyB id: " + BODY_B_ID + ", response acutal return bodyB id: " + bodyB);
        }

    }
    private Map<String, Object> getExpectCompareUserResult(String shouldMerge, String distNum, String srcNum) {
        Map<String, Object> result = new ConcurrentHashMap<>();
        result.put("graphMerge", shouldMerge);
        result.put("distNum", distNum);
        result.put("srcNum", srcNum);
        return result;
    }

    private void prepareUsersCanMerge() throws Exception{
        GRP_NAME = GRP_COMPARE_MERGE;
        //user: zhangfan1, setid: zhangfan111
        //user: zhangfan1, setid: zhangfan112
        Map<String, Object> paras = createRegisterMap(false);
        modifyRequestMap(paras, KEY_USERID, USER_COMPARE_ID1);
        modifyRequestMap(paras, KEY_SETID, "zhangfan111");
        sendRequest(paras);
        modifyRequestMap(paras, KEY_SETID, "zhangfan112");
        sendRequest(paras);

        //user: zhangfan2, setid: zhangfan221
        //user: zhangfan2, setid: zhangfan222
        modifyRequestMap(paras, KEY_USERID, USER_COMPARE_ID2);
        modifyRequestMap(paras, KEY_SETID, "zhangfan221");
        sendRequest(paras);
        modifyRequestMap(paras, KEY_SETID, "zhangfan222");
        sendRequest(paras);
    }

    private void prepareUsersCanNotMerge() throws Exception{
        GRP_NAME = GRP_COMPARE_NOT_MERGE;
        //user: zhangfan1, setid: zhangfan111
        //user: zhangfan1, setid: zhangfan112
        Map<String, Object> paras = createRegisterMap(false);
        modifyRequestMap(paras, KEY_USERID, USER_COMPARE_ID1);
        modifyRequestMap(paras, KEY_SETID, "zhangfan111");
        sendRequest(paras);
        modifyRequestMap(paras, KEY_SETID, "zhangfan112");
        sendRequest(paras);

        //user: zhangfan2, setid: zhangfan221, 与zhangfan1不同的照片
        //user: zhangfan2, setid: zhangfan222
        modifyRequestMap(paras, KEY_USERID, USER_COMPARE_ID2);
        modifyRequestMap(paras, KEY_SETID, "zhangfan221");
        modifyRequestMap(paras, KEY_DBINFO, getDiffDbinfo());
        sendRequest(paras);
        modifyRequestMap(paras, KEY_SETID, "zhangfan222");
        sendRequest(paras);
    }

    private String getBase64PicA() {
        BODY_A_ID = "ae46a2bb2faf6dc19b3c546f8e189251";
        return "base64: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";
    }

    private String getPicB() {
        BODY_B_ID = "254952f104df4793b60ed05232de226a";
        return "http://retail-huabei2.oss-cn-beijing.aliyuncs.com/body_DAILY/register/who-request/8-2019-03-13%2A%2A%2A3/b4413da8-9a2e-43b8-9e58-2df63a16__157%2A%2A%2A254952f104df4793b60ed05232de226a.jpg?Expires=3023690663&OSSAccessKeyId=LTAIlYpjA39n18Yr&Signature=akvsgfiAbsvOxl5c%2BV%2FjXcsT2oc%3D";
    }

    private void restoreDefaultValue() {
        APP_KEY  = "TEST-APP-KEY";
        GRP_NAME = "TEST-GRP-NAME";
        USER_ID  = "TEST-USER-ID";
        BODY_ID  = "bb5cdd54fe38b1ba00d022a5cc81d132";
        PIC_A    = "";
        PIC_B    = "";

    }

    private void clearExistedGroup() {
        try {
            Map<String, Object> paras = createDeleteGrpMap();
            sendRequestOnly(paras);
            Thread.sleep(5*1000);
            modifyRequestMap(paras, KEY_GRPNAME, GRP_COMPARE_MERGE);
            sendRequestOnly(paras);
            Thread.sleep(5*1000);
            modifyRequestMap(paras, KEY_GRPNAME, GRP_COMPARE_NOT_MERGE);
            sendRequestOnly(paras);
            Thread.sleep(60*1000);
        } catch (Exception e) {
            logger.error(e.toString());
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

        if (request.length() > 4096) {
            //do NOT save info greater than 4k
            request = request.substring(0, 4090);
        }
        if (response.length() > 4096) {
            //do NOT save info greater than 4k
            response = response.substring(0, 4090);
        }
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
//        checklist.setFailReason(failReason);
        if (isSuccess) {
            checklist.setResult("PASS");
        } else {
            checklist.setResult("FAIL");
        }
        caseDao.insert(checklist);
        sqlSession.commit();

    }

    @DataProvider(name = "MISSING_PARA_SEARCH_BODY")
    public Object[] searchMissing () {
        return new String[]{
                KEY_APPKEY,
                KEY_GRPNAME
        };
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

    @DataProvider(name = "MISSING_PARA_DELETE_BODY")
    public Object[] deleteBodyMissing () {
        return new String[]{
                KEY_APPKEY,
                KEY_GRPNAME,
                KEY_SETID,
                KEY_BODYID
        };
    }

    @DataProvider(name = "MISSING_PARA_DELETE_GRP")
    public Object[] deleteGrpMissing () {
        return new String[]{
                KEY_APPKEY,
                KEY_GRPNAME
        };
    }

    @DataProvider(name = "MISSING_PARA_QUERY_GRP")
    public Object[] queryGrpMissing () {
        return new String[]{
                KEY_APPKEY,
                KEY_GRPNAME
        };
    }

    @DataProvider(name = "MISSING_PARA_COMPARE_BODY")
    public Object[] compareBodyMissing () {
        return new String[]{
                KEY_APPKEY,
                KEY_PICTUREA,
                KEY_PICTUREB
        };
    }

    @DataProvider(name = "MISSING_PARA_COMPARE_USER")
    public Object[] compareUserMissing () {
        return new String[]{
                KEY_APPKEY,
                KEY_GRPNAME,
                KEY_USERA,
                KEY_USERB
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
        if (! IS_SUCCESS) {
            dingdingAlarm("人体算法回归测试失败", "请点击下面详细链接查看log", "", "@刘峤 @蔡思明");
        }
        //clean existed group
        clearExistedGroup();
    }
}
