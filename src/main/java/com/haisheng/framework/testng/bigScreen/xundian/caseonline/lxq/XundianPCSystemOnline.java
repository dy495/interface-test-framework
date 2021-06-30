package com.haisheng.framework.testng.bigScreen.xundian.caseonline.lxq;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.xundian.util.MendianInfoOnline;
import com.haisheng.framework.testng.bigScreen.xundian.util.StoreScenarioUtilOnline;
import com.haisheng.framework.testng.bigScreen.xundian.util.XundianScenarioUtilOnline;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @description :app 相关case --lvxueqing
 * @date :2020/12/22 11:15
 **/


public class XundianPCSystemOnline extends TestCaseCommon implements TestCaseStd {

    XundianScenarioUtilOnline xd = XundianScenarioUtilOnline.getInstance();
    StoreScenarioUtilOnline md = StoreScenarioUtilOnline.getInstance();
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




    @Test
    public void myReportShowChk() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            JSONArray list = xd.reportList(1,50,null,null,null,null).getJSONArray("list");
            for (int j = 0 ; j < list.size();j++){
                JSONObject obj = list.getJSONObject(j);
                Preconditions.checkArgument(obj.containsKey("report_name"),"未展示报表名称");
                Preconditions.checkArgument(obj.containsKey("report_type"),"未展示报表类型");
                Preconditions.checkArgument(obj.containsKey("report_update_time"),"未展示报表更新时间");
                Preconditions.checkArgument(obj.containsKey("shop_name"),"未展示相关门店");
                Preconditions.checkArgument(obj.containsKey("report_time_dimension"),"未展示报表时间维度");
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC[我的报表],列表展示项校验");
        }
    }

//    @Test
    public void myReportShowChk2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = xd.reportList(1,50,null,null,null,null).getJSONArray("list");
            List save = new ArrayList();
            for (int i = 0; i < list.size();i++){
                JSONObject obj = list.getJSONObject(i);
                String time = obj.getString("report_update_time")+":000";
                String a = dt.dateToTimestamp("yyyy-MM-dd HH:mm:ss:000",time);
                save.add(a); //每个时间转时间戳
            }
            //新建一个由大到小的list
            List newBittoSmall = save;
            Collections.sort(newBittoSmall,Collections.reverseOrder());
            Preconditions.checkArgument(newBittoSmall.equals(save),"未倒叙排列");
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC[我的报表]，校验根据报表更新时间倒叙排列");
        }
    }



    @Test //bug 6818
    public void myReportFilter1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray reportlist = xd.reporttype().getJSONArray("list");
            for (int i = 0 ; i < reportlist.size();i++){
                JSONObject obj = reportlist.getJSONObject(i);
                JSONArray list = xd.reportList(1,100,null,obj.getString("type"),null,null).getJSONArray("list");
                for (int j = 0 ; j < list.size();j++){
                    String reporttype = list.getJSONObject(j).getString("report_type");
                    Preconditions.checkArgument(reporttype.equals(obj.getString("type")),"根据"+obj.getString("type")+"查询，结果包含"+reporttype);
                }
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC[我的报表],根据报表类型筛选");
        }
    }

    @Test
    public void myReportFilter2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray reportlist = xd.reporttime().getJSONArray("list");
            for (int i = 0 ; i < reportlist.size();i++){
                JSONObject obj = reportlist.getJSONObject(i);
                JSONArray list = xd.reportList(1,100,null,null,obj.getString("type"),null).getJSONArray("list");
                for (int j = 0 ; j < list.size();j++){
                    String reporttype = list.getJSONObject(j).getString("report_time_dimension");
                    Preconditions.checkArgument(reporttype.equals(obj.getString("type")),"根据"+obj.getString("type")+"查询，结果包含"+reporttype);
                }
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC[我的报表],根据报表时间维度筛选");
        }
    }

    @Test(dataProvider = "SEARCH")
    public void myReportFilter3(String name) {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            JSONArray list = xd.reportList(1,100,name,null,null,null).getJSONArray("list");
            for (int j = 0 ; j < list.size();j++){
                String reportresult = list.getJSONObject(j).getString("report_name");
                Preconditions.checkArgument(reportresult.contains(name),"根据"+name+"查询，结果包含"+reportresult);
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC[我的报表],根据报表名称筛选");
        }
    }

    @Test(dataProvider = "SEARCH")
    public void myReportFilter4(String name) {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            JSONArray list = xd.reportList(1,100,null,null,null,name).getJSONArray("list");
            for (int j = 0 ; j < list.size();j++){
                String reportresult = list.getJSONObject(j).getString("shop_name");
                Preconditions.checkArgument(reportresult.contains(name),"根据"+name+"查询，结果包含"+reportresult);
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC[我的报表],根据相关门店筛选");
        }
    }

    @Test(dataProvider = "SEARCH")
    public void myReportFilter5(String name) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String type = xd.reporttype().getJSONArray("list").getJSONObject(0).getString("type");
            JSONArray list = xd.reportList(1,100,null,type,null,name).getJSONArray("list");
            for (int j = 0 ; j < list.size();j++){
                String reportresult = list.getJSONObject(j).getString("shop_name");
                String reporttype = list.getJSONObject(j).getString("report_type");
                Preconditions.checkArgument(reportresult.contains(name) && reporttype.equals(type),"根据门店名称="+name+"&&报表类型="+type+"查询，结果包含门店名称="+reportresult+"&&报表类型="+reporttype);
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC[我的报表],根据相关门店和报表类型组合筛选");
        }
    }

    @Test
    public void myReportFilter6() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            String type = xd.reporttype().getJSONArray("list").getJSONObject(0).getString("type");
            String time = xd.reporttime().getJSONArray("list").getJSONObject(0).getString("type");
            JSONArray list = xd.reportList(1,100,null,type,time,null).getJSONArray("list");
            for (int j = 0 ; j < list.size();j++){
                String reporttime = list.getJSONObject(j).getString("report_time_dimension");
                String reporttype = list.getJSONObject(j).getString("report_type");
                Preconditions.checkArgument(reporttime.equals(time) && reporttype.equals(type),"根据时间维度="+time+"&&报表类型="+type+"查询，结果包含时间维度="+reporttime+"&&报表类型="+reporttype);
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC[我的报表],根据时间维度和报表类型组合筛选");
        }
    }

    @Test
    public void myReportExport() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            JSONArray list = xd.reportList(1,100,null,null,null,null).getJSONArray("list");
            if (list.size()>0){
                int id = list.getJSONObject(0).getInteger("id");
                int code = xd.reportExport(id).getInteger("code");
                Preconditions.checkArgument(code==1000,"导出报表 id="+id+", 状态码"+code);
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC[我的报表],根据时间维度和报表类型组合筛选");
        }
    }

    @Test
    public void downldPageShowChk1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            JSONArray list = xd.downldPage(1,50,null,null,null,null).getJSONArray("list");
            for (int j = 0 ; j < list.size();j++){
                JSONObject obj = list.getJSONObject(j);
                Preconditions.checkArgument(obj.containsKey("task_name"),"未展示任务名称");
                Preconditions.checkArgument(obj.containsKey("task_type"),"未展示任务类型");
                Preconditions.checkArgument(obj.containsKey("time_frame"),"未展示时间范围");
                Preconditions.checkArgument(obj.containsKey("shop_name"),"未展示相关门店");
                Preconditions.checkArgument(obj.containsKey("applicant"),"未展示申请人");
                Preconditions.checkArgument(obj.containsKey("application_time"),"未展示申请时间");
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC[下载列表],列表展示项校验");
        }
    }

    @Test
    public void downldPageShowChk2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = xd.downldPage(1,50,null,null,null,null).getJSONArray("list");
            List save = new ArrayList();
            for (int i = 0; i < list.size();i++){
                JSONObject obj = list.getJSONObject(i);
                String time = obj.getString("application_time")+":000";
                String a = dt.dateToTimestamp("yyyy-MM-dd HH:mm:ss:000",time);
                save.add(a); //每个时间转时间戳
            }
            //新建一个由大到小的list
            List newBittoSmall = save;
            Collections.sort(newBittoSmall,Collections.reverseOrder());
            Preconditions.checkArgument(newBittoSmall.equals(save),"未倒叙排列");
        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC[下载列表]，校验根据申请时间倒叙排列");
        }
    }

    @Test
    public void downldPageFilter1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray reportlist = xd.downldTaskType().getJSONArray("list");
            for (int i = 0 ; i < reportlist.size();i++){
                JSONObject obj = reportlist.getJSONObject(i);
                JSONArray list = xd.downldPage(1,100,null,obj.getString("value"),null,null).getJSONArray("list");
                for (int j = 0 ; j < list.size();j++){
                    String tasktype = list.getJSONObject(j).getString("task_type");
                    Preconditions.checkArgument(tasktype.equals(obj.getString("type")),"根据"+obj.getString("type")+"查询，结果包含"+tasktype);
                }
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC[下载报表],根据报表类型筛选");
        }
    }

    @Ignore //接口问题 bug 6634
    @Test(dataProvider = "SEARCH")
    public void downldPageFilter2(String name) {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            JSONArray list = xd.downldPage(1,100,name,null,null,null).getJSONArray("list");
            for (int j = 0 ; j < list.size();j++){
                String reporttype = list.getJSONObject(j).getString("task_name");
                Preconditions.checkArgument(reporttype.contains(name),"根据"+name+"查询，结果包含"+reporttype);
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC[下载报表],根据任务名称筛选");
        }
    }

    @Ignore //接口问题 bug 6634
    @Test(dataProvider = "SEARCH")
    public void downldPageFilter3(String name) {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            JSONArray list = xd.downldPage(1,100,null,null,name,null).getJSONArray("list");
            for (int j = 0 ; j < list.size();j++){
                String reporttype = list.getJSONObject(j).getString("shop_name");
                Preconditions.checkArgument(reporttype.contains(name),"根据"+name+"查询，结果包含"+reporttype);
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC[下载报表],根据相关门店筛选");
        }
    }

    @Test
    public void downldPageFilter4() {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            JSONArray list = xd.downldPage(1,100,null,null,null,null).getJSONArray("list");
            for (int j = 0 ; j < list.size();j++){
                String reporttype = list.getJSONObject(j).getString("applicant");
                Preconditions.checkArgument(reporttype.equals(info.usernamechin),"结果包含"+reporttype);
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC[下载报表],申请人全部是当前登陆账号名称");
        }
    }

    @Ignore //接口问题 bug 6634
    @Test(dataProvider = "SEARCH")
    public void downldPageFilter5(String name) {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            JSONArray list = xd.downldPage(1,100,null,null,null,name).getJSONArray("list");
            Preconditions.checkArgument(list.size()==0,"根据"+name+"查询，结果不为空");


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC[下载报表],根据申请人筛选");
        }
    }

    @Ignore //没调通
    @Test(dataProvider = "SEARCH2")
    public void devicePageFilter1(String search, String mess) {
        logger.logCaseStart(caseResult.getCaseName());
        try {

            JSONArray list = md.device_page(search,"","","","CAMERA",1,100).getJSONArray("list");
            for (int i=0;i<list.size();i++){
                String shop = list.getJSONObject(i).getString("shop_name");
                Preconditions.checkArgument(info.usershop.contains(shop),"摄像头列表，包含了无权限门店"+shop);
            }

            JSONArray list2 = md.device_page("",search,"","","CAMERA",1,100).getJSONArray("list");
            for (int i=0;i<list.size();i++){
                String shop = list2.getJSONObject(i).getString("shop_name");
                Preconditions.checkArgument(info.usershop.contains(shop),"摄像头列表，包含了无权限门店"+shop);
            }

            JSONArray list3 = md.device_page(search,"","","","SERVER",1,100).getJSONArray("list");
            for (int i=0;i<list.size();i++){
                String shop = list3.getJSONObject(i).getString("shop_name");
                Preconditions.checkArgument(info.usershop.contains(shop),"服务器列表，包含了无权限门店"+shop);
            }

            JSONArray list4 = md.device_page("",search,"","","SERVER",1,100).getJSONArray("list");
            for (int i=0;i<list.size();i++){
                String shop = list4.getJSONObject(i).getString("shop_name");
                Preconditions.checkArgument(info.usershop.contains(shop),"服务器列表，包含了无权限门店"+shop);
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("PC[设备列表],根据"+mess+"筛选");
        }
    }














    @DataProvider(name = "SEARCH")
    public  Object[] search() {

        return new String[]{
               "报表",
                "1",
                "!@#$%^&*(",
                "ABCabc",
                "越秀售楼处",


        };
    }

    @DataProvider(name = "SEARCH2")
    public  Object[] search2() {

        return new String[][]{
                {"1","名称"},
                {"1","所属门店"},


        };
    }



}
