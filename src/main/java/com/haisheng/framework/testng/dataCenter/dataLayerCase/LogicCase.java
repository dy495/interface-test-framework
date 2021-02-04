package com.haisheng.framework.testng.dataCenter.dataLayerCase;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.xundianDaily.StoreScenarioUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.testng.dataCenter.interfaceUtil.DataLayerUtil;
import com.haisheng.framework.testng.dataCenter.interfaceUtil.LogicLayerUtil;
import com.haisheng.framework.util.CommonUtil;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.ImageUtil;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;
;import static com.google.common.base.Preconditions.checkArgument;
/**
 * @author : qingqing
 * @date :  2020/07/06
 */
public class LogicCase extends TestCaseCommon implements TestCaseStd {
    LogicLayerUtil logic = LogicLayerUtil.getInstance();
    String request_id = "8b21f20d-6af6-43ff-8fd3-4251e9";

    String face_url = "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=381876729,1649964117&fm=26&gp=0.jpg";
    String userId = "";
    String imgUrl = "";

    /**
     * @description: initial test class level config, such as appid/uid/ak/dinghook/push_rd_name
     */
    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before classs initial");
        CommonConfig commonConfig = new CommonConfig();
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_CLOUD_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CUSTOMER_DATA_SERVICE;
        commonConfig.checklistQaOwner = "青青";
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "data_center");
        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, "逻辑层接口 日常");
        // commonConfig.dingHook = DingWebhook.APP_DATA_LAYER_ALARM_GRP;
        commonConfig.dingHook = DingWebhook.DAILY_STORE_MANAGEMENT_PLATFORM_GRP;
        commonConfig.pushRd = new String[]{"18810332354", "15084928847"};
        commonConfig.shopId = getXundianShop(); //要改！！！
        beforeClassInit(commonConfig);
        logger.debug("dataCenter " + logic);
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

    /**
     * ====================1. 特殊人物注册、新增人脸(正确入参&格式)======================
     */
    @Test
    public void member_register() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject obj = new JSONObject();
            obj.put("user_id","7cfa3fe9-96c4-4e65-960b-44edc3f18b68");
            JSONArray array = new JSONArray();
            array.add(obj);
            JSONObject shop_user = new JSONObject();
            shop_user.put("22728",array);
            JSONObject res = logic.special_register("autotester","1112223333",shop_user,"NORMAL",true,face_url,false,true,null,null,null,null,null,null,null,null,null);
            Integer code = res.getInteger("code");
            String face_id = res.getString("face_id");
            checkArgument(code == 1000 && !face_id.isEmpty() , "1. 特殊人物注册、新增人脸(正确入参&格式),code="+code+"人脸ID："+face_id);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("特殊人物注册、新增人脸(正确入参&格式)");
        }
    }
    /**
     * ====================1. 特殊人物注册、新增人脸(必填项不填写)======================
     */
    @Test
    public void member_register1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject obj = new JSONObject();
            obj.put("user_id","7cfa3fe9-96c4-4e65-960b-44edc3f18b68");
            JSONArray array = new JSONArray();
            array.add(obj);
            JSONObject shop_user = new JSONObject();
            shop_user.put("22728",array);
            JSONObject res = logic.special_register("","",shop_user,"NORMAL",true,"",false,true,null,null,null,null,null,null,null,null,null);
            Integer code = res.getInteger("code");
            String face_id = res.getString("face_id");
            checkArgument(code == 1001  , "1. 特殊人物注册、新增人脸(必填项不填写)成功了,code="+code+"人脸ID："+face_id);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("特殊人物注册、新增人脸(必填项不填写)");
        }
    }
    /**
     * ====================1. 特殊人物注册、新增人脸(参数超出【支持的字符范围】)======================
     */
    @Test
    public void member_register2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject obj = new JSONObject();
            obj.put("user_id","7cfa3fe9-96c4-4e65-960b-44edc3f18b68");
            JSONArray array = new JSONArray();
            array.add(obj);
            JSONObject shop_user = new JSONObject();
            shop_user.put("22728",array);
            JSONObject res = logic.special_register("水水水水","……&*（**（",shop_user,"NORMAL",true,face_url,false,true,null,null,null,null,null,null,null,null,null);
            Integer code = res.getInteger("code");
            String face_id = res.getString("face_id");
            checkArgument(code == 1001  , "1. 特殊人物注册、新增人脸(参数超出【支持的字符范围】)成功了,code="+code+"人脸ID："+face_id);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("特殊人物注册、新增人脸(参数超出【支持的字符范围】");
        }
    }

    /**
     * ====================2. 特殊人物列表查询(正确入参&格式)======================
     */
    @Test
    public void specialMan_listSearch() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject res = logic.specialKu_serach("autotester");
            Integer code = res.getInteger("code");
            JSONArray person = res.getJSONArray("person");
            checkArgument(code == 1000 && person.size()!=0 , "特殊人物列表查询(正确入参&格式),code="+code+"人脸ID："+person.size());

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("特殊人物列表查询(正确入参&格式)");
        }
    }

    /**
     * ====================2. 特殊人物列表查询(必填项不填写)======================
     */
    @Test
    public void specialMan_listSearch1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject res = logic.specialKu_serach("");
            Integer code = res.getInteger("code");
            checkArgument(code == 1001 , "特殊人物列表查询(必填项不填写),code="+code);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("特殊人物列表查询(必填项不填写)");
        }
    }

    /**
     * ====================2. 特殊人物列表查询(参数超出【支持的字符范围】)======================
     */
    @Test
    public void specialMan_listSearch2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject res = logic.specialKu_serach("autotester&*&*&*……*……*……*");
            Integer code = res.getInteger("code");
            JSONArray person = res.getJSONArray("person");
            checkArgument(code == 1001 , "特殊人物列表查询(必填项不填写),code="+code);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("特殊人物列表查询(参数超出【支持的字符范围】)");
        }
    }

    /**
     * ====================2. 查询已注册的特定人物(正确入参&格式)======================
     */
    @Test
    public void specialMan_alSearch() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject obj = new JSONObject();
            obj.put("user_id","7cfa3fe9-96c4-4e65-960b-44eduhuh99");
            JSONArray array = new JSONArray();
            array.add(obj);
            JSONObject shop_user = new JSONObject();
            shop_user.put("22728",array);
            logic.special_register("autoUser","7cfa3fe9-96c4-4e65-960b-44eduhuh99",shop_user,"NORMAL",true,face_url,false,true,null,null,null,null,null,null,null,null,null);


            JSONObject res = logic.specialMan_serach("autoUser","7cfa3fe9-96c4-4e65-960b-44eduhuh99");
            Integer code = res.getInteger("code");
            JSONArray faces = res.getJSONArray("faces");
            checkArgument(code == 1000 && faces.size()!=0 , "查询已注册的特定人物(正确入参&格式),code="+code+"人脸ID："+faces.size());

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("查询已注册的特定人物(正确入参&格式)");
        }
    }

    /**
     * ====================2. 查询已注册的特定人物(必填项不填写)======================
     */
    @Test
    public void specialMan_alSearch1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject res = logic.specialMan_serach("","");
            Integer code = res.getInteger("code");
            checkArgument(code == 1001 , "查询已注册的特定人物(必填项不填写),code="+code);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("查询已注册的特定人物(必填项不填写)");
        }
    }

    /**
     * ====================2. 查询已注册的特定人物(参数超出【支持的字符范围】)======================
     */
    @Test
    public void specialMan_alSearch2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject res = logic.specialMan_serach("^&&&^*^*&*^*","**(&(*&*(&(&(");
            Integer code = res.getInteger("code");
            checkArgument(code == 1001 , "查询已注册的特定人物(参数超出【支持的字符范围】),code="+code);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("查询已注册的特定人物(参数超出【支持的字符范围】)");
        }
    }

    /**
     * ====================3. 特殊人物校验（人脸检索）(正确入参&格式)======================
     */
    @Test
    public void specialMan_faceSearch() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject res = logic.specialFace_serach("autotester",true,face_url,null,null,null);
            Integer code = res.getInteger("code");
            JSONArray faces = res.getJSONArray("faces");
            checkArgument(code == 1000 && faces.size()!= 0 , "特殊人物校验（人脸检索）(正确入参&格式),code="+code+"人脸信息"+faces);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("特殊人物校验（人脸检索）(正确入参&格式)");
        }
    }

    /**
     * ====================3. 特殊人物校验（人脸检索）(必填项不填写)======================
     */
    @Test
    public void specialMan_faceSearch1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject res = logic.specialFace_serach("",null,null,null,null,null);
            Integer code = res.getInteger("code");
            JSONArray faces = res.getJSONArray("faces");
            checkArgument(code == 1001, "特殊人物校验（人脸检索）(必填项不填写),code="+code);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("特殊人物校验（人脸检索）(必填项不填写)");
        }
    }

    /**
     * ====================3. 特殊人物校验（人脸检索）(参数超出【支持的字符范围】)======================
     */
    @Test
    public void specialMan_faceSearch2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject res = logic.specialFace_serach("autotester&&*^&^*",true,"*&&&(&*&(*(&",null,null,null);
            Integer code = res.getInteger("code");
            checkArgument(code == 1001 , "特殊人物校验（人脸检索）(参数超出【支持的字符范围】),code="+code);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("特殊人物校验（人脸检索）(参数超出【支持的字符范围】)");
        }
    }


    /**
     * ====================4. 特殊人物删除(正确入参&格式)======================
     */
    @Test
    public void specialMan_delete() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject obj = new JSONObject();
            obj.put("user_id","7cfa3fe9-96c4-4e65-960b-44edc3hu689");
            JSONArray array = new JSONArray();
            array.add(obj);
            JSONObject shop_user = new JSONObject();
            shop_user.put("22728",array);
            logic.special_register("autotester222","7cfa3fe9-96c4-4e65-960b-44edc3hu689",shop_user,"NORMAL",true,face_url,false,true,null,null,null,null,null,null,null,null,null);

            JSONObject res = logic.delete_man("autotester222","7cfa3fe9-96c4-4e65-960b-44edc3hu689");
            Integer code = res.getInteger("code");
            checkArgument(code == 1000  , "特殊人物校验（人脸检索）(正确入参&格式),code="+code);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("特殊人物校验（人脸检索）(正确入参&格式)");
        }
    }

    /**
     * ====================4. 特殊人物删除(必填项不传)======================
     */
    @Test
    public void specialMan_delete1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject res = logic.delete_man("","");
            Integer code = res.getInteger("code");
            checkArgument(code == 1001  , "特殊人物删除(必填项不传),code="+code);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("特殊人物删除(必填项不传)");
        }
    }

    /**
     * ====================4. 特殊人物删除(参数超出【支持的字符范围】)======================
     */
    @Test
    public void specialMan_delete2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject res = logic.delete_man("&*&（*（","……*&……*……");
            Integer code = res.getInteger("code");
            checkArgument(code == 1001  , "特殊人物删除(参数超出【支持的字符范围】),code="+code);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("特殊人物删除(参数超出【支持的字符范围】)");
        }
    }

    /**
     * ====================5. 特殊人物人脸删除(正确入参&格式)======================
     */
    @Test
    public void specialMan_faceDelete() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject obj = new JSONObject();
            obj.put("user_id","7cfa3fe9-96c4-4e65-960b-44eJkjl97987");
            JSONArray array = new JSONArray();
            array.add(obj);
            JSONObject shop_user = new JSONObject();
            shop_user.put("22728",array);
            JSONObject res = logic.special_register("autotester","7cfa3fe9-96c4-4e65-960b-44eJkjl97987",shop_user,"NORMAL",true,face_url,false,true,null,null,null,null,null,null,null,null,null);
            String face_id = res.getString("face_id");

            JSONObject res1 = logic.specialFace_delete("autotester222","7cfa3fe9-96c4-4e65-960b-44eJkjl97987",face_id);
            Integer code = res.getInteger("code");
            checkArgument(code == 1000  , "特殊人物人脸删除(正确入参&格式),code="+code);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("特殊人物人脸删除(正确入参&格式)");
        }
    }

    /**
     * ====================5. 特殊人物人脸删除(必填项不传)======================
     */
    @Test
    public void specialMan_faceDelete1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject res = logic.specialFace_delete("","","");
            Integer code = res.getInteger("code");
            checkArgument(code == 1001  , " 特殊人物人脸删除(必填项不传),code="+code);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData(" 特殊人物人脸删除(必填项不传)");
        }
    }

    /**
     * ====================5. 特殊人物人脸删除(参数超出【支持的字符范围】)======================
     */
    @Test
    public void specialMan_faceDelete2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject res = logic.specialFace_delete("&*&（*（","……*&……*……","&(*&(&*(");
            Integer code = res.getInteger("code");
            checkArgument(code == 1001  , "特殊人物人脸删除(参数超出【支持的字符范围】),code="+code);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("特殊人物人脸删除(参数超出【支持的字符范围】)");
        }
    }

    /**
     * ====================6. 自定义组删除(正确入参&格式)======================
     */
    @Test
    public void system_Delete() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject obj = new JSONObject();
            obj.put("user_id","7cfa3fe9-96c4-4e65-960b-44edc3f18b68");
            JSONArray array = new JSONArray();
            array.add(obj);
            JSONObject shop_user = new JSONObject();
            shop_user.put("22728",array);
            logic.special_register("autotesterss","111224534535",shop_user,"NORMAL",true,face_url,false,true,null,null,null,null,null,null,null,null,null);

            JSONObject res = logic.self_delete("autotesterss");
            Integer code = res.getInteger("code");
            checkArgument(code == 1000  , "自定义组删除(正确入参&格式),code="+code);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("特殊人物人脸删除(正确入参&格式)");
        }
    }

    /**
     * ====================6. 自定义组删除(必填项不传)======================
     */
    @Test
    public void system_Delete1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject res = logic.self_delete(null);
            Integer code = res.getInteger("code");
            checkArgument(code == 1001  , "自定义组删除(必填项不传),code="+code);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("特殊人物人脸删除(必填项不传)");
        }
    }

    /**
     * ====================6. 自定义组删除(参数超出【支持的字符范围】)======================
     */
    @Test
    public void system_Delete2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject res = logic.self_delete("&&*^*&^^^*^*^*^*&^*");
            Integer code = res.getInteger("code");
            checkArgument(code == 1001  , "自定义组删除(参数超出【支持的字符范围】),code="+code);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("特殊人物人脸删除(参数超出【支持的字符范围】)");
        }
    }


    /**
     * ====================7. 消费者身份转变(正确入参&格式)======================
     */
//    @Test
//    public void change_user() {
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            JSONObject obj = new JSONObject();
//            obj.put("user_id","7cfa3fe9-96c4-4e65-960b-44eJkjl97987");
//            JSONArray array = new JSONArray();
//            array.add(obj);
//            JSONObject shop_user = new JSONObject();
//            shop_user.put("22728",array);
//            String face_url = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fattachbak.dataguru.cn%2Fattachments%2Fportal%2F201812%2F29%2F161729gqqfq4qa4oli51oi.jpg&refer=http%3A%2F%2Fattachbak.dataguru.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1614309271&t=70335c668b3981828084589637d0b3f0";
//            JSONObject res = logic.special_register("oldUser","7cfa3fe9-96c4-4e65-960b-44eJkjluiu9897",shop_user,"NORMAL",true,face_url,false,true,null,null,null,null,null,null,null,null,null);
//            String face_id = res.getString("face_id");
//
//            JSONObject res1 = logic.changeUser("22728","7cfa3fe9-96c4-4e65-960b-44eJkjluiu9897","oldUser","878839128391yy",face_id);
//            Integer code = res.getInteger("code");
//            checkArgument(code == 1000  , "特殊人物人脸删除(正确入参&格式),code="+code);
//        } catch (AssertionError e) {
//            appendFailReason(e.toString());
//        } catch (Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            saveData("特殊人物人脸删除(正确入参&格式)");
//        }
//    }
    /**
     * ====================3.11. 查询组下所有人脸接口，返回每个人脸的Feature======================
     */
    //@Test
    public void default_search() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
             JSONObject res = logic.allFace_search("92412fb2-7ea7-46fa-b94a-7485d3342342jk3","gate","22728--DEFAULT--20210122");
             JSONArray data= res.getJSONArray("data");
              userId = data.getJSONObject(0).getString("userId");
              imgUrl = data.getJSONObject(0).getString("imgUrl");
             checkArgument(data.size()!=0  , "人物人脸查询(正确入参&格式),data:"+data.size());
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("人物人脸查询(正确入参&格式)");
        }
    }


    /**
     * ====================3.1. 默认组人脸检索======================
     */
    @Test
    public void default_search1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
              JSONObject ress = logic.allFace_search("92412fb2-7ea7-46fa-b94a-7485d33478234uU","gate","22728--DEFAULT--20210123");
              JSONArray data= ress.getJSONArray("data");
              String imgUrl = data.getJSONObject(0).getString("imgUrl");

              JSONObject res = logic.default_search(imgUrl,1,true,false,null,"FACE",false);
              Integer code = res.getInteger("code");
              JSONArray faces = res.getJSONArray("faces");
              checkArgument(code == 1000&& faces.size()!=0  , "默认组人脸检索(正确入参&格式),code="+code);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("默认组人脸检索(正确入参&格式)");
        }
    }
    /**
     * ====================3.1. 默认组人脸检索(必填项不填写)======================
     */
    @Test
    public void default_search2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject res = logic.default_search("",1,true,false,null,"FACE",false);
            Integer code = res.getInteger("code");
            checkArgument(code == 1001  , "默认组人脸检索(必填项不填写),code="+code);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("默认组人脸检索(必填项不填写)");
        }
    }
    /**
    * ====================3.2. 默认组用户查询======================
    */
    @Test
    public void defaultUser_search() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject ress = logic.allFace_search("92412fb2-7ea7-46fa-b94a-7485d333442jjw","gate","22728--DEFAULT--20210120");
            JSONArray data= ress.getJSONArray("data");
            String userId = data.getJSONObject(0).getString("userId");

            JSONObject res = logic.default_userSearch("22728",userId);
            Integer code = res.getInteger("code");
            checkArgument(code == 1000  , " 默认组用户查询(正确入参&格式),code="+code);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData(" 默认组用户查询(正确入参&格式)");
        }
    }

    /**
     * ====================3.2. 默认组用户查询======================
     */
    @Test
    public void defaultUser_search1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject res = logic.default_userSearch("","");
            Integer code = res.getInteger("code");
            checkArgument(code == 1001  , " 默认组用户查询(必填项不填写),code="+code);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData(" 默认组用户查询(必填项不填写)");
        }
    }


    /**
     * ====================3.  删除默认组用户(正确入参&格式)======================
     */
    //@Test
    public void defaultUser_delete() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
                JSONObject res = logic.allFace_search("92412fb2-7ea7-46fa-b94a-7485d3343e3e3e", "gate", "22728--DEFAULT--20210120");
                JSONArray data = res.getJSONArray("data");
                userId = data.getJSONObject(0).getString("userId");
                imgUrl = data.getJSONObject(0).getString("imgUrl");

                JSONObject res1 = logic.delete_default_user("22728", userId);
                Integer code = res1.getInteger("code");
                checkArgument(code == 1000, " 删除默认组用户(正确入参&格式),code=" + code);
            } catch (AssertionError e) {
                appendFailReason(e.toString());
            } catch (Exception e) {
                appendFailReason(e.toString());
            } finally {
                saveData(" 删除默认组用户(正确入参&格式)");
            }
        }


    /**
     * ====================3.  删除默认组用户(必填项不填写)======================
     */
    @Test
    public void defaultUser_delete1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject res1 = logic.delete_default_user(null, null);
            Integer code = res1.getInteger("code");
            checkArgument(code == 1001, " 删除默认组用户(必填项不填写),code=" + code);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData(" 删除默认组用户(必填项不填写)");
        }
    }
    /**
     * ====================3.  删除默认组用户(超出字符范围)======================
     */
    @Test
    public void defaultUser_delete2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject res1 = logic.delete_default_user("……*&……*", "*&*&……");
            Integer code = res1.getInteger("code");
            checkArgument(code == 1001, " 删除默认组用户(超出字符范围),code=" + code);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData(" 删除默认组用户(超出字符范围)");
        }
    }


    /**
     * ====================4.  删除默认组用户的人脸(正确入参&格式)======================
     */
    //@Test
    public void defaultFace_delete1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject res = logic.allFace_search("92412fb2-7ea7-46fa-b94a-7485d3343e3e3e", "gate", "22728--DEFAULT--20210110");
            String faceId = "";
            JSONArray data = res.getJSONArray("data");
            userId = data.getJSONObject(0).getString("userId");
            faceId = data.getJSONObject(0).getString("faceId");

            JSONObject res1 = logic.delete_default_face("22728", userId,faceId);
            Integer code = res1.getInteger("code");
            checkArgument(code == 1000, " 删除默认组用户的人脸(正确入参&格式),code=" + code);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData(" 删除默认组用户的人脸(正确入参&格式)");
        }
    }

    /**
     * ====================4.  删除默认组用户的人脸(必填项不填写)======================
     */
    @Test
    public void defaultFace_delete2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject res1 = logic.delete_default_face(null, null,null);
            Integer code = res1.getInteger("code");
            checkArgument(code == 1001, " 删除默认组用户的人脸(必填项不填写),code=" + code);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData(" 删除默认组用户的人脸(必填项不填写)");
        }
    }

    /**
     * ====================4.  删除默认组用户的人脸(超出字符范围)======================
     */
    @Test
    public void defaultFace_delete3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject res1 = logic.delete_default_face("*&*^&^*", "%*&&*","&*^&^(");
            Integer code = res1.getInteger("code");
            checkArgument(code == 1001, " 删除默认组用户的人脸(超出字符范围),code=" + code);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData(" 删除默认组用户的人脸(超出字符范围)");
        }
    }



}

