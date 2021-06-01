package com.haisheng.framework.testng.bigScreen.yuntong.lxq;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.*;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.enumerator.EnumAccount;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.SupporterUtil;
import com.haisheng.framework.testng.bigScreen.jiaochen.wm.util.UserUtil;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.general.EnumValueListScene;
import com.haisheng.framework.testng.bigScreen.yuntong.wm.scene.pc.manage.VoiceEvaluationPageScene;
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
public class HuiTing_SystemCase extends TestCaseCommon implements TestCaseStd {
    private static final EnumTestProduce PRODUCE = EnumTestProduce.JC_DAILY;
    private static final EnumAccount ALL_AUTHORITY = EnumAccount.ALL_AUTHORITY_DAILY_LXQ;
    private static final EnumAccount ALL_AUTHORITY_DAILY = EnumAccount.ALL_AUTHORITY_DAILY;
    private static final EnumAppletToken APPLET_USER_ONE = EnumAppletToken.JC_LXQ_DAILY;
    public VisitorProxy visitor = new VisitorProxy(PRODUCE);
    public UserUtil user = new UserUtil(visitor);
    public SupporterUtil util = new SupporterUtil(visitor);
    YunTongInfo info = new  YunTongInfo();



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
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.JIAOCHEN_DAILY_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, PRODUCE.getDesc() + commonConfig.checklistQaOwner);
        //替换钉钉推送
        commonConfig.dingHook = DingWebhook.CAR_OPEN_MANAGEMENT_PLATFORM_GRP;
        //放入shopId
        commonConfig.product = PRODUCE.getAbbreviation();
        commonConfig.referer = PRODUCE.getReferer();
        commonConfig.shopId = PRODUCE.getShopId();
        commonConfig.roleId = ALL_AUTHORITY.getRoleId();
        beforeClassInit(commonConfig);
    }

    @AfterClass
    @Override
    public void clean() {
        afterClassClean();
    }

    @BeforeMethod
    @Override
    public void createFreshCase(Method method) {
        user.loginApplet(APPLET_USER_ONE);
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

            JSONArray arr = VoiceEvaluationPageScene.builder().page(1).size(50).build().invoke(visitor).getJSONArray("list");
            for (int i = 0 ; i < arr.size();i++){
                JSONObject obj = arr.getJSONObject(i);
                Preconditions.checkArgument(obj.containsKey("id"),"没有");
                Preconditions.checkArgument(obj.containsKey("receptor_name"),"记录"+obj.getString("id")+"没有接待顾问姓名");
                Preconditions.checkArgument(obj.containsKey("reception_time"),"记录"+obj.getString("id")+"没有接待时间");
                Preconditions.checkArgument(obj.containsKey("reception_duration"),"记录"+obj.getString("id")+"没有接待时长");
                Preconditions.checkArgument(obj.containsKey("customer_name"),"记录"+obj.getString("id")+"没有客户姓名");
                Preconditions.checkArgument(obj.containsKey("customer_phone"),"记录"+obj.getString("id")+"没有客户联系方式");
                Preconditions.checkArgument(obj.containsKey("remark"),"记录"+obj.getString("id")+"没有备注");

                Preconditions.checkArgument(obj.containsKey("enter_status_name"),"记录"+obj.getString("id")+"没有进店情况");
                if (obj.getString("enter_status_name").equals("首次进店") && info.toMinute(obj.getString("reception_duration"))>5L){
                    Preconditions.checkArgument(obj.containsKey("evaluate_score"),"记录"+obj.getString("id")+"没有接待评分");
                    Preconditions.checkArgument(obj.containsKey("evaluate_status_name") && obj.getString("evaluate_status_name").equals("评分完成"),"记录"+obj.getString("id")+"评分状态不正确");
                }
                else {
                    Preconditions.checkArgument(obj.containsKey("evaluate_status_name") && obj.getString("evaluate_status_name").equals("无需评分"),"记录"+obj.getString("id")+"评分状态不正确");
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
            JSONArray arr = VoiceEvaluationPageScene.builder().page(1).size(50).build().invoke(visitor).getJSONArray("list");
            if (arr.size()>0){
                JSONObject obj = arr.getJSONObject(0);
                receptor_name = obj.getString("receptor_name");
                customer_name = obj.getString("customer_name");
                customer_phone = obj.getString("customer_phone");
                JSONArray arr1 = VoiceEvaluationPageScene.builder().page(1).size(50).receptorName(receptor_name).build().invoke(visitor).getJSONArray("list");
                for (int i = 0 ; i < arr1.size();i++){
                    JSONObject obj1 = arr1.getJSONObject(i);
                    String search_receptor_name = obj1.getString("receptor_name");
                    Preconditions.checkArgument(search_receptor_name.contains(receptor_name),"搜索接待顾问="+receptor_name+" ,结果包含"+search_receptor_name);
                }

                JSONArray arr2 = VoiceEvaluationPageScene.builder().page(1).size(50).customerName(customer_name).build().invoke(visitor).getJSONArray("list");
                for (int i = 0 ; i < arr2.size();i++){
                    JSONObject obj1 = arr2.getJSONObject(i);
                    String search_customer_name = obj1.getString("customer_name");
                    Preconditions.checkArgument(search_customer_name.contains(customer_name),"搜索客户姓名="+customer_name+" ,结果包含"+search_customer_name);
                }

                JSONArray arr3 = VoiceEvaluationPageScene.builder().page(1).size(50).customerPhone(customer_phone).build().invoke(visitor).getJSONArray("list");
                for (int i = 0 ; i < arr3.size();i++){
                    JSONObject obj1 = arr3.getJSONObject(i);
                    String search_customer_phone = obj1.getString("customer_phone");
                    Preconditions.checkArgument(search_customer_phone.contains(customer_phone),"搜索联系方式="+customer_phone+" ,结果包含"+search_customer_phone);
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

    @Test(dataProvider = "FILTER",dataProviderClass = YunTongInfo.class)
    public void voiceEvaluationFilter2(String search) {

        logger.logCaseStart(caseResult.getCaseName());
        try {

            JSONArray arr1 = VoiceEvaluationPageScene.builder().page(1).size(50).receptorName(search).build().invoke(visitor).getJSONArray("list");
            for (int i = 0 ; i < arr1.size();i++){
                JSONObject obj1 = arr1.getJSONObject(i);
                String search_receptor_name = obj1.getString("receptor_name").toUpperCase();
                Preconditions.checkArgument(search_receptor_name.contains(search),"搜索接待顾问="+search+" ,结果包含"+search_receptor_name);
            }
            JSONArray arr2 = VoiceEvaluationPageScene.builder().page(1).size(50).customerName(search).build().invoke(visitor).getJSONArray("list");
            for (int i = 0 ; i < arr2.size();i++){
                JSONObject obj1 = arr2.getJSONObject(i);
                String search_customer_name = obj1.getString("customer_name").toUpperCase();
                Preconditions.checkArgument(search_customer_name.contains(search),"搜索客户姓名="+search+" ,结果包含"+search_customer_name);
            }

            JSONArray arr3 = VoiceEvaluationPageScene.builder().page(1).size(50).customerPhone(search).build().invoke(visitor).getJSONArray("list");
            for (int i = 0 ; i < arr3.size();i++){
                JSONObject obj1 = arr3.getJSONObject(i);
                String search_customer_phone = obj1.getString("customer_phone").toUpperCase();
                Preconditions.checkArgument(search_customer_phone.contains(search),"搜索联系方式="+search+" ,结果包含"+search_customer_phone);
            }


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("语音评鉴列表文本框筛选");
        }
    }

    @Test(dataProvider = "TIME",dataProviderClass = YunTongInfo.class)
    public void voiceEvaluationFilter3(String start,String end, String mess, String bool) {

        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject obj = VoiceEvaluationPageScene.builder().page(1).size(50).receptionStart(start).receptionEnd(end).build().invoke(visitor,false);
            int code = obj.getInteger("code");
            if (bool.equals("true")){
                JSONArray arr1 = VoiceEvaluationPageScene.builder().page(1).size(50).receptionStart(start).receptionEnd(end).build().invoke(visitor).getJSONArray("list");
                for (int i = 0 ; i < arr1.size();i++){
                    JSONObject obj1 = arr1.getJSONObject(i);
                    String search_reception_time = obj1.getString("reception_time")+":000";
                    Preconditions.checkArgument(Long.valueOf(dt.dateToTimestamp1(start)) <= Long.valueOf(dt.dateToTimestamp1(search_reception_time)) &&
                            Long.valueOf(dt.dateToTimestamp1(search_reception_time)) <= Long.valueOf(dt.dateToTimestamp1(end)),
                            "搜索开始时间="+start +", 结束时间="+end +" , 结果包含"+search_reception_time );
                }
            }
            else {
                Preconditions.checkArgument(code==1001,mess+"状态码期待1001，实际"+code);
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
            JSONArray arr = EnumValueListScene.builder().enumType("ENTER_STORE_STATUS_LIST").build().invoke(visitor).getJSONArray("list");
            for (int i = 0 ; i < arr.size();i++){
                int enter_status = arr.getJSONObject(i).getInteger("key");
                String enter_status_name = arr.getJSONObject(i).getString("value");
                JSONArray arr1 = VoiceEvaluationPageScene.builder().page(1).size(50).enterStatus(enter_status).build().invoke(visitor).getJSONArray("list");
                for (int j = 0 ; j < arr1.size();j++){
                    String search = arr1.getJSONObject(j).getString("enter_status_name");
                    Preconditions.checkArgument(search.equals(enter_status_name),"搜索进店情况="+ enter_status_name+" ,结果包含"+search);
                }
            }
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("语音评鉴列表根据进店情况筛选");
        }
    }

    @Test
    public void voiceEvaluationFilter5() {

        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray arr = EnumValueListScene.builder().enumType("VOICE_EVALUATE_STATUS_LIST").build().invoke(visitor).getJSONArray("list");
            for (int i = 0 ; i < arr.size();i++){
                int evaluate_status = arr.getJSONObject(i).getInteger("key");
                String evaluate_status_name = arr.getJSONObject(i).getString("value");
                JSONArray arr1 = VoiceEvaluationPageScene.builder().page(1).size(50).evaluateStatus(evaluate_status).build().invoke(visitor).getJSONArray("list");
                for (int j = 0 ; j < arr1.size();j++){
                    String search = arr1.getJSONObject(j).getString("evaluate_status_name");
                    Preconditions.checkArgument(search.equals(evaluate_status_name),"搜索评分状态="+ evaluate_status_name+" ,结果包含"+search);
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

    @Test(dataProvider = "FILTER",dataProviderClass = YunTongInfo.class)
    public void voiceEvaluationFilter6(String search) {

        logger.logCaseStart(caseResult.getCaseName());
        try {

            JSONArray arr1 = VoiceEvaluationPageScene.builder().page(1).size(50).receptorName(search).customerPhone(search).build().invoke(visitor).getJSONArray("list");
            for (int i = 0 ; i < arr1.size();i++){
                JSONObject obj1 = arr1.getJSONObject(i);
                String search_receptor_name = obj1.getString("receptor_name").toUpperCase();
                String search_customer_phone = obj1.getString("customer_phone").toUpperCase();
                Preconditions.checkArgument(search_receptor_name.contains(search) && search_customer_phone.contains(search),"搜索接待顾问 和 搜索联系方式="+search+" ,结果包含 接待顾问="+search_receptor_name+" ，联系方式="+search_customer_phone);
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("语音评鉴列表文本框组合筛选");
        }
    }


}
