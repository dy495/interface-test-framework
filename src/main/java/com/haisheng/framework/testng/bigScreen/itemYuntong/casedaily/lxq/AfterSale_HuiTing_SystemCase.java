package com.haisheng.framework.testng.bigScreen.itemYuntong.casedaily.lxq;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.Response;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.*;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.aftermanage.EvaluationAddFavoriteScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.aftermanage.EvaluationPageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.afterspecialaudio.AfterSpecialAudioApprovalScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.afterspecialaudio.AfterSpecialAudioPageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.general.GeneralEnumValueListScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.scene.pc.speechtechnique.SpeechTechniquePageScene;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.util.SceneUtil;
import com.haisheng.framework.testng.bigScreen.itemYuntong.common.util.YunTongInfo;
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
 * 内容运营
 *
 * @author lxq
 * @date 2021/1/29 11:17
 */
public class AfterSale_HuiTing_SystemCase extends TestCaseCommon implements TestCaseStd {
    EnumTestProduct product = EnumTestProduct.YT_DAILY_ZH;
    EnumAccount ALL_AUTHORITY = EnumAccount.YT_DAILY_YS;
    VisitorProxy visitor = new VisitorProxy(product);
    SceneUtil businessUtil = new SceneUtil(visitor);

    YunTongInfo info = new YunTongInfo();

    CommonConfig commonConfig = new CommonConfig();


    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");

        //替换checklist的相关信息
        commonConfig.checklistAppId = EnumChecklistAppId.DB_APP_ID_SCREEN_SERVICE.getId();
        commonConfig.checklistConfId = EnumChecklistConfId.DB_SERVICE_ID_CRM_DAILY_SERVICE.getId();
        commonConfig.checklistQaOwner = "吕雪晴";
        //替换jenkins-job的相关信息
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.YUNTONG_DAILY_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, product.getDesc() + commonConfig.checklistQaOwner);
        //替换钉钉推送
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        //放入shopId
        commonConfig.setShopId(product.getShopId()).setRoleId(ALL_AUTHORITY.getRoleId()).setProduct(product.getAbbreviation());
        beforeClassInit(commonConfig);
        businessUtil.loginPc(ALL_AUTHORITY);

        visitor.setProduct(EnumTestProduct.YT_DAILY_GK);  //会听模块
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
     * --------------------------------- 语音评鉴列表 ---------------------------------
     */
    @Test
    public void voiceEvaluationShow() {

        logger.logCaseStart(caseResult.getCaseName());
        try {

            JSONArray arr = EvaluationPageScene.builder().page(1).size(50).build().visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < arr.size(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                Preconditions.checkArgument(obj.containsKey("id"), "没有");
                Preconditions.checkArgument(obj.containsKey("receptor_name"), "记录" + obj.getString("id") + "没有接待顾问姓名");
                Preconditions.checkArgument(obj.containsKey("reception_time"), "记录" + obj.getString("id") + "没有接待时间");
                Preconditions.checkArgument(obj.containsKey("reception_duration"), "记录" + obj.getString("id") + "没有接待时长");
                Preconditions.checkArgument(obj.containsKey("customer_name"), "记录" + obj.getString("id") + "没有客户姓名");
                Preconditions.checkArgument(obj.containsKey("customer_phone"), "记录" + obj.getString("id") + "没有客户联系方式");

                Preconditions.checkArgument(obj.containsKey("repair_type"), "记录" + obj.getString("id") + "没有服务内容");

                if (obj.getString("repair_type_name").equals("理赔")) {
                    Preconditions.checkArgument(obj.containsKey("evaluate_status_name") && obj.getString("evaluate_status_name").equals("无需评分"),
                            "记录" + obj.getString("id") + "评分状态不正确");
                }
                else {
                    Preconditions.checkArgument(obj.containsKey("evaluate_status_name") &&
                                    (obj.getString("evaluate_status_name").equals("评分未完成") || obj.getString("evaluate_status_name").equals("评分完成") ),
                            "记录" + obj.getString("id") + "评分状态不正确");
                }

            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("语音评鉴列表展示项校验");
        }
    }

    @Test
    public void voiceEvaluationFilter1() {

        logger.logCaseStart(caseResult.getCaseName());
        try {
            String receptor_name = "";
            String customer_name = "";
            String customer_phone = "";
            JSONArray arr = EvaluationPageScene.builder().page(1).size(50).build().visitor(visitor).execute().getJSONArray("list");
            if (arr.size() > 0) {
                JSONObject obj = arr.getJSONObject(0);
                receptor_name = obj.getString("receptor_name");
                customer_name = obj.getString("customer_name");
                customer_phone = obj.getString("customer_phone");
                JSONArray arr1 = EvaluationPageScene.builder().page(1).size(50).receptorName(receptor_name).build().visitor(visitor).execute().getJSONArray("list");
                for (int i = 0; i < arr1.size(); i++) {
                    JSONObject obj1 = arr1.getJSONObject(i);
                    String search_receptor_name = obj1.getString("receptor_name");
                    Preconditions.checkArgument(search_receptor_name.toUpperCase().contains(receptor_name.toUpperCase()), "搜索接待顾问=" + receptor_name + " ,结果包含" + search_receptor_name);
                }

                JSONArray arr2 = EvaluationPageScene.builder().page(1).size(50).customerName(customer_name).build().visitor(visitor).execute().getJSONArray("list");
                for (int i = 0; i < arr2.size(); i++) {
                    JSONObject obj1 = arr2.getJSONObject(i);
                    String search_customer_name = obj1.getString("customer_name");
                    Preconditions.checkArgument(search_customer_name.toUpperCase().contains(customer_name.toUpperCase()), "搜索客户姓名=" + customer_name + " ,结果包含" + search_customer_name);
                }

                JSONArray arr3 = EvaluationPageScene.builder().page(1).size(50).customerPhone(customer_phone).build().visitor(visitor).execute().getJSONArray("list");
                for (int i = 0; i < arr3.size(); i++) {
                    JSONObject obj1 = arr3.getJSONObject(i);
                    String search_customer_phone = obj1.getString("customer_phone");
                    Preconditions.checkArgument(search_customer_phone.contains(customer_phone), "搜索联系方式=" + customer_phone + " ,结果包含" + search_customer_phone);
                }
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("语音评鉴列表文本框筛选");
        }
    }

    @Test(dataProvider = "FILTER", dataProviderClass = YunTongInfo.class)
    public void voiceEvaluationFilter2(String search) {

        logger.logCaseStart(caseResult.getCaseName());
        try {

            JSONArray arr1 = EvaluationPageScene.builder().page(1).size(50).receptorName(search).build().visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < arr1.size(); i++) {
                JSONObject obj1 = arr1.getJSONObject(i);
                String search_receptor_name = obj1.getString("receptor_name").toUpperCase();
                Preconditions.checkArgument(search_receptor_name.toUpperCase().contains(search.toUpperCase()), "搜索接待顾问=" + search + " ,结果包含" + search_receptor_name);
            }
            JSONArray arr2 = EvaluationPageScene.builder().page(1).size(50).customerName(search).build().visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < arr2.size(); i++) {
                JSONObject obj1 = arr2.getJSONObject(i);
                String search_customer_name = obj1.getString("customer_name").toUpperCase();
                Preconditions.checkArgument(search_customer_name.toUpperCase().contains(search.toUpperCase()), "搜索客户姓名=" + search + " ,结果包含" + search_customer_name);
            }

            JSONArray arr3 = EvaluationPageScene.builder().page(1).size(50).customerPhone(search).build().visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < arr3.size(); i++) {
                JSONObject obj1 = arr3.getJSONObject(i);
                String search_customer_phone = obj1.getString("customer_phone").toUpperCase();
                Preconditions.checkArgument(search_customer_phone.contains(search), "搜索联系方式=" + search + " ,结果包含" + search_customer_phone);
            }


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("语音评鉴列表文本框筛选");
        }
    }

    @Test(dataProvider = "TIME", dataProviderClass = YunTongInfo.class)
    public void voiceEvaluationFilter3(String start, String end, String mess, String bool) {

        logger.logCaseStart(caseResult.getCaseName());
        try {
            Response obj = EvaluationPageScene.builder().page(1).size(50).receptionStart(start).receptionEnd(end).build().visitor(visitor).getResponse();
            int code = obj.getCode();
            if (bool.equals("true")) {
                JSONArray arr1 = EvaluationPageScene.builder().page(1).size(50).receptionStart(start).receptionEnd(end).build().visitor(visitor).execute().getJSONArray("list");
                for (int i = 0; i < arr1.size(); i++) {
                    JSONObject obj1 = arr1.getJSONObject(i);
                    String search_reception_time = obj1.getString("reception_time") + ":000";
                    Preconditions.checkArgument(Long.valueOf(dt.dateToTimestamp1(start + " 00:00:00:000")) <= Long.valueOf(dt.dateToTimestamp1(search_reception_time)) &&
                                    Long.valueOf(dt.dateToTimestamp1(search_reception_time)) <= Long.valueOf(dt.dateToTimestamp1(end + " 23:59:59:999")),
                            "搜索开始时间=" + start + ", 结束时间=" + end + " , 结果包含" + search_reception_time);
                }
            } else {
                Preconditions.checkArgument(code == 1001, mess + "状态码期待1001，实际" + code);
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("语音评鉴列表时间筛选框筛选");
        }
    }

    @Test
    public void voiceEvaluationFilter4() {

        logger.logCaseStart(caseResult.getCaseName());
        try {


            JSONArray arr1 = EvaluationPageScene.builder().page(1).size(50).repairType("MAINTENANCE").build().visitor(visitor).execute().getJSONArray("list");
            for (int j = 0; j < arr1.size(); j++) {
                String search = arr1.getJSONObject(j).getString("repair_type_name");
                Preconditions.checkArgument(search.equals("机修"), "搜索服务内容=机修 ,结果包含" + search);
            }

            JSONArray arr2 = EvaluationPageScene.builder().page(1).size(50).repairType("CLAIMS").build().visitor(visitor).execute().getJSONArray("list");
            for (int j = 0; j < arr2.size(); j++) {
                String search = arr2.getJSONObject(j).getString("repair_type_name");
                Preconditions.checkArgument(search.equals("理赔"), "搜索服务内容=理赔 ,结果包含" + search);
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("语音评鉴列表根据服务内容筛选");
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
                JSONArray arr1 = EvaluationPageScene.builder().page(1).size(50).evaluateStatus(evaluate_status).build().visitor(visitor).execute().getJSONArray("list");
                for (int j = 0; j < arr1.size(); j++) {
                    String search = arr1.getJSONObject(j).getString("evaluate_status_name");
                    Preconditions.checkArgument(search.equals(evaluate_status_name), "搜索评分状态=" + evaluate_status_name + " ,结果包含" + search);
                }
            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("语音评鉴列表根据评分状态筛选");
        }
    }

    @Test(dataProvider = "FILTER", dataProviderClass = YunTongInfo.class)
    public void voiceEvaluationFilter6(String search) {

        logger.logCaseStart(caseResult.getCaseName());
        try {

            JSONArray arr1 = EvaluationPageScene.builder().page(1).size(50).receptorName(search).customerPhone(search).build().visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < arr1.size(); i++) {
                JSONObject obj1 = arr1.getJSONObject(i);
                String search_receptor_name = obj1.getString("receptor_name").toUpperCase();
                String search_customer_phone = obj1.getString("customer_phone").toUpperCase();
                Preconditions.checkArgument(search_receptor_name.contains(search) && search_customer_phone.contains(search), "搜索接待顾问 和 搜索联系方式=" + search + " ,结果包含 接待顾问=" + search_receptor_name + " ，联系方式=" + search_customer_phone);
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("语音评鉴列表文本框组合筛选");
        }
    }


    @Test(dataProvider = "BOOLEAN", dataProviderClass = YunTongInfo.class)
    public void voiceEvaluationFilter7(String isfavorite) {

        logger.logCaseStart(caseResult.getCaseName());
        try {

            JSONArray arr1 = EvaluationPageScene.builder().page(1).size(30).isFavorite(Boolean.getBoolean(isfavorite)).build().visitor(visitor).execute().getJSONArray("list");
            for (int j = 0; j < arr1.size(); j++) {
                Boolean search = arr1.getJSONObject(j).getBoolean("is_favorite");
                Preconditions.checkArgument(search == Boolean.getBoolean(isfavorite), "搜索收藏状态为"+Boolean.getBoolean(isfavorite)+"结果不正确");
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("语音评鉴列表根据是否收藏筛选");
        }
    }


    @Test
    public void voiceEvaluationFavorite() {

        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long id= EvaluationPageScene.builder().page(1).size(30).isFavorite(false).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0).getLong("id");
            //收藏
            int code = EvaluationAddFavoriteScene.builder().id(id).build().visitor(visitor).getResponse().getCode();
            Preconditions.checkArgument(code==1000,"收藏语音，状态码为"+code);

            //todo 取消收藏

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("语音评鉴列表收藏/取消收藏语音");
        }
    }


    /**
     * ---------------------------------  售后没有 敏感词风控  先注销掉  ---------------------------------
     */


//    @Test
//    public void sensitiveShow() {
//
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            JSONArray arrlist = AfterSensitiveBehaviorPageScene.builder().page(1).size(50).build().visitor(visitor).execute().getJSONArray("list");
//            for (int j = 0; j < arrlist.size(); j++) {
//                Preconditions.checkArgument(arrlist.getJSONObject(j).containsKey("receptor_name"), "记录" + arrlist.getJSONObject(j).getString("id") + "没展示接待顾问");
//                Preconditions.checkArgument(arrlist.getJSONObject(j).containsKey("words"), "记录" + arrlist.getJSONObject(j).getString("id") + "没展示敏感词");
//                Preconditions.checkArgument(arrlist.getJSONObject(j).containsKey("sensitive_words_type_name"), "记录" + arrlist.getJSONObject(j).getString("id") + "没展示敏感词类型");
//                Preconditions.checkArgument(arrlist.getJSONObject(j).containsKey("approval_status_name"), "记录" + arrlist.getJSONObject(j).getString("id") + "没展示审核状态");
//                Preconditions.checkArgument(arrlist.getJSONObject(j).containsKey("reception_start_time"), "记录" + arrlist.getJSONObject(j).getString("id") + "没展示开始接待时间");
//            }
//
//        } catch (AssertionError e) {
//            appendFailReason(e.toString());
//        } catch (Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            saveData("敏感词风控列表展示项校验");
//        }
//    }
//
//
//    @Test
//    public void sensitiveFilter1() {
//
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            JSONArray arr = AfterSensitiveBehaviorPageScene.builder().page(1).size(1).build().visitor(visitor).execute().getJSONArray("list");
//            if (arr.size() > 0) {
//                String receptor_name = arr.getJSONObject(0).getString("receptor_name");
//                JSONArray arrlist = AfterSensitiveBehaviorPageScene.builder().page(1).size(20).receptorName(receptor_name).build().visitor(visitor).execute().getJSONArray("list");
//                Preconditions.checkArgument(arrlist.size() >= 0, "搜索列表存在的顾问，无结果");
//                for (int i = 0; i < arrlist.size(); i++) {
//                    String search = arrlist.getJSONObject(i).getString("receptor_name");
//                    Preconditions.checkArgument(search.contains(receptor_name), "搜索" + receptor_name + " ,结果包含" + search);
//                }
//            }
//
//        } catch (AssertionError e) {
//            appendFailReason(e.toString());
//        } catch (Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            saveData("敏感词风控文本框筛选");
//        }
//    }
//
//    @Test(dataProvider = "FILTER", dataProviderClass = YunTongInfo.class)
//    public void sensitiveFilter2(String receptor_name) {
//
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//
//            JSONArray arrlist = AfterSensitiveBehaviorPageScene.builder().page(1).size(20).receptorName(receptor_name).build().visitor(visitor).execute().getJSONArray("list");
//            for (int i = 0; i < arrlist.size(); i++) {
//                String search = arrlist.getJSONObject(i).getString("receptor_name").toUpperCase();
//                Preconditions.checkArgument(search.toUpperCase().contains(receptor_name.toUpperCase()), "搜索" + receptor_name + " ,结果包含" + search);
//            }
//
//
//        } catch (AssertionError e) {
//            appendFailReason(e.toString());
//        } catch (Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            saveData("敏感词风控接待顾问文本框筛选");
//        }
//    }
//
//    @Test
//    public void sensitiveFilter3() {
//
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            JSONArray arr = GeneralEnumValueListScene.builder().enumType("SENSITIVE_WORDS_TYPES").build().visitor(visitor).execute().getJSONArray("list");
//            for (int i = 0; i < arr.size(); i++) {
//                int evaluate_status = arr.getJSONObject(i).getInteger("key");
//                String evaluate_status_name = arr.getJSONObject(i).getString("value");
//                JSONArray arrlist = AfterSensitiveBehaviorPageScene.builder().page(1).size(50).sensitiveWordsType(evaluate_status).build().visitor(visitor).execute().getJSONArray("list");
//                for (int j = 0; j < arrlist.size(); j++) {
//                    String search = arrlist.getJSONObject(j).getString("sensitive_words_type_name");
//                    Preconditions.checkArgument(search.equals(evaluate_status_name), "搜索" + evaluate_status_name + " ,结果包含" + search);
//                }
//
//            }
//        } catch (AssertionError e) {
//            appendFailReason(e.toString());
//        } catch (Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            saveData("敏感词风控敏感词类别下拉框筛选");
//        }
//    }
//
//    @Test
//    public void sensitiveFilter4() {
//
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//            JSONArray arr = GeneralEnumValueListScene.builder().enumType("APPROVAL_STATUSES").build().visitor(visitor).execute().getJSONArray("list");
//            for (int i = 0; i < arr.size(); i++) {
//                int evaluate_status = arr.getJSONObject(i).getInteger("key");
//                String evaluate_status_name = arr.getJSONObject(i).getString("value");
//
//                JSONArray arrlist = AfterSensitiveBehaviorPageScene.builder().page(1).size(50).approvalStatus(evaluate_status).build().visitor(visitor).execute().getJSONArray("list");
//                for (int j = 0; j < arrlist.size(); j++) {
//                    String search = arrlist.getJSONObject(j).getString("approval_status_name");
//                    Preconditions.checkArgument(search.equals(evaluate_status_name), "搜索" + evaluate_status_name + " ,结果包含" + search);
//                }
//
//            }
//        } catch (AssertionError e) {
//            appendFailReason(e.toString());
//        } catch (Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            saveData("敏感词风控审核状态下拉框筛选");
//        }
//    }
//
//    @Test(dataProvider = "TIME", dataProviderClass = YunTongInfo.class)
//    public void sensitiveFilter5(String start, String end, String mess, String bool) {
//
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//
//            Response obj = AfterSensitiveBehaviorPageScene.builder().page(1).size(50).receptionStart(start).receptionEnd(end).build().visitor(visitor).getResponse();
//            int code = obj.getCode();
//            if (bool.equals("true")) {
//                JSONArray arr1 = AfterSensitiveBehaviorPageScene.builder().page(1).size(50).receptionStart(start).receptionEnd(end).build().visitor(visitor).execute().getJSONArray("list");
//                for (int i = 0; i < arr1.size(); i++) {
//                    JSONObject obj1 = arr1.getJSONObject(i);
//                    String search_reception_time = obj1.getString("reception_start_time") + " 00:00:00:000";
//                    Preconditions.checkArgument(Long.valueOf(dt.dateToTimestamp1(start + " 00:00:00:000")) <= Long.valueOf(dt.dateToTimestamp1(search_reception_time)) &&
//                                    Long.valueOf(dt.dateToTimestamp1(search_reception_time)) <= Long.valueOf(dt.dateToTimestamp1(end + " 23:59:59:999")),
//                            "搜索开始时间=" + start + ", 结束时间=" + end + " , 结果包含" + search_reception_time);
//                }
//            } else {
//                Preconditions.checkArgument(code == 1001, mess + "状态码期待1001，实际" + code);
//            }
//
//
//        } catch (AssertionError e) {
//            appendFailReason(e.toString());
//        } catch (Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            saveData("敏感词风控时间框筛选");
//        }
//    }
//
//    @Test
//    public void sensitiveBehaviorApproval1() {
//
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//
//            Long id;
//            //这个要去待审核的状态 100 随便写的 要改
//            JSONArray arrlist = AfterSensitiveBehaviorPageScene.builder().page(1).size(50).approvalStatus(100).build().visitor(visitor).execute().getJSONArray("list");
//            if (arrlist.size() > 0) {
//                id = arrlist.getJSONObject(0).getLong("id");
//                //审核前审核通过数量 状态数要改
//                int bef = AfterSensitiveBehaviorPageScene.builder().page(1).size(50).approvalStatus(200).build().visitor(visitor).execute().getInteger("total");
//
//                //审核通过 10 随便写的 要改
//                Response obj = AfterSensitiveBehaviorApprovalScene.builder().id(id).approvalStatus(200).build().visitor(visitor).getResponse();
//                Preconditions.checkArgument(obj.getCode() == 1000, "审核失败,提示" + obj.getMessage());
//
//                //审核后审核通过数量
//                int after = AfterSensitiveBehaviorPageScene.builder().page(1).size(50).approvalStatus(200).build().visitor(visitor).execute().getInteger("total");
//                Preconditions.checkArgument(after - bef == 1, "审核通过后，审核通过记录未+1");
//
//            } else {
//                Preconditions.checkArgument(false == true, "无待审核记录，case跳过");
//            }
//
//        } catch (AssertionError e) {
//            appendFailReason(e.toString());
//        } catch (Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            saveData("敏感词行为审核通过");
//        }
//    }
//
//    @Test
//    public void sensitiveBehaviorApproval2() {
//
//        logger.logCaseStart(caseResult.getCaseName());
//        try {
//
//            Long id;
//
//            JSONArray arrlist = AfterSensitiveBehaviorPageScene.builder().page(1).size(50).approvalStatus(100).build().visitor(visitor).execute().getJSONArray("list");
//            if (arrlist.size() > 0) {
//                id = arrlist.getJSONObject(0).getLong("id");
//                //审核前审核通过数量
//                int bef = AfterSensitiveBehaviorPageScene.builder().page(1).size(50).approvalStatus(300).build().visitor(visitor).execute().getInteger("total");
//
//                //审核通过
//                Response obj = AfterSensitiveBehaviorApprovalScene.builder().id(id).approvalStatus(300).build().visitor(visitor).getResponse();
//                Preconditions.checkArgument(obj.getCode() == 1000, "审核失败,提示" + obj.getMessage());
//
//                //审核后审核通过数量
//                int after = AfterSensitiveBehaviorPageScene.builder().page(1).size(50).approvalStatus(300).build().visitor(visitor).execute().getInteger("total");
//                Preconditions.checkArgument(after - bef == 1, "审核不通过后，审核不通过记录未+1");
//
//            } else {
//                Preconditions.checkArgument(false == true, "无待审核记录，case跳过");
//            }
//
//        } catch (AssertionError e) {
//            appendFailReason(e.toString());
//        } catch (Exception e) {
//            appendFailReason(e.toString());
//        } finally {
//            saveData("敏感词行为审核不通过");
//        }
//    }

    /**
     * --------------------------------- 特殊音频审核 ---------------------------------
     */

    @Test
    public void specialAudioShow() {

        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray arrlist = AfterSpecialAudioPageScene.builder().page(1).size(3).build().visitor(visitor).execute().getJSONArray("list");
            for (int j = 0; j < arrlist.size(); j++) {
                Preconditions.checkArgument(arrlist.getJSONObject(j).containsKey("receptor_name"), "记录" + arrlist.getJSONObject(j).getString("id") + "没展示接待顾问");
                Preconditions.checkArgument(arrlist.getJSONObject(j).containsKey("audio_duration"), "记录" + arrlist.getJSONObject(j).getString("id") + "没展示音频时长");
                Preconditions.checkArgument(arrlist.getJSONObject(j).containsKey("score"), "记录" + arrlist.getJSONObject(j).getString("id") + "没展示得分");
                Preconditions.checkArgument(arrlist.getJSONObject(j).containsKey("is_region_compliance"), "记录" + arrlist.getJSONObject(j).getString("id") + "没展示区域是否合规");
                Preconditions.checkArgument(arrlist.getJSONObject(j).containsKey("reception_time"), "记录" + arrlist.getJSONObject(j).getString("id") + "没展示开始接待时间");
                Preconditions.checkArgument(arrlist.getJSONObject(j).containsKey("approval_status"), "记录" + arrlist.getJSONObject(j).getString("id") + "没展示审核状态");
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("特殊音频审核列表展示项校验");
        }
    }

    @Test
    public void specialAudioFilter1() {

        logger.logCaseStart(caseResult.getCaseName());
        try {
            String receptor_name = "";

            JSONArray arr = AfterSpecialAudioPageScene.builder().page(1).size(50).build().visitor(visitor).execute().getJSONArray("list");
            if (arr.size() > 0) {
                JSONObject obj = arr.getJSONObject(0);
                receptor_name = obj.getString("receptor_name");

                JSONArray arr1 = AfterSpecialAudioPageScene.builder().page(1).size(50).receptorName(receptor_name).build().visitor(visitor).execute().getJSONArray("list");
                for (int i = 0; i < arr1.size(); i++) {
                    JSONObject obj1 = arr1.getJSONObject(i);
                    String search_receptor_name = obj1.getString("receptor_name");
                    Preconditions.checkArgument(search_receptor_name.contains(receptor_name), "搜索接待顾问=" + receptor_name + " ,结果包含" + search_receptor_name);
                }
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("特殊音频审核文本框筛选");
        }
    }

    @Test(dataProvider = "FILTER", dataProviderClass = YunTongInfo.class)
    public void specialAudioFilter2(String search) {

        logger.logCaseStart(caseResult.getCaseName());
        try {

            JSONArray arr1 = AfterSpecialAudioPageScene.builder().page(1).size(50).receptorName(search).build().visitor(visitor).execute().getJSONArray("list");
            for (int i = 0; i < arr1.size(); i++) {
                JSONObject obj1 = arr1.getJSONObject(i);
                String search_receptor_name = obj1.getString("receptor_name").toUpperCase();
                Preconditions.checkArgument(search_receptor_name.contains(search), "搜索接待顾问=" + search + " ,结果包含" + search_receptor_name);
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("特殊音频审核文本框筛选");
        }
    }

    @Test(dataProvider = "TIME", dataProviderClass = YunTongInfo.class)
    public void specialAudioFilter3(String start, String end, String mess, String bool) {

        logger.logCaseStart(caseResult.getCaseName());
        try {

            Response obj = AfterSpecialAudioPageScene.builder().page(1).size(50).receptionStart(start).receptionEnd(end).build().visitor(visitor).getResponse();
            int code = obj.getCode();
            if (bool.equals("true")) {
                JSONArray arr1 = AfterSpecialAudioPageScene.builder().page(1).size(50).receptionStart(start).receptionEnd(end).build().visitor(visitor).execute().getJSONArray("list");
                for (int i = 0; i < arr1.size(); i++) {
                    JSONObject obj1 = arr1.getJSONObject(i);
                    String search_reception_time = obj1.getString("reception_time") + " 00:00:00:000";
                    Preconditions.checkArgument(Long.valueOf(dt.dateToTimestamp1(start + " 00:00:00:000")) <= Long.valueOf(dt.dateToTimestamp1(search_reception_time)) &&
                                    Long.valueOf(dt.dateToTimestamp1(search_reception_time)) <= Long.valueOf(dt.dateToTimestamp1(end + " 23:59:59:999")),
                            "搜索开始时间=" + start + ", 结束时间=" + end + " , 结果包含" + search_reception_time);
                }
            } else {
                Preconditions.checkArgument(code == 1001, mess + "状态码期待1001，实际" + code);
            }


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("特殊音频审核时间框筛选");
        }
    }

    @Test
    public void specialAudioFilter4() {

        logger.logCaseStart(caseResult.getCaseName());
        try {

            String start = dt.getHistoryDate(-369);
            String end = dt.getHistoryDate(-1);

            Response obj = AfterSpecialAudioPageScene.builder().page(1).size(50).receptionStart(start).receptionEnd(end).build().visitor(visitor).getResponse();
            int code = obj.getCode();
            Preconditions.checkArgument(code == 1001, "时间跨度大于1年，期待失败，实际提示" + obj.getMessage());


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("特殊音频审核时间框筛选");
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
                JSONArray arr1 = AfterSpecialAudioPageScene.builder().page(1).size(50).approvalStatus(evaluate_status).build().visitor(visitor).execute().getJSONArray("list");
                for (int j = 0; j < arr1.size(); j++) {
                    String search = arr1.getJSONObject(j).getString("approval_status_name");
                    Preconditions.checkArgument(search.equals(evaluate_status_name), "搜索审核状态=" + evaluate_status_name + " ,结果包含" + search);
                }
            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("特殊音频审核根据状态筛选");
        }
    }

    @Test
    public void specialAudioApproval1() {

        logger.logCaseStart(caseResult.getCaseName());
        try {

            Long id;
            //这个要去待审核的状态
            JSONArray arrlist = AfterSpecialAudioPageScene.builder().page(1).size(50).approvalStatus(100).build().visitor(visitor).execute().getJSONArray("list");
            if (arrlist.size() > 0) {
                id = arrlist.getJSONObject(0).getLong("id");
                //审核前审核通过数量
                int bef = AfterSpecialAudioPageScene.builder().page(1).size(50).approvalStatus(200).build().visitor(visitor).execute().getInteger("total");

                //审核通过
                Response obj = AfterSpecialAudioApprovalScene.builder().id(id).approvalStatus(200).build().visitor(visitor).getResponse();
                Preconditions.checkArgument(obj.getCode() == 1000, "审核失败,提示" + obj.getMessage());

                //审核后审核通过数量
                int after = AfterSpecialAudioPageScene.builder().page(1).size(50).approvalStatus(200).build().visitor(visitor).execute().getInteger("total");
                Preconditions.checkArgument(after - bef == 1, "审核通过后，审核通过记录未+1");

            } else {
                Preconditions.checkArgument(false == true, "无待审核记录，case跳过");
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("特殊音频审核通过");
        }
    }

    @Test
    public void specialAudioApproval2() {

        logger.logCaseStart(caseResult.getCaseName());
        try {

            Long id;

            JSONArray arrlist = AfterSpecialAudioPageScene.builder().page(1).size(50).approvalStatus(100).build().visitor(visitor).execute().getJSONArray("list");
            if (arrlist.size() > 0) {
                id = arrlist.getJSONObject(0).getLong("id");
                //审核前审核通过数量
                int bef = AfterSpecialAudioPageScene.builder().page(1).size(50).approvalStatus(300).build().visitor(visitor).execute().getInteger("total");

                //审核通过
                Response obj = AfterSpecialAudioApprovalScene.builder().id(id).approvalStatus(300).build().visitor(visitor).getResponse();
                Preconditions.checkArgument(obj.getCode() == 1000, "审核失败,提示" + obj.getMessage());

                //审核后审核通过数量
                int after = AfterSpecialAudioPageScene.builder().page(1).size(50).approvalStatus(300).build().visitor(visitor).execute().getInteger("total");
                Preconditions.checkArgument(after - bef == 1, "审核不通过后，审核不通过记录未+1");

            } else {
                Preconditions.checkArgument(false == true, "无待审核记录，case跳过");
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("特殊音频审核不通过");
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
                    if (!evaluate_status_name.equals("全部环节")){
                        Preconditions.checkArgument(search.equals(evaluate_status_name), "接待环节=" + evaluate_status_name + " ,结果包含" + search);
                    }
                    else {
                        Preconditions.checkArgument(arr1.size()>0,"搜索全部环节结果为空");
                    }
                        }

                Preconditions.checkArgument(arr1.size() > 0, "搜索" + evaluate_status_name + "结果为空");
            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("话术考核设置根据接待环节进行筛选");
        }
    }

    //todo 合格值设置

}
