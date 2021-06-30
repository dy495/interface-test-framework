package com.haisheng.framework.testng.bigScreen.itemXundian.caseonline.lxq;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.itemXundian.util.MendianInfoOnline;
import com.haisheng.framework.testng.bigScreen.itemXundian.util.XundianScenarioUtilOnline;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import org.testng.annotations.*;

import java.lang.reflect.Method;

/**
 * @description :app 相关case --lvxueqing
 * @date :2020/12/22 11:15
 **/


public class XundianAppDataOnline extends TestCaseCommon implements TestCaseStd {

    XundianScenarioUtilOnline xd = XundianScenarioUtilOnline.getInstance();
    MendianInfoOnline info = new MendianInfoOnline();
    public String filepath="src/main/java/com/haisheng/framework/testng/bigScreen/xundianDaily/64.txt";  //巡店不合格图片base64

    /**
     * @description: initial test class level config, such as appid/uid/ak/dinghook/push_rd_name
     */
    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before classs initial");
        CommonConfig commonConfig = new CommonConfig();


        //replace checklist app id and conf id
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_MENDIAN_ONLINE_SERVICE;
        commonConfig.checklistQaOwner = "吕雪晴";


        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "mendian-online-test");

        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, "门店(巡店) 线上");

        commonConfig.dingHook = DingWebhook.ONLINE_STORE_MANAGEMENT_PLATFORM_GRP;
        commonConfig.pushRd = new String[]{"13604609869", "15084928847"};

        commonConfig.shopId = getXunDianShopOnline(); //要改！！！
        beforeClassInit(commonConfig);

        logger.debug("xundian " + xd);

        xd.login("storedemo@winsense.ai","b0581aa73b04d9fe6e3057a613e6f363");


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
     * @description :【远程/现场巡店-页面内一致性】提交结果时展示的不合格/合格/不适用项数==执行项选择时的不合格/合格/不适用项数
     * @date :2020/12/22 16:00
     **/
    @Test(dataProvider = "XDTYPE")
    public void xdOnePgae1(String type,String chinesetype) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int notok = 0; // 巡检过程中不合格数量
            int ok = 0; // 巡检过程中合格数量
            int notapply = 0; // 巡检过程中不适用数量


            //开始巡店,获取巡店清单&每个清单的项目&patrol_id
            JSONObject obj = xd.checkStartapp(info.shop_id_01,type,1,true);
            Long patrolID = obj.getLong("id");
            JSONArray checklist = obj.getJSONArray("check_lists");

            Long listID = 1L;
            for (int i = 0; i < checklist.size();i++){
                JSONObject eachlist = checklist.getJSONObject(i);
                listID = eachlist.getLong("id"); // 获取list id
                JSONArray chkitems = eachlist.getJSONArray("check_items");
                for (int j =0; j < chkitems.size();j++){
                    JSONObject eachitem = chkitems.getJSONObject(j);
                    Long itemID = eachitem.getLong("id"); //每个清单内循环 获取item id
                    //巡检项目结果 1合格；2不合格；3不适用
                    if (itemID %2 == 0){
                        xd.checks_item_submit(info.shop_id_01,patrolID,listID,itemID,1,"zdh合格",null);
                        ok = ok +1;
                    }
                    if (itemID %2 != 0 && itemID % 3  == 0) {
                        xd.checks_item_submit(info.shop_id_01,patrolID,listID,itemID,2,"zdh不合格",info.getpic(1));
                        notok = notok + 1;
                    }
                    if (itemID %2 != 0 && itemID % 3  != 0) {
                        xd.checks_item_submit(info.shop_id_01,patrolID,listID,itemID,3,"zdh不适用",null);
                        notapply = notapply + 1;
                    }
                }



            }
            xd.checks_submit(info.shop_id_01,patrolID,"一次巡店完成");
            JSONObject detail = xd.getShopChecksDetail(patrolID,info.shop_id_01,listID,null);
            int notokSUM = detail.getInteger("unqualified_num"); //提交结果后展示的不合格数量
            int okSUM = detail.getInteger("qualified_num");//提交结果后展示的合格数量
            int notapplySUM = detail.getInteger("inappropriate_num");//提交结果后展示的不适用数量

            Preconditions.checkArgument(notok==notokSUM,"选择不合格"+notok+"项，提交时展示"+notokSUM+"项");
            Preconditions.checkArgument(ok==okSUM,"选择合格"+ok+"项，提交时展示"+okSUM+"项");
            Preconditions.checkArgument(notapply==notapplySUM,"选择不适用"+notapply+"项，提交时展示"+notapplySUM+"项");

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("【"+chinesetype+"】提交结果时展示的不合格/合格/不适用项数==执行项选择时的不合格/合格/不适用项数");
        }
    }

    /**
     * @description :【远程/现场巡店-页面内一致性】提交一个执行项，执行清单的执行中分子+1
     * @date :2020/12/22 16:00
     **/
    @Test(dataProvider = "CHKRESULT") //ok
    public void xdOnePgae2(String type, String chinesetype, String result,String mes) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int checkedbefore = 0 ;

            //重置检查清单，开始巡店
            JSONObject obj = xd.checkStartapp(info.shop_id_01,type,1,true);
            Long patrolID = obj.getLong("id");
            JSONArray checklist = obj.getJSONArray("check_lists");
            //第一个清单的已检查数量
            checkedbefore = checklist.getJSONObject(0).getInteger("checked");
            JSONObject eachlist = checklist.getJSONObject(0);
            Long listID = eachlist.getLong("id"); // 获取list id

            JSONArray chkitems = eachlist.getJSONArray("check_items");
            JSONObject eachitem = chkitems.getJSONObject(0);
            Long itemID = eachitem.getLong("id"); //第一个清单的第一个item id
            //巡检项目结果 1合格；2不合格；3不适用
            xd.checks_item_submit(info.shop_id_01,patrolID,listID,itemID,Integer.parseInt(result),"zdh",info.getpic(1));


            //重新巡店，不重置清单，查看第一个清单完成数量
            int checkedafter = xd.checkStartapp(info.shop_id_01,type,0,true).getJSONArray("check_lists").getJSONObject(0).getInteger("checked");
            int a = checkedafter - checkedbefore;
            Preconditions.checkArgument(a==1,"提交一个"+mes+"执行项，分子增加了"+a);


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("【"+chinesetype+"】提交一个"+mes+"执行项，执行清单的执行中分子+1");
        }
    }

    /**
     * @description :【远程/现场巡店-页面内一致性】执行清单的总数量分母==执行清单的总条数
     * @date :2020/12/22 16:00
     **/
    @Test(dataProvider = "XDTYPE") //ok
    public void xdOnePgae3(String type, String chinesetype) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray chklist = xd.checkStartapp(info.shop_id_01,type,1,true).getJSONArray("check_lists");
            for (int i=0; i < chklist.size(); i++){
                JSONObject eachlist = chklist.getJSONObject(i);
                int total = eachlist.getInteger("total"); //分母数
                int itemsum = eachlist.getJSONArray("check_items").size();
                Preconditions.checkArgument(total==itemsum,"分母="+total+", 列表条数="+ itemsum);
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("【"+chinesetype+"】执行清单的总数量分母==执行清单的总条数");
        }
    }

    /**
     * @description :【远程/现场巡店-页面内一致性】执行清单的执行中分子==执行清单已执行完成的条数
     * @date :2020/12/22 16:00
     **/


    @Test(dataProvider = "XDTYPE")  //ok
    public void xdOnePgae4(String type,String chinesetype) {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            JSONArray chklist = xd.checkStartapp(info.shop_id_01,type,0,true).getJSONArray("check_lists");
            for (int i=0; i < chklist.size(); i++){
                int itemsum = 0;
                JSONObject eachlist = chklist.getJSONObject(i);
                int checked = eachlist.getInteger("checked"); //分子数

                JSONArray itemarr = eachlist.getJSONArray("check_items");
                for (int j = 0; j < itemarr.size();j++){
                    JSONObject obj = itemarr.getJSONObject(i);
                    if (obj.getInteger("check_result") != 0){
                        itemsum = itemsum + 1;
                    }
                }
                Preconditions.checkArgument(checked==itemsum,"分子="+checked+", 列表已完成条数="+ itemsum);
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("【"+chinesetype+"】执行清单的执行中分子==执行清单已执行完成的条数");
        }
    }



    @Test(dataProvider = "CHKRESULT")
    public void xdSevlPgae1(String type, String chinesetype, String result,String mes) {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            int bef = xd.getShopChecksPage(info.shop_id_01,null,null,null,null,null,1,null).getInteger("total");
            int bef2 = xd.shopChecksPage(1,1,info.shop_id_01).getInteger("total");

            //巡店
            info.xdOperate(info.shop_id_01,type,1,Integer.parseInt(result),true);
            int after = xd.getShopChecksPage(info.shop_id_01,null,null,null,null,null,1,null).getInteger("total");

            int after2 = xd.shopChecksPage(1,1,info.shop_id_01).getInteger("total");
            int add = after - bef;
            int add2 = after2 - bef2;

            Preconditions.checkArgument(add==1,"app【巡店记录】中巡店报告 期待增加1，实际增加"+add);
            Preconditions.checkArgument(add2==1,"PC【巡店中心详情中】巡店报告 期待增加1，实际增加"+add2);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("【"+chinesetype+"】提交一个"+mes+"报告，app【巡店记录】中巡店报告+1、PC【巡店中心详情中】巡店报告+1");
        }
    }

    @Test(dataProvider = "CHKRESULT")
    public void xdSevlPgae2(String type, String chinesetype, String result,String mes) {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            int bef1 =xd.xunDianCenterselect(1,1,info.shop_id_01_chin).getJSONArray("list").getJSONObject(0).getInteger("patrol_num");
            int bef2 = xd.xd_report_list("",info.shop_id_01_chin,"","",null,1,1).getInteger("total");
            //巡店
            info.xdOperate(info.shop_id_01,type,1,Integer.parseInt(result),true);
            int after1 =xd.xunDianCenterselect(1,1,info.shop_id_01_chin).getJSONArray("list").getJSONObject(0).getInteger("patrol_num");
            int after2 = xd.xd_report_list(null,info.shop_id_01_chin,null,null,null,1,1).getInteger("total");

            int add1 = after1 - bef1;
            int add2 = after2 - bef2;
            Preconditions.checkArgument(add1==1,"PC【巡店中心】巡店次数 期待增加1，实际增加"+add1);
            Preconditions.checkArgument(add2==1,"PC【巡店报告中心】巡店报告 期待增加1，实际增加"+add2);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("【"+chinesetype+"】提交一个"+mes+"报告，PC【巡店中心】巡店次数+1、PC【巡店报告中心】巡店报告+1");
        }
    }

    @Test(dataProvider = "XDTYPE")
    public void xdSevlPgae3(String type,String chinesetype) {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            double bef1 =xd.xd_analysis_data().getDouble("today_patrol_pass_rate");
            //巡店
            info.xdOperate(info.shop_id_01,type,1,2,true);
            double after1 =xd.xd_analysis_data().getDouble("today_patrol_pass_rate");
            double add1 = after1 - bef1;

            Preconditions.checkArgument(add1<0,"今日巡店合格率 增加"+add1);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("【"+chinesetype+"】提交一个不合格报告，PC【巡店分析】中的今日巡店合格率降低");
        }
    }

    @Test(dataProvider = "XDTYPE")
    public void xdSevlPgae4(String type,String chinesetype) {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            double bef1 =xd.xd_analysis_data().getDouble("today_patrol_pass_rate");
            //巡店
            info.xdOperate(info.shop_id_01,type,1,1,true);
            double after1 =xd.xd_analysis_data().getDouble("today_patrol_pass_rate");
            double add1 = after1 - bef1;

            Preconditions.checkArgument(add1>0,"今日巡店合格率 减少"+add1);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("【"+chinesetype+"】提交一个合格报告，PC【巡店分析】中的今日巡店合格率增加");
        }
    }










    @DataProvider(name = "CHKRESULT")
    public  Object[] chkResult() {

        return new String[][]{
                {"REMOTE","远程巡店","1","合格"},
                {"REMOTE","远程巡店","2","不合格"},
//                {"REMOTE","远程巡店","3","不适用"},
//                {"SPOT","现场巡店","1","合格"},
//                {"SPOT","现场巡店","2","不合格"},
                {"SPOT","现场巡店","3","不适用"},


        };
    }

    @DataProvider(name = "XDTYPE")
    public  Object[] xdtype() {

        return new String[][]{
                {"REMOTE","远程巡店"},
                {"SPOT","现场巡店"},

        };
    }






}
