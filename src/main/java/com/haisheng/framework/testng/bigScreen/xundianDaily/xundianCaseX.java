package com.haisheng.framework.testng.bigScreen.xundianDaily;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.haisheng.framework.testng.commonCase.TestCaseCommon;
import com.haisheng.framework.testng.commonCase.TestCaseStd;
import com.haisheng.framework.testng.commonDataStructure.CommonConfig;
import com.haisheng.framework.testng.commonDataStructure.DingWebhook;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @description :巡店中心相关case
 * @date :2020/6/24 12:24
 **/


public class xundianCaseX extends TestCaseCommon implements TestCaseStd {
    public String adminName = "yuexiu@test.com";
    public String adminPasswd = "f5b3e737510f31b88eb2d4b5d0cd2fb4";
    public String filepath="src/main/java/com/haisheng/framework/testng/bigScreen/xundianDaily/64.txt";  //巡店不合格图片base64

    xundianScenarioUtilX xd = xundianScenarioUtilX.getInstance();


    //读取文件内容
    public String texFile(String fileName) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(fileName));
        String str;
        str = in.readLine();
        return str;
    }
    public String getPicList (String filename) throws Exception {
        String pic_data0=texFile(filepath);
        JSONObject pic=xd.picUpload(1,pic_data0);
        String pic_list0=pic.getString("pic_path");
        return pic_list0;
    }
    /**
     * @description :1. 新增门店执行清单，门店详情清单+1 ok
     * @date :2020/6/21 15:19
     **/
    @Test
    public void checklist(){
        logger.logCaseStart(caseResult.getCaseName());
        SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm");
        String name="新建清单1"+df.format(new Date());
        String title="新建清单";
        String comment="新建清单";
        try{
            JSONObject list=xd.xunDianCenterDetail();
            JSONArray chengeB=list.getJSONArray("check_lists");
            int chengeBefore=chengeB.size();
            xd.CheckListAdd(name,"1",title,comment);

            JSONObject list2=xd.xunDianCenterDetail();
            JSONArray chengeA=list2.getJSONArray("check_lists");

            int chengeAfter=chengeA.size();
            int i=chengeAfter-chengeBefore;

            Preconditions.checkArgument(i==1,"新增门店执行清单，门店详情清单没+1");
        }catch (AssertionError e){
            appendFailreason(e.toString());
        }catch (Exception e){
            appendFailreason(e.toString());
        }finally {
            saveData("新增门店执行清单，门店详情清单+1");
        }
    }


    /**
     * 2.pc远程巡店清单全部合格 ok
     */
    @Test
    public void remoteXunDianMore(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONObject list=xd.checkStart("\"REMOTE\"",1);
            long patrol_id=list.getLong("id");//巡检记录id
            JSONArray check_lists=list.getJSONArray("check_lists");
            if(check_lists.size()==0){
                logger.info("该门店未配置执行清单");
                return;
            }

            for(int i=0;i<check_lists.size();i++){
                JSONArray check_items=check_lists.getJSONObject(i).getJSONArray("check_items");
                long listId2=check_lists.getJSONObject(i).getLong("id");

                for(int j=0;j<check_items.size();j++){
                    long itemId2=check_items.getJSONObject(j).getLongValue("id");
                    xd.submitOne(1, itemId2, listId2, patrol_id);
                }
            }

            xd.checkSubmit("\"自动化提交全部合格xiaxia\"",patrol_id);

        } catch (AssertionError e){
            appendFailreason(e.toString());
        }catch (Exception e){
            appendFailreason(e.toString());
        }finally {
            saveData("pc远程巡店");
        }
    }

    /**
     * @description :3.远程巡店全部不合格 ok
     * @date :2020/6/21 11:32
     **/
    @Test
    public void remotePatrolUptoStandard(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONObject list=xd.checkStart("\"REMOTE\"",1);
            long patrol_id=list.getLong("id");//巡检记录id
            JSONArray check_lists=list.getJSONArray("check_lists");
            String pic_data1=texFile(filepath);

            if(check_lists.size()==0){
                logger.info("该门店未配置执行清单");
                return;
            }

            for(int i=0;i<check_lists.size();i++){
                JSONArray check_items=check_lists.getJSONObject(i).getJSONArray("check_items");
                long listId2=check_lists.getJSONObject(i).getLong("id");
                for(int j=0;j<check_items.size();j++){
                    long itemId2=check_items.getJSONObject(j).getLongValue("id");
                    //上传现场巡店截图
                    JSONObject pic=xd.picUpload(1,pic_data1);
                    String pic_list0=pic.getString("pic_path");
                    List<String> pic_listT=new ArrayList<String>();
                    pic_listT.add(pic_list0);
                    xd.checksItemSubmitN(patrol_id,listId2,itemId2,pic_listT);
                    xd.submitOne(2, itemId2, listId2, patrol_id);
                }
            }
            xd.checkSubmit("\"自动化提交全不合格xiaxia\"",patrol_id);

        } catch (AssertionError e){
            appendFailreason(e.toString());
        }catch (Exception e){
            appendFailreason(e.toString());
        }finally {
            saveData("pc远程巡店");
        }
    }

    /**
     * pc远程巡店simple ok
     */
    //    @Test
    public void remoteXunDian(){
        logger.logCaseStart(caseResult.getCaseName());
        try{
            JSONObject list=xd.checkStart("\"REMOTE\"",0);

            long patrol_id=list.getLong("id");//巡检记录id
            JSONArray check_lists=list.getJSONArray("check_lists");


                // long list_id=3029;//巡检清单id
            long list_id=check_lists.getJSONObject(0).getLong("id");


               //获取json array 下标0 的
            JSONObject check_items=check_lists.getJSONObject(0);
//            JSONArray item=check_lists.getJSONArray(check_items);


            //  long item_id=6527;//巡检项目id
            long item_id=check_items.getJSONArray("check_items").getJSONObject(0).getLong("id");
            xd.submitOne(1,item_id,list_id,patrol_id);
            xd.checkSubmit("\"提交\"",patrol_id);
        } catch (AssertionError e){
            appendFailreason(e.toString());
        }catch (Exception e){
            appendFailreason(e.toString());
        }finally {
            saveData("pc远程巡店单执行清单");
        }
    }



    /**
     * 4.新建定检规则-执行周期按月 ok
     */
    @Test
    public void addScheduleCheckMonth() {

        logger.logCaseStart(caseResult.getCaseName());

        try {
            JSONArray list=xd.inspectorList().getJSONArray("list");
            if(list.size()==0){
                throw new Exception("定检员列表为空");
            }
            String inspectorId=list.getJSONObject(0).getString("id");
            System.out.println(inspectorId);
            String districtCode="130000";
            list = xd.shopList(inspectorId, districtCode).getJSONArray("list");
            if (list.size() == 0) {
                throw new Exception("新建定检任务时，可巡检门店列表为空！，定检员id = " + inspectorId);
            }
            String shopId = list.getJSONObject(0).getString("id");
            //新建定检任务
            String name = "定检任务·特殊规则月";
            String cycle = "MONTH";
            String sendTime = "09:00";
            String validStart = LocalDate.now().minusDays(1).toString();
            String validEnd = LocalDate.now().plusDays(20).toString();

            List<Integer> data=new ArrayList<Integer>();
            data.add(1);
            data.add(18);
            data.add(19);

            xd.addScheduleCheck(name, cycle, JSON.toJSONString(data), sendTime, validStart, validEnd,
                    inspectorId);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("新建定检任务-月");
        }
    }

    /**
     * 5.新建定检任务-周期特殊规则 ok
     */
    @Test
    public void addScheduleCheckSpecially() {

        logger.logCaseStart(caseResult.getCaseName());

        try {
            JSONArray list=xd.inspectorList().getJSONArray("list");
            if(list.size()==0){
                throw new Exception("定检员列表为空");
            }
            String inspectorId=list.getJSONObject(0).getString("id");
            logger.info("inspectorID:[{}]",inspectorId);

            String districtCode="130000";
            list = xd.shopList(inspectorId, districtCode).getJSONArray("list");
            if (list.size() == 0) {
                throw new Exception("新建定检任务时，可巡检门店列表为空！，定检员id = " + inspectorId);
            }
            String shopId = list.getJSONObject(0).getString("id");


//            新建定检任务
            String name = "定检任务·特殊规则";
            String cycle = "WEEK";

            String sendTime = "09:00";
            String validStart = LocalDate.now().minusDays(1).toString();
            String validEnd = LocalDate.now().plusDays(5).toString();

            List<String> data=new ArrayList<String>();
            data.add("MON");
            data.add("TUES");
            data.add("WED");

            xd.addScheduleCheck(name, cycle, JSON.toJSONString(data), sendTime, validStart, validEnd,
                    inspectorId);

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        } finally {
            saveData("新建定检任务特殊规则");
        }
    }

    /**
     *6. 删除定检任务 ok
     */
    @Test
    public void ScheduleCheckMonth(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
//            JSONArray list = xd.scheduleCheckList(10, 1).getJSONArray("list");
            JSONObject list1=xd.scheduleCheckList(10,1);
            JSONArray list=list1.getJSONArray("list");
            if (list.size()==0){
                throw new Exception("定检任务列表为空");
            }
            long id=list.getJSONObject(0).getLong("id");
            logger.info("{}",id);
            xd.scheduleCheckDelete(id);
        }catch (AssertionError e){
            appendFailreason(e.toString());
        }catch (Exception e){
            appendFailreason(e.toString());
        }finally {
            saveData("删除定检任务");
        }
    }

    /**
     * 7.case1:最多5次留痕 留痕6次  ok
    */
    @Test
    public void PictureMoreFiveA(){
        logger.logCaseStart(caseResult.getCaseName());
        try {


            JSONObject list=xd.checkStart("\"REMOTE\"",1);

            long patrol_id=list.getLong("id");//巡检记录id
            JSONArray check_lists=list.getJSONArray("check_lists");


            // long list_id=3029;//巡检清单id
            long list_id=check_lists.getJSONObject(0).getLong("id");


            //获取json array 下标0 的
            JSONObject check_items=check_lists.getJSONObject(0);
//            JSONArray item=check_lists.getJSONArray(check_items);


            //  long item_id=6527;//巡检项目id
            long item_id=check_items.getJSONArray("check_items").getJSONObject(0).getLong("id");


            String pic_list0=getPicList(filepath);
            String pic_list1=getPicList(filepath);
            String pic_list2=getPicList(filepath);
            String pic_list3=getPicList(filepath);
            String pic_list4=getPicList(filepath);
            String pic_list5=getPicList(filepath);


            // 上传5个list
            List<String> pic_listT=new ArrayList<String>();
            pic_listT.add(pic_list0);
            pic_listT.add(pic_list1);
            pic_listT.add(pic_list2);
            pic_listT.add(pic_list3);
            pic_listT.add(pic_list4);
            pic_listT.add(pic_list5);


//            提交留痕照片 ToDO:次数  for循环6次可验证超过五次流痕----返回预期如何写
            int code= xd.checksItemSubmitY(patrol_id,list_id,item_id,pic_listT);

            logger.info("{}",code);
            Preconditions.checkArgument(code==1001,"六张不合格图片上传成功");

        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        }
        finally {
            saveData("五次留痕");
        }
    }

    /**
     * @description :8.留痕5次
     * @date :2020/6/22 16:45
     **/
    @Test
    public void PictureMoreFiveXix(){
        logger.logCaseStart(caseResult.getCaseName());
        try {
            JSONObject list=xd.checkStart("\"REMOTE\"",1);

            long patrol_id=list.getLong("id");//巡检记录id
            JSONArray check_lists=list.getJSONArray("check_lists");


            // long list_id=3029;//巡检清单id
            long list_id=check_lists.getJSONObject(0).getLong("id");


            //获取json array 下标0 的
            JSONObject check_items=check_lists.getJSONObject(0);
            //  long item_id=6527;//巡检项目id
            long item_id=check_items.getJSONArray("check_items").getJSONObject(0).getLong("id");


            String pic_list0=getPicList(filepath);
            String pic_list1=getPicList(filepath);
            String pic_list2=getPicList(filepath);
            String pic_list3=getPicList(filepath);
            String pic_list4=getPicList(filepath);

            List<String> pic_listT=new ArrayList<String>();
            pic_listT.add(pic_list0);
            pic_listT.add(pic_list1);
            pic_listT.add(pic_list2);
            pic_listT.add(pic_list3);
            pic_listT.add(pic_list4);

           xd.checksItemSubmitN(patrol_id,list_id,item_id,pic_listT);


        } catch (AssertionError e) {
            appendFailreason(e.toString());
        } catch (Exception e) {
            appendFailreason(e.toString());
        }
        finally {
            saveData("五次留痕");
        }
    }

    /**
     * @description :9.巡店后，巡店次数加1，且巡店时间更新
     * @date :2020/6/23 16:51
     **/

    //获取门店的巡店次数
    public Integer patrol_num(JSONObject data)throws Exception{
        Integer patrol_num =0;
        long shop_id=Long.parseLong(getXunDianShop());
        JSONArray list=data.getJSONArray("list");
        for(int i=0;i<list.size();i++){
            long id=list.getJSONObject(i).getInteger("id");
            if(id==shop_id){
                patrol_num=list.getJSONObject(i).getInteger("patrol_num");
                break;
            }
        }
        return patrol_num;
    }
    //获取门店的最新巡店时间
    public String patrol_time(JSONObject data)throws Exception{
        String patrol_time ="";
        long shop_id=Long.parseLong(getXunDianShop());
        JSONArray list=data.getJSONArray("list");
        for(int i=0;i<list.size();i++){
            long id=list.getJSONObject(i).getInteger("id");
            if(id==shop_id){
                patrol_time=list.getJSONObject(i).getString("last_patrol_time");
                break;
            }
        }
        return patrol_time;
    }

    /**
     * @description :巡店
     * @date :2020/6/23 20:46
     **/
    public void xundianP(){
        try{
            JSONObject list=xd.checkStart("\"REMOTE\"",1);
            long patrol_id=list.getLong("id");//巡检记录id
            JSONArray check_lists=list.getJSONArray("check_lists");
            if(check_lists.size()==0){
                logger.info("该门店未配置执行清单");
                return;
            }

            for(int i=0;i<check_lists.size();i++){
                JSONArray check_items=check_lists.getJSONObject(i).getJSONArray("check_items");
                long listId2=check_lists.getJSONObject(i).getLong("id");
                //获取所有巡检清单的id加到list中
                for(int j=0;j<check_items.size();j++){
                    long itemId2=check_items.getJSONObject(j).getLongValue("id");
                    xd.submitOne(1, itemId2, listId2, patrol_id);
                }
            }
            xd.checkSubmit("\"自动化提交全部合格xiaxia\"",patrol_id);

        } catch (AssertionError e){
            appendFailreason(e.toString());
        }catch (Exception e){
            appendFailreason(e.toString());
        }finally {
            saveData("pc远程巡店");
        }
    }

/**
 * @description :case 9
 * @date :2020/6/24 12:20
 **/

    @Test
    public void xundianTimesAndTime(){
        try{
            //1.获取该店铺原始巡店次数和时间
            int before_num=0;
            String before_time="";
            JSONObject data=xd.xunDianCenterPage(1,10);
            int pages=data.getInteger("pages");

            for(int i=1;i<pages;i++){
                JSONObject data2=xd.xunDianCenterPage(i,10);
                before_num=patrol_num(data2);


                before_time=patrol_time(data2);
                logger.info("巡店前巡店时间：{}",before_time);
                if(before_num!=0){
                    logger.info("巡店前巡店次数:{}",before_num);
                    break;
                }
            }
            //2.巡店
            xundianP();
            //3.获取新的巡店时间和次数,巡店完成的数据必然是首页第一个数据故直接取0下标
            int dataAfter=xd.xunDianCenterPage(1,10).getJSONArray("list").getJSONObject(0).getInteger("patrol_num");
            String AfterTime=xd.xunDianCenterPage(1,10).getJSONArray("list").getJSONObject(0).getString("last_patrol_time");

            logger.info("巡店后巡店次数：{}",dataAfter);
            logger.info("巡店后巡店时间：{}", AfterTime);

            Preconditions.checkArgument((dataAfter-before_num)==1,"巡店后店铺巡店次数没加1");
            Preconditions.checkArgument(!before_time.equals(AfterTime),"巡店后店铺巡店时间没更新");

        }catch (AssertionError e){
            appendFailreason(e.toString());
        }catch (Exception e){
            appendFailreason(e.toString());
        }finally {
            saveData("巡店后次数加1巡店时间更新");
        }
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
//        commonConfig.checklistAppId = ChecklistDbInfo.DB_APP_ID_SCREEN_SERVICE;
//        commonConfig.checklistConfId = ChecklistDbInfo.DB_SERVICE_ID_CRM_DAILY_SERVICE;
//        commonConfig.checklistQaOwner = "";
//
//
//        //replace backend gateway url
//        //commonConfig.gateway = "";
//
//        //replace jenkins job name
//        commonConfig.checklistCiCmd = commonConfig.checklistCiCmd.replace(commonConfig.JOB_NAME, "crm-daily-test");

        //replace product name for ding push
        commonConfig.message = commonConfig.message.replace(commonConfig.TEST_PRODUCT, "巡店 日常");

        //replace ding push conf
        commonConfig.dingHook = DingWebhook.QA_TEST_GRP;
        //if need reset push rd, default are huachengyu,xiezhidong,yanghang
        //commonConfig.pushRd = {"1", "2"};

        //set shop id
        commonConfig.shopId = getXunDianShop(); //这里要改！！！！！
        beforeClassInit(commonConfig);

        logger.debug("xundian " + xd);


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

    @BeforeClass
    public void login() {
        xd.login(adminName,adminPasswd);
    }





}
