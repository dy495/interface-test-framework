package com.haisheng.framework.testng.bigScreen.xundianDaily;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.bigScreen.crm.CrmScenarioUtil;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.DateTimeUtil;
import com.haisheng.framework.util.FileUtil;
import com.haisheng.framework.util.JsonpathUtil;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;



/**
 * @author : qing
 * @date :  2020/06/28
 */

public class xundianQxCase extends TestCaseCommon implements TestCaseStd {
    xundianScenarioUtil xd = xundianScenarioUtil.getInstance();
    String xjy4="uid_663ad653";
    int page = 1;
    int size = 50;
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
     * ====================新建定检任务-是否发送到指定巡店员的待办事项中，并且不发送到其他巡店员的待办事项中======================
     * */
    @Test
    public void createdScheduleCheck() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack=false;
        try {
            //新建定检任务
            int startM=0;
            String names= "qingqing" + dt.getHHmm(startM);
            String cycle="WEEK";
            JSONArray  jal=new JSONArray();
            jal.add(0,"MON");
            jal.add(0,"TUES");
            String send_time=dt.getHHmm(startM);
            String valid_start="2020-06-16";
            String valid_end="2020-07-16";
            JSONArray  shoplist=new JSONArray();
            shoplist.add(0,28764);
            xd.scheduleCheckAdd(names,cycle,jal,send_time,valid_start,valid_end,xjy4,shoplist);

            //定检员4的待办事项列表
            xd.login("xunjianyuan4@winsense.ai","e10adc3949ba59abbe56e057f20f883e");
            boolean newTask = false; //标记是否添加成功了任务
            Integer type = 0;
            Integer size = 50;
            Long last_id = null;
            JSONArray list = xd.MTaskList(type,size,last_id).getJSONArray("list");
            for (int i=0;i<list.size();i++){
                JSONObject jsonObject = list.getJSONObject(i);
                String task_type = jsonObject.getString("task_type");
                String  time = jsonObject.getString("time");
                time = time.substring(11,16);
                if(task_type != null && task_type.equals("SCHEDULE_TASK") && time.equals(send_time)){
                    newTask = true;
                }
                break;
            }

            //巡检员1的待办事项列表
            xd.login("xunjianyuan1@winsense.ai","e10adc3949ba59abbe56e057f20f883e");
            boolean newTasks = false; //标记是否添加成功了任务
            Integer type1 = 0;
            Integer size1 = 50;
            Long last_id1 = null;
            JSONArray list1 = xd.MTaskList(type1,size1,last_id1).getJSONArray("list");
            for (int i=0;i<list1.size();i++){
                JSONObject jsonObject = list1.getJSONObject(i);
                String task_type = jsonObject.getString("task_type");
                String  time1  = jsonObject.getString("time");
                time1 = time1.substring(11,16);
                if(task_type != null && task_type.equals("SCHEDULE_TASK") && time1.equals(send_time)){
                    newTasks = true;
                }
                break;
            }

            Preconditions.checkArgument( (newTask == true && newTasks == false),"巡检员4的定检任务出现在了巡检员1的待办事项中");
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("新建定检任务-是否发送到指定巡店员的待办事项中，并且不发送到其他巡店员的待办事项中");
        }

    }




    /**
     *
     * ====================远程巡店不合格事项是否能发送到相应店长的待办事项中======================
     * */
    @Test
    public void patrolSshopChecksSubmit() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack=false;
        try {

            //未提交一个不合格的远程巡店事项的店长2的待办事项列表
            xd.login("dianzhang2@winsense.ai","e10adc3949ba59abbe56e057f20f883e");
            Integer type = 0;
            Integer size = 50;
            Long last_id = null;
            JSONArray list = xd.MTaskList(type,size,last_id).getJSONArray("list");
            int beforeSize = list.size();

            //巡检员2提交一个不合格的远程巡店事项
            xd.login("xunjianyuan2@winsense.ai","e10adc3949ba59abbe56e057f20f883e");
            long shop_id = 28760;
            String check_type = "REMOTE";//远程巡店
            Integer reset = 1;
            Long task_id = null;
            String pic_data1 = this.texFile(filepath);
            String audit_comment = "自动化测试专用审核意见哈哈哈哈";
            Integer check_result = 2;//审核结果为2代表不合格

            long patrol_id= xd.shopChecksStart(shop_id,check_type ,reset,task_id).getInteger("id");
            JSONArray checklists= xd.shopChecksStart(shop_id,check_type,reset,task_id).getJSONArray("check_lists");
            long list_id = 0;
            long item_id = 0;
            int checkitemsSize = 0;
            //第一个循环遍历获取List_id
            for(int i = 0;i<checklists.size();i++){
                list_id = checklists.getJSONObject(i).getInteger("id");
                JSONArray check_items= checklists.getJSONObject(i).getJSONArray("check_items");
                if (check_items == null){
                    continue;
                }
                //第二个循环遍历获取item_id
                for (int j = 0;j<check_items.size();j++){
                    checkitemsSize = checkitemsSize + check_items.size();
                    JSONArray  pic_list=new JSONArray();
                    item_id= check_items.getJSONObject(j).getInteger("id");
                    JSONObject pic =xd.picUpload(1,pic_data1);
                    pic_list.add(pic.getString("pic_path"));
                    xd.shopChecksItemSubmit(shop_id,patrol_id,list_id,item_id,check_result,audit_comment,pic_list);//提交执行项的结果
                }
            }
            String comment = "审核不通过来一波啊哈哈哈";


            //提交最后的审核结果
            xd.shopChecksSubmit(shop_id, patrol_id, comment);

            //店长2的待办事项列表
            xd.login("dianzhang2@winsense.ai","e10adc3949ba59abbe56e057f20f883e");//登录店长2的账号
            boolean Remote_task = false; //标记是否添加成功了任务
            Integer type1 = 0;
            Integer size1 = 50;
            Long last_id1 = null;
            JSONArray list1 = xd.MTaskList(type1,size1,last_id1).getJSONArray("list");
            Long id = list1.getJSONObject(0).getLong("id");//任务Id
            int afterSize = list1.size();
            int newAdd = afterSize - beforeSize;
            if(newAdd == checkitemsSize){
                Remote_task = true;
            }

            //店长2处理不合格事项
            JSONArray pic_list = new JSONArray();
            Integer recheckResult = 0;
            String comments = "青青的店长2处理不合格事项";
            int resultCode = xd.MstepSumit(shop_id,id,comments,pic_list,recheckResult).getInteger("code");//对不合格事项进行处理且将返回成功的值1000赋值给resultCode



             //巡检员2登录查看店长2提交的处理结果
            xd.login("xunjianyuan2@winsense.ai","e10adc3949ba59abbe56e057f20f883e");//巡检员2的账号
            JSONArray list3 = xd.MTaskList(type,size,last_id).getJSONArray("list");
            Long ids = list3.getJSONObject(0).getLong("id");//任务Id
            String commentOther = "青青的巡检员2处理不合格事项";
            int resultCode1 = xd.MstepSumit(shop_id,ids,commentOther,pic_list,recheckResult).getInteger("code");//对结果处理进行审核且将返回成功的值1000赋值给resultCode1


            Preconditions.checkArgument( (Remote_task == true ),"巡店不合格事项没有发送到相应店长的待办事项中");
            Preconditions.checkArgument( (resultCode == 1000 ),"店长没有收到巡检员发送的不合格处理事项");
            Preconditions.checkArgument( (resultCode1 == 1000 ),"巡检员没有收到店长的处理结果");




        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("远程巡店不合格事项是否能发送到相应店长的待办事项中|处理完远程巡店不合格事项后，是否能发送到相应巡店员的待办事项中");
        }

    }
    //获取shop_id
    public long getShopId (int page, int size) throws Exception {
        JSONArray check_list= xd.ShopPage(page,size).getJSONArray("list");
        return (long) check_list.getJSONObject(0).getInteger("id");
    }



    /**
     *
     * ====================现程巡店不合格事项是否能发送到相应店长的待办事项中======================
     * */
    @Test
    public void OnSiteInspection() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack=false;
        try {

            //未提交一个不合格的远程巡店事项的店长2的待办事项列表
            xd.login("dianzhang2@winsense.ai","e10adc3949ba59abbe56e057f20f883e");
            Integer type = 0;
            Integer size = 50;
            Long last_id = null;
            JSONArray list = xd.MTaskList(type,size,last_id).getJSONArray("list");
            int beforeSize = list.size();

            //巡检员2提交一个不合格的远程巡店事项
            xd.login("xunjianyuan2@winsense.ai","e10adc3949ba59abbe56e057f20f883e");
            long shop_id = 28760;
            String check_type = "SPOT";//现场巡店
            Integer reset = 1;
            Long task_id = null;
            String pic_data1 = this.texFile(filepath);
            String audit_comment = "自动化测试专用审核现场巡店意见";
            Integer check_result = 2;//审核结果为2代表不合格

            long patrol_id= xd.shopChecksStart(shop_id,check_type ,reset,task_id).getInteger("id");
            JSONArray checklists= xd.shopChecksStart(shop_id,check_type,reset,task_id).getJSONArray("check_lists");
            long list_id = 0;
            long item_id = 0;
            int checkitemsSize = 0;
            //第一个循环遍历获取List_id
            for(int i = 0;i<checklists.size();i++){
                list_id = checklists.getJSONObject(i).getInteger("id");
                JSONArray check_items= checklists.getJSONObject(i).getJSONArray("check_items");
                if (check_items == null){
                    continue;
                }
                //第二个循环遍历获取item_id
                for (int j = 0;j<check_items.size();j++){
                    checkitemsSize = checkitemsSize + check_items.size();
                    JSONArray  pic_list=new JSONArray();
                    item_id= check_items.getJSONObject(j).getInteger("id");
                    JSONObject pic =xd.picUpload(1,pic_data1);
                    pic_list.add(pic.getString("pic_path"));
                    xd.shopChecksItemSubmit(shop_id,patrol_id,list_id,item_id,check_result,audit_comment,pic_list);//提交执行项的结果
                }
            }
            String comment = "审核不通过来一波啊哈哈哈我是第二波了现场巡店";


            //提交最后的审核结果
            xd.shopChecksSubmit(shop_id, patrol_id, comment);

            //店长2的待办事项列表
            xd.login("dianzhang2@winsense.ai","e10adc3949ba59abbe56e057f20f883e");//登录店长2的账号
            boolean Remote_task = false; //标记是否添加成功了任务
            Integer type1 = 0;
            Integer size1 = 50;
            Long last_id1 = null;
            JSONArray list1 = xd.MTaskList(type1,size1,last_id1).getJSONArray("list");
            Long id = list1.getJSONObject(0).getLong("id");//任务Id
            int afterSize = list1.size();
            int newAdd = afterSize - beforeSize;
            if(newAdd == checkitemsSize){
                Remote_task = true;
            }

            //店长2处理不合格事项
            JSONArray pic_list = new JSONArray();
            Integer recheckResult = 0;
            String comments = "青青的店长2处理不合格事项现场巡店";
            int resultCode = xd.MstepSumit(shop_id,id,comments,pic_list,recheckResult).getInteger("code");//对不合格事项进行处理且将返回成功的值1000赋值给resultCode



            //巡检员2登录查看店长2提交的处理结果
            xd.login("xunjianyuan2@winsense.ai","e10adc3949ba59abbe56e057f20f883e");//巡检员2的账号
            JSONArray list3 = xd.MTaskList(type,size,last_id).getJSONArray("list");
            Long ids = list3.getJSONObject(0).getLong("id");//任务Id
            String commentOther = "青青的巡检员2处理不合格事项现场巡店";
            int resultCode1 = xd.MstepSumit(shop_id,ids,commentOther,pic_list,recheckResult).getInteger("code");//对结果处理进行审核且将返回成功的值1000赋值给resultCode1


            Preconditions.checkArgument( (Remote_task == true ),"巡店2不合格事项没有发送到相应店长2的待办事项中");
            Preconditions.checkArgument( (resultCode == 1000 ),"店长2没有收到巡检员2发送的不合格处理事项");
            Preconditions.checkArgument( (resultCode1 == 1000 ),"巡检员2没有收到店长2的处理结果");




        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("现场巡店不合格事项是否能发送到相应店长的待办事项中|处理完现场巡店不合格事项后，是否能发送到相应巡店员的待办事项中，并且不发送到其他巡店员的待办事项中");
        }

    }

    /**
     *
     * ====================定检任务巡店不合格事项是否能发送到相应店长的待办事项中======================
     * */
    @Test
    public void scheduleCheckInspection() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack=false;
        try {

            //获取定检任务列表的第一个定检任务的id(也是task_id)
            JSONArray ScheckList = xd.scheduleCheckPage(page, size).getJSONArray("list");
            long task_id = ScheckList.getJSONObject(0).getInteger("id");


            //未提交一个不合格的定检任务巡店事项的店长2的待办事项列表
            xd.login("dianzhang2@winsense.ai","e10adc3949ba59abbe56e057f20f883e");
            Integer type = 0;
            Integer size = 50;
            Long last_id = null;
            JSONArray list = xd.MTaskList(type,size,last_id).getJSONArray("list");
            int beforeSize = list.size();

            //巡检员2提交一个不合格的定检任务巡店事项
            xd.login("xunjianyuan2@winsense.ai","e10adc3949ba59abbe56e057f20f883e");
            long shop_id = 28760;
            String check_type = "SCHEDULED";//定检任务巡店
            Integer reset = 1;
            String pic_data1 = this.texFile(filepath);
            String audit_comment = "自动化测试专用审核定检任务巡店意见";
            Integer check_result = 2;//审核结果为2代表不合格


            long patrol_id= xd.shopChecksStart(shop_id,check_type ,reset,task_id).getInteger("id");
            JSONArray checklists= xd.shopChecksStart(shop_id,check_type,reset,task_id).getJSONArray("check_lists");
            long list_id = 0;
            long item_id = 0;
            int checkitemsSize = 0;
            //第一个循环遍历获取List_id
            for(int i = 0;i<checklists.size();i++){
                list_id = checklists.getJSONObject(i).getInteger("id");
                JSONArray check_items= checklists.getJSONObject(i).getJSONArray("check_items");
                if (check_items == null){
                    continue;
                }
                //第二个循环遍历获取item_id
                for (int j = 0;j<check_items.size();j++){
                    checkitemsSize = checkitemsSize + check_items.size();
                    JSONArray  pic_list=new JSONArray();
                    item_id= check_items.getJSONObject(j).getInteger("id");
                    JSONObject pic =xd.picUpload(1,pic_data1);
                    pic_list.add(pic.getString("pic_path"));
                    xd.shopChecksItemSubmit(shop_id,patrol_id,list_id,item_id,check_result,audit_comment,pic_list);//提交执行项的结果
                }
            }
            String comment = "审核不通过来一波啊哈哈哈我是第二波了定检任务巡店";


            //提交最后的审核结果
            xd.shopChecksSubmit(shop_id, patrol_id, comment);

            //店长2的待办事项列表
            xd.login("dianzhang2@winsense.ai","e10adc3949ba59abbe56e057f20f883e");//登录店长2的账号
            boolean Remote_task = false; //标记是否添加成功了任务
            Integer type1 = 0;
            Integer size1 = 50;
            Long last_id1 = null;
            JSONArray list1 = xd.MTaskList(type1,size1,last_id1).getJSONArray("list");
            Long id = list1.getJSONObject(0).getLong("id");//任务Id
            int afterSize = list1.size();
            int newAdd = afterSize - beforeSize;
            if(newAdd == checkitemsSize){
                Remote_task = true;
            }

            //店长2处理不合格事项
            JSONArray pic_list = new JSONArray();
            Integer recheckResult = 0;
            String comments = "青青的店长2处理不合格事项定检任务巡店";
            int resultCode = xd.MstepSumit(shop_id,id,comments,pic_list,recheckResult).getInteger("code");//对不合格事项进行处理且将返回成功的值1000赋值给resultCode



            //巡检员2登录查看店长2提交的处理结果
            xd.login("xunjianyuan2@winsense.ai","e10adc3949ba59abbe56e057f20f883e");//巡检员2的账号
            JSONArray list3 = xd.MTaskList(type,size,last_id).getJSONArray("list");
            Long ids = list3.getJSONObject(0).getLong("id");//任务Id
            String commentOther = "青青的巡检员2处理不合格事项定检任务巡店";
            int resultCode1 = xd.MstepSumit(shop_id,ids,commentOther,pic_list,recheckResult).getInteger("code");//对结果处理进行审核且将返回成功的值1000赋值给resultCode1


            Preconditions.checkArgument( (Remote_task == true ),"巡店2不合格事项没有发送到相应店长2的待办事项中");
            Preconditions.checkArgument( (resultCode == 1000 ),"店长2没有收到巡检员2发送的不合格处理事项");
            Preconditions.checkArgument( (resultCode1 == 1000 ),"巡检员2没有收到店长2的处理结果");


            //删除新建的定检任务
            xd.scheduleCheckDelete(task_id);


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("定检任务巡店不合格事项是否能发送到相应店长的待办事项中|处理完定检任务巡店不合格事项后，是否能发送到相应巡店员的待办事项中，并且不发送到其他巡店员的待办事项中");
        }

    }

    /**
     *
     * ====================巡检员2巡检门店1-店长1处理门店1-“查看处理结果”发送到巡检员2的待办事项里======================
     * */
    @Test
    public void ShopInspectionResult() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack=false;
        try {
            //巡检员2提交一个不合格的定检任务巡店事项
            xd.login("xunjianyuan2@winsense.ai","e10adc3949ba59abbe56e057f20f883e");
            long shop_id = 28758;
            String check_type = "SPOT";//现场巡店
            Integer reset = 1;
            String pic_data1 = this.texFile(filepath);
            String audit_comment = "自动化测试专用审核定检任务巡店意见";
            Integer check_result = 2;//审核结果为2代表不合格
            Long task_id = null;


            long patrol_id= xd.shopChecksStart(shop_id,check_type ,reset,task_id).getInteger("id");
            JSONArray checklists= xd.shopChecksStart(shop_id,check_type,reset,task_id).getJSONArray("check_lists");
            long list_id = 0;
            long item_id = 0;
            int checkitemsSize = 0;
            //第一个循环遍历获取List_id
            for(int i = 0;i<checklists.size();i++){
                list_id = checklists.getJSONObject(i).getInteger("id");
                JSONArray check_items= checklists.getJSONObject(i).getJSONArray("check_items");
                if (check_items == null){
                    continue;
                }
                //第二个循环遍历获取item_id
                for (int j = 0;j<check_items.size();j++){
                    checkitemsSize = checkitemsSize + check_items.size();
                    JSONArray  pic_list=new JSONArray();
                    item_id= check_items.getJSONObject(j).getInteger("id");
                    JSONObject pic =xd.picUpload(1,pic_data1);
                    pic_list.add(pic.getString("pic_path"));
                    xd.shopChecksItemSubmit(shop_id,patrol_id,list_id,item_id,check_result,audit_comment,pic_list);//提交执行项的结果
                }
            }
            String comment = "审核不通过来一波啊哈哈哈我是第二波了定检任务巡店";


            //提交最后的审核结果
            xd.shopChecksSubmit(shop_id, patrol_id, comment);


            //店长1的待办事项列表
            xd.login("dianzhang1@winsense.ai","e10adc3949ba59abbe56e057f20f883e");//登录店长2的账号
            boolean Remote_task = false; //标记是否添加成功了任务
            Integer type = 0;
            Long last_id = null;
            JSONArray list1 = xd.MTaskList(type,size,last_id).getJSONArray("list");
            Long id = list1.getJSONObject(0).getLong("id");//任务Id


            //店长1处理不合格事项
            JSONArray pic_list = new JSONArray();
            Integer recheckResult = 0;
            String comments = "青青的店长2处理不合格事项定检任务巡店";
            xd.MstepSumit(shop_id,id,comments,pic_list,recheckResult).getInteger("code");//对不合格事项进行处理

            //巡检员2登录查看店长2提交的处理结果
            xd.login("xunjianyuan2@winsense.ai","e10adc3949ba59abbe56e057f20f883e");//巡检员2的账号
            JSONArray list3 = xd.MTaskList(type,size,last_id).getJSONArray("list");
            Long ids = list3.getJSONObject(0).getLong("id");//任务Id
            String commentOther = "青青的巡检员2处理不合格事项定检任务巡店";
            int resultCode1 = xd.MstepSumit(shop_id,ids,commentOther,pic_list,recheckResult).getInteger("code");//对结果处理进行审核且将返回成功的值1000赋值给resultCode1

            Preconditions.checkArgument( (resultCode1 == 1000 ),"巡检员2没有收到店长1的处理结果");
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("巡检员2巡检门店1-店长1处理门店1-“查看处理结果”发送到巡检员2的待办事项里");
        }

    }

}