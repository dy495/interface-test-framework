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
import org.testng.annotations.Test;

import java.lang.reflect.Method;

/**
 * @description :app 相关case --lvxueqing
 * @date :2020/12/22 11:15
 **/


public class XundianAppData extends TestCaseCommon implements TestCaseStd {

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
     * @description :【远程巡店-页面内一致性】提交结果时展示的不合格/合格/不适用项数==执行项选择时的不合格/合格/不适用项数
     * @date :2020/12/22 16:00
     **/
    @Test
    public void RemoteOnePgae1() {
        logger.logCaseStart(caseResult.getCaseName());
        try {
            int notok = 0; // 巡检过程中不合格数量
            int ok = 0; // 巡检过程中合格数量
            int notapply = 0; // 巡检过程中不适用数量


            //开始巡店,获取巡店清单&每个清单的项目&patrol_id
            JSONObject obj = xd.checkStartapp(info.shop_id_01,"REMOTE",0);
            Long patrolID = obj.getLong("id");
            JSONArray checklist = obj.getJSONArray("check_lists");
            for (int i = 0; i < checklist.size();i++){
                JSONObject eachlist = checklist.getJSONObject(0);
                Long listID = eachlist.getLong("id"); // 获取list id
                JSONArray chkitems = eachlist.getJSONArray("check_items");
                for (int j =0; j < chkitems.size();j++){
                    JSONObject eachitem = chkitems.getJSONObject(j);
                    Long itemID = eachitem.getLong("id"); //每个清单内循环 获取item id
                    //巡检项目结果 1合格；2不合格；3不适用
                    if (itemID == 1){
                        xd.checks_item_submit(info.shop_id_01,patrolID,listID,itemID,1,"zdh合格",null);
                        ok = ok +1;
                    }
                    if (itemID == 2) {
                        xd.checks_item_submit(info.shop_id_01,patrolID,listID,itemID,2,"zdh不合格",null);
                        notok = notok + 1;
                    }
                    else {
                        xd.checks_item_submit(info.shop_id_01,patrolID,listID,itemID,3,"zdh不适用",null);
                        notapply = notapply + 1;
                    }
                }

            }
            xd.checks_submit(info.shop_id_01,patrolID,"一次巡店完成");
            JSONObject detail = xd.getShopChecksDetail(patrolID,info.shop_id_01,null,null);
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
            saveData("【远程巡店】提交结果时展示的不合格/合格/不适用项数==执行项选择时的不合格/合格/不适用项数");
        }
    }

    /**
     * @description :【远程巡店-页面内一致性】提交一个执行项，执行清单的执行中分子+1
     * @date :2020/12/22 16:00
     **/
    @Test
    public void RemoteOnePgae2() {
        logger.logCaseStart(caseResult.getCaseName());
        try {


            //开始巡店,获取巡店清单&每个清单的项目&patrol_id
            JSONObject obj = xd.checkStartapp(info.shop_id_01,"REMOTE",0);
            Long patrolID = obj.getLong("id");
            JSONArray checklist = obj.getJSONArray("check_lists");
            for (int i = 0; i < checklist.size();i++){
                JSONObject eachlist = checklist.getJSONObject(0);
                Long listID = eachlist.getLong("id"); // 获取list id
                JSONArray chkitems = eachlist.getJSONArray("check_items");
                for (int j =0; j < chkitems.size();j++) {
                    JSONObject eachitem = chkitems.getJSONObject(j);
                    Long itemID = eachitem.getLong("id"); //每个清单内循环 获取item id
                    //巡检项目结果 1合格；2不合格；3不适用
                }
            }


        } catch (AssertionError e) {
            appendFailReason(e.toString());
        } catch (Exception e) {
            appendFailReason(e.toString());
        } finally {
            saveData("【远程巡店】提交一个执行项，执行清单的执行中分子+1");
        }
    }






}
