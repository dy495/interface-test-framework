package com.haisheng.framework.testng.bigScreen.itemYuntong.casedaily.lxq;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumChecklistAppId;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumChecklistConfId;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumJobName;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.util.YunTongInfo;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.general.GeneralEnumValueListScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.manage.VoiceDetailScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.manage.VoiceEvaluationPageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.sensitivewords.SensitiveWordsLabelListScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.sensitivewords.SensitiveBehaviorDetailScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.sensitivewords.SensitiveBehaviorPageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.util.SceneUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * ????????????
 *
 * @author lxq
 * @date 2021/1/29 11:17
 */
public class HuiTing_DataCase extends TestCaseCommon implements TestCaseStd {
    EnumTestProduct product = EnumTestProduct.YT_DAILY_GK;
    EnumAccount ALL_AUTHORITY = EnumAccount.YT_DAILY_YS;
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
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.JIAOCHEN_DAILY_TEST.getJobName());
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
     * --------------------------------- ?????????????????? ---------------------------------
     */

    @Test
    public void voiceEvaluationIn1() {

        logger.logCaseStart(caseResult.getCaseName());
        try {//????????????0 ??????
            JSONArray arr1 = VoiceEvaluationPageScene.builder().page(1).size(50).build().visitor(visitor).execute().getJSONArray("list");
            if (arr1.size() > 0) {
                JSONObject obj = arr1.getJSONObject(0);
                String list_receptor_name = obj.getString("receptor_name");
                String list_reception_time = obj.getString("reception_time").substring(0, 10);
//                String list_reception_duration = obj.getString("reception_duration");
                String list_customer_name = obj.getString("customer_name");
                String list_customer_phone = obj.getString("customer_phone");
                Long id = obj.getLong("id");

                JSONObject detailObj = VoiceDetailScene.builder().id(id).build().visitor(visitor).execute();
                String detail_receptor_name = detailObj.getString("receptor_name");
                String detail_reception_time = detailObj.getString("start_time").substring(0, 10);
//                String detail_reception_duration = detailObj.getString("reception_duration");
                String detail_customer_name = detailObj.getString("name");
                String detail_customer_phone = detailObj.getString("phone");

                Preconditions.checkArgument(list_receptor_name.equals(detail_receptor_name), "???????????????????????????=" + list_receptor_name + " ,???????????????????????????=" + detail_receptor_name);
                Preconditions.checkArgument(list_reception_time.equals(detail_reception_time), "?????????????????????=" + list_reception_time + " ,?????????????????????=" + detail_reception_time);
//                Preconditions.checkArgument(list_reception_duration.equals(detail_reception_duration), "?????????????????????=" + list_reception_duration + " ,?????????????????????=" + detail_reception_duration);
                Preconditions.checkArgument(list_customer_name.equals(detail_customer_name), "?????????????????????=" + list_customer_name + " ,?????????????????????=" + detail_customer_name);
                Preconditions.checkArgument(list_customer_phone.equals(detail_customer_phone), "?????????????????????=" + list_customer_phone + " ,?????????????????????=" + detail_customer_phone);
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("????????????????????????????????????????????????");
        }
    }

    @Test
    public void voiceEvaluationDetailIn1() {

        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray arr1 = VoiceEvaluationPageScene.builder().page(1).size(20).evaluateStatus(500).build().visitor(visitor).execute().getJSONArray("list");
            if (arr1.size() > 0) {
                for (int i = 0; i < arr1.size(); i++) {
                    JSONObject obj = arr1.getJSONObject(i);
                    Long id = obj.getLong("id");
                    JSONObject detailObj = VoiceDetailScene.builder().id(id).build().visitor(visitor).execute();
                    int average_score = detailObj.getInteger("average_score");
                    int score = 0;
                    JSONArray scores = detailObj.getJSONArray("scores");
                    for (int j = 0; j < scores.size(); j++) {
                        score = score + scores.getJSONObject(j).getInteger("score");
                    }
                    int jisuan = Math.round(score / 5);
                    Preconditions.checkArgument(average_score <= jisuan + 1 && average_score >= jisuan - 1, "???????????????" + average_score + " != ??????????????????" + jisuan);
                }
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("?????????????????????????????????=?????????????????????/5 ????????????");
        }
    }

    @Test
    public void voiceEvaluationDetailIn2() {

        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray arr1 = VoiceEvaluationPageScene.builder().page(1).size(20).evaluateStatus(500).build().visitor(visitor).execute().getJSONArray("list");
            if (arr1.size() > 0) {
                for (int i = 0; i < arr1.size(); i++) {
                    JSONObject obj = arr1.getJSONObject(i);
                    Long id = obj.getLong("id");
                    JSONObject detailObj = VoiceDetailScene.builder().id(id).build().visitor(visitor).execute();
                    List lable = new ArrayList<>();
                    JSONArray link_label_list = detailObj.getJSONArray("link_label_list");
                    for (int j = 0; j < link_label_list.size(); j++) { //?????? link_label_list ???????????????????????????
                        JSONArray link_labels = link_label_list.getJSONObject(j).getJSONArray("labels");
                        for (int k = 0; k < link_labels.size(); k++) {
                            if (!link_labels.getJSONObject(k).getBoolean("is_hit"))
                                lable.add(link_labels.getJSONObject(k).getString("lable"));
                        }
                    }
                    JSONArray advice_list = detailObj.getJSONArray("advice_list");
                    for (int j = 0; j < advice_list.size(); j++) {
                        String advice_lable = advice_list.getJSONObject(j).getString("lable");
                        Preconditions.checkArgument(lable.contains(advice_lable), "??????ID" + id + "???????????????" + advice_lable + "?????????????????????");
                    }
                }
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("????????????????????????????????????????????? ??? ???????????????????????? ???????????? ?????????");
        }
    }

    //@Test
    public void voiceEvaluationDetailIn3() {

        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray arr1 = VoiceEvaluationPageScene.builder().page(1).size(20).evaluateStatus(500).build().visitor(visitor).execute().getJSONArray("list");
            if (arr1.size() > 0) {
                for (int i = 0; i < arr1.size(); i++) {
                    JSONObject obj = arr1.getJSONObject(i);
                    Long id = obj.getLong("id");
                    JSONObject detailObj = VoiceDetailScene.builder().id(id).build().visitor(visitor).execute();
                    //????????????????????????
                    HashMap<String, String> showscore = new HashMap<>();
                    JSONArray scores = detailObj.getJSONArray("scores");
                    for (int j = 0; j < scores.size(); j++) {
                        JSONObject obj1 = scores.getJSONObject(j);
                        showscore.put(obj1.getString("type"), obj1.getString("score"));
                    }
                    HashMap<String, String> jisuanscore = new HashMap<>();

                    //????????????????????????????????????????????????
                    JSONArray link_label_list = detailObj.getJSONArray("link_label_list");
                    for (int j = 0; j < link_label_list.size(); j++) {
                        int count = 0;
                        JSONArray link_labels = link_label_list.getJSONObject(j).getJSONArray("labels");
                        for (int k = 0; k < link_labels.size(); k++) {
                            if (link_labels.getJSONObject(k).getBoolean("is_hit"))
                                count++;
                        }
                        jisuanscore.put(link_label_list.getJSONObject(j).getString("type"), Integer.toString(Math.round(count * 100 / link_labels.size())));
                    }
                    //??????????????????
                    for (String key : showscore.keySet()) {
                        Preconditions.checkArgument(showscore.get(key).equals(jisuanscore.get(key)), key + "????????????PC????????????" + showscore.get(key) + " != ??????????????????????????????" + jisuanscore.get(key));
                    }
                }
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("???????????????????????????????????? ????????? ???????????? ???????????????/???????????? * 100");
        }
    }


    /**
     * --------------------------------- ??????????????? ---------------------------------
     */

    //@Test
    //todo ?????????
    public void sensitiveIn1() {

        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray arr = GeneralEnumValueListScene.builder().enumType("SENSITIVE_WORDS_TYPES").build().visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < arr.size(); i++) {
                int evaluate_status = arr.getJSONObject(i).getInteger("key");
                String evaluate_status_name = arr.getJSONObject(i).getString("value"); //??????????????????

                int lablecount = 0; // ????????????
                JSONArray lablearray = SensitiveWordsLabelListScene.builder().sensitiveWordsType(evaluate_status).build().visitor(visitor).execute().getJSONArray("list");
                for (int j = 0; j < lablearray.size(); j++) {
                    lablecount += lablearray.getJSONObject(j).getInteger("count");
                }

                int listcount = 0; //????????????????????????????????????????????????
                int total = SensitiveBehaviorPageScene.builder().page(1).size(1).sensitiveWordsType(evaluate_status).build().visitor(visitor).execute().getInteger("total");
                if (total <= 100) {
                    JSONArray listarray = SensitiveBehaviorPageScene.builder().page(1).size(100).sensitiveWordsType(evaluate_status).build().visitor(visitor).execute().getJSONArray("list");
                    for (int k = 0; k < listarray.size(); k++) {
                        Long id = listarray.getJSONObject(k).getLong("id");
                        listcount += SensitiveBehaviorDetailScene.builder().id(id).build().visitor(visitor).execute().getJSONArray("sensitive_words").size();
                    }
                } else {
                    for (int j = 1; j < total / 100 + 1; j++) {
                        JSONArray listarray = SensitiveBehaviorPageScene.builder().page(j).size(100).sensitiveWordsType(evaluate_status).build().visitor(visitor).execute().getJSONArray("list");
                        for (int k = 0; k < listarray.size(); k++) {
                            Long id = listarray.getJSONObject(k).getLong("id");
                            listcount += SensitiveBehaviorDetailScene.builder().id(id).build().visitor(visitor).execute().getJSONArray("sensitive_words").size();
                        }
                    }
                }
                Preconditions.checkArgument(lablecount == listcount, "??????????????????" + evaluate_status_name + " ?????????????????????" + lablecount + " != ?????????????????????" + listcount);


            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {

            appendFailReason(e.toString());
        } finally {
            saveData("???????????????????????????????????????????????????????????????????????????");
        }
    }

    /**
     * --------------------------------- ?????????????????? ---------------------------------
     */


}
