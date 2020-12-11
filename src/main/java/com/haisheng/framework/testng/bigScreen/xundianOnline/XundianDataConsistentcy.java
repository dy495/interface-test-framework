package com.haisheng.framework.testng.bigScreen.xundianOnline;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.xundianOnline.StoreScenarioUtilOnline;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.CommonUtil;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;

import static com.google.common.base.Preconditions.checkArgument;

/**
 *
 */

public class XundianDataConsistentcy extends TestCaseCommon implements TestCaseStd {

    XundianScenarioUtilOnline xd = XundianScenarioUtilOnline.getInstance();
    long shop_id = 4116;
    String xjy4="uid_663ad653";
    int page=1;
    int size=50;
    String cycle_type = "RECENT_THIRTY";
    String filepath = "src/main/java/com/haisheng/framework/testng/bigScreen/xundianDaily/64.txt";

    public String texFile(String fileName) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(fileName));
        String str;
        str = in.readLine();
        return str;
    }





    /**
     * @description: initial test class level config, such as appid/uid/ak/dinghook/push_rd_name
     *
     */
    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before classs initial");
        CommonConfig commonConfig = new CommonConfig();


        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_XUNDIAN_DAILY_SERVICE;
        commonConfig.checklistQaOwner = "青青";


        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "xundian-daily-test");

        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, "巡店 日常");


        commonConfig.dingHook = DingWebhook.DAILY_STORE_MANAGEMENT_PLATFORM_GRP;
        commonConfig.pushRd = new String[]{"13581630214","15084928847"};
        //13436941018 吕雪晴
        //17610248107 廖祥茹
        //15084928847 黄青青
        //13581630214 马琨
        //18513118484 杨航
        //13259979249 黄鑫
        //18672733045 高凯
        //15898182672 华成裕
        //18810332354 刘峤


        commonConfig.shopId = getXundianShop(); //要改！！！
        beforeClassInit(commonConfig);

        logger.debug("xundian " + xd);

        xd.login("yuexiu@test.com","f5b3e737510f31b88eb2d4b5d0cd2fb4");


    }

    @AfterClass
    @Override
    public void clean() {
        afterClassClean();
    }

    /**
     * @description: get a fresh case ds to save case result, such as result/response
     *
     */
    @BeforeMethod
    @Override
    public void createFreshCase(Method method) {
        logger.debug("beforeMethod");
        caseResult = getFreshCaseResult(method);
        logger.debug("case: " + caseResult);
    }

    /**
     *
     * ====================pc门店列表中门店=app巡店中心中门店======================
     * */
    // @Test
    public void getTaskDetail() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack=false;
        try {
            int page=1;
            int size=10;
            int shops=xd.taskDetail().getInteger("shops");
            int shoptotals=xd.ShopPage(page,size).getInteger("total");

            Preconditions.checkArgument(shops==shoptotals,"门店列表中得门店数=" + shoptotals + "不等于巡店中心中的门店数=" + shops);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("pc门店列表中门店=app巡店中心中门店");
        }
    }



    /**
     *
     * ====================巡店中心各个门店的巡店次数=巡店详情中的巡店条数======================
     * */
    //@Test
    public void StoreCheckDataComparison() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack=false;
        try {
            //获取巡店中心列表中的巡店次数
            JSONArray check_list= xd.ShopPage(page,size).getJSONArray("list");
            int patrol_num=check_list.getJSONObject(0).getInteger("patrol_num");

            int shop_id = check_list.getJSONObject(0).getInteger("id");
            //获取巡店详情中的巡店条数
            int total = xd.shopChecksPage(page,size,shop_id).getInteger("total");

            Preconditions.checkArgument(patrol_num == total,"巡店中心各个门店的巡店次数" + patrol_num + "不等于巡店详情中的巡店条数=" + total);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("巡店中心各个门店的巡店次数=巡店详情中的巡店条数");
        }
    }

    /**
     *
     * ====================巡店记录的巡店详情中的巡店结果中的总项数=执行清单中的总项数======================
     * */
    //@Test
    public void TotalOfResultsComparison() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack=false;
        try {
            //获取shop_id
            JSONArray check_list= xd.ShopPage(page,size).getJSONArray("list");
            int patrol_num=check_list.getJSONObject(0).getInteger("patrol_num");
            int shop_id = check_list.getJSONObject(0).getInteger("id");
            //获取巡店记录id
            JSONArray detailList=xd.shopChecksPage(page,size,shop_id).getJSONArray("list");
            int id = detailList.getJSONObject(0).getInteger("id");

            int inappropriate_num = xd.shopChecksDetail(id,shop_id).getInteger("inappropriate_num");
            int qualified_num = xd.shopChecksDetail(id,shop_id).getInteger("qualified_num");
            int unqualified_num = xd.shopChecksDetail(id,shop_id).getInteger("unqualified_num");
            //不适用项+不合格项+合格项=最终结果项
            int rusultsNum=inappropriate_num + qualified_num + unqualified_num;

            //获取执行清单中的总项数
            int sum = 0;
            JSONArray checklists = xd.shopChecksDetail(id,shop_id).getJSONArray("check_lists");
            int size2 = checklists.size();
            for (int i = 0;i < size2; i++){
                JSONObject jsonObject = checklists.getJSONObject(i);
                if (jsonObject !=null){
                    sum += jsonObject.getInteger("total");
                }
            }
            Preconditions.checkArgument(rusultsNum == sum,"巡店记录的巡店详情中的巡店结果中的总项数" + rusultsNum + "不等于执行清单中的总项数=" + sum);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("巡店记录的巡店详情中的巡店结果中的总项数=执行清单中的总项数");
        }
    }


    /**
     *
     * ====================修改巡店清单后，门店基本信息中变为修改之后的======================
     * */
    //@Test
    public void  changeCheckListNoComparison() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack=false;
        try {
            //获取执行清单的id
            JSONArray list= xd.checklistPage(page,size).getJSONArray("list");
            long id = list.getJSONObject(0).getInteger("id");
            int startM=2;
            String name= dt.getHHmm(startM)+"qingqing";
            String desc = list.getJSONObject(0).getString("desc");
            JSONArray  items=new JSONArray();//new一个数组
            JSONObject jsonObject = new JSONObject();//数组里面是JSONObject
            jsonObject.put("order",0);
            jsonObject.put("title","我是青青第一项");
            jsonObject.put("comment","要怎么检查啊啊啊啊啊啊啊");
            items.add(0,jsonObject);
            JSONArray  shoplist=new JSONArray();
            shoplist.add(0,28760);
            //对一个执行清单进行编辑
            xd.checkListEdit( id,name,desc,items,shoplist);
            //查看门店基本详情
            long id2 = 28760;
            JSONArray check_lists=xd.shopDetail(id2).getJSONArray("check_lists");
            int size3 = check_lists.size();
            boolean check = false;
            for(int i = 0;i < size3;i++){
                String string = check_lists.getString(i);
                if (string.equals(name)){
                    check = true;
                }
            }
            Preconditions.checkArgument(check,"修改巡店清单后，门店基本信息中没有变为修改之后的");
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("修改巡店清单后，门店基本信息中变为修改之后的");
        }
    }



    /**
     *
     * ====================某定检任务中的发送设置=对应巡店员的待办事项中的定检任务数===================
     * */
    //@Test
    public void  CheckTaskNoDataComparison() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack=false;
        try {
            //第一次获取待办事项中的SCHEDULE_TASK值-count1
            Integer type = 0;
            Long last_id = null;
            JSONArray thingsList = xd.MTaskList(type,size,last_id).getJSONArray("list");//这里得到一个[] array array 里面是object{}
            int theSize1 = thingsList.size();
            int count1 = 0;
            for(int i = 0;i < theSize1;i++) {
                JSONObject jsonObject = thingsList.getJSONObject(i);
                String task_type = jsonObject.getString("task_type"); // .var
                String name1 = jsonObject.getString("name");//这里得到的就是任务的名字
                //这里是计算SCHEDULE_TASK 出现的次数
                if (task_type != null && task_type.equals("SCHEDULE_TASK")) {
                    count1 ++;
                }
            }

            //新建一个定检任务
            String name="qingqingtest001";
            String cycle="WEEK";
            JSONArray  jal=new JSONArray();
            jal.add(0,"MON");
            jal.add(0,"TUES");
            int startM=8;
            String send_time= dt.getHHmm(startM);//获取当前时间
            String valid_start=dt.getHistoryDate(0); //今天日期;
            String valid_end=dt.getHistoryDate(startM); //今天日期;;
            JSONArray  shoplist=new JSONArray();
//            shoplist.add(0,28758);
            shoplist.add(0,28760);
            xd.scheduleCheckAdd(name,cycle,jal,send_time,valid_start,valid_end,xjy4,shoplist);


            //新建一个定检任务以后，再次去获取待办事项列表
            JSONArray thingsLists = xd.MTaskList(type,size,last_id).getJSONArray("list");//这里得到一个[] array array 里面是object{}
            int theSize = thingsLists.size();
            int count = 0;
            int counts=count -1;//已生成定检任务后的待办事项中定检任务数-1=未生成定检任务前的待办事项中定检任务数
            boolean newTask = false; //标记是否添加成功了任务
            for(int i = 0;i < theSize;i++) {
                JSONObject jsonObject = thingsLists.getJSONObject(i);
                String task_type = jsonObject.getString("task_type"); // .var
                String name1 = jsonObject.getString("name");//这里得到的就是任务的名字
                //这里是计算SCHEDULE_TASK 出现的次数
                if (task_type != null && task_type.equals("SCHEDULE_TASK")) {
                    count++;
                }
            }
            Preconditions.checkArgument(count1 == count,"未生成定检任务前的待办事项中定检任务数=" + count1 + "不等于已生成定检任务后的待办事项中定检任务数-1=" + counts);

            //获取定检任务列表的第一个定检任务的id(也是task_id)
            JSONArray ScheckList = xd.scheduleCheckPage(page, size).getJSONArray("list");
            long task_id = ScheckList.getJSONObject(0).getInteger("id");

            //删除新建的定检任务
            xd.scheduleCheckDelete(task_id);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("某定检任务中的发送设置=对应巡店员的待办事项中的定检任务数");
        }
    }
    /*********************************************************门店4.0**********************************************/




    /**
     * ====================今日巡店覆盖率==【巡店报告中心】今日巡店门店数/门店总数======================
     */
    @Test
    public void today_coverage() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取巡店报告中心列表里面报告的提交时间,
            JSONArray reportList = xd.xd_report_list("","","","",null,page,size).getJSONArray("list");
            String today= dt.getHistoryDate(0);//YYYY-MM-DD
            int count=0;
            ArrayList<Long> list = new ArrayList<>();
            for(int i=0;i<reportList.size();i++){
                String reportTime = reportList.getJSONObject(i).getString("report_submit_time").substring(0, 10);
                long shop_id = reportList.getJSONObject(i).getLong("shop_id");

                //获取列表报告的提交时间，看是否为今天提交的报告
                if(reportTime.equals(today)){
                    if( !list.contains(shop_id)){
                        count++;
                        list.add(shop_id);
                    }
                }else {
                    break;
                }
            }
            //获取该账号下巡店中心列表中的门店数量
            int total = xd.ShopPage(page,size).getInteger("total");

            //根据今日巡店报告数和权限下门店总数得到今日覆盖率
            String ss=  CommonUtil.getPercent(count,total,4);
            String today_coverage =  ss.replace("%","");

            //从巡店分析巡店概况中获取今日巡店覆盖率
            String coverage_rate = xd.xd_analysis_data().getString("today_patrol_coverage_rate");

            checkArgument(coverage_rate.equals(today_coverage), "【巡店分析】今日巡店覆盖率!=【巡店报告中心】今日巡店门店数/门店总数" + coverage_rate +"!=" +today_coverage);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("【巡店分析】今日巡店覆盖率==【巡店报告中心】今日巡店门店数/门店总数");
        }

    }

    /**
     * ====================昨日巡店覆盖率==【巡店报告中心】昨日现有门店的巡店门店数/现有门店总数======================
     */
    @Test
    public void yesterday_coverage() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取巡店报告中心列表里面报告的提交时间,
            JSONArray reportList = xd.xd_report_list("","","","",null,page,size).getJSONArray("list");
            String yesterday= dt.getHistoryDate(-1);//YYYY-MM-DD
            int count=0;
            ArrayList<Long> list = new ArrayList<>();
            for(int i=0;i<reportList.size();i++){
                String reportTime = reportList.getJSONObject(i).getString("report_submit_time").substring(0, 10);
                long shop_id = reportList.getJSONObject(i).getLong("shop_id");
                //获取列表报告的提交时间，看是否为今天提交的报告
                if(reportTime.equals(yesterday)){
                    if( !list.contains(shop_id)){
                        count++;
                        list.add(shop_id);
                    }
                }
            }
            //获取该账号下巡店中心列表中的门店数量
            int total = xd.ShopPage(page,size).getInteger("total");
            //根据昨日巡店报告数和权限下门店总数得到昨日覆盖率
            String ss=  CommonUtil.getPercent(count,total,4);
            String today_coverage =  ss.replace("%","");
            //从巡店分析巡店概况中获取昨日巡店覆盖率
            String coverage_rate = xd.xd_analysis_data().getString("yesterday_patrol_coverage_rate");

            checkArgument(coverage_rate.equals(today_coverage), "【巡店分析】】昨日巡店覆盖率 !=【巡店报告中心】昨日现有门店的巡店门店数/现有门店总数" + coverage_rate +"!=" +today_coverage);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("【巡店分析】昨日巡店覆盖率==【巡店报告中心】昨日现有门店的巡店门店数/现有门店总数");
        }

    }

    /**
     * ====================今日巡检门店的数量==【巡店报告中心】今天提交了巡检报告的门店去重数量======================
     */
    @Test
    public void riskRuleAdd() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取巡店报告中心列表里面报告的提交时间,
            JSONArray reportList = xd.xd_report_list("","","","",null,page,size).getJSONArray("list");
            String today= dt.getHistoryDate(0);//YYYY-MM-DD
            int count=0;
            ArrayList<Long> list = new ArrayList<>();
            for(int i=0;i<reportList.size();i++){
                String reportTime = reportList.getJSONObject(i).getString("report_submit_time").substring(0, 10);
                long shop_id = reportList.getJSONObject(i).getLong("shop_id");
                //获取列表报告的提交时间，看是否为今天提交的报告
                if(reportTime.equals(today) ){
                    if(!list.contains(shop_id)){
                        count++;
                        list.add(shop_id);
                    }

                }else {
                    break;
                }
            }

            //从巡店分析巡店概况中获取今日巡店的门店数量
            int todayStore_total = xd.xd_analysis_data().getInteger("today_patrol_shop_number");

            checkArgument(todayStore_total == count, "【巡店分析】今日巡检门店的数量!=【巡店报告中心】今天提交了巡检报告的门店去重数量" + todayStore_total +"!=" +count);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("【巡店分析】今日巡检门店的数量==【巡店报告中心】今天提交了巡检报告的门店去重数量");
        }

    }


    /**
     * ====================今日巡检人数==【巡店报告中心】今天巡检的账号数======================
     */
    @Test
    public void today_checker() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取巡店报告中心列表里面报告的提交时间,
            JSONArray reportList = xd.xd_report_list("","","","",null,page,size).getJSONArray("list");
            String today= dt.getHistoryDate(0);//YYYY-MM-DD
            int count=0;
            ArrayList<String> list = new ArrayList<>();
            for(int i=0;i<reportList.size();i++){
                String reportTime = reportList.getJSONObject(i).getString("report_submit_time").substring(0, 10);
                //获取执行人的姓名
                String person_name = reportList.getJSONObject(i).getString("patrol_person_name");
                //获取列表报告的提交时间，看是否为今天提交的报告
                if(reportTime.equals(today)){
                    if(!list.contains(person_name)){
                        count++;
                        list.add(person_name);
                    }
                }else {
                    break;
                }
            }

            //从巡店分析巡店概况中获取今日巡店巡检人数
            int todaychecker_total = xd.xd_analysis_data().getInteger("today_patrol_person_number");

            checkArgument(todaychecker_total == count, "【巡店分析】今日巡检门店的数量!=【巡店报告中心】今天提交了巡检报告的门店去重数量" + todaychecker_total +"!=" +count);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("【巡店分析】今日巡检人数==【巡店报告中心】今天巡检的账号数");
        }

    }

    /**
     * ====================昨日巡检人数==【巡店报告中心】昨日巡检的账号数======================
     */
    @Test
    public void ystday_checker() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取巡店报告中心列表里面报告的提交时间,
            JSONArray reportList = xd.xd_report_list("","","","",null,page,size).getJSONArray("list");
            String ystoday= dt.getHistoryDate(-1);//YYYY-MM-DD
            int count=0;
            ArrayList<String> list = new ArrayList<>();
            for(int i=0;i<reportList.size();i++){
                String reportTime = reportList.getJSONObject(i).getString("report_submit_time").substring(0, 10);
                //获取执行人的姓名
                String person_name = reportList.getJSONObject(i).getString("patrol_person_name");
                //获取列表报告的提交时间，看是否为今天提交的报告
                if(reportTime.equals(ystoday)){
                    if(!list.contains(person_name)){
                        count++;
                        list.add(person_name);
                    }
                }
            }
            //从巡店分析巡店概况中获取昨日巡店巡检人数
            int yetdaychecker_total = xd.xd_analysis_data().getInteger("yesterday_patrol_person_number");

            checkArgument(yetdaychecker_total == count, "【巡店分析】昨日巡检人数!=【巡店报告中心】昨日巡检的账号数" + yetdaychecker_total +"!=" +count);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("【巡店分析】昨日巡检人数==【巡店报告中心】昨日巡检的账号数");
        }

    }

    /**
     * ====================今日巡检合格率==【巡店报告中心】今日巡店合格报告数/总提交报告数======================
     */
    @Test
    public void today_qualified_rate() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String today= dt.getHistoryDate(0);//YYYY-MM-DD
            int count1=0;
            //获取巡店报告中心列表今天提交的合格报告数
            JSONArray reportList1 = xd.xd_report_list("","","QUALIFIED","",null,page,size).getJSONArray("list");
            for(int j=0;j<reportList1.size();j++){
                String reportTime = reportList1.getJSONObject(j).getString("report_submit_time").substring(0, 10);

                //获取列表报告的提交时间，看是否为今天提交的报告
                if(reportTime.equals(today)){
                    count1++;
                }else {
                    break;
                }
            }
            //获取巡店报告中心今日提交报告的全部报告数量
            JSONArray reportList2 = xd.xd_report_list("","","","",null,page,size).getJSONArray("list");
            int count2=0;
            for(int i=0;i<reportList2.size();i++){
                String reportTime = reportList2.getJSONObject(i).getString("report_submit_time").substring(0, 10);
                //获取列表报告的提交时间，看是否为今天提交的报告
                if(reportTime.equals(today)){
                    count2++;
                }else {
                    break;
                }
            }
            //【巡店报告中心】今日巡店合格报告数/总提交报告数
            String ss=  CommonUtil.getPercent(count1,count2,4);
            String rate =  ss.replace("%","");
            //从巡店分析巡店概况中获取今日巡店合格率
            String qualified_rate = xd.xd_analysis_data().getString("today_patrol_pass_rate");

            checkArgument(qualified_rate.equals(rate), "【巡店分析】今日巡检合格率!=【巡店报告中心】今日巡店合格报告数/总提交报告数" + qualified_rate +"!=" +rate);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("【巡店分析】今日巡检合格率==【巡店报告中心】今日巡店合格报告数/总提交报告数");
        }

    }

    /**
     * ====================昨日巡检合格率==【巡店报告中心】昨日巡店合格报告数/昨日总提交报告数======================
     */
    @Test
    public void ystday_qualified_rate() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String yesterday= dt.getHistoryDate(-1);//YYYY-MM-DD
            int count1=0;
            //获取巡店报告中心列表昨天提交的合格报告数
            JSONArray reportList1 = xd.xd_report_list("","","QUALIFIED","",null,page,size).getJSONArray("list");
            for(int j=0;j<reportList1.size();j++){
                String reportTime = reportList1.getJSONObject(j).getString("report_submit_time").substring(0, 10);

                //获取列表报告的提交时间，看是否为昨天提交的报告
                if(reportTime.equals(yesterday)){
                    count1++;
                }
            }

            //获取巡店报告中心昨日提交报告的全部报告数量
            JSONArray reportList2 = xd.xd_report_list("","","","",null,page,size).getJSONArray("list");
            int count2=0;
            for(int i=0;i<reportList2.size();i++){
                String reportTime = reportList2.getJSONObject(i).getString("report_submit_time").substring(0, 10);
                //获取列表报告的提交时间，看是否为昨天提交的报告
                if(reportTime.equals(yesterday)){
                    count2++;
                }
            }
            //【巡店报告中心】昨日巡店合格报告数/总提交报告数
            String ss=  CommonUtil.getPercent(count1,count2,4);
            String rate =  ss.replace("%","");
            //从巡店分析巡店概况中获取昨日巡店合格率
            String qualified_rate = xd.xd_analysis_data().getString("yesterday_patrol_pass_rate");

            checkArgument(qualified_rate.equals(rate), "【巡店分析】昨日巡检合格率!=【巡店报告中心】昨日巡店合格报告数/昨日总提交报告数" + qualified_rate +"!=" +rate);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("【巡店分析】昨日巡检合格率==【巡店报告中心】昨日巡店合格报告数/昨日总提交报告数");
        }

    }

    /**
     * ====================今日待整改项数==【巡店报告中心】条件为：今天，不合格；待处理的不合格项数======================
     */
    @Test
    public void today_fix_wait() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String today= dt.getHistoryDate(0);//YYYY-MM-DD
            int count1=0;
            int id = 0;
            long shop_id =0;
            int unqualified_num =0;
            int count2=0;
            //获取巡店报告中心列表今天提交的合格报告数
            JSONArray reportList1 = xd.xd_report_list("","","UNQUALIFIED","PENDING",null,page,size).getJSONArray("list");
            for(int i=0;i<reportList1.size();i++){
                String reportTime = reportList1.getJSONObject(i).getString("report_submit_time").substring(0, 10);

                //获取列表报告的提交时间，看是否为今天提交的报告
                if(reportTime.equals(today)){
                    id = reportList1.getJSONObject(i).getInteger("id");
                    shop_id = reportList1.getJSONObject(i).getInteger("shop_id");
                    //获取今天每个不合格待处理的不合格项数
                    unqualified_num = xd.shopChecksDetail(id,shop_id).getInteger("unqualified_num");
                    count2 += unqualified_num;
                }
            }
            //从巡店分析巡店概况中获取今日待整改项
            int fix_wait = xd.xd_analysis_data().getInteger("today_need_rectific_number");
            checkArgument(fix_wait == count2, "【巡店分析】今日待整改项数"+ fix_wait +"!=【巡店报告中心】条件为：今天，不合格；待处理的不合格项数" +count2);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("【巡店分析】今日待整改项数==【巡店报告中心】条件为：今天，不合格；待处理的不合格项数");
        }

    }

    /**
     * ====================昨天待整改数>=【巡店报告中心】条件为：昨天，不合格；待处理的不合格项数======================
     */
    @Test
    public void yesterday_fix_wait() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String today= dt.getHistoryDate(-1);//YYYY-MM-DD
            int count1=0;
            int id = 0;
            Long shop_id =null;
            int unqualified_num =0;
            int count2=0;
            //获取巡店报告中心列表昨天提交的不合格报告数
            JSONArray reportList1 = xd.xd_report_list("","","UNQUALIFIED","PENDING",null,page,size).getJSONArray("list");
            for(int i=0;i<reportList1.size();i++){
                String reportTime = reportList1.getJSONObject(i).getString("report_submit_time").substring(0, 10);

                //获取列表报告的提交时间，看是否为昨天提交的报告
                if(reportTime.equals(today)){
                    id = reportList1.getJSONObject(i).getInteger("id");
                    shop_id = reportList1.getJSONObject(i).getLong("shop_id");
                    //获取今天每个不合格待处理的不合格项数
                    unqualified_num = xd.shopChecksDetail(id,shop_id).getInteger("unqualified_num");
                    count2 += unqualified_num;
                }
            }
            //从巡店分析巡店概况中获取昨日待整改项
            int fix_wait = xd.xd_analysis_data().getInteger("yesterday_need_rectific_number");
            checkArgument(fix_wait == count2, "【巡店分析】昨天待整改数"+ fix_wait +"!=【巡店报告中心】条件为：昨天，不合格；待处理的不合格项数"  +count2);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("【巡店分析】昨天待整改数>=【巡店报告中心】条件为：昨天，不合格；待处理的不合格项数");
        }

    }

    /**
     * ====================【巡店分析】中最近7/14/30/60天的数据>=0======================
     */
    @Test(dataProvider = "CYCLE_TYPE",dataProviderClass = XundianScenarioUtilOnline.class)
    public void selectTimeData(String cycle_type) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            boolean resulit = false;
            String own_shop_number= xd.xd_analysis_indeicators(cycle_type,"").getString("own_shop_number");
            String total_patrol_number= xd.xd_analysis_indeicators(cycle_type,"").getString("total_patrol_number");
            String total_patrol_avg_time= xd.xd_analysis_indeicators(cycle_type,"").getString("total_patrol_avg_time");
            if(own_shop_number !=null &&total_patrol_number!=null && total_patrol_avg_time!=null){
                resulit = true;
            }
            checkArgument( resulit = true, "【巡店分析】中选择最近7/14/30/60天的【巡店核心指标数据】有异常" + "权限门店数量:"+own_shop_number  +"巡店报告数量:"+total_patrol_number +"平均巡店时长:"+total_patrol_avg_time);

            JSONArray uncheck_list = xd.xd_analysis_uncheckTotal(cycle_type,"").getJSONArray("trend_list");
            for(int i=0;i<5;i++){
                String unqualified_number = uncheck_list.getJSONObject(i).getString("unqualified_number");
                String abscissa = uncheck_list.getJSONObject(i).getString("abscissa");
                checkArgument( resulit = true, "【巡店分析】中选择最近7/14/30/60天的【巡店不合格项趋势】有异常" + "日期为:"+ abscissa +"不合格项数为:"+unqualified_number );
            }

            //获取问题分析中两个数组的值
            JSONArray uncheckRate_list = xd.xd_analysis_question(cycle_type,"").getJSONArray("unqualified_rate_list");
            JSONArray unqualified_shop_rank = xd.xd_analysis_question(cycle_type,"").getJSONArray("unqualified_shop_rank");
            checkArgument( uncheckRate_list.size() !=0 && unqualified_shop_rank.size() !=0, "【巡店分析】中选择最近7/14/30/60天的【问题分析】有异常");

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("【巡店分析】中选择自然日;选择自然月;最近7/14/30/60天的数据>=0");
        }

    }



    /**
     * ====================【巡店分析】权限门店数量==【巡店中心】列表该账号下的门店数量======================
     */
    @Test()
    public void timeDiffData() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject patrol_coverage = xd.xd_analysis_indeicators(cycle_type,"").getJSONObject("patrol_coverage");
            String own_shop_number= patrol_coverage.getString("own_shop_number");
            int own_shop_number1 = Integer.valueOf(own_shop_number);

            //获取该账号下巡店中心列表中的门店数量
            int total = xd.ShopPage(page,size).getInteger("total");


            checkArgument( own_shop_number1 == total, "" + "权限门店数量:"+own_shop_number  +"!= 巡店报告数量:"+total);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("【巡店分析】权限门店数量==【巡店中心】列表该账号下的门店数量");
        }

    }

    /**
     * ====================【巡店分析】巡店整体覆盖率==【巡店中心】下巡店门店的总数量/权限下门店总数＊100%======================
     */
    @Test(dataProvider = "CYCLE_TYPE",dataProviderClass = XundianScenarioUtilOnline.class)
    public void all_coverage(String cycle_type) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = xd.ShopPage(page,size).getJSONArray("list");
            double count = 0;
            for(int i=0;i<list.size();i++){
                Integer patrol_num = list.getJSONObject(i).getInteger("patrol_num");
                if(patrol_num != 0 && patrol_num != null ){
                    count++;
                }
            }

            //获取该账号下巡店中心列表中的门店数量
            double total = xd.ShopPage(page,size).getInteger("total");

            //根据计算公式得到巡店整体覆盖率
            String coverage_rate=  CommonUtil.getPercent(count,total,4);
            //  String coverage_rate =  ss.replace("%","");

            //获取巡店分析中核心指标中的巡店整体覆盖率
            JSONObject patrol_coverage = xd.xd_analysis_indeicators(cycle_type,"").getJSONObject("patrol_coverage");
            String patrol_coverage_rate_str= patrol_coverage.getString("patrol_coverage_rate_str");

            checkArgument( patrol_coverage_rate_str.equals(coverage_rate) , "" + "【巡店分析】巡店整体覆盖率:"+patrol_coverage_rate_str  +"!= 【巡店中心】下巡店门店的总数量/权限下门店总数＊100%:"+coverage_rate);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("【巡店分析】巡店整体覆盖率==【巡店中心】下巡店门店的总数量/权限下门店总数＊100%");
        }

    }

    /**
     * ====================【巡店分析】报告合格率==【巡店报告中心】合格报告总数/累积报告数量＊100%======================
     */
    @Test(dataProvider = "CYCLE_TYPE",dataProviderClass = XundianScenarioUtilOnline.class)
    public void qualt_rate(String cycle_type) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取巡店分析中核心指标中的巡店报告合格率
            JSONObject total_patrol_qualified = xd.xd_analysis_indeicators(cycle_type,"").getJSONObject("total_patrol_qualified");
            String total_patrol_qualified_rate_str= total_patrol_qualified.getString("total_patrol_qualified_rate_str");

            //获取巡店报告中心列表提交的合格报告数
            int qualt_totals = xd.xd_report_list("","","QUALIFIED","",null,page,size).getInteger("total");

            //获取巡店报告中心列表提交的所有报告数
            int all_totals = xd.xd_report_list("","","","",null,page,size).getInteger("total");

            //根据计算公式:合格报告总数/累积报告数量＊100%得到巡店报告合格率
            String qualtfied_rate=  CommonUtil.getPercent(qualt_totals,all_totals,4);

            checkArgument( total_patrol_qualified_rate_str.equals(qualtfied_rate) , "" + "【巡店分析】报告合格率:"+total_patrol_qualified_rate_str  +"!= 【巡店报告中心】合格报告总数/累积报告数量＊100%:"+qualtfied_rate);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("【巡店分析】报告合格率==【巡店中心】下巡店门店的总数量/权限下门店总数＊100%");
        }

    }

    /**
     * ====================【巡店分析】环状图中的不合格率相加==100%======================
     */
    @Test(dataProvider = "CYCLE_TYPE",dataProviderClass = XundianScenarioUtilOnline.class)
    public void unqualt_rate_sum(String cycle_type) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //从问题分析获取不合格项占比的前五和其他的比例
            JSONArray unquali_rateList = xd.xd_analysis_question(cycle_type,"").getJSONArray("unqualified_rate_list");
            double rate_sum = 0;
            for(int i=0;i<unquali_rateList.size();i++){
                double unqualified_rate = unquali_rateList.getJSONObject(i).getDouble("unqualified_rate");
                if(unqualified_rate != 0 ){
                    rate_sum += unqualified_rate;
                }
            }
            double result = Math.abs(rate_sum-100);
            checkArgument( result <=1, "" + "【巡店分析】报环状图中的不合格率相加:"+rate_sum  +"!= 100");
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("【巡店分析】环状图中的不合格率相加==100%");
        }

    }
    /**
     * ====================【巡店分析】环状图中的不合格项数相加==【巡店核心指标】中的总不合格项数======================
     */
    @Test(dataProvider = "CYCLE_TYPE",dataProviderClass = XundianScenarioUtilOnline.class)
    public void unqual_sum_info(String cycle_type) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //从问题分析获取不合格项占比的前五和其他的比例
            JSONArray unquali_rateList = xd.xd_analysis_question(cycle_type,"").getJSONArray("unqualified_rate_list");
            int uq_sum = 0;
            for(int i=0;i<unquali_rateList.size();i++){
                int unqualified_number = unquali_rateList.getJSONObject(i).getInteger("unqualified_number");
                if(unqualified_number != 0 ){
                    uq_sum += unqualified_number;
                }
            }
            JSONObject uq_obj = xd.xd_analysis_indeicators(cycle_type,"").getJSONObject("total_patrol_unqualified");
            int unqualified_number = uq_obj.getInteger("total_patrol_unqualified_number");

            checkArgument( uq_sum ==unqualified_number, "" + "【巡店分析】报环状图中的不合格项相加:"+uq_sum  +"!= 【巡店核心指标】中的总不合格项"+unqualified_number);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("【巡店分析】环状图中的不合格项数相加==【巡店核心指标】中的总不合格项数");
        }

    }

    /**
     * ====================【巡店分析】问题分析中的不合格项门店排名中的门店数量<=5======================
     */
    @Test(dataProvider = "CYCLE_TYPE",dataProviderClass = XundianScenarioUtilOnline.class)
    public void unquali_store_num(String cycle_type) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //从问题分析获取不合格项占比的前五的门店
            JSONArray unqualified_shop_rank = xd.xd_analysis_question(cycle_type,"").getJSONArray("unqualified_shop_rank");

            checkArgument( unqualified_shop_rank.size() <= 5, "" + "【巡店分析】问题分析中的不合格项门店排名中的门店数量<=5");
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("【巡店分析】问题分析中的不合格项门店排名中的门店数量<=5");
        }

    }


    /**
     * ====================【巡店报告中心】报告提交时间==【巡店中心】中提交巡检报告的时间======================
     */
    @Test
    public void report_detail_data() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //从巡店报告中心列表第一个的数据和巡店中心的同一数据进行筛选
            JSONArray list = xd.xd_report_list("","","","",1,page,size).getJSONArray("list");
            Long shop_id = list.getJSONObject(0).getLong("shop_id");
            String shop_name = list.getJSONObject(0).getString("shop_name");
            String report_submit_time = list.getJSONObject(0).getString("report_submit_time");

            //从巡店详情中获取上述门店的列表第一个报告
            JSONArray store_list = xd.shopChecksPage(page,size,shop_id).getJSONArray("list");
            String check_time = store_list.getJSONObject(0).getString("check_time");

            checkArgument( report_submit_time.equals(check_time), "" + "【巡店报告中心】报告提交时间:"+report_submit_time+"!=【巡店中心】中提交巡检报告的时间:"+check_time);
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("【巡店报告中心】报告提交时间==【巡店中心】中提交巡检报告的时间");
        }

    }

    /**
     * ====================【巡店报告中心】巡店者，巡检门店==【巡店中心】提交报告的账号人员和巡检的门店======================
     */
    @Test
    public void report_other_data() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //从巡店报告中心列表第一个的数据和巡店中心的同一数据进行筛选
            JSONArray list = xd.xd_report_list("","","","",null,page,size).getJSONArray("list");
            Long shop_id = list.getJSONObject(0).getLong("shop_id");
            String person_name = list.getJSONObject(0).getString("patrol_person_name");
            String submit_time = list.getJSONObject(0).getString("report_submit_time");

            //从巡店详情中获取上述门店的列表第一个报告
            JSONArray store_list = xd.shopChecksPage(page,size,shop_id).getJSONArray("list");
            String check_time = store_list.getJSONObject(0).getString("check_time");
            String inspector_name = store_list.getJSONObject(0).getString("inspector_name");
            Long shop_id1 = store_list.getJSONObject(0).getLong("shop_id");

            checkArgument( inspector_name.equals(person_name) && shop_id.equals(shop_id1),   "【巡店报告中心】巡店者，巡检门店!=【巡店中心】提交报告的账号人员和巡检的门店");
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("【巡店报告中心】巡店者，巡检门店==【巡店中心】提交报告的账号人员和巡检的门店");
        }

    }

    /**
     * ====================【巡店报告中心】执行项不合格率==当次巡检报告中不合格项/总执行项======================
     */
    @Test
    public void report_unquRate() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //从巡店报告中心列表执行项不合格率
            JSONArray list = xd.xd_report_list("","","","",0,page,size).getJSONArray("list");
            for(int i=0;i<list.size();i++){
                String item_unqu_rate = list.getJSONObject(i).getString("item_unqualified_rate_str");
                int id = list.getJSONObject(i).getInteger("id");
                Long shop_id = list.getJSONObject(i).getLong("shop_id");

                //通过报告id和门店id去详情页拿到不合格的项数
                JSONArray check_lists = xd.shopChecksDetail(id ,shop_id).getJSONArray("check_lists");
                int totals = 0;
                for(int j=0;j<check_lists.size();j++){
                    int total = check_lists.getJSONObject(j).getInteger("total");
                    if(total != 0){
                        totals += total;
                    }

                }
                Integer unqualified_num = xd.shopChecksDetail(id ,shop_id).getInteger("unqualified_num");
                String unqua_rate=  CommonUtil.getPercent(unqualified_num,totals,4);
                checkArgument( item_unqu_rate.equals(unqua_rate),   "【巡店报告中心】执行项不合格率;"+item_unqu_rate+" !=【巡店报告中心】当次巡检报告中不合格项/总执行项"+ unqua_rate);
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("【巡店报告中心】执行项不合格率==当次巡检报告中不合格项/总执行项");
        }

    }
    /**
     * ====================【巡店报告中心-不合格趋势图】中选择时间段内的不合格项数累计==【巡店核心指标】中同一时段的总不合格项数======================
     */
    @Test(dataProvider = "CYCLE_TYPE",dataProviderClass = XundianScenarioUtilOnline.class)
    public void unqu_data_sum(String cycle_type) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取巡店核心指标中的不合格项数
            JSONObject  obj = xd.xd_analysis_indeicators(cycle_type,"").getJSONObject("total_patrol_unqualified");
            int unqualified_num =obj.getInteger("total_patrol_unqualified_number");

            JSONArray trend_list = xd.xd_analysis_uncheckTotal(cycle_type,"").getJSONArray("trend_list");
            int sum = 0;
            for(int i=0;i<trend_list.size();i++){
                int unqualified_number = trend_list.getJSONObject(i).getInteger("unqualified_number");
                if(unqualified_number !=0){
                    sum += unqualified_number;
                }
            }
            checkArgument( unqualified_num == sum,   "【巡店报告中心-不合格趋势图】中选择时间段内的不合格项数累计:"+sum+" 【巡店核心指标】中同一时段的总不合格项数"+ unqualified_num);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("【巡店报告中心-不合格趋势图】中选择时间段内的不合格项数累计==【巡店核心指标】中同一时段的总不合格项数");
        }

    }

    //获取shop_id
    public long getShopId (int page, int size) throws Exception {
        JSONArray check_list= xd.ShopPage(page,size).getJSONArray("list");
        return (long) check_list.getJSONObject(0).getInteger("id");
    }

    /**
     * ====================test======================
     */
    @Test
    public void getnum() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //从巡店报告中心列表执行项不合格率
            JSONArray list = xd.xd_report_list("","","","",0,page,size).getJSONArray("list");
            int count1 = 0;
            int count2 = 0;
            int count3 = 0;
            for(int i=0;i<list.size();i++){
                String shop_name = list.getJSONObject(i).getString("shop_name");
                if(shop_name.equals("AI-Test(门店订单录像)")){
                    int id = list.getJSONObject(i).getInteger("id");
                    Long shop_id = list.getJSONObject(i).getLong("shop_id");
                    //通过报告id和门店id去详情页拿到不合格的项数
                    int qualified_num = xd.shopChecksDetail(id ,shop_id).getInteger("qualified_num");
                    int inappropriate_num =  xd.shopChecksDetail(id ,shop_id).getInteger("inappropriate_num");
                    int unqualified_num = xd.shopChecksDetail(id ,shop_id).getInteger("unqualified_num");
                    count1 += qualified_num;
                    count2 += inappropriate_num;
                    count3 += unqualified_num;

                }
            }
            //ai_TEST门店的执行项整体合格率
            int totals = count1+count2+count3;
            String A=  CommonUtil.getPercent(count1,totals,4);
            System.out.print(A);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {

            saveData("test");
        }

    }
}

