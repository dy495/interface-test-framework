package com.haisheng.framework.testng.bigScreen.xundian.casedaily.gly;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.crm.wm.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumChecklistUser;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumJobName;
import com.haisheng.framework.testng.bigScreen.crm.wm.enumerator.config.EnumTestProduce;
import com.haisheng.framework.testng.bigScreen.xundian.util.StoreScenarioUtil;
import com.haisheng.framework.testng.bigScreen.xundian.scene.checkrisk.EventTotalScene;
import com.haisheng.framework.testng.bigScreen.xundian.scene.checkrisk.tasks.ListScene;
import com.haisheng.framework.testng.bigScreen.xundian.scene.checkriskalarm.AlarmDetailScene;
import com.haisheng.framework.testng.bigScreen.xundian.casedaily.gly.util.Constant;
import com.haisheng.framework.testng.bigScreen.xundian.enumerator.AccountEnum;
import com.haisheng.framework.testng.bigScreen.xundian.util.SupporterUtil;
import com.haisheng.framework.testng.bigScreen.xundian.util.UserUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

public class StoreInspectionCase extends TestCaseCommon implements TestCaseStd {
    private final static EnumTestProduce PRODUCE = EnumTestProduce.INS_DAILY;
    public VisitorProxy visitor = new VisitorProxy(PRODUCE);
    public UserUtil user = new UserUtil(visitor);
    public SupporterUtil util = new SupporterUtil(visitor);
    StoreScenarioUtil su=StoreScenarioUtil.getInstance();
    public Long shopId=43072L;
    public String shopName="AI-Test(门店订单录像)";

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        CommonConfig commonConfig = new CommonConfig();
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_XUNDIAN_DAILY_SERVICE;
        commonConfig.checklistQaOwner = EnumChecklistUser.GLY.getName();
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.XUNDIAN_DAILY_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, PRODUCE.getDesc() + commonConfig.checklistQaOwner);
        commonConfig.dingHook = DingWebhook.DAILY_STORE_MANAGEMENT_PLATFORM_GRP;
        commonConfig.product = PRODUCE.getAbbreviation();
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
        logger.debug("beforeMethod");
        user.loginPc(AccountEnum.YUE_XIU_DAILY);
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
        su.login("yuexiu@test.com", "f5b3e737510f31b88eb2d4b5d0cd2fb4");
    }

    /**
     *巡店风控事件统计-列表项校验    ok
     */
    @Test(description = "巡店风控事件统计-列表项校验")
    public void storeSystemCase1(){
        logger.logCaseStart(caseResult.getCaseName());
       try{
           IScene scene= EventTotalScene.builder().page(1).size(10).build();
           JSONObject response=visitor.invokeApi(scene,true);
           int pages=response.getInteger("pages");
           for(int page=1;page<=pages;page++){
               JSONArray list=EventTotalScene.builder().page(page).size(10).build().invoke(visitor,true).getJSONArray("list");
               for(int i=0;i<list.size();i++){
                   Long shopId=list.getJSONObject(i).getLong("shop_id");
                   String shopName=list.getJSONObject(i).getString("shop_name");
                   String shopManager=list.getJSONObject(i).getString("shop_manager");
                   int eventTotal=list.getJSONObject(i).getInteger("event_total");
                   int processedTotal=list.getJSONObject(i).getInteger("processed_total");
                   int pendingTotal=list.getJSONObject(i).getInteger("pending_total");
                   int urgentTotal=list.getJSONObject(i).getInteger("urgent_total");
                   Preconditions.checkArgument(shopId!=null&&shopName!=null&&shopManager!=null&&eventTotal>=0&&processedTotal>=0&&pendingTotal>=0&&urgentTotal>=0,"列表项校验失败");
               }
           }
       }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("巡店风控事件统计-列表项校验");
        }
    }

    /**
     *巡店风控事件统计-筛选栏校验（门店）   ok
     */
    @Test(description = "巡店风控事件统计-筛选栏校验（门店）")
    public void storeSystemCase2(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //获取列表的信息
            JSONArray list1=EventTotalScene.builder().page(1).size(10).build().invoke(visitor,true).getJSONArray("list");
            if(list1.size()>0){
                //获取第一行的shopId
                String shopName=list1.getJSONObject(0).getString("shop_name");
                JSONObject response1=EventTotalScene.builder().page(1).size(10).shopName(shopName).build().invoke(visitor,true);
                int pages=response1.getInteger("pages");
                System.err.println(pages);
                for(int page=1;page<=pages;page++){
                    JSONArray list=EventTotalScene.builder().page(page).size(10).shopName(shopName).build().invoke(visitor,true).getJSONArray("list");
                    for(int i=0;i<list.size();i++){
                        String shopName1=list.getJSONObject(i).getString("shop_name");
                        System.out.println("筛选栏中填写的门店的shopId为："+shopName+"  。列表中的shopId为："+shopName1);
                        Preconditions.checkArgument(shopName.equals(shopName1),"筛选栏中填写的门店的shopId为："+shopName+"  。列表中的shopId为："+shopName1);
                    }
                }
            }
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("巡店风控事件统计-筛选栏校验（门店）");
        }
    }

    /**
     *门店巡店记录列表-列表项校验    ok
     */
    @Test(description = "门店巡店记录列表-列表项校验")
    public void storeSystemCase3(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
             //门店的风控告警详情
            IScene scene= ListScene.builder().page(1).size(10).shopId(shopId).build();
            JSONObject response=visitor.invokeApi(scene,true);
            int pages=response.getInteger("pages");
            for(int page=1;page<=pages;page++){
                JSONArray list=ListScene.builder().page(page).size(10).shopId(shopId).build().invoke(visitor,true).getJSONArray("list");
                for(int i=0;i<list.size();i++){
                    String triggerTime=list.getJSONObject(i).getString("trigger_time");
                    String triggerRule=list.getJSONObject(i).getString("trigger_rule");
                    String triggerRuleName=list.getJSONObject(i).getString("trigger_rule_name");
//                    String reviewTime=list.getJSONObject(i).getString("review_time");
//                    String reviewer=list.getJSONObject(i).getString("reviewer");
                    String eventState=list.getJSONObject(i).getString("event_state");
                    int leaveMarkNum=list.getJSONObject(i).getInteger("leave_mark_num");

                    Preconditions.checkArgument(triggerTime!=null&&triggerRule!=null&&triggerRuleName!=null&&eventState!=null&&leaveMarkNum>=0,"列表项校验失败");
                }
            }
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("门店巡店记录列表-列表项校验");
        }
    }

    /**
     *门店巡店记录列表-筛选栏校验（触发规则、门店状态）   ok
     */
    @Test(description = "门店巡店记录列表-筛选栏校验（触发规则、门店状态）",dataProvider = "SELECT_tasksListFilter",dataProviderClass = Constant.class)
    public void storeSystemCase4(String pram, String output){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //获取列表的信息
            JSONObject response = su.tasksListPage(shopId, "1", "10", "", "");
            if (response.getJSONArray("list").size() > 0) {
                String result = response.getJSONArray("list").getJSONObject(0).getString(output);
                JSONObject response1 =  su.tasksListPage( shopId,"1", "10", pram, result);
                int pages = response1.getInteger("pages");
                for (int page = 1; page <= pages; page++) {
                    JSONArray list =  su.tasksListPage( shopId,String.valueOf(page), "10", pram, result).getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        String Flag = list.getJSONObject(i).getString(output);
                        System.out.println("门店巡店记录列表按照" + result + "查询，结果错误" + Flag);
                        System.out.println("门店巡店记录列表按" + result + "查询，结果错误" + Flag);
                        Preconditions.checkArgument(Flag.contains(result), "门店巡店记录列表按" + result + "查询，结果错误" + Flag);
                    }
                }
            }
        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("门店巡店记录列表-筛选栏校验（触发规则、门店状态）");
        }
    }

    /**
     *巡店告警详情-门店告警列表与详情数据对比      ok
     */
    @Test(description = "巡店告警详情-门店告警列表与详情数据对比")
    public void storeSystemCase5(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //获取门店告警列表第一行的参数
            JSONObject respond=ListScene.builder().page(1).size(10).shopId(shopId).build().invoke(visitor,true).getJSONArray("list").getJSONObject(0);
            Long eventId= respond.getLong("id");
            String triggerTime=respond.getString("trigger_time");
            String triggerRule=respond.getString("trigger_rule");
            String triggerRuleName=respond.getString("trigger_rule_name");
            String reviewTime=respond.getString("review_time");
            String reviewer=respond.getString("reviewer");
            String eventStateName=respond.getString("event_state_name");
            int leaveMarkNum=respond.getInteger("leave_mark_num");

            //门店的风控告警详情
            IScene scene= AlarmDetailScene.builder().eventId(eventId).build();
            JSONObject response=visitor.invokeApi(scene,true);
            String timeDetail=response.getString("time");
            String ruleDetail=response.getString("rule");
            String eventStateDetail=response.getString("event_state");
            //触发时长
            String durationDetail=response.getString("duration");
            int num=response.getInteger("num");
            String describe=response.getString("describe");

            Preconditions.checkArgument(timeDetail.equals(triggerTime),"门店告警列表中的展示为："+triggerTime+" 告警详情中的展示为："+timeDetail);
            Preconditions.checkArgument(ruleDetail.equals(triggerRuleName),"门店告警列表中的展示为："+triggerRuleName+" 告警详情中的展示为："+ruleDetail);
            Preconditions.checkArgument(eventStateDetail.equals(eventStateName),"门店告警列表中的展示为："+eventStateName+" 告警详情中的展示为："+eventStateDetail);
            Preconditions.checkArgument(num==leaveMarkNum,"门店告警列表中的展示为："+leaveMarkNum+" 告警详情中的数据为："+num);
            Preconditions.checkArgument(describe!=null,"告警详情中的展示为："+describe);

        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("巡店告警详情-门店告警列表与详情数据对比");
        }
    }


    /**
     * 数据一致性：触发事件数==此门店触发事件数之合&待确认事件==此门店待确认事件数之合&待确认紧急事件==此门店待确认紧急事件之合&已确认事件==此门店已确认事件之合
     */
    @Test(description = "触发事件数==此门店触发事件数之合&待确认事件==此门店待确认事件数之合&待确认紧急事件==此门店待确认紧急事件之合&已确认事件==此门店已确认事件之合")
    public void storeDateCase1(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            //智慧巡店页面
            IScene scene= EventTotalScene.builder().page(1).size(10).build();
            JSONObject response=visitor.invokeApi(scene,true);
            int pages=response.getInteger("pages");
            for(int page=1;page<=pages;page++){
                JSONArray list=EventTotalScene.builder().page(page).size(10).build().invoke(visitor,true).getJSONArray("list");
                for(int i=0;i<list.size();i++){
                    Long shopId1=list.getJSONObject(i).getLong("shop_id");
                    if(shopId1.equals(shopId)){
                        //触发事件数
                        int eventTotal=list.getJSONObject(i).getInteger("event_total");
                        //已确认事件数
                        int processedTotal=list.getJSONObject(i).getInteger("processed_total");
                        //待确认事件数
                        int pendingTotal=list.getJSONObject(i).getInteger("pending_total");
                        //紧急事件数
                        int urgentTotal=list.getJSONObject(i).getInteger("urgent_total");

                        //进入对应的门店的告警事件页面
                        JSONObject response1=ListScene.builder().page(1).size(10).shopId(shopId1).build().invoke(visitor,true);
                        //触发事件数
                        int total=response1.getInteger("total");
                        //已确认的事件数
                        int processed=ListScene.builder().page(1).size(10).eventState("ALARM_CONFIRMED").shopId(shopId1).build().invoke(visitor,true).getInteger("total");
                        //待确认事件数
                        int pending=ListScene.builder().page(1).size(10).eventState("WAITING_ALARM_CONFIRM").shopId(shopId1).build().invoke(visitor,true).getInteger("total");
                        //紧急事件数--规则为口罩的事件数
                        int urgent=ListScene.builder().page(1).size(10).triggerRule("MASK_MONITOR").shopId(shopId1).build().invoke(visitor,true).getInteger("total");

                        Preconditions.checkArgument(eventTotal==total,"门店列表触发的事件数："+eventTotal+" 门店告警列表中触发事件数为： "+total);
                        Preconditions.checkArgument(processed==processedTotal,"门店列表已处理事件数："+processedTotal+" 门店告警列表中已处理事件数为： "+processed);
                        Preconditions.checkArgument(pendingTotal==pending,"门店列表待处理事件数："+pendingTotal+" 门店告警列表中待处理事件数为： "+pending);
                        Preconditions.checkArgument(urgent==urgentTotal,"门店列表待紧急处理事件数："+urgentTotal+" 门店告警列表中待紧急处理事件数为： "+urgent);
                    }
                }
            }


        }catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("触发事件数==此门店触发事件数之合&待确认事件==此门店待确认事件数之合&待确认紧急事件==此门店待确认紧急事件之合&已确认事件==此门店已确认事件之合");
        }
    }







}