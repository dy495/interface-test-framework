package com.haisheng.framework.testng.bigScreen.itemYuntong.caseonline.lxq;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.Response;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumChecklistAppId;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumChecklistConfId;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumJobName;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.specialaudio.SpecialAudioApprovalScene;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.util.YunTongInfo;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.general.GeneralEnumValueListScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.manage.VoiceEvaluationPageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.sensitivewords.SensitiveBehaviorApprovalScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.sensitivewords.SensitiveBehaviorPageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.specialaudio.SpecialAudioPageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.speechtechnique.SpeechTechniquePageScene;
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

/**
 * ????????????
 *
 * @author lxq
 * @date 2021/1/29 11:17
 */
public class HuiTing_SystemCase extends TestCaseCommon implements TestCaseStd {
    EnumTestProduct product = EnumTestProduct.YT_ONLINE_GK;
    EnumAccount ALL_AUTHORITY = EnumAccount.YT_ONLINE_YS;
    VisitorProxy visitor = new VisitorProxy(product);
    SceneUtil util = new SceneUtil(visitor);
    YunTongInfoOnline info = new YunTongInfoOnline(visitor);

    CommonConfig commonConfig = new CommonConfig();


    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");

        //??????checklist???????????????
        commonConfig.checklistAppId = EnumChecklistAppId.DB_APP_ID_SCREEN_SERVICE.getId();
        commonConfig.checklistConfId = EnumChecklistConfId.DB_SERVICE_ID_CRM_ONLINE_SERVICE.getId();
        commonConfig.checklistQaOwner = "?????????";
        //??????jenkins-job???????????????
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.YUNTONG_ONLINE_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, product.getDesc() + commonConfig.checklistQaOwner);
        //??????????????????
        commonConfig.dingHook = DingWebhook.ONLINE_CAR_CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        //??????shopId
        commonConfig.setShopId(product.getShopId()).setRoleId(ALL_AUTHORITY.getRoleId()).setProduct(product.getAbbreviation());
        beforeClassInit(commonConfig);
        util.loginPc(ALL_AUTHORITY);
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
    public void voiceEvaluationShow() {

        logger.logCaseStart(caseResult.getCaseName());
        try {

            JSONArray arr = VoiceEvaluationPageScene.builder().page(1).size(30).build().visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < arr.size(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                Preconditions.checkArgument(obj.containsKey("id"), "??????");
                Preconditions.checkArgument(obj.containsKey("receptor_name"), "??????" + obj.getString("id") + "????????????????????????");
                Preconditions.checkArgument(obj.containsKey("reception_time"), "??????" + obj.getString("id") + "??????????????????");
                Preconditions.checkArgument(obj.containsKey("reception_duration"), "??????" + obj.getString("id") + "??????????????????");
                Preconditions.checkArgument(obj.containsKey("customer_name"), "??????" + obj.getString("id") + "??????????????????");
                Preconditions.checkArgument(obj.containsKey("customer_phone"), "??????" + obj.getString("id") + "????????????????????????");

                Preconditions.checkArgument(obj.containsKey("enter_status_name"), "??????" + obj.getString("id") + "??????????????????");
                if (obj.getString("enter_status_name").equals("????????????") && info.toMinute(obj.getString("reception_duration")) > 5L) {
//                    Preconditions.checkArgument(obj.containsKey("evaluate_score"), "??????" + obj.getString("id") + "??????????????????");
//                    Preconditions.checkArgument(obj.containsKey("evaluate_status_name") && obj.getString("evaluate_status_name").equals("????????????"), "??????" + obj.getString("id") + "?????????????????????");
                } else {
                    Preconditions.checkArgument(obj.containsKey("evaluate_status_name") && obj.getString("evaluate_status_name").equals("????????????"), "??????" + obj.getString("id") + "?????????????????????");
                }

            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("?????????????????????????????????");
        }
    }

    @Test
    public void voiceEvaluationFilter1() {

        logger.logCaseStart(caseResult.getCaseName());
        try {
            String receptor_name = "";
            String customer_name = "";
            String customer_phone = "";
            JSONArray arr = VoiceEvaluationPageScene.builder().page(1).size(50).build().visitor(visitor).execute().getJSONArray("list");
            if (arr.size() > 0) {
                JSONObject obj = arr.getJSONObject(0);
                receptor_name = obj.getString("receptor_name");
                customer_name = obj.getString("customer_name");
                customer_phone = obj.getString("customer_phone");
                JSONArray arr1 = VoiceEvaluationPageScene.builder().page(1).size(50).receptorName(receptor_name).build().visitor(visitor).execute().getJSONArray("list");
                for (int i = 0; i < arr1.size(); i++) {
                    JSONObject obj1 = arr1.getJSONObject(i);
                    String search_receptor_name = obj1.getString("receptor_name");
                    Preconditions.checkArgument(search_receptor_name.toUpperCase().contains(receptor_name.toUpperCase()), "??????????????????=" + receptor_name + " ,????????????" + search_receptor_name);
                }

                JSONArray arr2 = VoiceEvaluationPageScene.builder().page(1).size(50).customerName(customer_name).build().visitor(visitor).execute().getJSONArray("list");
                for (int i = 0; i < arr2.size(); i++) {
                    JSONObject obj1 = arr2.getJSONObject(i);
                    String search_customer_name = obj1.getString("customer_name");
                    Preconditions.checkArgument(search_customer_name.toUpperCase().contains(customer_name.toUpperCase()), "??????????????????=" + customer_name + " ,????????????" + search_customer_name);
                }

                JSONArray arr3 = VoiceEvaluationPageScene.builder().page(1).size(50).customerPhone(customer_phone).build().visitor(visitor).execute().getJSONArray("list");
                for (int i = 0; i < arr3.size(); i++) {
                    JSONObject obj1 = arr3.getJSONObject(i);
                    String search_customer_phone = obj1.getString("customer_phone");
                    Preconditions.checkArgument(search_customer_phone.contains(customer_phone), "??????????????????=" + customer_phone + " ,????????????" + search_customer_phone);
                }
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("?????????????????????????????????");
        }
    }

    @Test(dataProvider = "FILTER", dataProviderClass = YunTongInfo.class)
    public void voiceEvaluationFilter2(String search) {

        logger.logCaseStart(caseResult.getCaseName());
        try {

            JSONArray arr1 = VoiceEvaluationPageScene.builder().page(1).size(50).receptorName(search).build().visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < arr1.size(); i++) {
                JSONObject obj1 = arr1.getJSONObject(i);
                String search_receptor_name = obj1.getString("receptor_name").toUpperCase();
                Preconditions.checkArgument(search_receptor_name.toUpperCase().contains(search.toUpperCase()), "??????????????????=" + search + " ,????????????" + search_receptor_name);
            }
            JSONArray arr2 = VoiceEvaluationPageScene.builder().page(1).size(50).customerName(search).build().visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < arr2.size(); i++) {
                JSONObject obj1 = arr2.getJSONObject(i);
                String search_customer_name = obj1.getString("customer_name").toUpperCase();
                Preconditions.checkArgument(search_customer_name.toUpperCase().contains(search.toUpperCase()), "??????????????????=" + search + " ,????????????" + search_customer_name);
            }

            JSONArray arr3 = VoiceEvaluationPageScene.builder().page(1).size(50).customerPhone(search).build().visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < arr3.size(); i++) {
                JSONObject obj1 = arr3.getJSONObject(i);
                String search_customer_phone = obj1.getString("customer_phone").toUpperCase();
                Preconditions.checkArgument(search_customer_phone.contains(search), "??????????????????=" + search + " ,????????????" + search_customer_phone);
            }


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("?????????????????????????????????");
        }
    }

    @Test(dataProvider = "TIME", dataProviderClass = YunTongInfo.class)
    public void voiceEvaluationFilter3(String start, String end, String mess, String bool) {

        logger.logCaseStart(caseResult.getCaseName());
        try {
            Response obj = VoiceEvaluationPageScene.builder().page(1).size(50).receptionStart(start).receptionEnd(end).build().visitor(visitor).getResponse();
            int code = obj.getCode();
            if (bool.equals("true")) {
                JSONArray arr1 = VoiceEvaluationPageScene.builder().page(1).size(50).receptionStart(start).receptionEnd(end).build().visitor(visitor).execute().getJSONArray("list");
                for (int i = 0; i < arr1.size(); i++) {
                    JSONObject obj1 = arr1.getJSONObject(i);
                    String search_reception_time = obj1.getString("reception_time") + ":000";
                    Preconditions.checkArgument(Long.valueOf(dt.dateToTimestamp1(start + " 00:00:00:000")) <= Long.valueOf(dt.dateToTimestamp1(search_reception_time)) &&
                                    Long.valueOf(dt.dateToTimestamp1(search_reception_time)) <= Long.valueOf(dt.dateToTimestamp1(end + " 23:59:59:999")),
                            "??????????????????=" + start + ", ????????????=" + end + " , ????????????" + search_reception_time);
                }
            } else {
                Preconditions.checkArgument(code == 1001, mess + "???????????????1001?????????" + code);
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("???????????????????????????????????????");
        }
    }

    @Test
    public void voiceEvaluationFilter4() {

        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray arr = GeneralEnumValueListScene.builder().enumType("ENTER_STORE_STATUS_LIST").build().visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < arr.size(); i++) {
                int enter_status = arr.getJSONObject(i).getInteger("key");
                String enter_status_name = arr.getJSONObject(i).getString("value");
                JSONArray arr1 = VoiceEvaluationPageScene.builder().page(1).size(50).enterStatus(enter_status).build().visitor(visitor).execute().getJSONArray("list");
                for (int j = 0; j < arr1.size(); j++) {
                    String search = arr1.getJSONObject(j).getString("enter_status_name");
                    Preconditions.checkArgument(search.equals(enter_status_name), "??????????????????=" + enter_status_name + " ,????????????" + search);
                }
            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("??????????????????????????????????????????");
        }
    }

    @Test
    public void voiceEvaluationFilter5() {

        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray arr = GeneralEnumValueListScene.builder().enumType("VOICE_EVALUATE_STATUS_LIST").build().visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < arr.size(); i++) {
                int evaluate_status = arr.getJSONObject(i).getInteger("key");
                String evaluate_status_name = arr.getJSONObject(i).getString("value");
                JSONArray arr1 = VoiceEvaluationPageScene.builder().page(1).size(50).evaluateStatus(evaluate_status).build().visitor(visitor).execute().getJSONArray("list");
                for (int j = 0; j < arr1.size(); j++) {
                    String search = arr1.getJSONObject(j).getString("evaluate_status_name");
                    Preconditions.checkArgument(search.equals(evaluate_status_name), "??????????????????=" + evaluate_status_name + " ,????????????" + search);
                }
            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("??????????????????????????????????????????");
        }
    }

    @Test(dataProvider = "FILTER", dataProviderClass = YunTongInfo.class)
    public void voiceEvaluationFilter6(String search) {

        logger.logCaseStart(caseResult.getCaseName());
        try {

            JSONArray arr1 = VoiceEvaluationPageScene.builder().page(1).size(50).receptorName(search).customerPhone(search).build().visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < arr1.size(); i++) {
                JSONObject obj1 = arr1.getJSONObject(i);
                String search_receptor_name = obj1.getString("receptor_name").toUpperCase();
                String search_customer_phone = obj1.getString("customer_phone").toUpperCase();
                Preconditions.checkArgument(search_receptor_name.contains(search) && search_customer_phone.contains(search), "?????????????????? ??? ??????????????????=" + search + " ,???????????? ????????????=" + search_receptor_name + " ???????????????=" + search_customer_phone);
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("???????????????????????????????????????");
        }
    }

    /**
     * --------------------------------- ??????????????? ---------------------------------
     */


    @Test
    public void sensitiveShow() {

        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray arrlist = SensitiveBehaviorPageScene.builder().page(1).size(50).build().visitor(visitor).execute().getJSONArray("list");
            for (int j = 0; j < arrlist.size(); j++) {
                Preconditions.checkArgument(arrlist.getJSONObject(j).containsKey("receptor_name"), "??????" + arrlist.getJSONObject(j).getString("id") + "?????????????????????");
                Preconditions.checkArgument(arrlist.getJSONObject(j).containsKey("words"), "??????" + arrlist.getJSONObject(j).getString("id") + "??????????????????");
                Preconditions.checkArgument(arrlist.getJSONObject(j).containsKey("sensitive_words_type_name"), "??????" + arrlist.getJSONObject(j).getString("id") + "????????????????????????");
                Preconditions.checkArgument(arrlist.getJSONObject(j).containsKey("approval_status_name"), "??????" + arrlist.getJSONObject(j).getString("id") + "?????????????????????");
                Preconditions.checkArgument(arrlist.getJSONObject(j).containsKey("reception_start_time"), "??????" + arrlist.getJSONObject(j).getString("id") + "???????????????????????????");
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("????????????????????????????????????");
        }
    }


    @Test
    public void sensitiveFilter1() {

        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray arr = SensitiveBehaviorPageScene.builder().page(1).size(1).build().visitor(visitor).execute().getJSONArray("list");
            if (arr.size() > 0) {
                String receptor_name = arr.getJSONObject(0).getString("receptor_name");
                JSONArray arrlist = SensitiveBehaviorPageScene.builder().page(1).size(20).receptorName(receptor_name).build().visitor(visitor).execute().getJSONArray("list");
                Preconditions.checkArgument(arrlist.size() >= 0, "???????????????????????????????????????");
                for (int i = 0; i < arrlist.size(); i++) {
                    String search = arrlist.getJSONObject(i).getString("receptor_name");
                    Preconditions.checkArgument(search.contains(receptor_name), "??????" + receptor_name + " ,????????????" + search);
                }
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("??????????????????????????????");
        }
    }

    @Test(dataProvider = "FILTER", dataProviderClass = YunTongInfo.class)
    public void sensitiveFilter2(String receptor_name) {

        logger.logCaseStart(caseResult.getCaseName());
        try {

            JSONArray arrlist = SensitiveBehaviorPageScene.builder().page(1).size(20).receptorName(receptor_name).build().visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < arrlist.size(); i++) {
                String search = arrlist.getJSONObject(i).getString("receptor_name").toUpperCase();
                Preconditions.checkArgument(search.toUpperCase().contains(receptor_name.toUpperCase()), "??????" + receptor_name + " ,????????????" + search);
            }


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("??????????????????????????????????????????");
        }
    }

    @Test
    public void sensitiveFilter3() {

        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray arr = GeneralEnumValueListScene.builder().enumType("SENSITIVE_WORDS_TYPES").build().visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < arr.size(); i++) {
                int evaluate_status = arr.getJSONObject(i).getInteger("key");
                String evaluate_status_name = arr.getJSONObject(i).getString("value");
                JSONArray arrlist = SensitiveBehaviorPageScene.builder().page(1).size(50).sensitiveWordsType(evaluate_status).build().visitor(visitor).execute().getJSONArray("list");
                for (int j = 0; j < arrlist.size(); j++) {
                    String search = arrlist.getJSONObject(j).getString("sensitive_words_type_name");
                    Preconditions.checkArgument(search.equals(evaluate_status_name), "??????" + evaluate_status_name + " ,????????????" + search);
                }

            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("?????????????????????????????????????????????");
        }
    }

    @Test
    public void sensitiveFilter4() {

        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray arr = GeneralEnumValueListScene.builder().enumType("APPROVAL_STATUSES").build().visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < arr.size(); i++) {
                int evaluate_status = arr.getJSONObject(i).getInteger("key");
                String evaluate_status_name = arr.getJSONObject(i).getString("value");

                JSONArray arrlist = SensitiveBehaviorPageScene.builder().page(1).size(50).approvalStatus(evaluate_status).build().visitor(visitor).execute().getJSONArray("list");
                for (int j = 0; j < arrlist.size(); j++) {
                    String search = arrlist.getJSONObject(j).getString("approval_status_name");
                    Preconditions.checkArgument(search.equals(evaluate_status_name), "??????" + evaluate_status_name + " ,????????????" + search);
                }

            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("??????????????????????????????????????????");
        }
    }

    @Test(dataProvider = "TIME", dataProviderClass = YunTongInfo.class)
    public void sensitiveFilter5(String start, String end, String mess, String bool) {

        logger.logCaseStart(caseResult.getCaseName());
        try {

            Response obj = SensitiveBehaviorPageScene.builder().page(1).size(50).receptionStart(start).receptionEnd(end).build().visitor(visitor).getResponse();
            int code = obj.getCode();
            if (bool.equals("true")) {
                JSONArray arr1 = SensitiveBehaviorPageScene.builder().page(1).size(50).receptionStart(start).receptionEnd(end).build().visitor(visitor).execute().getJSONArray("list");
                for (int i = 0; i < arr1.size(); i++) {
                    JSONObject obj1 = arr1.getJSONObject(i);
                    String search_reception_time = obj1.getString("reception_start_time") + " 00:00:00:000";
                    Preconditions.checkArgument(Long.valueOf(dt.dateToTimestamp1(start + " 00:00:00:000")) <= Long.valueOf(dt.dateToTimestamp1(search_reception_time)) &&
                                    Long.valueOf(dt.dateToTimestamp1(search_reception_time)) <= Long.valueOf(dt.dateToTimestamp1(end + " 23:59:59:999")),
                            "??????????????????=" + start + ", ????????????=" + end + " , ????????????" + search_reception_time);
                }
            } else {
                Preconditions.checkArgument(code == 1001, mess + "???????????????1001?????????" + code);
            }


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("??????????????????????????????");
        }
    }

    @Test
    public void sensitiveBehaviorApproval1() {

        logger.logCaseStart(caseResult.getCaseName());
        try {

            Long id;
            //?????????????????????????????? 100 ???????????? ??????
            JSONArray arrlist = SensitiveBehaviorPageScene.builder().page(1).size(50).approvalStatus(100).build().visitor(visitor).execute().getJSONArray("list");
            if (arrlist.size() > 0) {
                id = arrlist.getJSONObject(0).getLong("id");
                //??????????????????????????? ???????????????
                int bef = SensitiveBehaviorPageScene.builder().page(1).size(50).approvalStatus(200).build().visitor(visitor).execute().getInteger("total");

                //???????????? 10 ???????????? ??????
                Response obj = SensitiveBehaviorApprovalScene.builder().id(id).approvalStatus(200).build().visitor(visitor).getResponse();
                Preconditions.checkArgument(obj.getCode() == 1000, "????????????,??????" + obj.getMessage());

                //???????????????????????????
                int after = SensitiveBehaviorPageScene.builder().page(1).size(50).approvalStatus(200).build().visitor(visitor).execute().getInteger("total");
                Preconditions.checkArgument(after - bef == 1, "???????????????????????????????????????+1");

            } else {
                Preconditions.checkArgument(false == true, "?????????????????????case??????");
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("???????????????????????????");
        }
    }

    @Test
    public void sensitiveBehaviorApproval2() {

        logger.logCaseStart(caseResult.getCaseName());
        try {

            Long id;
            //?????????????????????????????? 100 ???????????? ??????
            JSONArray arrlist = SensitiveBehaviorPageScene.builder().page(1).size(50).approvalStatus(100).build().visitor(visitor).execute().getJSONArray("list");
            if (arrlist.size() > 0) {
                id = arrlist.getJSONObject(0).getLong("id");
                //??????????????????????????? ???????????????
                int bef = SensitiveBehaviorPageScene.builder().page(1).size(50).approvalStatus(300).build().visitor(visitor).execute().getInteger("total");

                //???????????? 10 ???????????? ??????
                Response obj = SensitiveBehaviorApprovalScene.builder().id(id).approvalStatus(300).build().visitor(visitor).getResponse();
                Preconditions.checkArgument(obj.getCode() == 1000, "????????????,??????" + obj.getMessage());

                //???????????????????????????
                int after = SensitiveBehaviorPageScene.builder().page(1).size(50).approvalStatus(300).build().visitor(visitor).execute().getInteger("total");
                Preconditions.checkArgument(after - bef == 1, "?????????????????????????????????????????????+1");

            } else {
                Preconditions.checkArgument(false == true, "?????????????????????case??????");
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("??????????????????????????????");
        }
    }

    /**
     * --------------------------------- ?????????????????? ---------------------------------
     */

    @Test
    public void specialAudioShow() {

        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray arrlist = SpecialAudioPageScene.builder().page(1).size(3).build().visitor(visitor).execute().getJSONArray("list");
            for (int j = 0; j < arrlist.size(); j++) {
                Preconditions.checkArgument(arrlist.getJSONObject(j).containsKey("receptor_name"), "??????" + arrlist.getJSONObject(j).getString("id") + "?????????????????????");
                Preconditions.checkArgument(arrlist.getJSONObject(j).containsKey("audio_duration"), "??????" + arrlist.getJSONObject(j).getString("id") + "?????????????????????");
                Preconditions.checkArgument(arrlist.getJSONObject(j).containsKey("score"), "??????" + arrlist.getJSONObject(j).getString("id") + "???????????????");
                Preconditions.checkArgument(arrlist.getJSONObject(j).containsKey("is_region_compliance"), "??????" + arrlist.getJSONObject(j).getString("id") + "???????????????????????????");
                Preconditions.checkArgument(arrlist.getJSONObject(j).containsKey("reception_time"), "??????" + arrlist.getJSONObject(j).getString("id") + "???????????????????????????");
                Preconditions.checkArgument(arrlist.getJSONObject(j).containsKey("approval_status"), "??????" + arrlist.getJSONObject(j).getString("id") + "?????????????????????");
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("???????????????????????????????????????");
        }
    }

    @Test
    public void specialAudioFilter1() {

        logger.logCaseStart(caseResult.getCaseName());
        try {
            String receptor_name = "";

            JSONArray arr = SpecialAudioPageScene.builder().page(1).size(50).build().visitor(visitor).execute().getJSONArray("list");
            if (arr.size() > 0) {
                JSONObject obj = arr.getJSONObject(0);
                receptor_name = obj.getString("receptor_name");

                JSONArray arr1 = SpecialAudioPageScene.builder().page(1).size(50).receptorName(receptor_name).build().visitor(visitor).execute().getJSONArray("list");
                for (int i = 0; i < arr1.size(); i++) {
                    JSONObject obj1 = arr1.getJSONObject(i);
                    String search_receptor_name = obj1.getString("receptor_name");
                    Preconditions.checkArgument(search_receptor_name.contains(receptor_name), "??????????????????=" + receptor_name + " ,????????????" + search_receptor_name);
                }
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("?????????????????????????????????");
        }
    }

    @Test(dataProvider = "FILTER", dataProviderClass = YunTongInfo.class)
    public void specialAudioFilter2(String search) {

        logger.logCaseStart(caseResult.getCaseName());
        try {

            JSONArray arr1 = SpecialAudioPageScene.builder().page(1).size(50).receptorName(search).build().visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < arr1.size(); i++) {
                JSONObject obj1 = arr1.getJSONObject(i);
                String search_receptor_name = obj1.getString("receptor_name").toUpperCase();
                Preconditions.checkArgument(search_receptor_name.contains(search), "??????????????????=" + search + " ,????????????" + search_receptor_name);
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("?????????????????????????????????");
        }
    }

    @Test(dataProvider = "TIME", dataProviderClass = YunTongInfo.class)
    public void specialAudioFilter3(String start, String end, String mess, String bool) {

        logger.logCaseStart(caseResult.getCaseName());
        try {

            Response obj = SpecialAudioPageScene.builder().page(1).size(50).receptionStart(start).receptionEnd(end).build().visitor(visitor).getResponse();
            int code = obj.getCode();
            if (bool.equals("true")) {
                JSONArray arr1 = SpecialAudioPageScene.builder().page(1).size(50).receptionStart(start).receptionEnd(end).build().visitor(visitor).execute().getJSONArray("list");
                for (int i = 0; i < arr1.size(); i++) {
                    JSONObject obj1 = arr1.getJSONObject(i);
                    String search_reception_time = obj1.getString("reception_time") + " 00:00:00:000";
                    Preconditions.checkArgument(Long.valueOf(dt.dateToTimestamp1(start + " 00:00:00:000")) <= Long.valueOf(dt.dateToTimestamp1(search_reception_time)) &&
                                    Long.valueOf(dt.dateToTimestamp1(search_reception_time)) <= Long.valueOf(dt.dateToTimestamp1(end + " 23:59:59:999")),
                            "??????????????????=" + start + ", ????????????=" + end + " , ????????????" + search_reception_time);
                }
            } else {
                Preconditions.checkArgument(code == 1001, mess + "???????????????1001?????????" + code);
            }


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("?????????????????????????????????");
        }
    }

    @Test
    public void specialAudioFilter4() {

        logger.logCaseStart(caseResult.getCaseName());
        try {

            String start = dt.getHistoryDate(-369);
            String end = dt.getHistoryDate(-1);

            Response obj = SpecialAudioPageScene.builder().page(1).size(50).receptionStart(start).receptionEnd(end).build().visitor(visitor).getResponse();
            int code = obj.getCode();
            Preconditions.checkArgument(code == 1001, "??????????????????1?????????????????????????????????" + obj.getMessage());


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("?????????????????????????????????");
        }
    }

    @Test
    public void specialAudioFilter5() {

        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray arr = GeneralEnumValueListScene.builder().enumType("APPROVAL_STATUSES").build().visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < arr.size(); i++) {
                int evaluate_status = arr.getJSONObject(i).getInteger("key");
                String evaluate_status_name = arr.getJSONObject(i).getString("value");
                JSONArray arr1 = SpecialAudioPageScene.builder().page(1).size(50).approvalStatus(evaluate_status).build().visitor(visitor).execute().getJSONArray("list");
                for (int j = 0; j < arr1.size(); j++) {
                    String search = arr1.getJSONObject(j).getString("approval_status_name");
                    Preconditions.checkArgument(search.equals(evaluate_status_name), "??????????????????=" + evaluate_status_name + " ,????????????" + search);
                }
            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("????????????????????????????????????");
        }
    }

    @Test
    public void specialAudioApproval1() {

        logger.logCaseStart(caseResult.getCaseName());
        try {

            Long id;
            //??????????????????????????????
            JSONArray arrlist = SpecialAudioPageScene.builder().page(1).size(50).approvalStatus(100).build().visitor(visitor).execute().getJSONArray("list");
            if (arrlist.size() > 0) {
                id = arrlist.getJSONObject(0).getLong("id");
                //???????????????????????????
                int bef = SpecialAudioPageScene.builder().page(1).size(50).approvalStatus(200).build().visitor(visitor).execute().getInteger("total");

                //????????????
                Response obj = SpecialAudioApprovalScene.builder().id(id).approvalStatus(200).build().visitor(visitor).getResponse();
                Preconditions.checkArgument(obj.getCode() == 1000, "????????????,??????" + obj.getMessage());

                //???????????????????????????
                int after = SpecialAudioPageScene.builder().page(1).size(50).approvalStatus(200).build().visitor(visitor).execute().getInteger("total");
                Preconditions.checkArgument(after - bef == 1, "???????????????????????????????????????+1");

            } else {
                Preconditions.checkArgument(false == true, "?????????????????????case??????");
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("????????????????????????");
        }
    }

    @Test
    public void specialAudioApproval2() {

        logger.logCaseStart(caseResult.getCaseName());
        try {

            Long id;

            JSONArray arrlist = SpecialAudioPageScene.builder().page(1).size(50).approvalStatus(100).build().visitor(visitor).execute().getJSONArray("list");
            if (arrlist.size() > 0) {
                id = arrlist.getJSONObject(0).getLong("id");
                //???????????????????????????
                int bef = SpecialAudioPageScene.builder().page(1).size(50).approvalStatus(300).build().visitor(visitor).execute().getInteger("total");

                //????????????
                Response obj = SpecialAudioApprovalScene.builder().id(id).approvalStatus(300).build().visitor(visitor).getResponse();
                Preconditions.checkArgument(obj.getCode() == 1000, "????????????,??????" + obj.getMessage());

                //???????????????????????????
                int after = SpecialAudioPageScene.builder().page(1).size(50).approvalStatus(300).build().visitor(visitor).execute().getInteger("total");
                Preconditions.checkArgument(after - bef == 1, "?????????????????????????????????????????????+1");

            } else {
                Preconditions.checkArgument(false == true, "?????????????????????case??????");
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("???????????????????????????");
        }
    }


    @Test
    public void speechFilter1() {

        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray arr = GeneralEnumValueListScene.builder().enumType("RECEPTION_LINKS").build().visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < arr.size(); i++) {
                int evaluate_status = arr.getJSONObject(i).getInteger("key");
                String evaluate_status_name = arr.getJSONObject(i).getString("value");
                JSONArray arr1 = SpeechTechniquePageScene.builder().page(1).size(50).type(evaluate_status).build().visitor(visitor).execute().getJSONArray("list");

                for (int j = 0; j < arr1.size(); j++) {
                    String search = arr1.getJSONObject(j).getString("link_name");
                    Preconditions.checkArgument(search.equals(evaluate_status_name), "????????????=" + evaluate_status_name + " ,????????????" + search);
                }

                Preconditions.checkArgument(arr1.size() > 0, "??????" + evaluate_status_name + "????????????");
            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("????????????????????????????????????????????????");
        }
    }


}
