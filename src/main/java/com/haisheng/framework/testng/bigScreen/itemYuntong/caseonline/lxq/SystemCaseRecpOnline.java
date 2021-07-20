package com.haisheng.framework.testng.bigScreen.itemYuntong.caseonline.lxq;

import com.alibaba.fastjson.JSONArray;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumChecklistAppId;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumChecklistConfId;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumJobName;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.app.presalesreception.AppPreSalesReceptionCreateScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.app.presalesreception.voiceSubmit;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.customermanage.PreSaleCustomerModelListScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.customermanage.PreSaleCustomerStyleListScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.presalesreception.FinishReceptionScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.presalesreception.PreSalesReceptionPageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.util.SceneUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumAccount;
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
public class SystemCaseRecpOnline extends TestCaseCommon implements TestCaseStd {



    EnumTestProduce PRODUCE = EnumTestProduce.YT_ONLINE_SSO;
    EnumAccount ALL_AUTHORITY = EnumAccount.YT_ALL_ONLINE_LXQ;
    VisitorProxy visitor = new VisitorProxy(PRODUCE);
    SceneUtil businessUtil = new SceneUtil(visitor);



    YunTongInfoOnline info = new YunTongInfoOnline();


    CommonConfig commonConfig = new CommonConfig();

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        //替换checklist的相关信息
        commonConfig.checklistAppId = EnumChecklistAppId.DB_APP_ID_SCREEN_SERVICE.getId();
        commonConfig.checklistConfId = EnumChecklistConfId.DB_SERVICE_ID_CRM_ONLINE_SERVICE.getId();
        commonConfig.checklistQaOwner = "吕雪晴";
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.YUNTONG_ONLINE_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, PRODUCE.getDesc() + commonConfig.checklistQaOwner);
        //替换钉钉推送
        commonConfig.dingHook = DingWebhook.ONLINE_CAR_CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        //放入shopId
        commonConfig.product = PRODUCE.getAbbreviation();
        commonConfig.referer = PRODUCE.getReferer();
        commonConfig.shopId = PRODUCE.getShopId();
        commonConfig.roleId = ALL_AUTHORITY.getRoleId();
        beforeClassInit(commonConfig);
        businessUtil.loginPc(ALL_AUTHORITY);

        visitor.setProduct(EnumTestProduce.YT_ONLINE_CAR);  //展厅接待模块

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
     * -------------------------------服务管理 - 评价管理 -----------------------------------------
     */

//    /**
//     *     手机评价
//     */
//    @Test
//    public void doEvaluate() {
//        logger.logCaseStart(caseResult.getCaseName());
//
//        try {
//            //销售接待
//            String phone = "1380110" + Integer.toString((int) ((Math.random() * 9 + 1) * 1000));//手机号
//            Long car_style_id = PreSaleCustomerStyleListScene.builder().shopId(info.oneshopid).build().invoke(visitor).getJSONArray("list").getJSONObject(0).getLong("style_id");
//            Long car_model_id = PreSaleCustomerModelListScene.builder().styleId(car_style_id).build().invoke(visitor).getJSONArray("list").getJSONObject(0).getLong("model_id");
//            AppPreSalesReceptionCreateScene.builder().customerName("自动化创建的").customerPhone(phone).sexId("1").intentionCarModelId(Long.toString(car_model_id)).estimateBuyCarTime("2100-07-12").build().invoke(visitor);
//            //获取接待id
//            Long recId = PreSalesReceptionPageScene.builder().phone(phone).build().invoke(visitor).getJSONArray("list").getJSONObject(0).getLong("id");
//            //完成接待
//            FinishReceptionScene.builder().id(recId).shopId(info.oneshopid).build().invoke(visitor);
//
//            //获取评价选项
//            JSONArray evaluate_info_list = new JSONArray();
//
//            JSONArray optarr = PreSalesRecpEvaluateOpt.builder().reception_id(recId).build().invoke(visitor).getJSONArray("list");
//
//            for (int i = 0; i < optarr.size(); i++) {
//                JSONObject asubmit = new JSONObject();
//                Long id = optarr.getJSONObject(i).getLong("id");
//                int score;
//                JSONArray aopt = optarr.getJSONObject(i).getJSONArray("answer_list");
//                if (i % 2 == 0) {
//                    score = aopt.getJSONObject(aopt.size()).getInteger("score");
//                } else {
//                    score = aopt.getJSONObject(0).getInteger("score");
//                }
//                asubmit.put("id", id);
//                asubmit.put("score", score);
//                evaluate_info_list.add(asubmit);
//
//            }
//
//            //提交评价
//            PreSalesRecpEvaluateSubmit.builder().reception_id(recId).evaluate_info_list(evaluate_info_list).build().invoke(visitor);
//        } catch (AssertionError e) {
//            appendFailReason(e.toString());
//        } catch (Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            saveData("接待后评价");
//        }
//    }


        @Test
        public void voice() {
            logger.logCaseStart(caseResult.getCaseName());

            try {


            String base1 = encodeBase64File("src/main/java/com/haisheng/framework/testng/bigScreen/itemYuntong/casedaily/lxq/费用.aac");
            String base2 = encodeBase64File("src/main/java/com/haisheng/framework/testng/bigScreen/itemYuntong/casedaily/lxq/必须.aac");
            String base3 = encodeBase64File("src/main/java/com/haisheng/framework/testng/bigScreen/itemYuntong/casedaily/lxq/其他.aac");


            //销售接待
                Long starttime = System.currentTimeMillis();
                String record_name = "9999_"+starttime+".acc";
                JSONArray reception_nodes = new JSONArray();

                String phone1 = "1380110" + Integer.toString((int) ((Math.random() * 9 + 1) * 1000));//手机号
                String phone2 = "1380220" + Integer.toString((int) ((Math.random() * 9 + 1) * 1000));//手机号
                String phone3 = "1380330" + Integer.toString((int) ((Math.random() * 9 + 1) * 1000));//手机号
                Long car_style_id = PreSaleCustomerStyleListScene.builder().shopId(info.oneshopid).build().invoke(visitor).getJSONArray("list").getJSONObject(0).getLong("style_id");
                Long car_model_id = PreSaleCustomerModelListScene.builder().styleId(car_style_id).build().invoke(visitor).getJSONArray("list").getJSONObject(0).getLong("model_id");

                //第一个接待
                AppPreSalesReceptionCreateScene.builder().customerName("自自").customerPhone(phone1).sexId("1").intentionCarModelId(Long.toString(car_model_id)).estimateBuyCarTime("2100-07-12").build().invoke(visitor);
                //获取接待id
                Long recId1 = PreSalesReceptionPageScene.builder().phone(phone1).build().invoke(visitor).getJSONArray("list").getJSONObject(0).getLong("id");

                //第二个接待
                AppPreSalesReceptionCreateScene.builder().customerName("动动").customerPhone(phone2).sexId("0").intentionCarModelId(Long.toString(car_model_id)).estimateBuyCarTime("2100-07-12").build().invoke(visitor);
                //获取接待id
                Long recId2 = PreSalesReceptionPageScene.builder().phone(phone2).build().invoke(visitor).getJSONArray("list").getJSONObject(0).getLong("id");

                //第三个接待
                AppPreSalesReceptionCreateScene.builder().customerName("化化").customerPhone(phone3).sexId("1").intentionCarModelId(Long.toString(car_model_id)).estimateBuyCarTime("2100-07-12").build().invoke(visitor);
                //获取接待id
                Long recId3 = PreSalesReceptionPageScene.builder().phone(phone3).build().invoke(visitor).getJSONArray("list").getJSONObject(0).getLong("id");



                Thread.sleep(301000);
                Long endtime = System.currentTimeMillis();
                visitor.setProduct(EnumTestProduce.YT_ONLINE_CONTROL);
                voiceSubmit.builder().base64(base1).record_name(record_name).start_time(starttime).end_time(endtime).reception_id(recId1).reception_nodes(reception_nodes).build().invoke(visitor);
                voiceSubmit.builder().base64(base2).record_name(record_name).start_time(starttime).end_time(endtime).reception_id(recId2).reception_nodes(reception_nodes).build().invoke(visitor);
                voiceSubmit.builder().base64(base3).record_name(record_name).start_time(starttime).end_time(endtime).reception_id(recId3).reception_nodes(reception_nodes).build().invoke(visitor);



                visitor.setProduct(EnumTestProduce.YT_ONLINE_CAR);
                //完成接待
                FinishReceptionScene.builder().id(recId1).shopId(info.oneshopid).build().invoke(visitor);
                FinishReceptionScene.builder().id(recId2).shopId(info.oneshopid).build().invoke(visitor);
                FinishReceptionScene.builder().id(recId3).shopId(info.oneshopid).build().invoke(visitor);


            } catch (AssertionError e) {
                appendFailReason(e.toString());
            } catch (Exception e) {
                appendFailReason(e.toString());
            } finally {
                saveData("接待带语音");
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




}
