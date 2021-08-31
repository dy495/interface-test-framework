package com.haisheng.framework.testng.bigScreen.itemXundian.caseonline.gly;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.proxy.VisitorProxy;
import com.haisheng.framework.testng.bigScreen.itemBasic.base.scene.IScene;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumChecklistUser;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumJobName;
import com.haisheng.framework.testng.bigScreen.itemBasic.enumerator.EnumTestProduct;
import com.haisheng.framework.testng.bigScreen.itemXundian.casedaily.gly.util.BusinessUtil;
import com.haisheng.framework.testng.bigScreen.itemXundian.casedaily.gly.util.Constant;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.enumerator.EventStateEnum;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.enumerator.TriggerRuleEnum;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.enumerator.handleStatus;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.checkrisk.EventTotalScene;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.checkrisk.tasks.ListScene;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.scene.checkriskalarm.AlarmDetailScene;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.util.MendianInfo;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.util.StoreScenarioUtil;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.util.SceneUtil;
import com.haisheng.framework.testng.bigScreen.itemXundian.common.util.UserUtil;
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

public class StoreInspectionOnlineCase extends TestCaseCommon implements TestCaseStd {
    private final EnumTestProduct product = EnumTestProduct.XD_ONLINE;
    public VisitorProxy visitor = new VisitorProxy(product);
    public UserUtil user = new UserUtil(visitor);
    public SceneUtil util = new SceneUtil(visitor);
    BusinessUtil businessUtil = new BusinessUtil(visitor);
    StoreScenarioUtil su = StoreScenarioUtil.getInstance();
    public Long shopId = 14630L;
    public String shopName = "中关村1号店";
    CommonConfig commonConfig = new CommonConfig();
    MendianInfo mi = new MendianInfo();

    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before class initial");
        su.exChangeIpPort();
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_MENDIAN_ONLINE_SERVICE;
        commonConfig.checklistQaOwner = EnumChecklistUser.GLY.getName();
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, EnumJobName.XUNDIAN_ONLINE_TEST.getJobName());
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, product.getDesc() + commonConfig.checklistQaOwner);
        commonConfig.dingHook = DingWebhook.ONLINE_STORE_MANAGEMENT_PLATFORM_GRP;
        commonConfig.setShopId("14630").setReferer(product.getReferer()).setRoleId(product.getRoleId()).setProduct(product.getAbbreviation());
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
        su.login("storedemo@winsense.ai", "b0581aa73b04d9fe6e3057a613e6f363");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
    }

    /**
     * 巡店风控事件统计-列表项校验    ok
     */
    @Test(description = "巡店风控事件统计-列表项校验")
    public void storeSystemCase1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            IScene scene = EventTotalScene.builder().page(1).size(10).build();
            JSONObject response = scene.visitor(visitor).execute();
            int pages = response.getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                JSONArray list = EventTotalScene.builder().page(page).size(10).build().visitor(visitor).execute().getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    Long shopId = list.getJSONObject(i).getLong("shop_id");
                    String shopName = list.getJSONObject(i).getString("shop_name");
                    String shopManager = list.getJSONObject(i).getString("shop_manager");
                    int eventTotal = list.getJSONObject(i).getInteger("event_total");
                    int processedTotal = list.getJSONObject(i).getInteger("processed_total");
                    int pendingTotal = list.getJSONObject(i).getInteger("pending_total");
                    int urgentTotal = list.getJSONObject(i).getInteger("urgent_total");
                    Preconditions.checkArgument(shopId != null && shopName != null && shopManager != null && eventTotal >= 0 && processedTotal >= 0 && pendingTotal >= 0 && urgentTotal >= 0, "列表项校验失败");
                }
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("巡店风控事件统计-列表项校验");
        }
    }

    /**
     * 巡店风控事件统计-筛选栏校验（门店）   ok
     */
    @Test(description = "巡店风控事件统计-筛选栏校验（门店）")
    public void storeSystemCase2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取列表的信息
            JSONArray list1 = EventTotalScene.builder().page(1).size(10).build().visitor(visitor).execute().getJSONArray("list");
            if (list1.size() > 0) {
                //获取第一行的shopId
                String shopName = list1.getJSONObject(0).getString("shop_name");
                JSONObject response1 = EventTotalScene.builder().page(1).size(10).shopName(shopName).build().visitor(visitor).execute();
                int pages = response1.getInteger("pages");
                System.err.println(pages);
                for (int page = 1; page <= pages; page++) {
                    JSONArray list = EventTotalScene.builder().page(page).size(10).shopName(shopName).build().visitor(visitor).execute().getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        String shopName1 = list.getJSONObject(i).getString("shop_name");
                        System.out.println("筛选栏中填写的门店的shopId为：" + shopName + "  。列表中的shopId为：" + shopName1);
                        Preconditions.checkArgument(shopName.equals(shopName1), "筛选栏中填写的门店的shopId为：" + shopName + "  。列表中的shopId为：" + shopName1);
                    }
                }
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("巡店风控事件统计-筛选栏校验（门店）");
        }
    }

    /**
     * 门店巡店记录列表-列表项校验    ok
     */
    @Test(description = "门店巡店记录列表-列表项校验")
    public void storeSystemCase3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //门店的风控告警详情
            IScene scene = ListScene.builder().page(1).size(10).shopId(shopId).build();
            JSONObject response = scene.visitor(visitor).execute();
            int pages = response.getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                JSONArray list = ListScene.builder().page(page).size(10).shopId(shopId).build().visitor(visitor).execute().getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    String triggerTime = list.getJSONObject(i).getString("trigger_time");
                    String triggerRule = list.getJSONObject(i).getString("trigger_rule");
                    String triggerRuleName = list.getJSONObject(i).getString("trigger_rule_name");
//                    String reviewTime=list.getJSONObject(i).getString("review_time");
//                    String reviewer=list.getJSONObject(i).getString("reviewer");
                    String eventState = list.getJSONObject(i).getString("event_state");
                    int leaveMarkNum = list.getJSONObject(i).getInteger("leave_mark_num");

                    Preconditions.checkArgument(triggerTime != null && triggerRule != null && triggerRuleName != null && eventState != null && leaveMarkNum >= 0, "列表项校验失败");
                }
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("门店巡店记录列表-列表项校验");
        }
    }

    /**
     * 门店巡店记录列表-筛选栏校验（触发规则、门店状态）   ok
     */
    @Test(description = "门店巡店记录列表-筛选栏校验（触发规则、门店状态）", dataProvider = "SELECT_tasksListFilter", dataProviderClass = Constant.class)
    public void storeSystemCase4(String pram, String output) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取列表的信息
            JSONObject response = su.tasksListPage(shopId, "1", "10", "", "");
            if (response.getJSONArray("list").size() > 0) {
                String result = response.getJSONArray("list").getJSONObject(0).getString(output);
                JSONObject response1 = su.tasksListPage(shopId, "1", "10", pram, result);
                int pages = response1.getInteger("pages");
                for (int page = 1; page <= pages; page++) {
                    JSONArray list = su.tasksListPage(shopId, String.valueOf(page), "10", pram, result).getJSONArray("list");
                    for (int i = 0; i < list.size(); i++) {
                        String Flag = list.getJSONObject(i).getString(output);
                        System.out.println("门店巡店记录列表按照" + result + "查询，结果错误" + Flag);
                        System.out.println("门店巡店记录列表按" + result + "查询，结果错误" + Flag);
                        Preconditions.checkArgument(Flag.contains(result), "门店巡店记录列表按" + result + "查询，结果错误" + Flag);
                    }
                }
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("门店巡店记录列表-筛选栏校验（触发规则、门店状态）");
        }
    }

    /**
     * 门店巡店记录列表-筛选栏校验（触发时间筛选）
     */
    @Test()
    public void storeSystemCase5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String startTime = dt.getHistoryDate(-10) + " 00";
            String endTime = dt.getHistoryDate(10) + " 23";
            JSONObject respond = su.tasksListTimePage(shopId, "1", "10", startTime, endTime);
            int pages = respond.getInteger("pages") > 10 ? 10 : respond.getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                JSONArray list = su.tasksListTimePage(shopId, String.valueOf(page), "10", startTime, endTime).getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    String triggerTime = list.getJSONObject(i).containsKey("trigger_time") ? list.getJSONObject(i).getString("trigger_time").substring(0, 13) : startTime;
                    System.out.println("开始时间：" + startTime + " 结束时间：" + endTime + "列表中的有效的开始时间:" + triggerTime);
                    Preconditions.checkArgument(triggerTime.compareTo(startTime) >= 0 && triggerTime.compareTo(endTime) <= 0, "开始时间：" + startTime + " 结束时间：" + endTime + "列表中的有效的开始时间:" + triggerTime);
                }
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("门店巡店记录列表-筛选栏校验（触发时间筛选）");
        }
    }

    /**
     * 巡店告警详情-门店告警列表与详情数据对比      ok
     */
    @Test(description = "巡店告警详情-门店告警列表与详情数据对比")
    public void storeSystemCase6() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取门店告警列表第一行的参数
            JSONObject respond = ListScene.builder().page(1).size(10).shopId(shopId).build().visitor(visitor).execute().getJSONArray("list").getJSONObject(0);
            Long eventId = respond.getLong("id");
            String triggerTime = respond.getString("trigger_time");
            String triggerRuleName = respond.getString("trigger_rule_name");
            String eventStateName = respond.getString("event_state_name");
            int leaveMarkNum = respond.getInteger("leave_mark_num");
            if (respond.containsKey("reviewer")) {
                //门店的风控告警详情
                IScene scene = AlarmDetailScene.builder().eventId(eventId).build();
                JSONObject response = scene.visitor(visitor).execute();
                String timeDetail = response.getString("time");
                String ruleDetail = response.getString("rule");
                String eventStateDetail = response.getString("event_state");
                //判断事件是否处理
                String isHandle = response.getString("is_handle");
                //触发时长
                String durationDetail = response.getString("duration");
                int num = response.getInteger("num");
                String describe = response.getString("describe");

                Preconditions.checkArgument(timeDetail.equals(triggerTime), "门店告警列表中的展示为：" + triggerTime + " 告警详情中的展示为：" + timeDetail);
                Preconditions.checkArgument(ruleDetail.equals(triggerRuleName), "门店告警列表中的展示为：" + triggerRuleName + " 告警详情中的展示为：" + ruleDetail);
                Preconditions.checkArgument(eventStateDetail.equals(eventStateName), "门店告警列表中的展示为：" + eventStateName + " 告警详情中的展示为：" + eventStateDetail);
                Preconditions.checkArgument(num == leaveMarkNum, "门店告警列表中的展示为：" + leaveMarkNum + " 告警详情中的数据为：" + num);
                Preconditions.checkArgument(describe != null, "告警详情中的展示为：" + describe);
                Preconditions.checkArgument(isHandle.equals("1"), "巡店列表中显示此事件已经被处理，详情中现实的是未处理");

            } else {
                //门店的风控告警详情
                IScene scene = AlarmDetailScene.builder().eventId(eventId).build();
                JSONObject response = scene.visitor(visitor).execute();
                String timeDetail = response.getString("time");
                String ruleDetail = response.getString("rule");
                String eventStateDetail = response.getString("event_state");
                //触发时长
                String durationDetail = response.getString("duration");
                int num = response.getInteger("num");
                String describe = response.getString("describe");

                Preconditions.checkArgument(timeDetail.equals(triggerTime), "门店告警列表中的展示为：" + triggerTime + " 告警详情中的展示为：" + timeDetail);
                Preconditions.checkArgument(ruleDetail.equals(triggerRuleName), "门店告警列表中的展示为：" + triggerRuleName + " 告警详情中的展示为：" + ruleDetail);
                Preconditions.checkArgument(eventStateDetail.equals(eventStateName), "门店告警列表中的展示为：" + eventStateName + " 告警详情中的展示为：" + eventStateDetail);
                Preconditions.checkArgument(num == leaveMarkNum, "门店告警列表中的展示为：" + leaveMarkNum + " 告警详情中的数据为：" + num);
                Preconditions.checkArgument(describe != null, "告警详情中的展示为：" + describe);

            }

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("巡店告警详情-门店告警列表与详情数据对比");
        }
    }


    /**
     * 数据一致性：触发事件数==此门店触发事件数之合&待确认事件==此门店待确认事件数之合&待确认紧急事件==此门店待确认紧急事件之合&已确认事件==此门店已确认事件之合       ok
     */
    @Test(description = "触发事件数==此门店触发事件数之合&待确认事件==此门店待确认事件数之合&待确认紧急事件==此门店待确认紧急事件之合&已确认事件==此门店已确认事件之合")
    public void storeDateCase1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //智慧巡店页面
            IScene scene = EventTotalScene.builder().page(1).size(10).build();
            JSONObject response = scene.visitor(visitor).execute();
            int pages = response.getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                JSONArray list = EventTotalScene.builder().page(page).size(10).build().visitor(visitor).execute().getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    Long shopId1 = list.getJSONObject(i).getLong("shop_id");
                    //触发事件数
                    int eventTotal = list.getJSONObject(i).getInteger("event_total");
                    //已确认事件数
                    int processedTotal = list.getJSONObject(i).getInteger("processed_total");
                    //待确认事件数
                    int pendingTotal = list.getJSONObject(i).getInteger("pending_total");
                    //紧急事件数
                    int urgentTotal = list.getJSONObject(i).getInteger("urgent_total");

                    //进入对应的门店的告警事件页面
                    JSONObject response1 = ListScene.builder().page(1).size(10).shopId(shopId1).build().visitor(visitor).execute();
                    //触发事件数
                    int total = response1.getInteger("total");
                    //已确认的事件数
                    int processed = ListScene.builder().page(1).size(10).eventState(EventStateEnum.ALARM_CONFIRMED.getEventState()).shopId(shopId1).build().visitor(visitor).execute().getInteger("total");
                    //无需处理事件
                    int noNeed = ListScene.builder().page(1).size(10).eventState(EventStateEnum.NO_NEED_HANDLE.getEventState()).shopId(shopId1).build().visitor(visitor).execute().getInteger("total");
                    //待确认事件数
                    int pending = ListScene.builder().page(1).size(10).eventState(EventStateEnum.WAITING_ALARM_CONFIRM.getEventState()).shopId(shopId1).build().visitor(visitor).execute().getInteger("total");
                    //紧急事件数--规则为口罩的事件数
                    int urgent = ListScene.builder().page(1).size(10).triggerRule(TriggerRuleEnum.MASK_MONITOR.getTriggerRule()).eventState(EventStateEnum.WAITING_ALARM_CONFIRM.getEventState()).shopId(shopId1).build().visitor(visitor).execute().getInteger("total");

                    Preconditions.checkArgument(eventTotal == total, "门店列表触发的事件数：" + eventTotal + " 门店告警列表中触发事件数为： " + total);
                    Preconditions.checkArgument(processed + noNeed == processedTotal, "门店列表已处理事件数：" + processedTotal + " 门店告警列表中已处理事件数为： " + processed);
                    Preconditions.checkArgument(pendingTotal == pending, "门店列表待处理事件数：" + pendingTotal + " 门店告警列表中待处理事件数为： " + pending);
                    Preconditions.checkArgument(urgent == urgentTotal, "门店列表待紧急处理事件数：" + urgentTotal + " 门店告警列表中待紧急处理事件数为： " + urgent);

                }
            }


        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("触发事件数==此门店触发事件数之合&待确认事件==此门店待确认事件数之合&待确认紧急事件==此门店待确认紧急事件之合&已确认事件==此门店已确认事件之合");
        }
    }

    /**
     * 数据一致性：触发事件数=待确认事件+已确认事件     ok
     */
    @Test(description = "触发事件数=待确认事件+已确认事件")
    public void storeDateCase2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //智慧巡店页面
            IScene scene = EventTotalScene.builder().page(1).size(10).build();
            JSONObject response = scene.visitor(visitor).execute();
            int pages = response.getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                JSONArray list = EventTotalScene.builder().page(page).size(10).build().visitor(visitor).execute().getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    //触发事件数
                    int eventTotal = list.getJSONObject(i).getInteger("event_total");
                    //已确认事件数
                    int processedTotal = list.getJSONObject(i).getInteger("processed_total");
                    //待确认事件数
                    int pendingTotal = list.getJSONObject(i).getInteger("pending_total");

                    Preconditions.checkArgument(eventTotal == processedTotal + pendingTotal, "触发的总事件为：" + eventTotal + " 已确认的事件为：" + processedTotal + " 待确认的事件为：" + pendingTotal);

                }
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("触发事件数=待确认事件+已确认事件");
        }
    }

    /**
     * 数据一致性：待确认事件>=待确认紧急事件     ok
     */
    @Test(description = "待确认事件>=待确认紧急事件")
    public void storeDateCase3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //智慧巡店页面
            IScene scene = EventTotalScene.builder().page(1).size(10).build();
            JSONObject response = scene.visitor(visitor).execute();
            int pages = response.getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                JSONArray list = EventTotalScene.builder().page(page).size(10).build().visitor(visitor).execute().getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    //待确认事件数
                    int pendingTotal = list.getJSONObject(i).getInteger("pending_total");
                    //紧急事件数
                    int urgentTotal = list.getJSONObject(i).getInteger("urgent_total");

                    Preconditions.checkArgument(pendingTotal >= urgentTotal, "待确认事件数为：" + pendingTotal + " 紧急事件数为：" + urgentTotal);
                }
            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("待确认事件>=待确认紧急事件");
        }
    }

    /**
     * 构建场景--人员A触发员工口罩规则，触发事件数+1，待确认事件数+1，待确认紧急待确认事件+1,此规则留痕数量、图片+1
     */
    @Test(description = "人员A触发员工口罩规则，触发事件数+1，待确认事件数+1，待确认紧急待确认事件+1,此规则留痕数量、图片+1")
    public void storeDateCase4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Boolean flag = true;
            int leaveMarkBeforeNum = 0;
            int id = 0;
            //门店列表页面-触发规则之前
            IScene sceneShopBefore = EventTotalScene.builder().page(1).size(10).shopName(shopName).build();
            JSONArray shopBeforeList = sceneShopBefore.visitor(visitor).execute().getJSONArray("list");
            int eventTotalBefore = shopBeforeList.getJSONObject(0).getInteger("event_total");
            int processedTotalBefore = shopBeforeList.getJSONObject(0).getInteger("processed_total");
            int pendingTotalBefore = shopBeforeList.getJSONObject(0).getInteger("pending_total");
            int urgentTotalBefore = shopBeforeList.getJSONObject(0).getInteger("urgent_total");
            //门店事件页面
            IScene scene = ListScene.builder().page(1).size(10).shopId(shopId).build();
            JSONObject response = scene.visitor(visitor).execute();
            int pages = response.getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                JSONArray list = ListScene.builder().page(page).size(10).shopId(shopId).build().visitor(visitor).execute().getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    String triggerTime = list.getJSONObject(i).getString("trigger_time").substring(0, 13);
                    String triggerRule = list.getJSONObject(i).getString("trigger_rule");
                    String eventState = list.getJSONObject(i).getString("event_state");
                    //获取系统当前时间
                    String time = String.valueOf(dt.currentDateToTimestamp()).substring(0, 13);
                    System.out.println(time + "    " + triggerTime);
                    //判断当前门店的列表中是当前小时是否有待处理的口罩事件
                    if (time.equals(triggerTime) && triggerRule.equals(TriggerRuleEnum.MASK_MONITOR.getTriggerRule()) && eventState.equals(EventStateEnum.WAITING_ALARM_CONFIRM.getEventState())) {
                        flag = false;
                        leaveMarkBeforeNum = list.getJSONObject(i).getInteger("leave_mark_num");
                        id = list.getJSONObject(i).getInteger("id");
                        System.err.println("id:" + id);
                    }
                }
            }
            System.err.println("flag:" + flag);
            //触发口罩规则
            storeEventCase1();
            sleep(3);
            //门店列表页面-触发规则之后
            IScene sceneShopAfter = EventTotalScene.builder().page(1).size(10).shopName(shopName).build();
            JSONArray shopAfterList = sceneShopAfter.visitor(visitor).execute().getJSONArray("list");
            int eventTotalAfter = shopAfterList.getJSONObject(0).getInteger("event_total");
            int processedTotalAfter = shopAfterList.getJSONObject(0).getInteger("processed_total");
            int pendingTotalAfter = shopAfterList.getJSONObject(0).getInteger("pending_total");
            int urgentTotalAfter = shopAfterList.getJSONObject(0).getInteger("urgent_total");
            //当前图片的留痕张数
            int leaveMarkAfterNum = businessUtil.getLeaveMarkBeforeNum(shopId, id);
            System.out.println("--------leaveMarkAfterNum:" + leaveMarkAfterNum);


            //判断当前时间段是否触发规则&&规则师傅是否已处理
            if (flag) {
                System.out.println("---------" + flag);
                Preconditions.checkArgument(eventTotalBefore == eventTotalAfter - 1, "触发规则之前的触发规则的条数：" + eventTotalBefore + "  触发规则之后的列触发规则条数：" + eventTotalAfter);
                Preconditions.checkArgument(processedTotalBefore == processedTotalAfter, "触发规则之前的告警已处理的的条数：" + processedTotalBefore + "  触发规则之后的告警已处理条数：" + processedTotalAfter);
                Preconditions.checkArgument(pendingTotalBefore == pendingTotalAfter - 1, "触发规则之前的告警待处理的的条数：" + pendingTotalBefore + "  触发规则之后的告警待处理条数：" + pendingTotalAfter);
                Preconditions.checkArgument(urgentTotalBefore == urgentTotalAfter - 1, "触发规则之前的待确认紧急事件的条数：" + urgentTotalBefore + "  触发规则之后的待确认紧急事件的条数：" + urgentTotalAfter);
                Preconditions.checkArgument(leaveMarkAfterNum == 1, "触发规则之前的留痕条数为的条数：" + leaveMarkBeforeNum + "  触发规则之后的留痕事件的条数：" + leaveMarkBeforeNum);

            } else {

                Preconditions.checkArgument(eventTotalBefore == eventTotalAfter, "触发规则之前的触发规则的条数：" + eventTotalBefore + "  触发规则之后的列触发规则条数：" + eventTotalAfter);
                Preconditions.checkArgument(processedTotalBefore == processedTotalAfter, "触发规则之前的告警已处理的的条数：" + processedTotalBefore + "  触发规则之后的告警已处理条数：" + processedTotalAfter);
                Preconditions.checkArgument(pendingTotalBefore == pendingTotalAfter, "触发规则之前的告警待处理的的条数：" + pendingTotalBefore + "  触发规则之后的告警待处理条数：" + pendingTotalAfter);
                Preconditions.checkArgument(urgentTotalBefore == urgentTotalAfter, "触发规则之前的待确认紧急事件的条数：" + urgentTotalBefore + "  触发规则之后的待确认紧急事件的条数：" + urgentTotalAfter);
                Preconditions.checkArgument(leaveMarkAfterNum == leaveMarkBeforeNum + 1, "触发规则之前的留痕条数为的条数：" + leaveMarkBeforeNum + "  触发规则之后的留痕事件的条数：" + leaveMarkBeforeNum);

            }

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("人员A触发员工口罩规则，触发事件数+1，待确认事件数+1，待确认紧急待确认事件+1,此规则留痕数量、图片+1");
        }
    }

    /**
     * 构建场景--人员A触发制服规则，触发事件数+1，待确认事件数+1，待确认紧急待确认事件+1,此规则留痕数量、图片+1
     */
    @Test(description = "人员A触发制服规则，触发事件数+1，待确认事件数+1，待确认紧急待确认事件+1,此规则留痕数量、图片+1")
    public void storeDateCase5() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Boolean flag = true;
            int leaveMarkBeforeNum = 0;
            int id = 0;
            //门店列表页面-触发规则之前
            IScene sceneShopBefore = EventTotalScene.builder().page(1).size(10).shopName(shopName).build();
            JSONArray shopBeforeList = sceneShopBefore.visitor(visitor).execute().getJSONArray("list");
            int eventTotalBefore = shopBeforeList.getJSONObject(0).getInteger("event_total");
            int processedTotalBefore = shopBeforeList.getJSONObject(0).getInteger("processed_total");
            int pendingTotalBefore = shopBeforeList.getJSONObject(0).getInteger("pending_total");
            int urgentTotalBefore = shopBeforeList.getJSONObject(0).getInteger("urgent_total");

            //门店事件页面
            IScene scene = ListScene.builder().page(1).size(10).shopId(shopId).build();
            JSONObject response = scene.visitor(visitor).execute();
            int pages = response.getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                JSONArray list = ListScene.builder().page(page).size(10).shopId(shopId).build().visitor(visitor).execute().getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    String triggerTime = list.getJSONObject(i).getString("trigger_time").substring(0, 13);
                    String triggerRule = list.getJSONObject(i).getString("trigger_rule");
                    String eventState = list.getJSONObject(i).getString("event_state");
                    //获取系统当前时间
                    String time = String.valueOf(dt.currentDateToTimestamp()).substring(0, 13);
                    //判断当前门店的列表中是当前小时是否有待处理的口罩事件
                    if (time.equals(triggerTime) && triggerRule.equals(TriggerRuleEnum.UNIFORM_MONITOR.getTriggerRule()) && eventState.equals(EventStateEnum.WAITING_ALARM_CONFIRM.getEventState())) {
                        flag = false;
                        leaveMarkBeforeNum = list.getJSONObject(i).getInteger("leave_mark_num");
                        id = list.getJSONObject(i).getInteger("id");
                    }
                }
            }
            System.err.println("flag:" + flag);
            //触发制服规则
            storeEventCase2();
            sleep(3);
            //门店列表页面-触发规则之后
            IScene sceneShopAfter = EventTotalScene.builder().page(1).size(10).shopName(shopName).build();
            JSONArray shopAfterList = sceneShopAfter.visitor(visitor).execute().getJSONArray("list");
            int eventTotalAfter = shopAfterList.getJSONObject(0).getInteger("event_total");
            int processedTotalAfter = shopAfterList.getJSONObject(0).getInteger("processed_total");
            int pendingTotalAfter = shopAfterList.getJSONObject(0).getInteger("pending_total");
            int urgentTotalAfter = shopAfterList.getJSONObject(0).getInteger("urgent_total");
            //当前图片的留痕张数
            int leaveMarkAfterNum = businessUtil.getLeaveMarkBeforeNum(shopId, id);

            //判断当前时间段是否触发规则&&规则师傅已处理
            if (flag) {
                System.out.println("---------" + flag);
                Preconditions.checkArgument(eventTotalBefore == eventTotalAfter - 1, "触发规则之前的触发规则的条数：" + eventTotalBefore + "  触发规则之后的列触发规则条数：" + eventTotalAfter);
                Preconditions.checkArgument(processedTotalBefore == processedTotalAfter, "触发规则之前的告警已处理的的条数：" + processedTotalBefore + "  触发规则之后的告警已处理条数：" + processedTotalAfter);
                Preconditions.checkArgument(pendingTotalBefore == pendingTotalAfter - 1, "触发规则之前的告警待处理的的条数：" + pendingTotalBefore + "  触发规则之后的告警待处理条数：" + pendingTotalAfter);
                Preconditions.checkArgument(urgentTotalBefore == urgentTotalAfter, "触发规则之前的待确认紧急事件的条数：" + urgentTotalBefore + "  触发规则之后的待确认紧急事件的条数：" + urgentTotalAfter);
                Preconditions.checkArgument(leaveMarkAfterNum == 1, "触发规则之前的留痕条数为的条数：" + leaveMarkBeforeNum + "  触发规则之后的留痕事件的条数：" + leaveMarkBeforeNum);

            } else {

                Preconditions.checkArgument(eventTotalBefore == eventTotalAfter, "触发规则之前的触发规则的条数：" + eventTotalBefore + "  触发规则之后的列触发规则条数：" + eventTotalAfter);
                Preconditions.checkArgument(processedTotalBefore == processedTotalAfter, "触发规则之前的告警已处理的的条数：" + processedTotalBefore + "  触发规则之后的告警已处理条数：" + processedTotalAfter);
                Preconditions.checkArgument(pendingTotalBefore == pendingTotalAfter, "触发规则之前的告警待处理的的条数：" + pendingTotalBefore + "  触发规则之后的告警待处理条数：" + pendingTotalAfter);
                Preconditions.checkArgument(urgentTotalBefore == urgentTotalAfter, "触发规则之前的待确认紧急事件的条数：" + urgentTotalBefore + "  触发规则之后的待确认紧急事件的条数：" + urgentTotalAfter);
                Preconditions.checkArgument(leaveMarkAfterNum == leaveMarkBeforeNum + 1, "触发规则之前的留痕条数为的条数：" + leaveMarkBeforeNum + "  触发规则之后的留痕事件的条数：" + leaveMarkBeforeNum);
            }


        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("人员A触发制服规则，触发事件数+1，待确认事件数+1，待确认紧急待确认事件+1,此规则留痕数量、图片+1");
        }
    }

    /**
     * 构建场景--人员A触发口罩+工服，触发事件数+2，待确认事件数+2，待确认紧急待确认事件+1,此规则留痕数量、图片+1
     */
    @Test(description = "人员A触发口罩+工服，触发事件数+2，待确认事件数+2，待确认紧急待确认事件+1,此规则留痕数量、图片+1")
    public void storeDateCase6() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Boolean flagMask = true;
            Boolean flagUniform = true;
            int leaveMarkBeforeMashNum = 0;
            int leaveMarkBeforeUniformNum = 0;
            int idMash = 0;
            int idUniform = 0;
            //门店列表页面-触发规则之前
            IScene sceneShopBefore = EventTotalScene.builder().page(1).size(10).shopName(shopName).build();
            JSONArray shopBeforeList = sceneShopBefore.visitor(visitor).execute().getJSONArray("list");
            int eventTotalBefore = shopBeforeList.getJSONObject(0).getInteger("event_total");
            int processedTotalBefore = shopBeforeList.getJSONObject(0).getInteger("processed_total");
            int pendingTotalBefore = shopBeforeList.getJSONObject(0).getInteger("pending_total");
            int urgentTotalBefore = shopBeforeList.getJSONObject(0).getInteger("urgent_total");

            //门店事件页面
            IScene scene = ListScene.builder().page(1).size(10).shopId(shopId).build();
            JSONObject response = scene.visitor(visitor).execute();
            int pages = response.getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                JSONArray list = ListScene.builder().page(page).size(10).shopId(shopId).build().visitor(visitor).execute().getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    String triggerTime = list.getJSONObject(i).getString("trigger_time").substring(0, 13);
                    String triggerRule = list.getJSONObject(i).getString("trigger_rule");
                    String eventState = list.getJSONObject(i).getString("event_state");
                    //获取系统当前时间
                    String time = String.valueOf(dt.currentDateToTimestamp()).substring(0, 13);
                    //判断当前门店的列表中是当前小时是否有待处理的口罩事件
                    if (time.equals(triggerTime) && triggerRule.equals(TriggerRuleEnum.MASK_MONITOR.getTriggerRule()) && eventState.equals(EventStateEnum.WAITING_ALARM_CONFIRM.getEventState())) {
                        flagMask = false;
                        leaveMarkBeforeMashNum = list.getJSONObject(i).getInteger("leave_mark_num");
                        idMash = list.getJSONObject(i).getInteger("id");
                    }
                    if (time.equals(triggerTime) && triggerRule.equals(TriggerRuleEnum.UNIFORM_MONITOR.getTriggerRule()) && eventState.equals(EventStateEnum.WAITING_ALARM_CONFIRM.getEventState())) {
                        flagUniform = false;
                        leaveMarkBeforeUniformNum = list.getJSONObject(i).getInteger("leave_mark_num");
                        idUniform = list.getJSONObject(i).getInteger("id");
                    }
                }
            }
            //触发口罩+制服事件
            storeEventCase4();
            sleep(3);
            //门店列表页面-触发规则之后
            IScene sceneShopAfter = EventTotalScene.builder().page(1).size(10).shopName(shopName).build();
            JSONArray shopAfterList = sceneShopAfter.visitor(visitor).execute().getJSONArray("list");
            int eventTotalAfter = shopAfterList.getJSONObject(0).getInteger("event_total");
            int processedTotalAfter = shopAfterList.getJSONObject(0).getInteger("processed_total");
            int pendingTotalAfter = shopAfterList.getJSONObject(0).getInteger("pending_total");
            int urgentTotalAfter = shopAfterList.getJSONObject(0).getInteger("urgent_total");
            //当前图片的留痕张数--口罩
            int leaveMarkAfterMashNum = businessUtil.getLeaveMarkBeforeNum(shopId, idMash);
            //当前图片的留痕张数--制服
            int leaveMarkAfterUniformNum = businessUtil.getLeaveMarkBeforeNum(shopId, idUniform);

            //判断当前时间段是否触发规则&&规则师傅已处理
            if (flagMask.equals(true) && flagUniform.equals(true)) {
                System.out.println(flagUniform + "----1-----" + flagMask);
                Preconditions.checkArgument(eventTotalBefore == eventTotalAfter - 2, "触发规则之前的触发规则的条数：" + eventTotalBefore + "  触发规则之后的列触发规则条数：" + eventTotalAfter);
                Preconditions.checkArgument(processedTotalBefore == processedTotalAfter, "触发规则之前的告警已处理的的条数：" + processedTotalBefore + "  触发规则之后的告警已处理条数：" + processedTotalAfter);
                Preconditions.checkArgument(pendingTotalBefore == pendingTotalAfter - 2, "触发规则之前的告警待处理的的条数：" + pendingTotalBefore + "  触发规则之后的告警待处理条数：" + pendingTotalAfter);
                Preconditions.checkArgument(urgentTotalAfter == urgentTotalBefore + 1, "触发规则之前的待确认紧急事件的条数：" + urgentTotalBefore + "  触发规则之后的待确认紧急事件的条数：" + urgentTotalAfter);
                Preconditions.checkArgument(leaveMarkAfterMashNum == 1, "触发口罩规则之前的留痕条数为的条数：" + leaveMarkBeforeMashNum + "  触发口罩规则之后的留痕事件的条数：" + leaveMarkAfterMashNum);
                Preconditions.checkArgument(leaveMarkAfterUniformNum == 1, "触发制服规则之前的留痕条数为的条数：" + leaveMarkBeforeUniformNum + "  触发制服规则之后的留痕事件的条数：" + leaveMarkAfterUniformNum);

            } else if (flagMask.equals(false) && flagUniform.equals(true)) {
                System.out.println(flagUniform + "-----2----" + flagMask);
                Preconditions.checkArgument(eventTotalBefore == eventTotalAfter - 1, "触发规则之前的触发规则的条数：" + eventTotalBefore + "  触发规则之后的列触发规则条数：" + eventTotalAfter);
                Preconditions.checkArgument(processedTotalBefore == processedTotalAfter, "触发规则之前的告警已处理的的条数：" + processedTotalBefore + "  触发规则之后的告警已处理条数：" + processedTotalAfter);
                Preconditions.checkArgument(pendingTotalBefore == pendingTotalAfter - 1, "触发规则之前的告警待处理的的条数：" + pendingTotalBefore + "  触发规则之后的告警待处理条数：" + pendingTotalAfter);
                Preconditions.checkArgument(urgentTotalBefore == urgentTotalAfter, "触发规则之前的待确认紧急事件的条数：" + urgentTotalBefore + "  触发规则之后的待确认紧急事件的条数：" + urgentTotalAfter);
                Preconditions.checkArgument(leaveMarkAfterMashNum == leaveMarkBeforeMashNum + 1, "触发口罩规则之前的留痕条数为的条数：" + leaveMarkBeforeMashNum + "  触发口罩规则之后的留痕事件的条数：" + leaveMarkAfterMashNum);

            } else if (flagUniform.equals(false) && flagMask.equals(true)) {
                System.out.println(flagUniform + "-----3----" + flagMask);
                Preconditions.checkArgument(eventTotalBefore == eventTotalAfter - 1, "触发规则之前的触发规则的条数：" + eventTotalBefore + "  触发规则之后的列触发规则条数：" + eventTotalAfter);
                Preconditions.checkArgument(processedTotalBefore == processedTotalAfter, "触发规则之前的告警已处理的的条数：" + processedTotalBefore + "  触发规则之后的告警已处理条数：" + processedTotalAfter);
                Preconditions.checkArgument(pendingTotalBefore == pendingTotalAfter - 1, "触发规则之前的告警待处理的的条数：" + pendingTotalBefore + "  触发规则之后的告警待处理条数：" + pendingTotalAfter);
                Preconditions.checkArgument(urgentTotalBefore == urgentTotalAfter - 1, "触发规则之前的待确认紧急事件的条数：" + urgentTotalBefore + "  触发规则之后的待确认紧急事件的条数：" + urgentTotalAfter);
                Preconditions.checkArgument(leaveMarkAfterUniformNum == leaveMarkBeforeUniformNum + 1, "触发制服规则之前的留痕条数为的条数：" + leaveMarkBeforeUniformNum + "  触发制服规则之后的留痕事件的条数：" + leaveMarkAfterUniformNum);

            } else {
                System.out.println(flagUniform + "-----4----" + flagMask);
                Preconditions.checkArgument(eventTotalBefore == eventTotalAfter, "触发规则之前的触发规则的条数：" + eventTotalBefore + "  触发规则之后的列触发规则条数：" + eventTotalAfter);
                Preconditions.checkArgument(processedTotalBefore == processedTotalAfter, "触发规则之前的告警已处理的的条数：" + processedTotalBefore + "  触发规则之后的告警已处理条数：" + processedTotalAfter);
                Preconditions.checkArgument(pendingTotalBefore == pendingTotalAfter, "触发规则之前的告警待处理的的条数：" + pendingTotalBefore + "  触发规则之后的告警待处理条数：" + pendingTotalAfter);
                Preconditions.checkArgument(urgentTotalBefore == urgentTotalAfter, "触发规则之前的待确认紧急事件的条数：" + urgentTotalBefore + "  触发规则之后的待确认紧急事件的条数：" + urgentTotalAfter);
                Preconditions.checkArgument(leaveMarkAfterUniformNum == leaveMarkBeforeUniformNum + 1, "触发规则之前的留痕条数为的条数：" + leaveMarkBeforeUniformNum + "  触发规则之后的留痕事件的条数：" + leaveMarkAfterUniformNum);
                Preconditions.checkArgument(leaveMarkAfterMashNum == leaveMarkBeforeMashNum + 1, "触发规则之前的留痕条数为的条数：" + leaveMarkBeforeMashNum + "  触发规则之后的留痕事件的条数：" + leaveMarkAfterMashNum);

            }
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("人员A触发口罩+工服，触发事件数+2，待确认事件数+2，待确认紧急待确认事件+1,此规则留痕数量、图片+1");
        }
    }

    /**
     * 构建场景--人员A触发帽子规则，触发事件数+1，待确认事件数+1，待确认紧急待确认事件+1,此规则留痕数量、图片+1
     */
    @Test(description = "人员A触发帽子规则，触发事件数+1，待确认事件数+1，待确认紧急待确认事件+1,此规则留痕数量、图片+1", enabled = false)
    public void storeDateCase7() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Boolean flag = true;
            int leaveMarkBeforeNum = 0;
            int id = 0;
            //门店列表页面-触发规则之前
            IScene sceneShopBefore = EventTotalScene.builder().page(1).size(10).shopName(shopName).build();
            JSONArray shopBeforeList = sceneShopBefore.visitor(visitor).execute().getJSONArray("list");
            int eventTotalBefore = shopBeforeList.getJSONObject(0).getInteger("event_total");
            int processedTotalBefore = shopBeforeList.getJSONObject(0).getInteger("processed_total");
            int pendingTotalBefore = shopBeforeList.getJSONObject(0).getInteger("pending_total");
            int urgentTotalBefore = shopBeforeList.getJSONObject(0).getInteger("urgent_total");

            //门店事件页面
            IScene scene = ListScene.builder().page(1).size(10).shopId(shopId).build();
            JSONObject response = scene.visitor(visitor).execute();
            int pages = response.getInteger("pages");
            for (int page = 1; page <= pages; page++) {
                JSONArray list = ListScene.builder().page(page).size(10).shopId(shopId).build().visitor(visitor).execute().getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    String triggerTime = list.getJSONObject(i).getString("trigger_time").substring(0, 13);
                    String triggerRule = list.getJSONObject(i).getString("trigger_rule");
                    String eventState = list.getJSONObject(i).getString("event_state");
                    //获取系统当前时间
                    String time = String.valueOf(dt.currentDateToTimestamp()).substring(0, 13);
//                    System.err.println(triggerTime+"    "+ time);
                    //判断当前门店的列表中是当前小时是否有待处理的口罩事件
                    if (time.equals(triggerTime) && triggerRule.equals(TriggerRuleEnum.HAT_MONITOR.getTriggerRule()) && eventState.equals(EventStateEnum.WAITING_ALARM_CONFIRM.getEventState())) {
                        flag = false;
                        leaveMarkBeforeNum = list.getJSONObject(i).getInteger("leave_mark_num");
                        id = list.getJSONObject(i).getInteger("id");
                    }
                }
            }
            System.err.println("flag:" + flag);
            //触发帽子规则
            storeEventCase3();
            sleep(3);
            //门店列表页面-触发规则之后
            IScene sceneShopAfter = EventTotalScene.builder().page(1).size(10).shopName(shopName).build();
            JSONArray shopAfterList = sceneShopAfter.visitor(visitor).execute().getJSONArray("list");
            int eventTotalAfter = shopAfterList.getJSONObject(0).getInteger("event_total");
            int processedTotalAfter = shopAfterList.getJSONObject(0).getInteger("processed_total");
            int pendingTotalAfter = shopAfterList.getJSONObject(0).getInteger("pending_total");
            int urgentTotalAfter = shopAfterList.getJSONObject(0).getInteger("urgent_total");
            //当前图片的留痕张数
            int leaveMarkAfterNum = businessUtil.getLeaveMarkBeforeNum(shopId, id);

            //判断当前时间段是否触发规则&&规则师傅已处理
            if (flag) {
                System.out.println("---------" + flag);
                Preconditions.checkArgument(eventTotalBefore == eventTotalAfter - 1, "触发规则之前的触发规则的条数：" + eventTotalBefore + "  触发规则之后的列触发规则条数：" + eventTotalAfter);
                Preconditions.checkArgument(processedTotalBefore == processedTotalAfter, "触发规则之前的告警已处理的的条数：" + processedTotalBefore + "  触发规则之后的告警已处理条数：" + processedTotalAfter);
                Preconditions.checkArgument(pendingTotalBefore == pendingTotalAfter - 1, "触发规则之前的告警待处理的的条数：" + pendingTotalBefore + "  触发规则之后的告警待处理条数：" + pendingTotalAfter);
                Preconditions.checkArgument(urgentTotalBefore == urgentTotalAfter, "触发规则之前的待确认紧急事件的条数：" + urgentTotalBefore + "  触发规则之后的待确认紧急事件的条数：" + urgentTotalAfter);
                Preconditions.checkArgument(leaveMarkAfterNum == 1, "触发规则之前的留痕条数为的条数：" + leaveMarkBeforeNum + "  触发规则之后的留痕事件的条数：" + leaveMarkBeforeNum);

            } else {

                Preconditions.checkArgument(eventTotalBefore == eventTotalAfter, "触发规则之前的触发规则的条数：" + eventTotalBefore + "  触发规则之后的列触发规则条数：" + eventTotalAfter);
                Preconditions.checkArgument(processedTotalBefore == processedTotalAfter, "触发规则之前的告警已处理的的条数：" + processedTotalBefore + "  触发规则之后的告警已处理条数：" + processedTotalAfter);
                Preconditions.checkArgument(pendingTotalBefore == pendingTotalAfter, "触发规则之前的告警待处理的的条数：" + pendingTotalBefore + "  触发规则之后的告警待处理条数：" + pendingTotalAfter);
                Preconditions.checkArgument(urgentTotalBefore == urgentTotalAfter, "触发规则之前的待确认紧急事件的条数：" + urgentTotalBefore + "  触发规则之后的待确认紧急事件的条数：" + urgentTotalAfter);
                Preconditions.checkArgument(leaveMarkAfterNum == leaveMarkBeforeNum + 1, "触发规则之前的留痕条数为的条数：" + leaveMarkBeforeNum + "  触发规则之后的留痕事件的条数：" + leaveMarkBeforeNum);

            }

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("人员A触发帽子规则，触发事件数+1，待确认事件数+1，待确认紧急待确认事件+1,此规则留痕数量、图片+1");
        }
    }

    /**
     * 待确认事件被确认，已确认事件+1，待确认事件-1
     */
    @Test(description = "待确认事件被确认，已确认事件+1，待确认事件-1")
    public void storeDateCase8() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取待处理的规则
            long eventId = businessUtil.waitingAlarmConfirmOnline(shopId).get(0);
            sleep(3);
            //门店列表页面-确认规则之前
            IScene sceneShopBefore = EventTotalScene.builder().page(1).size(10).shopName(shopName).build();
            JSONArray shopBeforeList = sceneShopBefore.visitor(visitor).execute().getJSONArray("list");
            int processedTotalBefore = shopBeforeList.getJSONObject(0).getInteger("processed_total");
            int pendingTotalBefore = shopBeforeList.getJSONObject(0).getInteger("pending_total");
            //确认规则
            int code = businessUtil.checkRiskAlarmHandle(eventId, handleStatus.ALARM_CONFIRMED.getHandleState());
            System.out.println("code:" + code);
            //门店列表页面-确认规则之后
            IScene sceneShopAfter = EventTotalScene.builder().page(1).size(10).shopName(shopName).build();
            JSONArray shopAfterList = sceneShopAfter.visitor(visitor).execute().getJSONArray("list");
            int processedTotalAfter = shopAfterList.getJSONObject(0).getInteger("processed_total");
            int pendingTotalAfter = shopAfterList.getJSONObject(0).getInteger("pending_total");

            Preconditions.checkArgument(processedTotalBefore == processedTotalAfter - 1, "确认规则前的已确认的事件：" + processedTotalBefore + "  确认规则后的已确认的事件：" + processedTotalAfter);
            Preconditions.checkArgument(pendingTotalBefore == pendingTotalAfter + 1, "确认规则前的待确认的事件：" + pendingTotalBefore + "  确认规则后的待确认的事件：" + pendingTotalAfter);


        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("待确认事件被确认，已确认事件+1，待确认事件-1");
        }
    }

    /**
     * 待确认紧急事件被确认，已确认事件+1，待确认事件-1,待确认紧急事件-1
     */
    @Test(description = "待确认紧急事件被确认，已确认事件+1，待确认事件-1,待确认紧急事件-1")
    public void storeDateCase9() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取待处理的规则
            long eventId = businessUtil.waitingUrgentAlarmConfirmOnline(shopId).get(0);
            //门店列表页面-确认规则之前
            IScene sceneShopBefore = EventTotalScene.builder().page(1).size(10).shopName(shopName).build();
            JSONArray shopBeforeList = sceneShopBefore.visitor(visitor).execute().getJSONArray("list");
            int processedTotalBefore = shopBeforeList.getJSONObject(0).getInteger("processed_total");
            int pendingTotalBefore = shopBeforeList.getJSONObject(0).getInteger("pending_total");
            int urgentTotalBefore = shopBeforeList.getJSONObject(0).getInteger("urgent_total");

            //确认规则
            int code = businessUtil.checkRiskAlarmHandle(eventId, handleStatus.ALARM_CONFIRMED.getHandleState());
            System.out.println("code:" + code);
            //门店列表页面-确认规则之后
            IScene sceneShopAfter = EventTotalScene.builder().page(1).size(10).shopName(shopName).build();
            JSONArray shopAfterList = sceneShopAfter.visitor(visitor).execute().getJSONArray("list");
            int processedTotalAfter = shopAfterList.getJSONObject(0).getInteger("processed_total");
            int pendingTotalAfter = shopAfterList.getJSONObject(0).getInteger("pending_total");
            int urgentTotalAfter = shopAfterList.getJSONObject(0).getInteger("urgent_total");

            Preconditions.checkArgument(processedTotalBefore == processedTotalAfter - 1, "确认规则前的已确认的事件：" + processedTotalBefore + "  确认规则后的已确认的事件：" + processedTotalAfter);
            Preconditions.checkArgument(pendingTotalBefore == pendingTotalAfter + 1, "确认规则前的待确认的事件：" + pendingTotalBefore + "  确认规则后的待确认的事件：" + pendingTotalAfter);
            Preconditions.checkArgument(urgentTotalBefore == urgentTotalAfter + 1, "确认规则前的紧急待确认的事件：" + pendingTotalBefore + "  确认规则后的紧急待确认的事件：" + pendingTotalAfter);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("待确认紧急事件被确认，已确认事件+1，待确认事件-1,待确认紧急事件-1");
        }
    }

    /**
     * 待确认事件被已取消，已确认事件+1，待确认事件-1
     */
    @Test(description = "待确认事件被已取消，已确认事件+1，待确认事件-1")
    public void storeDateCase10() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取待处理的规则
            long eventId = businessUtil.waitingAlarmConfirmOnline(shopId).get(0);
            sleep(3);
            //门店列表页面-确认规则之前
            IScene sceneShopBefore = EventTotalScene.builder().page(1).size(10).shopName(shopName).build();
            JSONArray shopBeforeList = sceneShopBefore.visitor(visitor).execute().getJSONArray("list");
            int processedTotalBefore = shopBeforeList.getJSONObject(0).getInteger("processed_total");
            int pendingTotalBefore = shopBeforeList.getJSONObject(0).getInteger("pending_total");
            //取消规则
            int code = businessUtil.checkRiskAlarmHandle(eventId, handleStatus.NO_NEED_HANDLE.getHandleState());
            System.out.println("code:" + code);
            //门店列表页面-确认规则之后
            IScene sceneShopAfter = EventTotalScene.builder().page(1).size(10).shopName(shopName).build();
            JSONArray shopAfterList = sceneShopAfter.visitor(visitor).execute().getJSONArray("list");
            int processedTotalAfter = shopAfterList.getJSONObject(0).getInteger("processed_total");
            int pendingTotalAfter = shopAfterList.getJSONObject(0).getInteger("pending_total");

            Preconditions.checkArgument(processedTotalBefore == processedTotalAfter - 1, "取消规则前的已确认的事件：" + processedTotalBefore + "  取消规则后的已确认的事件：" + processedTotalAfter);
            Preconditions.checkArgument(pendingTotalBefore == pendingTotalAfter + 1, "取消规则前的待确认的事件：" + pendingTotalBefore + "  取消规则后的待确认的事件：" + pendingTotalAfter);


        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("待确认事件被已取消，已确认事件+1，待确认事件-1");
        }
    }


    /**
     * 待确认紧急事件被取消，已确认事件+1，待确认事件-1,待确认紧急事件-1
     */
    @Test(description = "待确认紧急事件被取消，已确认事件+1，待确认事件-1,待确认紧急事件-1")
    public void storeDateCase11() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取待处理的规则
            long eventId = businessUtil.waitingUrgentAlarmConfirmOnline(shopId).get(0);
            sleep(3);
            //门店列表页面-确认规则之前
            IScene sceneShopBefore = EventTotalScene.builder().page(1).size(10).shopName(shopName).build();
            JSONArray shopBeforeList = sceneShopBefore.visitor(visitor).execute().getJSONArray("list");
            int processedTotalBefore = shopBeforeList.getJSONObject(0).getInteger("processed_total");
            int pendingTotalBefore = shopBeforeList.getJSONObject(0).getInteger("pending_total");
            int urgentTotalBefore = shopBeforeList.getJSONObject(0).getInteger("urgent_total");

            //取消规则
            int code = businessUtil.checkRiskAlarmHandle(eventId, handleStatus.NO_NEED_HANDLE.getHandleState());
            System.out.println("code:" + code);
            //门店列表页面-确认规则之后
            IScene sceneShopAfter = EventTotalScene.builder().page(1).size(10).shopName(shopName).build();
            JSONArray shopAfterList = sceneShopAfter.visitor(visitor).execute().getJSONArray("list");
            int processedTotalAfter = shopAfterList.getJSONObject(0).getInteger("processed_total");
            int pendingTotalAfter = shopAfterList.getJSONObject(0).getInteger("pending_total");
            int urgentTotalAfter = shopAfterList.getJSONObject(0).getInteger("urgent_total");

            Preconditions.checkArgument(processedTotalBefore == processedTotalAfter - 1, "取消规则前的已确认的事件：" + processedTotalBefore + "  取消规则后的已确认的事件：" + processedTotalAfter);
            Preconditions.checkArgument(pendingTotalBefore == pendingTotalAfter + 1, "取消规则前的待确认的事件：" + pendingTotalBefore + "  取消规则后的待确认的事件：" + pendingTotalAfter);
            Preconditions.checkArgument(urgentTotalBefore == urgentTotalAfter + 1, "取消规则前的紧急待确认的事件：" + pendingTotalBefore + "  取消规则后的紧急待确认的事件：" + pendingTotalAfter);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("待确认紧急事件被取消，已确认事件+1，待确认事件-1,待确认紧急事件-1");
        }
    }


    /**
     * 单人触发口罩事件--中关村1号店(可更门店的shopId更改门店)-验证数据层的逻辑-图片触发不带口罩的规则
     */
    @Test(description = "单人触发口罩事件")
    public void storeEventCase1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
//            //上传不带口罩的图片，触发口罩事件
//            String path = "src\\main\\java\\com\\haisheng\\framework\\testng\\bigScreen\\itemXundian\\caseonline\\zt\\mainOnline.py";
//            String picPath = "src\\main\\java\\com\\haisheng\\framework\\testng\\bigScreen\\itemXundian\\common\\multimedia\\picture\\没有戴口罩.jpg";
//            mi.getPy(path, picPath);

            JSONObject response = su.maskEventOnline(shopId, false, "customerFalse", true);
            int code = response.getInteger("code");
            Preconditions.checkArgument(code == 1000, "口罩事件触发失败，code的返回值为：" + code);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("单人触发口罩事件");
        }
    }

    /**
     * 单人触发制服事件--巡店测试门店1(可更门店的shopId更改门店)
     */
    @Test(description = "单人触发制服事件")
    public void storeEventCase2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //上传不穿制服的图片，触发口罩事件
//            String path = "src\\main\\java\\com\\haisheng\\framework\\testng\\bigScreen\\itemXundian\\caseonline\\zt\\mainOnline.py";
//            String picPath = "src\\main\\java\\com\\haisheng\\framework\\testng\\bigScreen\\itemXundian\\common\\multimedia\\picture\\多人戴口罩没有穿制服.jpg";
//            mi.getPy(path, picPath);

            JSONObject response = su.maskEventOnline(shopId, true, "customer", true);
            int code = response.getInteger("code");
            Preconditions.checkArgument(code == 1000, "制服事件触发失败，code的返回值为：" + code);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("单人触发制服事件");
        }
    }


    /**
     * 单人触发帽子事件--巡店测试门店1(可更门店的shopId更改门店)
     */
    @Test(description = "单人触发帽子事件")
    public void storeEventCase3() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject response = su.maskEventOnline(shopId, true, "customer1", false);
            int code = response.getInteger("code");
            Preconditions.checkArgument(code == 1000, "帽子事件触发失败，code的返回值为：" + code);
        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("单人触发帽子事件");
        }
    }

    /**
     * 同时触发制服和口罩事件--(可更门店的shopId更改门店)
     */
    @Test(description = "同时触发制服和口罩事件", enabled = false)
    public void storeEventCase4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //上传不带口罩不穿制服的图片，触发口罩制服两个事件
            String path = "src\\main\\java\\com\\haisheng\\framework\\testng\\bigScreen\\itemXundian\\caseonline\\zt\\mainOnline.py";
            String picPath = "src\\main\\java\\com\\haisheng\\framework\\testng\\bigScreen\\itemXundian\\common\\multimedia\\picture\\口罩帽子同时触发.jpg";
            mi.getPy(path, picPath);

//            JSONObject response=su.maskEventOnline(shopId,true,"customer",true);
//            int code=response.getInteger("code");
//            Preconditions.checkArgument(code==1000,"制服事件触发失败，code的返回值为："+code);

        } catch (AssertionError | Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("同时触发制服和口罩事件");
        }
    }


}