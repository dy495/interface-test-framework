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
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

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
        commonConfig.shopId = getXundianShop(); //要改！！！
        beforeClassInit(commonConfig);

        logger.debug("store " + xd);
        xd.login("yuexiu@test.com", "f5b3e737510f31b88eb2d4b5d0cd2fb4");


    }

    @AfterClass
    @Override
    public void clean() {
        afterClassClean();
    }

    @Override
    public void createFreshCase(Method method) {

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
                int handleStatusname = obj.getInteger("handle_status_name");
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
    @Test(dataProvider = "HANDLESTATUS")
    public void xdRecordFilter2(String type, String mes) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = xd.getShopChecksPage(info.shop_id_01,Integer.parseInt(type),null,null,null,
                    null,100,null).getJSONArray("list");
            for (int i = 0; i < list.size();i++){
                JSONObject obj = list.getJSONObject(i);
                int check_result = obj.getInteger("check_result");
                int check_result_name = obj.getInteger("check_result_name");
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

    @Test
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
                Preconditions.checkArgument(obj.containsKey(""),"未展示巡店方式"); //接口文档未给出 待补充

                if (obj.getInteger("check_result")==1){
                    //不合格一定有说明
                    Preconditions.checkArgument(obj.containsKey(""),"未展示报告提交说明"); //接口文档未给出 待补充
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

    @Test
    public void xdRecordDisplay2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONArray list = xd.getShopChecksPage(info.shop_id_01,null,null,null,null,
                    null,100,null).getJSONArray("list");
            List save = new ArrayList();
            for (int i = 0; i < list.size();i++){
                JSONObject obj = list.getJSONObject(i);
                String time = obj.getString("check_time");
                save.add(dt.dateToTimestamp("yyyy-MM-dd HH:mm:ss:000",time)); //每个时间转时间戳
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


    @Test
    public void xdRecordDetailFilter1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取列表中第一个记录的id
            Long id = xd.getShopChecksPage(info.shop_id_01,null,null,null,null,
                    null,100,null).getJSONArray("list").getJSONObject(0).getLong("id");
            //查看第一个记录的清单id
            JSONArray checklist = new JSONArray();
            JSONArray recordDetail = xd.getShopChecksDetail(id,info.shop_id_01,null,null).getJSONArray("check_lists");
            for (int i = 0; i < recordDetail.size();i++){
                JSONObject obj = recordDetail.getJSONObject(i);
                checklist.add(obj.getLong("id"));
            }
            for (int j = 0 ; j < checklist.size(); j++){
                Long listid = (Long) checklist.get(j);
                JSONArray recordDetail1 = xd.getShopChecksDetail(id,info.shop_id_01, listid,null).getJSONArray("check_lists");
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

    @Test(dataProvider = "CHECKRESULT") //这块接口文档有点问题！！！！！
    public void xdRecordDetailFilter2(String type, String mes) {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            //获取列表中第一个记录的id
            Long id = xd.getShopChecksPage(info.shop_id_01,null,null,null,null,
                    null,100,null).getJSONArray("list").getJSONObject(0).getLong("id");

            JSONArray checklist = new JSONArray();
            JSONArray recordDetail = xd.getShopChecksDetail(id,info.shop_id_01,null,Long.parseLong(type)).getJSONArray("check_lists");
            for (int i = 0 ; i < recordDetail.size();i++){
                JSONObject obj = recordDetail.getJSONObject(i);
                JSONArray itemlist = obj.getJSONArray("check_items");
                for (int j = 0 ; j < itemlist.size();j++){
                    Long checkresult = itemlist.getJSONObject(j).getLong("check_result");
                    Preconditions.checkArgument(checkresult==Long.parseLong(type),"筛选"+type+mes+"结果包含"+checkresult);
                }
            }

        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("app【巡店报告详情】，根据执行项结果筛选");
        }
    }


    @Test
    public void xdHistory() {
        logger.logCaseStart(caseResult.getCaseName());
        try {


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("app【远程巡店-历史画面】，根据日期/摄像头筛选");
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
                {"2","无需处理"},

        };
    }




}
