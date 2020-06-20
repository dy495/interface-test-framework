package com.haisheng.framework.testng.bigScreen.xundianDaily;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
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

/**
 *
 */

public class xundianDataConsistentcy extends TestCaseCommon implements TestCaseStd {

    xundianScenarioUtil xd = xundianScenarioUtil.getInstance();
    String xjy4="uid_663ad653";
    int page=1;
    int size=100;




    /**
     * @description: initial test class level config, such as appid/uid/ak/dinghook/push_rd_name
     *
     */
    @BeforeClass
    @Override
    public void initial() {
        logger.debug("before classs initial");
        CommonConfig commonConfig = new CommonConfig();


        //replace checklist app id and conf id
        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_XUNDIAN_DAILY_SERVICE;
        commonConfig.checklistQaOwner = "青青";

//
//        //replace backend gateway url
//        //commonConfig.gateway = "";
//
//        //replace jenkins job name
        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "xundian-daily-test");

        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, "巡店 日常");

        //replace ding push conf
        //commonConfig.dingHook = DingWebhook.QA_TEST_GRP;
        //if need reset push rd, default are huachengyu,xiezhidong,yanghang
        //commonConfig.pushRd = {"1", "2"};

        //set shop id
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
    @Test
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
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("pc门店列表中门店=app巡店中心中门店");
        }
    }
    /**
     *
     * ====================执行清单中的执行项数=选该清单的门店中该清单的执行项数======================
     * */
//    @Test     //这块逻辑稍微有点复杂，之后找时间写
//    public void ComparisonOfNumberOfExecution() {
//        logger.logCaseStart(caseResult.getCaseName());
//        boolean needLoginBack=false;
//        try {
//            long shop_id=4118;
//            String check_type="REMOTE";
//            Integer reset=1;
//            long task_id= Long.parseLong(null);
//
//            //获取列表中的执行清单Id
//            long id=xd.checklistPage().getInteger("id");
//            //获取执行清单详情中的执行项数量
//            int itemsLength=xd.checkListDetail(id).getInteger("items.size");
//
//            //获取正在使用清单门店的清单执行项
//            JSONArray check_lists=xd.shopChecksStart(shop_id,check_type,reset,task_id).getJSONArray("check_lists");
//            int total=check_lists.getJSONObject(0).getInteger("total");
//
//
//
//        } catch (AssertionError e) {
//            appendFailreason(e.toString());
//        } catch (Exception e) {
//            appendFailreason(e.toString());
//        } finally {
//
//            saveData("PC端巡店中心列表");
//        }
//
//    }
    /**
     *
     * ====================执行清单数=增删改之后的清单数======================
     * */
    @Test
    public void checkListDataComparison() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack=false;
        try {
            //获取执行清单列表的数量total
            int total = xd.checklistPage(page,size).getInteger("total");

            //新增一个执行清单
            int startM=2;
            String name= dt.getHHmm(startM);
            String desc="是青青创建的哦，为了测试用的";
            JSONArray  items=new JSONArray();//new一个数组
            JSONObject jsonObject = new JSONObject();//数组里面是JSONObject
            jsonObject.put("order",0);
            jsonObject.put("title","我是青青第一线");
            jsonObject.put("comment","要怎么检查啊啊");
            items.add(0,jsonObject);
            JSONArray  shoplist=new JSONArray();
              shoplist.add(0,28764);
              xd.checkListAdd(name,desc,items,shoplist);

            int Newtotal=xd.checklistPage(page,size).getInteger("total");
            int num = Newtotal - 1;
            Preconditions.checkArgument(total == num,"执行清单数" + total + "不等于新增后的执行清单数=" + num);

            //删除一个执行清单
            JSONArray list=xd.checklistPage(page,size).getJSONArray("list");
            long id=list.getJSONObject(0).getInteger("id");//获取列表中的执行清单Id
            xd.checkListDelete(id);
            int NewDeletotal=xd.checklistPage(page,size).getInteger("total");
            int checklist=NewDeletotal;
            Preconditions.checkArgument(total == checklist,"执行清单数=" + total + "不等于删除后的执行清单数=" + checklist);



        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("执行清单数=增删改之后的清单数");
        }
    }


    /**
     *
     * ====================巡店中心各个门店的巡店次数=巡店详情中的巡店条数======================
     * */
    @Test
    public void StoreCheckDataComparison() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack=false;
        try {
          //获取巡店中心列表中的巡店次数
          JSONArray check_list= xd.ShopPage(page,size).getJSONArray("list");
           int patrol_num=check_list.getJSONObject(0).getInteger("patrol_num");

           int shop_id = check_list.getJSONObject(0).getInteger("id");
         //获取巡店详情中的巡店条数
            int total = xd.shopChecksPage(shop_id,page,size).getInteger("total");

            Preconditions.checkArgument(patrol_num == total,"巡店中心各个门店的巡店次数" + patrol_num + "不等于巡店详情中的巡店条数=" + total);
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("巡店中心各个门店的巡店次数=巡店详情中的巡店条数");
        }
    }

    /**
     *
     * ====================巡店记录的巡店详情中的巡店结果中的总项数=执行清单中的总项数======================
     * */
    @Test
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
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("巡店记录的巡店详情中的巡店结果中的总项数=执行清单中的总项数");
        }
    }

    /**
     *
     * ====================巡店记录的巡店详情中的巡店结果中的不合格项数=执行清单中的不合格项数======================
     * */
    @Test
    public void unqualifiedItemsComparison() {
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

           //获取不合格项的结果
            int unqualified_num = xd.shopChecksDetail(id,shop_id).getInteger("unqualified_num");

            //获取执行清单中的不合格项数
            JSONArray checklists = xd.shopChecksDetail(id,shop_id).getJSONArray("check_lists");
            int size = checklists.size();
            int count= 0;
            for (int i = 0;i < size; i++){
                JSONObject jsonObject = checklists.getJSONObject(i);
                if (jsonObject !=null){
                    Integer check_result = jsonObject.getInteger("check_result");
                    if (check_result != null && check_result == 2){
                        count ++;
                    }
                }
            }
            Preconditions.checkArgument(unqualified_num == count,"巡店记录的巡店详情中的巡店结果中的不合格项数" + unqualified_num + "执行清单中的不合格项数=" + count);
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("巡店记录的巡店详情中的巡店结果中的不合格项数=执行清单中的不合格项数");
        }
    }

    /**
     *
     * ====================每个店铺的巡店次数=各个巡店员巡检该店铺的总数======================
     * */
    @Test
    public void  StoreCheckNoComparison() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack=false;
        try {

            //获取每个店铺的巡店次数
            //获取shop_id
            JSONArray check_list= xd.ShopPage(page,size).getJSONArray("list");
            int patrol_num=check_list.getJSONObject(0).getInteger("patrol_num");
            int shop_id = check_list.getJSONObject(0).getInteger("id");

            //获取各个巡店员检查该店铺的总数

            JSONArray list= xd.shopChecksPage(page,size,shop_id).getJSONArray("list");
            int size = list.size();
            int count= 0;
            for (int i = 0;i < size; i++){
                JSONObject jsonObject = list.getJSONObject(i);
                if (jsonObject !=null){
                    String inspector_name = jsonObject.getString("inspector_name");
                    if (inspector_name != null ){
                        count ++;
                    }
                }
            }
            Preconditions.checkArgument(patrol_num == count,"每个店铺的巡店次数=" + patrol_num + "各个巡店员巡检该店铺的总数=" + count);
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("每个店铺的巡店次数=各个巡店员巡检该店铺的总数");
        }
    }

}
