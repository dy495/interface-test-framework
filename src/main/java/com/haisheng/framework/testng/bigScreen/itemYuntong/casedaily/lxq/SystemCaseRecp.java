package com.haisheng.framework.testng.bigScreen.itemYuntong.casedaily.lxq;

import com.alibaba.fastjson.JSONArray;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumChecklistAppId;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumChecklistConfId;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumJobName;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.app.presalesreception.AppPreSalesReceptionCreateScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.app.voicerecord.AppVoiceRecordSubmitScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.customermanage.PreSaleCustomerModelListScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.customermanage.PreSaleCustomerStyleListScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.manage.EvaluateFollowUpScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.manage.EvaluatePageV4Scene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.presalesreception.FinishReceptionScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.presalesreception.PreSalesReceptionPageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.presalesreception.PreSalesRecpEvaluateSubmit;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.util.SceneUtil;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.util.YunTongInfo;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumAccount;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import sun.misc.BASE64Encoder;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;

/**
 * @author lxq
 * @date 2021/1/29 11:17
 */
public class SystemCaseRecp extends TestCaseCommon implements TestCaseStd {

    EnumTestProduct product = EnumTestProduct.YT_DAILY_JD;
    EnumAccount ALL_AUTHORITY = EnumAccount.YT_DAILY_LXQ;
    VisitorProxy visitor = new VisitorProxy(product);
    SceneUtil businessUtil = new SceneUtil(visitor);
    YunTongInfo info = new YunTongInfo();
    CommonConfig commonConfig = new CommonConfig();

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        //??????checklist???????????????
        commonConfig.checklistAppId = EnumChecklistAppId.DB_APP_ID_SCREEN_SERVICE.getId();
        commonConfig.checklistConfId = EnumChecklistConfId.DB_SERVICE_ID_CRM_DAILY_SERVICE.getId();
        commonConfig.checklistQaOwner = "?????????";
        //??????jenkins-job???????????????
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.YUNTONG_DAILY_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, product.getDesc() + commonConfig.checklistQaOwner);
        //??????????????????
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        //??????shopId
        commonConfig.setShopId(product.getShopId()).setRoleId(ALL_AUTHORITY.getRoleId()).setProduct(product.getAbbreviation());
        beforeClassInit(commonConfig);
        businessUtil.loginPc(ALL_AUTHORITY);

    }

    @AfterClass
    @Override
    public void clean() {
        afterClassClean();
    }

    @BeforeMethod
    @Override
    public void createFreshCase(Method method) {

        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);

    }

    /**
     * -------------------------------???????????? - ???????????? -----------------------------------------
     */

    /**
     * ?????????????????????
     */
    @Test
    public void doEvaluate() {
        logger.logCaseStart(caseResult.getCaseName());

        try {
            //?????????????????????????????????
            Long recId = info.startrecption(true);

            //??????????????????
            visitor.setProduct(EnumTestProduct.YT_DAILY_JD);
            commonConfig.setShopId(null);
            commonConfig.setRoleId(null);
            JSONArray evaluate_info_list = info.evaluateInfo(recId, "mid");

            //????????????
            PreSalesRecpEvaluateSubmit.builder().reception_id(recId).evaluate_info_list(evaluate_info_list).build().visitor(visitor).execute();

            //PC??????
            commonConfig.setShopId(product.getShopId());
            commonConfig.setRoleId(ALL_AUTHORITY.getRoleId());
            Long id = EvaluatePageV4Scene.builder().page(1).size(1).evaluateType(5).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getLong("id");
            EvaluateFollowUpScene.builder().id(id).evaluate_type(5).shopId(info.oneshopid).remark("????????????????????????????????????????????????????????????????????????").build().visitor(visitor).execute();

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("???????????????+??????");

        }
    }


    @Test
    public void voice() {
        logger.logCaseStart(caseResult.getCaseName());

        try {


            String base1 = encodeBase64File("src/main/java/com/haisheng/framework/testng/bigScreen/itemYuntong/casedaily/lxq/??????.aac");
            String base2 = encodeBase64File("src/main/java/com/haisheng/framework/testng/bigScreen/itemYuntong/casedaily/lxq/??????.aac");
            String base3 = encodeBase64File("src/main/java/com/haisheng/framework/testng/bigScreen/itemYuntong/casedaily/lxq/??????.aac");


            //????????????
            Long starttime = System.currentTimeMillis();
            String record_name = "9999_" + starttime + ".acc";
            JSONArray reception_nodes = new JSONArray();

            String phone1 = "1380110" + Integer.toString((int) ((Math.random() * 9 + 1) * 1000));//?????????
            String phone2 = "1380220" + Integer.toString((int) ((Math.random() * 9 + 1) * 1000));//?????????
            String phone3 = "1380330" + Integer.toString((int) ((Math.random() * 9 + 1) * 1000));//?????????

            String name1 = "??????" + dt.getHistoryDate(0);
            String name2 = "??????" + dt.getHistoryDate(0);
            String name3 = "??????" + dt.getHistoryDate(0);
            Long car_style_id = PreSaleCustomerStyleListScene.builder().shopId(info.oneshopid).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getLong("style_id");
            Long car_model_id = PreSaleCustomerModelListScene.builder().styleId(car_style_id).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getLong("model_id");

            //???????????????
            AppPreSalesReceptionCreateScene.builder().customerName(name1).customerPhone(phone1).sexId("1").intentionCarModelId(Long.toString(car_model_id)).estimateBuyCarTime("2100-07-12").build().visitor(visitor).execute();
            //????????????id
            Long recId1 = PreSalesReceptionPageScene.builder().phone(phone1).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getLong("id");

            //???????????????
            AppPreSalesReceptionCreateScene.builder().customerName(name2).customerPhone(phone2).sexId("0").intentionCarModelId(Long.toString(car_model_id)).estimateBuyCarTime("2100-07-12").build().visitor(visitor).execute();
            //????????????id
            Long recId2 = PreSalesReceptionPageScene.builder().phone(phone2).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getLong("id");

            //???????????????
            AppPreSalesReceptionCreateScene.builder().customerName(name3).customerPhone(phone3).sexId("1").intentionCarModelId(Long.toString(car_model_id)).estimateBuyCarTime("2100-07-12").build().visitor(visitor).execute();
            //????????????id
            Long recId3 = PreSalesReceptionPageScene.builder().phone(phone3).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getLong("id");


            Thread.sleep(301000);
            Long endtime = System.currentTimeMillis();
            visitor.setProduct(EnumTestProduct.YT_DAILY_GK);
            AppVoiceRecordSubmitScene.builder().base64(base1).recordName(record_name).startTime(starttime).endTime(endtime).receptionId(recId1).receptionNodes(reception_nodes).build().visitor(visitor).execute();
            AppVoiceRecordSubmitScene.builder().base64(base2).recordName(record_name).startTime(starttime).endTime(endtime).receptionId(recId2).receptionNodes(reception_nodes).build().visitor(visitor).execute();
            AppVoiceRecordSubmitScene.builder().base64(base3).recordName(record_name).startTime(starttime).endTime(endtime).receptionId(recId3).receptionNodes(reception_nodes).build().visitor(visitor).execute();
//            voiceSubmit.builder().base64(base1).record_name(record_name).start_time(starttime).end_time(endtime).reception_id(recId1).reception_nodes(reception_nodes).build().invoke(visitor);
//            voiceSubmit.builder().base64(base2).record_name(record_name).start_time(starttime).end_time(endtime).reception_id(recId2).reception_nodes(reception_nodes).build().invoke(visitor);
//            voiceSubmit.builder().base64(base3).record_name(record_name).start_time(starttime).end_time(endtime).reception_id(recId3).reception_nodes(reception_nodes).build().invoke(visitor);


            visitor.setProduct(EnumTestProduct.YT_DAILY_JD);
            //????????????
            FinishReceptionScene.builder().id(recId1).shopId(info.oneshopid).build().visitor(visitor).execute();
            FinishReceptionScene.builder().id(recId2).shopId(info.oneshopid).build().visitor(visitor).execute();
            FinishReceptionScene.builder().id(recId3).shopId(info.oneshopid).build().visitor(visitor).execute();

            //??????

            //??????????????????
            commonConfig.setShopId(null);
            commonConfig.setRoleId(null);

            visitor.setProduct(EnumTestProduct.YT_DAILY_JD);
            //??????
            JSONArray evaluate_info_list1 = info.evaluateInfo(recId1, "high");
            //????????????
            PreSalesRecpEvaluateSubmit.builder().reception_id(recId1).evaluate_info_list(evaluate_info_list1).build().visitor(visitor).execute();

            JSONArray evaluate_info_list2 = info.evaluateInfo(recId2, "low");
            PreSalesRecpEvaluateSubmit.builder().reception_id(recId2).evaluate_info_list(evaluate_info_list2).build().visitor(visitor).execute();

            JSONArray evaluate_info_list3 = info.evaluateInfo(recId3, "mid");
            PreSalesRecpEvaluateSubmit.builder().reception_id(recId3).evaluate_info_list(evaluate_info_list3).build().visitor(visitor).execute();


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("?????? ???????????????+???????????????");
        }

    }


    public static String encodeBase64File(String path) throws Exception {
        File file = new File(path);
        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[(int) file.length()];
        inputFile.read(buffer);
        inputFile.close();
        return new BASE64Encoder().encode(buffer);
    }


    // todo ??????????????????+??????????????????
    // ??????= 100min, ??????=1
    // ?????? =1min ??????=100

    //todo ?????????????????? ????????????



}
