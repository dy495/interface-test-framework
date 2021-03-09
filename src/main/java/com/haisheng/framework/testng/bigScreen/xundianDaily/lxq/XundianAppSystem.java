package com.haisheng.framework.testng.bigScreen.xundianDaily.lxq;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.xundianDaily.MendianInfo;
import com.haisheng.framework.testng.bigScreen.xundianDaily.XundianScenarioUtil;
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


public class XundianAppSystem extends TestCaseCommon implements TestCaseStd {

    XundianScenarioUtil xd = XundianScenarioUtil.getInstance();
    MendianInfo info = new MendianInfo();
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
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_MENDIAN_DAILY_SERVICE;
        commonConfig.checklistQaOwner = "吕雪晴";
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "xundian-daily-test");
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, "门店 日常");
        commonConfig.dingHook = DingWebhook.DAILY_STORE_MANAGEMENT_PLATFORM_GRP;
        commonConfig.pushRd = new String[]{"13581630214","15084928847"};
        //commonConfig.shopId = getXundianShop(); //要改！！！
        commonConfig.shopId = Long.toString(info.shop_id_01);
        beforeClassInit(commonConfig);

        logger.debug("store " + xd);
        xd.login("yuexiu@test.com", "f5b3e737510f31b88eb2d4b5d0cd2fb4");


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
     * @description :app【巡店记录】-筛选框-处理事项
     * @date :2020/12/23 11:00
     **/
    @Test(dataProvider = "HANDLESTATUS")
    public void xdRecordFilter1(String type, String mes) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = xd.getShopChecksPage(info.shop_id_01,null,Integer.parseInt(type),null,null,
                    null,100,null).getJSONArray("list");
            for (int i = 0; i < list.size();i++){
                JSONObject obj = list.getJSONObject(i);
                int handleStatus = obj.getInteger("handle_status");
                String handleStatusname = obj.getString("handle_status_name");
                Preconditions.checkArgument(handleStatus== Integer.parseInt(type),"根据"+mes+"进行筛选，结果包含"+handleStatusname);
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("app【巡店记录】，根据处理事项进行筛选：待处理、已处理、无需处理");
        }
    }

    /**
     * @description :app【巡店记录】-筛选框-巡店结果
     * @date :2020/12/23 11:00
     **/
    @Test(dataProvider = "CHECKRESULT") //ok
    public void xdRecordFilter2(String type, String mes) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = xd.getShopChecksPage(info.shop_id_01,Integer.parseInt(type),null,null,null,
                    null,100,null).getJSONArray("list");
            for (int i = 0; i < list.size();i++){
                JSONObject obj = list.getJSONObject(i);
                int check_result = obj.getInteger("check_result");
                String check_result_name = obj.getString("check_result_name");
                Preconditions.checkArgument(check_result== Integer.parseInt(type),"根据"+mes+"进行筛选，结果包含"+check_result_name);
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("app【巡店记录】，根据巡店结果进行筛选：合格、不合格");
        }
    }

    @Test//ok
    public void xdRecordDisplay1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = xd.getShopChecksPage(info.shop_id_01,null,null,null,null,
                    null,100,null).getJSONArray("list");
            for (int i = 0; i < list.size();i++){
                JSONObject obj = list.getJSONObject(i);
                Preconditions.checkArgument(obj.containsKey("check_time"),"未展示提交时间");
                Preconditions.checkArgument(obj.containsKey("check_result") && obj.containsKey("check_result_name"),"未展示巡店结果");
                Preconditions.checkArgument(obj.containsKey("handle_status") && obj.containsKey("handle_status_name"),"未展示处理状态");
                Preconditions.checkArgument(obj.containsKey("inspector_name"),"未展示报告提交者");
                Preconditions.checkArgument(obj.containsKey("check_report_type"),"未展示巡店方式");

                if (obj.getInteger("check_result")==1){
                    //不合格一定有说明
                    Preconditions.checkArgument(obj.containsKey("submit_comment"),"未展示报告提交说明");
                }

            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("app【巡店记录列表】，事项展示校验");
        }
    }

    @Test //ok
    public void xdRecordDisplay2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = xd.getShopChecksPage(info.shop_id_01,null,null,null,null,
                    null,100,null).getJSONArray("list");
            List save = new ArrayList();
            for (int i = 0; i < list.size();i++){
                JSONObject obj = list.getJSONObject(i);
                String time = obj.getString("check_time")+":000";
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
            saveData("app【巡店记录列表】，校验根据提交时间倒叙排列");
        }
    }


    @Ignore //暂时注释掉没想到好办法写
    @Test
    public void xdRecordDetailFilter1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取列表中第一个记录的id
            Long id = xd.getShopChecksPage(info.shop_id_01,null,null,null,null,
                    null,100,null).getJSONArray("list").getJSONObject(0).getLong("id");
            //查看第一个记录的清单id
            JSONArray checklist = new JSONArray();
            JSONArray recordDetail = xd.getShopChecksDetail(id,info.shop_id_01,null,null).getJSONArray("check");
            for (int i = 0; i < recordDetail.size();i++){
                JSONObject obj = recordDetail.getJSONObject(i);
                checklist.add(obj.getLong("id"));
            }
            for (int j = 0 ; j < checklist.size(); j++){
                Long listid = (Long) checklist.get(j);
                JSONArray recordDetail1 = xd.getShopChecksDetail(id,info.shop_id_01, listid,null).getJSONArray("check");
                Preconditions.checkArgument(recordDetail1.size()==1,"根据清单名称进行筛选，结果包含多个清单");
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("app【巡店报告详情】，根据清单名称筛选");
        }
    }


    @Ignore
    @Test(dataProvider = "CHECKRESULT") // 没改完 有问题
    public void xdRecordDetailFilter2(String type, String mes) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取列表中第一个记录的id
            Long id = xd.getShopChecksPage(info.shop_id_01,null,null,null,null,
                    null,100,null).getJSONArray("list").getJSONObject(0).getLong("id");

            JSONArray checklist = new JSONArray();
            JSONArray itemlist = xd.getShopChecksDetail(id,info.shop_id_01,null,Long.parseLong(type)).getJSONArray("check").getJSONObject(0).getJSONArray("check_items");

                for (int j = 0 ; j < itemlist.size();j++){
                    Long checkresult = itemlist.getJSONObject(j).getLong("check_result");
                    Preconditions.checkArgument(checkresult==Long.parseLong(type),"筛选"+type+mes+"结果包含"+checkresult);
                }


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("app【巡店报告详情】，根据执行项结果筛选");
        }
    }

    @Test //ok
    public void xdRecordDetailDisplay() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = xd.getShopChecksPage(info.shop_id_01,null,null,null,null,
                    null,100,null).getJSONArray("list");
            for (int i = 0; i < list.size();i++){
                JSONObject obj = list.getJSONObject(i);
                Long id = obj.getLong("id");
                JSONArray getlistid =  xd.patrol_detail(info.shop_id_01,id).getJSONArray("list");
                for (int j = 0; j < getlistid.size();j++){
                    JSONObject obj2 = getlistid.getJSONObject(j);
                    JSONObject obj1 = xd.getShopChecksDetail(id,info.shop_id_01,obj2.getLong("id"),null);

                    Preconditions.checkArgument(obj1.containsKey("check_time"),"记录"+id+"未展示提交时间");
                    Preconditions.checkArgument(obj1.containsKey("submit_comment") ,"记录"+id+"未展示提交说明");
                    Preconditions.checkArgument(obj1.containsKey("inspector_name"),"记录"+id+"未展示巡店者");
                    Preconditions.checkArgument(obj1.containsKey("check_type_name"),"记录"+id+"未展示巡店方式");
                    Preconditions.checkArgument(obj1.containsKey("inappropriate_num"),"记录"+id+"未展示不适用项数");
                    Preconditions.checkArgument(obj1.containsKey("qualified_num"),"记录"+id+"未展示合格项数");
                    Preconditions.checkArgument(obj1.containsKey("unqualified_num"),"记录"+id+"未展示不合格项数");
                }

            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("app【巡店报告详情-报告信息】，事项展示校验");
        }
    }

    @Test(dataProvider = "REPLAY") //ok
    public void xdHistory(String deviceid, String date,String time) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int code = xd.device_replay(deviceid,info.shop_id_01,date,time).getInteger("code");
            Preconditions.checkArgument(code==1000,"选择设备"+deviceid+",日期="+date+"时间="+time+"状态码"+code);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("app【远程巡店-历史画面】，根据日期/摄像头筛选");
        }
    }

    @Test // bug 6502
    public void xdHistoryErr() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int code = xd.device_replay(info.deviceId,info.shop_id_01,dt.getHistoryDate(1),"09:00:00").getInteger("code");
            Preconditions.checkArgument(code==1001,"状态码期待1001，实际"+code);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("app【远程巡店-历史画面】，日期选择明天");
        }
    }

    @Test(dataProvider = "XDRESULT")
    public void commmitxdResult(String chkcode, String content, String mess) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            Long id = info.xdOperateitem(info.shop_id_01,"REMOTE",1,1,true);
            int code = 0;
            if (chkcode.equals("1000")){
                code = xd.checks_submitNotChk(info.shop_id_01, id, content).getInteger("code");
            }
            if (chkcode.equals("1001")){
                code = xd.checks_submitNotChk(info.shop_id_01, id, content).getInteger("code");
            }
            Preconditions.checkArgument(code==Integer.parseInt(chkcode),mess+"期待状态码"+chkcode+"实际"+code);

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("app【提交巡店结果】，提交说明的字数内容校验");
        }
    }

    @Test
    public void test() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            xd.reporttype();
            xd.reporttime();

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("");
        }
    }











    @DataProvider(name = "HANDLESTATUS")
    public  Object[] handle_status() {

        return new String[][]{
                {"0","无需处理"},
                {"1","已处理"},
                {"2","待处理"},

        };
    }

    @DataProvider(name = "CHECKRESULT")
    public  Object[] check_result() {

        return new String[][]{
                {"0","合格"},
                {"1","不合格"},
//                {"2","无需处理"},

        };
    }

    @DataProvider(name = "REPLAY")
    public  Object[] replay() {

        return new String[][]{
                {info.deviceId,dt.getHistoryDate(0),"03:00:00"},
                {info.deviceId2,dt.getHistoryDate(-1),"20:30:00"},
                {info.deviceId,dt.getHistoryDate(-7),"08:00:00"},


        };
    }

    @DataProvider(name = "XDRESULT")
    public  Object[] xdresult() {

        return new String[][]{
                {"1000","这是自动化的提交说明，100个字，～！@#¥%……&*（）AAAaaa123456765432123这是自动化的提交说明，100个字，～！@#¥%……&*（）AAAaaa123456765432123","100个字的说明"},
                {"1000","这是自动化的提交说明，100个字，～！@#¥%……&*（）AAAaaa123456765432123","50个字的说明"},
                //{"1001","这是自动化的提交说明，100个字，～！@#¥%……&*（）AAAaaa123456765432123这是自动化的提交说明，100个字，～！@#¥%……&*（）AAAaaa1234567654321231","101个字的说明"},

        };
    }




}
