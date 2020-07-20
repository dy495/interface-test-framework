package com.haisheng.framework.testng.bigScreen.xundianDaily;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.ChecklistDbInfo;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import com.haisheng.framework.util.StringUtil;
import org.springframework.util.StringUtils;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 *
 */

public class xundianDataConsistentcy extends TestCaseCommon implements TestCaseStd {

    xundianScenarioUtil xd = xundianScenarioUtil.getInstance();
    String xjy4="uid_663ad653";
    int page=1;
    int size=50;
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
            int total = xd.shopChecksPage(page,size,shop_id).getInteger("total");

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

    /**
     *
     * ====================修改巡店清单后，门店基本信息中变为修改之后的======================
     * */
    @Test
    public void  changeCheckListNoComparison() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack=false;
        try {
            //获取执行清单的id
             JSONArray list= xd.checklistPage(page,size).getJSONArray("list");
             long id = list.getJSONObject(0).getInteger("id");
             int startM=2;
             String name= dt.getHHmm(startM);
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
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("修改巡店清单后，门店基本信息中变为修改之后的");
        }
    }

    /**
     *
     * ====================巡店执行清单==执行清单中的总项数======================
     * */
    @Test
    public void  CheckListNoDataComparison() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack=false;
        try {
            //获取执行清单的id
            JSONArray list= xd.checklistPage(page,size).getJSONArray("list");
            long id = list.getJSONObject(0).getInteger("id");
            JSONArray items= xd.checkListDetail(id).getJSONArray("items");
            String name = xd.checkListDetail(id).getString("name");
            //执行清单中的总项数
            int size5 = items.size();
            long shop_id = 4116;
            String check_type = "REMOTE";
            Integer reset = 1;
            Long task_id = null;

            //获取巡店某个执行清单的项数
            JSONArray checklists= xd.shopChecksStart(shop_id,check_type,reset,task_id).getJSONArray("check_lists");
            int total = 0;
            for(int i = 0;i < checklists.size();i++){
                String names = checklists.getJSONObject(i).getString("name");//遍历每一个name在下一步去做比较，如果name一样，则获取这个Name地total
                if (names.equals(name)){
                    total = checklists.getJSONObject(i).getInteger("total");
                }
            }

            Preconditions.checkArgument(size5 == total,"巡店执行清单=" + size5 + "不等于执行清单中的总项数=" + total);
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("巡店执行清单==执行清单中的总项数");
        }
    }

    /**
     *
     * ====================某定检任务中的发送设置=对应巡店员的待办事项中的定检任务数===================
     * */
    @Test
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
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("某定检任务中的发送设置=对应巡店员的待办事项中的定检任务数");
        }
    }


    /**
     *
     * ====================XX事项不合格=与巡店员发送的不合格事项个数相等======================
     * */
    @Test
    public void  ReCheckNoDataComparison() {
        logger.logCaseStart(caseResult.getCaseName());
        boolean needLoginBack=false;
        try {
           //获取待办事项列表得不合格事项
            Integer type = 0;
            Long last_id = null;
            JSONArray thingsList = xd.MTaskList(type,size,last_id).getJSONArray("list");
            int theSize1 = thingsList.size();
            int count = 0;
            for(int i = 0;i < theSize1;i++) {
                JSONObject jsonObject = thingsList.getJSONObject(i);
                String task_type = jsonObject.getString("task_type");
                //这里是计算REMOTE_UNQUALIFIED 出现的次数
                if (task_type != null && task_type.equals("REMOTE_UNQUALIFIED")) {
                    count ++;
                }
            }

            //提交不合格的巡店记录
//            long shop_id = this.getShopId(page, size);//获取shop_id
            long shop_id = 28760;
            String check_type = "REMOTE";
            Integer reset = 1;
            Long task_id = null;
            String pic_data1 = this.texFile(filepath);
            String audit_comment = "自动化测试专用审核意见哈哈哈哈";
            Integer check_result = 2;
            long patrol_id= xd.shopChecksStart(shop_id,check_type ,reset,task_id).getInteger("id");
            JSONArray checklists= xd.shopChecksStart(shop_id,check_type,reset,task_id).getJSONArray("check_lists");
            long list_id = 0;
            long item_id = 0;
            int checkSize= 0;
            for(int i = 0;i<checklists.size();i++){
                list_id = checklists.getJSONObject(i).getInteger("id");
                JSONArray check_items= checklists.getJSONObject(i).getJSONArray("check_items");
                if (check_items == null){
                    continue;
                }
                //第二个循环遍历获取item_id
                for (int j = 0;j<check_items.size();j++){
                    JSONArray  pic_list=new JSONArray();
                    item_id= check_items.getJSONObject(j).getInteger("id");
                    JSONObject pic =xd.picUpload(1,pic_data1);
                    pic_list.add(pic.getString("pic_path"));
                    if(item_id !=0){
                        checkSize ++;
                    }
                    xd.shopChecksItemSubmit(shop_id,patrol_id,list_id,item_id,check_result,audit_comment,pic_list);//提交执行项的结果
                }
            }

            String comment = "审核不通过来一波啊哈哈哈";
            xd.shopChecksSubmit(shop_id,patrol_id,comment);

            //新建一个不合格的巡店记录以后，再次去获取待办事项列表
            JSONArray thingsLists = xd.MTaskList(type,size,last_id).getJSONArray("list");//这里得到一个[] array array 里面是object{}
            int theSize = thingsLists.size();
            int count2 = 0;
            for(int i = 0;i < theSize;i++) {
                JSONObject jsonObject = thingsLists.getJSONObject(i);
                String task_type = jsonObject.getString("task_type");
                //这里是计算REMOTE_UNQUALIFIED 出现的次数
                if (task_type != null && task_type.equals("REMOTE_UNQUALIFIED")) {
                    count2 ++;
                }
            }
            int counts=count2 -checkSize;//已生成定检任务后的待办事项中定检任务数-1=未生成定检任务前的待办事项中定检任务数

            Preconditions.checkArgument(count == counts,"巡店员发送之前得不合格事项" + count + "与巡店员发送的不合格事项个数=" + counts);
        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {

            saveData("XX事项不合格=与巡店员发送的不合格事项个数相等");
        }
    }
    //获取shop_id
    public long getShopId (int page, int size) throws Exception {
        JSONArray check_list= xd.ShopPage(page,size).getJSONArray("list");
        return (long) check_list.getJSONObject(0).getInteger("id");
    }
}
